package net.caffeinemc.phosphor.gui;

import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.gui.module.BaseModule;
import net.caffeinemc.phosphor.gui.module.BindableModule;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;
import net.minecraft.client.util.InputUtil;

import java.util.Locale;

public class Util {
    public static void reverse(ImBoolean bool) {
        bool.set(!bool.get());
    }

    public static String getKey(int key) {
        return InputUtil.Type.KEYSYM.createFromCode(key).getLocalizedText().getString().toUpperCase(Locale.ROOT);
    }

    public static String getHeader(BaseModule module) {
        //StringBuilder builder = new StringBuilder(module.getName());
//
        //if (module instanceof ToggleableModule toggleableModule && toggleableModule.getToggle(RadiumMenu.config()).get()) {
        //    builder.append(" (ON)");
        //}
//
        //if (module instanceof BindableModule bindableModule && bindableModule.showKeybinding()) {
        //    builder.append(" [").append(getKey(bindableModule.getKeybinding(RadiumMenu.config()))).append("]");
        //}
//
        //return builder.toString();
        return module.getName();
    }

    public static boolean isSimpleModule(BaseModule module) {
        return !(module instanceof BindableModule || module instanceof RenderableModule);
    }

    private Util() {
    }
}
