package net.caffeinemc.phosphor.mixin;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.text.Text;
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
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class TriggerBotMixin {
    private final SecureRandom random = new SecureRandom();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private int hmm = 0;
    private final Lock lock = new ReentrantLock();
    private static int previousHurtTime = -1;
    private static boolean isHoldingWeapon() {
        PlayerInventory inventory = MinecraftClient.getInstance().player.getInventory();
        ItemStack heldItem = inventory.getMainHandStack();
        return heldItem.getItem() instanceof SwordItem || heldItem.getItem() instanceof AxeItem;
    }

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
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

        if (!(mc.player.getAttackCooldownProgress(0) >= random.nextFloat(0.87f, 1.0f))) {
            return;
        } else {
            if (lock.tryLock()) {
                hmm++;
                if (hmm == 2) {
                    hmm--;
                    return;
                }
            } else {
                return;
            }
        }


        int value = random.nextInt(random.nextInt(random.nextInt(9000, 10000), random.nextInt(21000, 25000)));
        if (random.nextInt(1, 10) == 3) {
            value += random.nextInt(0, 15000);
        } else if (random.nextInt(1, 10) == 3) {
            value -= random.nextInt(0, 15000);
        }
        if (value < 1000) {
            value = random.nextInt(1000, 10000);
        }

        int finalValue = value;
        if (OwoMenu.config().getTriggerHitselect().get()) {
            if (previousHurtTime != -1 && mc.player.hurtTime > previousHurtTime) {
                executor.schedule(() -> {
                    executor.schedule(this::click, finalValue, TimeUnit.MICROSECONDS);
                }, random.nextInt(40, 50), TimeUnit.MILLISECONDS);
            } else {
                executor.schedule(this::click, finalValue, TimeUnit.MICROSECONDS);
            }
        } else {
            executor.schedule(this::click, finalValue, TimeUnit.MICROSECONDS);
        }
        previousHurtTime = mc.player.hurtTime;
    }

    private void click() {
        User32.INSTANCE.PostMessage(OwoMenu.hwnd, 0x0201, new WinDef.WPARAM(0), new WinDef.LPARAM(0));
        executor.schedule(() -> {
            User32.INSTANCE.PostMessage(OwoMenu.hwnd, 0x0202, new WinDef.WPARAM(0), new WinDef.LPARAM(0));
            hmm--;
            lock.unlock();
        }, random.nextInt(1000, 7500), TimeUnit.MICROSECONDS);
    }
}