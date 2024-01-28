package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;
import net.caffeinemc.phosphor.gui.module.BindableModule;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;

public class StrafeModule implements ToggleableModule  {

    @Override
    public String getName() {
        return "Strafe";
    }
    @Override
    public String getTabName() {
        return "Movement";
    }
    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getStrafeEnabled();
    }

    @Override
    public void onTick() {

    }

    @Override
    public String getInfo() {
        return null;
    }
}