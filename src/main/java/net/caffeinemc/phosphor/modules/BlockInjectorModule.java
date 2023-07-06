package net.caffeinemc.phosphor.modules;

import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class BlockInjectorModule implements ToggleableModule {

    @Override
    public String getName() {
        return "BlockInjector";
    }
    @Override
    public String getTabName() {
        return "Settings";
    }
    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getBlockInjectorEnabled();
    }
}



