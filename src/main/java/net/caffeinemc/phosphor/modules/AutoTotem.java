package net.caffeinemc.phosphor.modules;

import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;


public class AutoTotem implements ToggleableModule {
    @Override
    public String getName() {
        return "AutoTotem";
    }

    @Override
    public String getTabName() {
        return "Combat";
    }

    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getAutoTotemEnabled();
    }

    @Override
    public void onTick() {

    }

    @Override
    public String getInfo() {
        return null;
    }
}