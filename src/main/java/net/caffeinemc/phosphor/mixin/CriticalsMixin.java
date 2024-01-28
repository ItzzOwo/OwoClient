package net.caffeinemc.phosphor.mixin;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.security.SecureRandom;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class CriticalsMixin {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private float lastSwingProgress = 0;
    private final SecureRandom secureRandom = new SecureRandom();
    private boolean pCrit = false;
    private boolean jumped = false;
    private boolean goingToJump = false;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final Lock lock = new ReentrantLock();
    private static int previousHurtTime = -1;

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        if (mc.player == null || !OwoMenu.isClientEnabled() || !OwoMenu.config().getCriticalsEnabled().get() || mc.player.isBlocking() || mc.currentScreen instanceof HandledScreen) {
            return;
        }
        if (mc.player.getAttackCooldownProgress(0) <= 0.05F && lastSwingProgress != 0.0F) {
            Entity entity = getTargetedEntity(mc);
            if (entity instanceof PlayerEntity) {
                if (secureRandom.nextInt(0, 100) <= OwoMenu.config().getCriticalsChance().intValue()) {
                    executor.schedule(() -> {
                        if (mc.player.isOnGround() && !jumped) {
                            jump(mc);
                            lock.unlock();
                            jumped = true;
                        }
                    }, secureRandom.nextInt(secureRandom.nextInt(165, 175), secureRandom.nextInt(225, 235)), TimeUnit.MILLISECONDS);
                }
            }
        }
        lastSwingProgress = mc.player.getAttackCooldownProgress(0);
        if (mc.player.getVelocity().y <= 0.0 && mc.player.getY() % 1.0 < 0.25 && !mc.player.isOnGround()) {
            if (jumped) {
                mc.player.setSprinting(false);
                jumped = false;
            }
        }
        if (OwoMenu.config().getCriticalsPCrit().get()) {
            if (previousHurtTime != -1 && mc.player.hurtTime > previousHurtTime && mc.player.isSprinting()) {
                executor.schedule(() -> {
                    if (!pCrit) {
                        mc.player.setSprinting(false);
                        jumped = false;
                        pCrit = true;
                    }
                }, secureRandom.nextInt(40, 55), TimeUnit.MILLISECONDS);
            } else if (mc.player.isOnGround()) {
                if (pCrit) {
                    mc.player.setSprinting(true);
                    pCrit = false;
                }
            }
        }
        previousHurtTime = mc.player.hurtTime;
    }

    private Entity getTargetedEntity(MinecraftClient mc) {
        HitResult hitResult = mc.crosshairTarget;
        if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult) hitResult).getEntity();
        }
        return null;
    }

    private void jump(MinecraftClient mc) {
        simulateKeyDown(0x20);
        executor.schedule(() -> {
            simulateKeyUp(0x20);
        }, secureRandom.nextInt(10, 75), TimeUnit.MILLISECONDS);
    }

    private static void simulateKeyDown(int keyCode) {
        WinUser.INPUT input = new WinUser.INPUT();
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        input.input.setType("ki");
        input.input.ki.wVk = new WinDef.WORD(keyCode);
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), (WinUser.INPUT[]) input.toArray(1), input.size());
    }

    private static void simulateKeyUp(int keyCode) {
        WinUser.INPUT input = new WinUser.INPUT();
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        input.input.setType("ki");
        input.input.ki.wVk = new WinDef.WORD(keyCode);
        input.input.ki.dwFlags = new WinDef.DWORD(WinUser.KEYBDINPUT.KEYEVENTF_KEYUP);
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), (WinUser.INPUT[]) input.toArray(1), input.size());
    }
}
