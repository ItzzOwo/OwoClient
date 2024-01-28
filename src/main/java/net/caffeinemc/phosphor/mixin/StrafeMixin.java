package net.caffeinemc.phosphor.mixin;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.security.SecureRandom;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// ... (other parts of your code remain the same)

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class StrafeMixin {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final SecureRandom secureRandom = new SecureRandom();

    private boolean lookingAtEntity() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            Entity cameraEntity = client.getCameraEntity();
            if (cameraEntity instanceof PlayerEntity) {
                HitResult hitResult = client.crosshairTarget;
                if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                    Entity targetEntity = ((EntityHitResult) hitResult).getEntity();
                    if (targetEntity instanceof PlayerEntity) {
                        double distance = cameraEntity.getPos().distanceTo(targetEntity.getPos());
                        return distance <= 3.5;
                    }
                }
            }
        }
        return false;
    }

    private boolean moving = false;

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || !OwoMenu.isClientEnabled() || mc.currentScreen instanceof HandledScreen || !OwoMenu.config().getStrafeEnabled().get()) {
            return;
        }
        if (lookingAtEntity() && !moving) {
            if (secureRandom.nextInt(0, 100) < 30) {
                int strafeTime = secureRandom.nextInt(100, 600);
                if (secureRandom.nextInt(0, 5) < 2)
                {
                    strafeTime = secureRandom.nextInt(10, 100);
                }
                if (secureRandom.nextBoolean()) {
                    moving = true;
                    simulateKeyDown('A');
                    executor.schedule(() -> {
                        simulateKeyUp('A');
                        moving = false;
                    }, strafeTime, TimeUnit.MILLISECONDS);
                } else {
                    moving = true;
                    simulateKeyDown('D');
                    executor.schedule(() -> {
                        simulateKeyUp('D');
                        moving = false;
                    }, strafeTime, TimeUnit.MILLISECONDS);
                }
            }
        }
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

