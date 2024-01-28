package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class FlyMixin {

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        MinecraftClient MC = MinecraftClient.getInstance();
        MC.player.getAbilities().allowFlying = false;
        if (MC.player == null || !OwoMenu.isClientEnabled() || !OwoMenu.config().getFlyEnabled().get() || MC.player.isBlocking() || MC.player.isUsingItem() || MC.currentScreen instanceof HandledScreen) {
            return;
        }
        int FlyValue = OwoMenu.config().getFlyValue().intValue();
        MC.player.getAbilities().allowFlying = true;
        MC.player.getAbilities().setFlySpeed(FlyValue);
    }}

