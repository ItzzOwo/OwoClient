package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.caffeinemc.phosphor.gui.module.BindableModule;
import net.caffeinemc.phosphor.gui.module.RenderableModule;

public class OwoSettingsModule implements RenderableModule, BindableModule {
    @Override
    public String getName() {
        return "Owo Settings";
    }
    @Override
    public String getTabName() {
        return "Settings";
    }
    @Override
    public String getKeybindingText() {
        return "Toggle Owo Menu";
    }

    @Override
    public boolean showKeybinding() {
        return false;
    }

    @Override
    public boolean canBeUsedInMenus() {
        return true;
    }

    @Override
    public int getKeybinding(OwoConfig config) {
        return config.getVisibilityKeybinding();
    }

    @Override
    public void setKeybinding(OwoConfig config, int key) {
        config.setVisibilityKeybinding(key);
    }

    @Override
    public void onKeybindingPressed() {
        OwoMenu.toggleVisibility();
    }

    @Override
    public void render(OwoConfig config) {
        if (ImGui.button("Destruct")) {
            OwoMenu.stopClient();
            OwoMenu.toggleVisibility();
        }
        ImGui.sameLine();
        if (ImGui.button("Hide")) {
            OwoMenu.toggleVisibility();
        }

    }
}

