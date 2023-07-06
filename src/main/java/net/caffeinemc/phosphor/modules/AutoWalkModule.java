package net.caffeinemc.phosphor.modules;

import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class AutoWalkModule implements ToggleableModule {
    @Override
    public String getName() {
        return "AutoWalk";
    }
    @Override
    public String getTabName() {
        return "Settings";
    }
    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getAutoWalkEnabled();
    }
}
