package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class ForceGhostBlockModule implements ToggleableModule, RenderableModule {

    @Override
    public String getName() {
        return "ForceGhostBlock";
    }
    @Override
    public String getTabName() {
        return "Utils";
    }
    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getForceGhostBlock();
    }

    @Override
    public void render(OwoConfig config) {
        ImGui.checkbox("Slime", config.getSlimeBlocksEnabled());
        ImGui.checkbox("Cobblestone", config.getCobblestoneBlocksEnabled());
        ImGui.checkbox("Fence", config.getFenceBlocksEnabled());
        ImGui.checkbox("Command Block", config.getCommandBlocksEnabled());
        ImGui.checkbox("Air", config.getAirBlockEnabled());
    }
}



