package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
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
    private float Y_OFFSET = OwoMenu.config().getAimAssistYOffset().floatValue();
    private float X_OFFSET = OwoMenu.config().getAimAssistXOffset().floatValue();
    private float Y_MULTIPLIER = OwoMenu.config().getAimAssistYMultiplier().floatValue();
    private float X_MULTIPLIER = OwoMenu.config().getAimAssistXMultiplier().floatValue();
    private float FOV_RANGE = OwoMenu.config().getAimAssistFOVRange().floatValue();
    private float MAX_DISTANCE = OwoMenu.config().getAimAssistRange().floatValue();
    private SecureRandom random = new SecureRandom();

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(boolean tick, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null || mc.currentScreen instanceof HandledScreen || !OwoMenu.isClientEnabled() ||  !OwoMenu.config().getAimAssistEnabled().get()) {
            return;
        }

        Y_OFFSET = OwoMenu.config().getAimAssistYOffset().floatValue();
        X_OFFSET = OwoMenu.config().getAimAssistXOffset().floatValue();
        FOV_RANGE = OwoMenu.config().getAimAssistFOVRange().floatValue();
        MAX_DISTANCE = OwoMenu.config().getAimAssistRange().floatValue();
        Y_MULTIPLIER = OwoMenu.config().getAimAssistYMultiplier().floatValue();
        X_MULTIPLIER = OwoMenu.config().getAimAssistXMultiplier().floatValue();
        Entity target = getClosestEntity();
        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();
        if (target instanceof LivingEntity && isEntityInFOV(target)) {
            if (OwoMenu.config().getAimAssistPlayersOnly().get() && !(target instanceof PlayerEntity))
            {
                return;
            }
            float targetYaw = getYawToEntity(target);
            float targetPitch = getPitchToEntity(target);
            float yawDifference = MathHelper.wrapDegrees(targetYaw - yaw);
            float pitchDifference = MathHelper.wrapDegrees(targetPitch - pitch);
            yaw += ((yawDifference / (OwoMenu.config().getAimAssistSmooth().floatValue() * random.nextInt(29, 31))) + Y_OFFSET) * Y_MULTIPLIER;
            pitch += ((pitchDifference / (OwoMenu.config().getAimAssistSmooth().floatValue() * random.nextInt(29, 31))) + X_OFFSET) * X_MULTIPLIER;
        }
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }


    private Entity getClosestEntity() {
        MinecraftClient client = MinecraftClient.getInstance();
        Vec3d playerPos = client.player.getPos();
        Vec3d playerEyesPos = playerPos.add(0, client.player.getEyeHeight(client.player.getPose()), 0);
        Vec3d lookVec = client.player.getRotationVec(1.0F);
        double closestDistance = MAX_DISTANCE;
        Entity closestEntity = null;
        for (Entity entity : client.world.getEntities()) {
            if (entity != client.player && entity.isAlive()) {
                Vec3d entityPos = entity.getPos();
                double distance = playerEyesPos.distanceTo(entityPos);
                if (distance <= MAX_DISTANCE) {
                    double angle = Math.toDegrees(lookVec.normalize().dotProduct(entityPos.subtract(playerEyesPos).normalize()));
                    if (angle > FOV_RANGE) {
                        continue;
                    }
                    Vec3d crosshairPos = playerEyesPos.add(lookVec.multiply(distance));
                    double crosshairDistance = entityPos.distanceTo(crosshairPos);
                    if (crosshairDistance < closestDistance) {
                        closestDistance = crosshairDistance;
                        closestEntity = entity;
                    }
                }
            }
        }
        return closestEntity;
    }

    private float getYawToEntity(Entity targetEntity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || targetEntity == null) {
            return 0.0f;
        }
        double deltaX = targetEntity.getX() - client.player.getX();
        double deltaZ = targetEntity.getZ() - client.player.getZ();
        double angle = Math.atan2(deltaZ, deltaX) * (180 / Math.PI) - 90.0;
        return (float) angle;
    }

    private float getPitchToEntity(Entity targetEntity) {
        MinecraftClient client = MinecraftClient.getInstance();
        float distanceX = (float)(targetEntity.getX() - client.player.getX());
        float distanceY = (float)((targetEntity.getY() + targetEntity.getStandingEyeHeight()) - (client.player.getY() + client.player.getStandingEyeHeight()));
        float distanceZ = (float)(targetEntity.getZ() - client.player.getZ());

        float horizontalDistance = MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ);

        float pitch = (float) Math.toDegrees(-MathHelper.atan2(distanceY, horizontalDistance));
        float yaw = (float) Math.toDegrees(MathHelper.atan2(distanceZ, distanceX)) - 90.0F;
        float yawDifference = MathHelper.wrapDegrees(yaw - client.player.getYaw());
        float fovModifier = MathHelper.sqrt(yawDifference * yawDifference + pitch * pitch);

        if (fovModifier <= FOV_RANGE / 2.0F) {
            return pitch;
        }

        return client.player.getPitch();
    }

    private boolean isEntityInFOV(Entity targetEntity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || targetEntity == null) {
            return false;
        }
        float yawToEntity = getYawToEntity(targetEntity);
        float pitchToEntity = getPitchToEntity(targetEntity);
        float yawDifference = Math.abs(client.player.getYaw() - yawToEntity);
        float pitchDifference = Math.abs(client.player.getPitch() - pitchToEntity);
        yawDifference = yawDifference > 180.0f ? 360.0f - yawDifference : yawDifference;
        pitchDifference = pitchDifference > 180.0f ? 360.0f - pitchDifference : pitchDifference;
        return yawDifference <= FOV_RANGE && pitchDifference <= FOV_RANGE;
    }
}