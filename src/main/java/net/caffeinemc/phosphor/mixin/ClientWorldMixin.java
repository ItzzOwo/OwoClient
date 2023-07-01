package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    private static final Random random = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) {
            return;
        }
        if (!OwoMenu.isClientEnabled()) {
            return;
        }
        if (!OwoMenu.config().getAutoJumpResetEnabled().get()) {
            return;
        }
        if (mc.player.isBlocking()) {
            return;
        }
        if (mc.player.isUsingItem()) {
            return;
        }
        if (mc.currentScreen instanceof HandledScreen) {
            return;
        }
        if (!mc.player.isOnGround()) {
            return;
        }
        if (mc.player.maxHurtTime == 0) {
            return;
        }
        if (mc.player.hurtTime == 0) {
            return;
        }
        if (mc.player.hurtTime == mc.player.maxHurtTime - 1) {
            if (random.nextFloat() < 0.80) {
                int value = random.nextInt(11) + 20;
                executor.schedule(() -> mc.player.jump(), value, TimeUnit.MILLISECONDS);

        }
    }

}}