package net.caffeinemc.phosphor.modules;

import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class ForceCrashModule implements ToggleableModule {

    @Override
    public String getName() {
        return "ForceCrash";
    }

    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getForceCrashEnabled();
    }
}



