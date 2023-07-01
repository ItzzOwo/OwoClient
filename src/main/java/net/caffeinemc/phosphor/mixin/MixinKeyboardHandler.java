package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.gui.OwoMenu;
import net.caffeinemc.phosphor.gui.module.BindableModule;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Keyboard.class)
public class MixinKeyboardHandler {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(method = "onKey", at = @At("HEAD"))
    public void onKeyPressed(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (OwoMenu.isClientEnabled() && window == this.mc.getWindow().getHandle() && action == GLFW.GLFW_PRESS) {
            if (OwoMenu.isListening()) {
                OwoMenu.setKey(key);
            } else {
                List<BindableModule> modules = OwoMenu.getModules().stream()
                        .filter(BindableModule.class::isInstance)
                        .map(BindableModule.class::cast)
                        .filter(module -> module.getKeybinding(OwoMenu.config()) == key)
                        .toList();

                if ((this.mc.currentScreen == null || this.mc.currentScreen.passEvents) && !(mc.currentScreen instanceof HandledScreen)) {
                    modules.forEach(BindableModule::onKeybindingPressed);
                } else {
                    modules.stream().filter(BindableModule::canBeUsedInMenus).forEach(BindableModule::onKeybindingPressed);
                }
            }
        }
    }
}
