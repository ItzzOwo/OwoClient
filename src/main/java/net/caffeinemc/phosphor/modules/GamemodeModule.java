package net.caffeinemc.phosphor.modules;

import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class GamemodeModule implements ToggleableModule {

    @Override
    public String getName() {
        return "FakeGamemode";
    }

    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getGameModeEnabled();
    }
}



