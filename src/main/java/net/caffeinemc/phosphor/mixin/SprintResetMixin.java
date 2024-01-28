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

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.PlayerEntity;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.AxeItem;

import net.minecraft.entity.player.PlayerInventory;
@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class SprintResetMixin {
    private final SecureRandom secureRandom = new SecureRandom();
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
        if (!OwoMenu.config().getAutoSprintResetEnabled().get()) {
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
        float randomValue = 0.82f + secureRandom.nextFloat() * (1.0f - 0.82f);
        float randomDelay = 0.50f + secureRandom.nextFloat() * (0.60f - 0.40f);
        //requires toggle-sprint to be disabled
        if ((mc.player.isOnGround() && mc.player.getAttackCooldownProgress(randomDelay) < randomValue) || (!mc.player.isOnGround() && mc.player.getAttackCooldownProgress(randomDelay) < randomValue)) {
            int value2 = secureRandom.nextInt(27) + secureRandom.nextInt(secureRandom.nextInt(14,15), secureRandom.nextInt(20,21));
            executor.schedule(() -> mc.player.setSprinting(true), value2, TimeUnit.MILLISECONDS);
        }
    }
}