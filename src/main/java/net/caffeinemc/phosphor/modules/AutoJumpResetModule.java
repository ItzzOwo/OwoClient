package net.caffeinemc.phosphor.modules;

import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class AutoJumpResetModule implements ToggleableModule {
    @Override
    public String getName() {
        return "AutoJumpReset";
    }
    @Override
    public String getTabName() {
        return "Combat";
    }
    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getAutoJumpResetEnabled();
    }

    @Override
    public void onTick() {

    }

    @Override
    public String getInfo() {
        return null;
    }
}
