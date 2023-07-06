package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.BindableModule;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class TriggerModule implements BindableModule, RenderableModule, ToggleableModule {
    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getTriggerEnabled();
    }

    @Override
    public int getKeybinding(OwoConfig config) {
        return config.getTriggerKeybinding();
    }

    @Override
    public void setKeybinding(OwoConfig config, int key) {
        config.setTriggerKeybinding(key);
    }

    @Override
    public String getName() {
        return "TriggerBot";
    }
    @Override
    public String getTabName() {
        return "Combat";
    }
    @Override
    public void render(OwoConfig config) {
        ImGui.checkbox("Permanent Trigger", config.getPermTriggerEnabled());
        ImGui.checkbox("Weapon Only", config.getTriggerWeaponOnly());
    }
}
