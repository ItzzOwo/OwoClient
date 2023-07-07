package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class GamemodeModule implements ToggleableModule, RenderableModule {

    @Override
    public String getName() {
        return "FakeGamemode";
    }

    @Override
    public String getTabName() {
        return "Utils";
    }

    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getGameModeEnabled();
    }

    @Override
    public void render(OwoConfig config) {
        ImGui.checkbox("Survival", config.getSurvivalEnabled());
        ImGui.checkbox("Creative", config.getCreativeEnabled());
        ImGui.checkbox("Spectator", config.getSpectatorEnabled());
        ImGui.checkbox("Adventure", config.getAdventureEnabled());
    }
}
