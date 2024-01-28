package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class BlockInjectorModule implements ToggleableModule, RenderableModule {

    @Override
    public String getName() {
        return "BlockInjector";
    }
    @Override
    public String getTabName() {
        return "Utils";
    }
    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getBlockInjectorEnabled();
    }

    @Override
    public void onTick() {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void render(OwoConfig config) {
        ImGui.checkbox("Slime", config.getSlimeBlocksEnabled());
        ImGui.checkbox("Door", config.getDoorBlocksEnabled());
        ImGui.checkbox("Fence", config.getFenceBlocksEnabled());
        ImGui.checkbox("Command Block", config.getCommandBlocksEnabled());

    }
}



