package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;
import net.caffeinemc.phosphor.gui.module.BindableModule;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;

public class FlyModule implements ToggleableModule, RenderableModule  {

    @Override
    public String getName() {
        return "Fly";
    }
    @Override
    public String getTabName() {
        return "Movement";
    }
    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getFlyEnabled();
    }

    @Override
    public void onTick() {

    }

    @Override
    public String getInfo() {
        return null;
    }

    public void render(OwoConfig config) {
        ImInt FlyValue = new ImInt(config.getFlyValue());
        ImGui.sliderInt("Fly Speed", FlyValue.getData(), 0, 10);
        config.setFlyValue(FlyValue);
    }
}
