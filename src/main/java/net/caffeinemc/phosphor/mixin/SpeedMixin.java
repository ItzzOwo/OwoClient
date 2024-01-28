package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class SpeedMixin {

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        double baseSpeed = mc.player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (mc.player == null || !OwoMenu.isClientEnabled() || !OwoMenu.config().getSpeedEnabled().get() || mc.player.isBlocking() || mc.player.isUsingItem() || mc.currentScreen instanceof HandledScreen) {
            return;
        }

        int speedValue = OwoMenu.config().getSpeedValue().intValue();
        float speedMultiplier = speedValue / 100.0f;
        double newSpeed = baseSpeed * speedMultiplier;
        mc.player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(newSpeed);
    }
}
