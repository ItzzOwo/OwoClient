package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.BindableModule;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;

public class SpeedModule implements ToggleableModule, RenderableModule, BindableModule {

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public String getTabName() {
        return "Movement";
    }

    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getSpeedEnabled();
    }

    @Override
    public void onTick() {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public int getKeybinding(OwoConfig config) {
        return config.getSpeedKeybinding();
    }
    @Override
    public void setKeybinding(OwoConfig config, int key) {
        config.setSpeedKeybinding(key);
    }

    public void render(OwoConfig config) {
        ImInt speedValue = new ImInt(config.getSpeedValue());
        ImGui.sliderInt("Speed Value", speedValue.getData(), 0, 500);
        config.setSpeedValue(speedValue);
    }
}
