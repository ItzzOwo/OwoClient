package net.caffeinemc.phosphor.mixin;

import imgui.ImGui;
import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.caffeinemc.phosphor.modules.AimAssistModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class AimAssistMixin {
    private static final float Y_OFFSET_DEFAULT = 0f;
    private static final float X_OFFSET_DEFAULT = 0f;
    private static final float FOV_RANGE_DEFAULT = 90f;
    private static final float AIM_RANGE_DEFAULT = 4.5f;
    private static final float SCALE_FACTOR = 1.2f;  // Determines the speed factor of the dynamic aim assist.

    private float SMOOTHNESS = 1.0f;
    private float yMultiplier = 1.0f;
    private float xMultiplier = 1.0f;
    private float Y_OFFSET = Y_OFFSET_DEFAULT;
    private float X_OFFSET = X_OFFSET_DEFAULT;
    private float FOV_RANGE = FOV_RANGE_DEFAULT;
    private float AIM_RANGE = AIM_RANGE_DEFAULT;

    private final SecureRandom random = new SecureRandom();
    private static final long ENTITY_TRACKING_DELAY = 5000; // 5 seconds
    private long lastEntityTrackingTime = 0;
    private final Map<Entity, Long> trackedEntities = new HashMap<>();

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(boolean tick, CallbackInfo ci) {
        if (!OwoMenu.config().getAimAssistEnabled().get()) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();

        if (shouldSkipAimAssist(mc)) {
            return;
        }

        updateConfigValues();

        Entity target = getClosestEntity(mc);
        if (shouldSkipTarget(target)) {
            return;
        }

        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();

        if (target != null && target instanceof LivingEntity) {
            if (!target.isAlive()) {
                return;
            }

            float targetYaw = getAimAssistYaw(mc.player, (LivingEntity) target);
            float targetPitch = getAimAssistPitch(mc.player, (LivingEntity) target);

            float yawDifference = MathHelper.wrapDegrees(targetYaw - yaw);
            float pitchDifference = MathHelper.wrapDegrees(targetPitch - pitch);

            if (AimAssistModule.currentStyle.get() == 1) {
                if (shouldStopAiming(yawDifference, pitchDifference)) {
                    return;
                }

                float distanceToTarget = (float) mc.player.getPos().distanceTo(target.getPos());
                float speedScale = distanceToTarget * SCALE_FACTOR;

                yaw += ((((yawDifference + (X_OFFSET * 10)) / (SMOOTHNESS * 30) * speedScale)) + generateNoise()) * xMultiplier;
                pitch += ((((pitchDifference + (Y_OFFSET * 10)) / (SMOOTHNESS * 30) * speedScale)) + generateNoise()) * yMultiplier;
            } else {
                if (shouldStopAiming(yawDifference, pitchDifference)) {
                    return;
                }

                yaw += ((yawDifference + (X_OFFSET * 10)) / (SMOOTHNESS * 30) + generateNoise()) * xMultiplier;
                pitch += ((pitchDifference + (Y_OFFSET * 10)) / (SMOOTHNESS * 30) + generateNoise()) * yMultiplier;
            }
        }

        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }

    private boolean shouldSkipAimAssist(MinecraftClient mc) {
        return mc.player == null || mc.interactionManager == null || mc.currentScreen instanceof HandledScreen || !OwoMenu.isClientEnabled() || !mc.player.isAlive();
    }

    private void updateConfigValues() {
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
        AIM_RANGE = OwoMenu.config().getAimAssistAimRange().floatValue();
    }

    private boolean shouldSkipTarget(Entity target) {
        return OwoMenu.config().getAimAssistPlayersOnly().get() && !(target instanceof PlayerEntity)
                || OwoMenu.config().getAimAssistAntiBot().get() && AntiBot(target);
    }

    private boolean shouldStopAiming(float yawDifference, float pitchDifference) {
        return yawDifference < 5.0F && pitchDifference < 5.0F && random.nextInt(100) == 0;
    }

    private int noiseDurationTicks = 0;
    private float noiseValue = 0.0f;

    private float generateNoise() {
        if (noiseDurationTicks > 0) {
            noiseDurationTicks--;
            return noiseValue;
        }

        float mean = 0.0f;
        float variance = 0.01f;
        float noise = (float) (mean + random.nextGaussian() * variance);

        // Add a chance for the noise to be very low or very high for a few ticks
        if (random.nextInt(100) < 5) {
            noise *= random.nextBoolean() ? -1.0f : 1.0f;
            noiseDurationTicks = random.nextInt(25) + 1; // Set a random duration between 1 and 25 ticks
            noiseValue = noise;
        }

        noise = MathHelper.clamp(noise, -0.1f, 0.1f);
        return noise;
    }



    private boolean isEntityFlying(Entity entity, MinecraftClient mc) {
        // Checking if the entity is a player
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Vec3d velocity = player.getVelocity();
            if (player.getAbilities().flying && !mc.player.getAbilities().flying) {
                return true;
            }
            if (player.getAbilities().allowFlying && !mc.player.getAbilities().allowFlying) {
                return true;
            }
            if (Math.abs(velocity.y) == 0 && !entity.isOnGround()) {
                return true;
            }
        }
        return false;
    }

    private boolean AntiBot(Entity entity) {
        // TODO: Implement a check for movement. Many anticheat systems use entities to fly around players to detect aim assist/aimbot. By checking if the entity is flying or has irregular movement patterns, we can identify potential bots.
        // TODO: Additionally, we can develop an algorithm to analyze patterns among all players. By establishing a pattern based on legitimate player behavior, we can identify entities that do not conform to this pattern. If an entity deviates from the established pattern, it is likely to be a staff member or a bot.
        MinecraftClient mc = MinecraftClient.getInstance();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            String playerName = player.getName().getString();
            boolean hasIllegalCharacters = playerName.matches("^[a-zA-Z0-9_]{2,16}$");
            if (hasIllegalCharacters) {
                return true;
            }
            if (isEntityFlying(entity, mc)) {
                return true;
            }
            if (entity.getEntityName().startsWith("§c")) {
                return true;
            }
            String uuidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
            String n = entity.getUuidAsString();
            if (!Pattern.matches(uuidPattern, n)) {
                return true;
            }
            if (entity.getEntityName().isEmpty()) {
                return true;
            }
            if (entity.getDisplayName().getString().isEmpty()) {
                return true;
            }
            if (((PlayerEntity) entity).canTakeDamage() && !mc.player.canTakeDamage()) {
                return true;
            }
        }
        return false;
    }

    private void updateTrackedEntities(MinecraftClient client) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEntityTrackingTime >= ENTITY_TRACKING_DELAY) {
            // Update tracked entities
            trackedEntities.clear();
            for (Entity entity : client.world.getEntities()) {
                if (entity != client.player && entity.isAlive()) {
                    trackedEntities.put(entity, currentTime);
                }
            }
            lastEntityTrackingTime = currentTime;
        }
    }

    public Entity getClosestEntity(MinecraftClient client) {
        Vec3d playerPos = client.player.getPos();
        Vec3d playerEyesPos = playerPos.add(0, client.player.getEyeHeight(client.player.getPose()), 0);
        Vec3d lookVec = client.player.getRotationVec(1.0F);

        double closestDistance = AIM_RANGE;
        Entity closestEntity = null;

        updateTrackedEntities(client);

        for (Entity entity : trackedEntities.keySet()) {
            long entityTrackingTime = trackedEntities.get(entity);
            double distance = playerEyesPos.distanceTo(entity.getPos());

            if (System.currentTimeMillis() - entityTrackingTime >= ENTITY_TRACKING_DELAY) {
                // Remove entities that haven't been tracked for the specified delay
                trackedEntities.remove(entity);
                continue;
            }

            if (distance <= AIM_RANGE) {
                double angle = Math.toDegrees(lookVec.normalize().dotProduct(entity.getPos().subtract(playerEyesPos).normalize()));
                if (angle > FOV_RANGE) {
                    continue; // Entity is outside the field of view
                }

                // Calculate the distance between the entity and the crosshair position
                Vec3d crosshairPos = playerEyesPos.add(lookVec.multiply(distance));
                double crosshairDistance = entity.getPos().distanceTo(crosshairPos);

                // Compare distances to prioritize closest entity
                if (crosshairDistance < closestDistance) {
                    closestDistance = crosshairDistance;
                    closestEntity = entity;
                }
            }
        }
        return closestEntity;
    }

    private float getAimAssistYaw(PlayerEntity player, LivingEntity target) {
        double deltaX = target.getX() - player.getX();
        double deltaZ = target.getZ() - player.getZ();
        double angle = MathHelper.atan2(deltaZ, deltaX) * (180.0 / Math.PI) - 90.0;
        float angleDifference = MathHelper.wrapDegrees((float) (angle - player.getYaw()));

        // Calculate the absolute angle difference
        float absAngleDifference = Math.abs(angleDifference);

        // Calculate the angle difference in the opposite direction
        float oppositeAngleDifference = MathHelper.wrapDegrees(angleDifference + 180.0F);

        // Set the maximum allowable angle for the target to be considered within the field of view
        float maxAllowedAngle = FOV_RANGE / 2.0F;

        if (absAngleDifference <= maxAllowedAngle || oppositeAngleDifference <= maxAllowedAngle) {
            return (float) angle;
        }

        return player.getYaw();
    }

    private float getAimAssistPitch(PlayerEntity player, LivingEntity target) {
        float distanceX = (float) (target.getX() - player.getX());
        float distanceY = (float) ((target.getY() + target.getStandingEyeHeight()) - (player.getY() + player.getStandingEyeHeight()));
        float distanceZ = (float) (target.getZ() - player.getZ());

        float horizontalDistance = MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ);

        float pitch = (float) Math.toDegrees(-MathHelper.atan2(distanceY, horizontalDistance));
        float errorFactor = generateNoise();
        return MathHelper.clamp(pitch + errorFactor, player.getPitch(), 90.0f);
    }
}
