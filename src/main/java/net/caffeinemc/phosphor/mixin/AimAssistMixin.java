package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import java.security.SecureRandom;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class AimAssistMixin {
    private float SMOOTHNESS = 1f;
    private float yMultiplier = 1f;
    private float xMultiplier = 1f;
    private float Y_OFFSET = OwoMenu.config().getAimAssistYOffset().floatValue();
    private float X_OFFSET = OwoMenu.config().getAimAssistXOffset().floatValue();
    private float FOV_RANGE = OwoMenu.config().getAimAssistFOVRange().floatValue();
    SecureRandom random = new SecureRandom();
    private LivingEntity lockedTarget;
    private long lockExpirationTime;
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(boolean tick, CallbackInfo ci) {
        if (!OwoMenu.config().getAimAssistEnabled().get()) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null || mc.currentScreen instanceof HandledScreen || !OwoMenu.isClientEnabled()) {
            return;
        }

        float SMOOTHNESSMinValue = OwoMenu.config().getAimAssistSmoothness()[0].floatValue();
        float SMOOTHNESSMaxValue = OwoMenu.config().getAimAssistSmoothness()[1].floatValue();
        SMOOTHNESS = SMOOTHNESSMinValue + random.nextFloat() * (SMOOTHNESSMaxValue - SMOOTHNESSMinValue);

        float yMultiplierMinValue = OwoMenu.config().getAimAssistYMultiplier()[0].floatValue();
        float yMultiplierMaxValue = OwoMenu.config().getAimAssistYMultiplier()[1].floatValue();
        yMultiplier = yMultiplierMinValue + random.nextFloat() * (yMultiplierMaxValue - yMultiplierMinValue);

        float xMultiplierMinValue = OwoMenu.config().getAimAssistXMultiplier()[0].floatValue();
        float xMultiplierMaxValue = OwoMenu.config().getAimAssistXMultiplier()[1].floatValue();
        xMultiplier = xMultiplierMinValue + random.nextFloat() * (xMultiplierMaxValue - xMultiplierMinValue);

        Y_OFFSET = OwoMenu.config().getAimAssistYOffset().floatValue();
        X_OFFSET = OwoMenu.config().getAimAssistXOffset().floatValue();
        FOV_RANGE = OwoMenu.config().getAimAssistFOVRange().floatValue();
        Entity target = getClosestEntity(mc);

        if (OwoMenu.config().getAimAssistPlayersOnly().get()) {
            if (!(target instanceof PlayerEntity)) {
                return;
            }
        }

        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();

        if (target != null && target instanceof LivingEntity) {
            float targetYaw = getAimAssistYaw(mc.player, (LivingEntity) target);
            float targetPitch = getAimAssistPitch(mc.player, (LivingEntity) target);

            float yawDifference = MathHelper.wrapDegrees(targetYaw - yaw);
            float pitchDifference = MathHelper.wrapDegrees(targetPitch - pitch);

            // Apply smoothing
            yaw += ((yawDifference / (SMOOTHNESS * 30)) + Y_OFFSET) * xMultiplier;
            pitch += ((pitchDifference / (SMOOTHNESS * 30)) + X_OFFSET) * yMultiplier;
        }


        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }

    public Entity getClosestEntity(MinecraftClient client) {
        float DISTANCE = 3.5f;

        Vec3d playerPos = client.player.getPos();
        Vec3d playerEyesPos = playerPos.add(0, client.player.getEyeHeight(client.player.getPose()), 0);
        Vec3d lookVec = client.player.getRotationVec(1.0F);

        double closestDistance = DISTANCE;
        Entity closestEntity = null;

        for (Entity entity : client.world.getEntities()) {
            if (entity != client.player && entity.isAlive()) {
                Vec3d entityPos = entity.getPos();
                double distance = playerEyesPos.distanceTo(entityPos);

                if (distance <= DISTANCE) {
                    double angle = Math.toDegrees(lookVec.normalize().dotProduct(entityPos.subtract(playerEyesPos).normalize()));
                    if (angle > FOV_RANGE) {
                        continue; // Entity is outside the field of view
                    }

                    // Calculate the distance between the entity and the crosshair position
                    Vec3d crosshairPos = playerEyesPos.add(lookVec.multiply(distance));
                    double crosshairDistance = entityPos.distanceTo(crosshairPos);

                    // Compare distances to prioritize closest entity
                    if (crosshairDistance < closestDistance) {
                        closestDistance = crosshairDistance;
                        closestEntity = entity;
                    }
                }
            }
        }

        return closestEntity;
    }

    private float getAimAssistYaw(PlayerEntity player, LivingEntity target) {
        float angle = MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(target.getZ() - player.getZ(), target.getX() - player.getX())) - 90.0F);
        float angleDifference = MathHelper.wrapDegrees(angle - player.getYaw());

        if (Math.abs(angleDifference) <= FOV_RANGE / 2.0F) {
            return angle;
        }

        return player.getYaw();
    }

    private float getAimAssistPitch(PlayerEntity player, LivingEntity target) {
        float distanceX = (float)(target.getX() - player.getX());
        float distanceY = (float)((target.getY() + target.getStandingEyeHeight()) - (player.getY() + player.getStandingEyeHeight()));
        float distanceZ = (float)(target.getZ() - player.getZ());

        float horizontalDistance = MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ);

        float pitch = (float) Math.toDegrees(-MathHelper.atan2(distanceY, horizontalDistance));
        float yaw = (float) Math.toDegrees(MathHelper.atan2(distanceZ, distanceX)) - 90.0F;
        float yawDifference = MathHelper.wrapDegrees(yaw - player.getYaw());
        float fovModifier = MathHelper.sqrt(yawDifference * yawDifference + pitch * pitch);

        if (fovModifier <= FOV_RANGE / 2.0F) {
            return pitch;
        }

        return player.getPitch();
    }
}