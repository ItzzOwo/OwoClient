package net.caffeinemc.phosphor.mixin;

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

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class TriggerBotMixin {
    private static final Random random = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static boolean isHoldingWeapon() {
        PlayerInventory inventory = MinecraftClient.getInstance().player.getInventory();
        ItemStack heldItem = inventory.getMainHandStack();

        return heldItem.getItem() instanceof SwordItem || heldItem.getItem() instanceof AxeItem;
    }
    @Inject(at = @At("HEAD"), method = "tick")
    private void onStartTick(CallbackInfo ci) {
        float randomValue = 0.82f + random.nextFloat() * (1.0f - 0.82f);
        float randomDelay = 0.50f + random.nextFloat() * (0.60f - 0.40f);
        MinecraftClient mc = MinecraftClient.getInstance();
        Entity target = mc.crosshairTarget instanceof EntityHitResult result ? result.getEntity() : null;

        if (mc.player == null || mc.interactionManager == null || mc.currentScreen instanceof HandledScreen) {
            return;
        }

        if (!OwoMenu.isClientEnabled() || !OwoMenu.config().getTriggerEnabled().get()
                || (!OwoMenu.config().getPermTriggerEnabled().get() && !mc.options.attackKey.isPressed())) {
            return;
        }

        if (OwoMenu.config().getTriggerWeaponOnly().get() && !isHoldingWeapon()) {
            return;
        }

        if (mc.player.isBlocking() || mc.player.isUsingItem() || !(target instanceof LivingEntity) || ((LivingEntity) target).getHealth() <= 0.0f) {
            return;
        }

        if ((mc.player.isOnGround() && mc.player.getAttackCooldownProgress(randomDelay) < randomValue) || (!mc.player.isOnGround() && mc.player.getAttackCooldownProgress(randomDelay) < randomValue)) {
            return;
        }
        int value = random.nextInt(26) + 15;
        executor.schedule(() -> mc.interactionManager.attackEntity(mc.player, target), value, TimeUnit.MILLISECONDS);
        executor.schedule(() -> mc.player.swingHand(Hand.MAIN_HAND), value, TimeUnit.MILLISECONDS);
    }

    @Inject(method = "run", at = @At("HEAD"))
    private void onStart(CallbackInfo ci) {
        OwoMenu.toggleVisibility();
    }
}