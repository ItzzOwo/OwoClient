package net.caffeinemc.phosphor.mixin;

import com.sun.jna.platform.win32.User32;
import net.caffeinemc.phosphor.gui.ImguiLoader;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {
    @Shadow
    @Final
    private long handle;

    @Inject(at = @At("TAIL"), method = "<init>", remap = false)
    private void onGLFWInit(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, String videoMode, String title, CallbackInfo ci) {
        ImguiLoader.onGlfwInit(handle);
        OwoMenu.hwnd = User32.INSTANCE.FindWindow("glfw30", null);
        OwoMenu.toggleVisibility();
    }
}