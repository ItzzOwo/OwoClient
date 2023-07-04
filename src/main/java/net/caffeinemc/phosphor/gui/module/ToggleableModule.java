package net.caffeinemc.phosphor.gui.module;

import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;

public non-sealed interface ToggleableModule extends BaseModule {
    ImBoolean getToggle(OwoConfig config);

    default String getToggleText() {
        return getName();
    }
}
