package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;
public class Criticals implements ToggleableModule, RenderableModule {

    @Override
    public String getName() {
        return "Criticals";
    }
    @Override
    public String getTabName() {
        return "Combat";
    }
    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getCriticalsEnabled();
    }

    @Override
    public void onTick() {

    }

    @Override
    public String getInfo() {
        return null;
    }

    public void render(OwoConfig config) {
        ImGui.checkbox("P-Crit", config.getCriticalsPCrit());
        int[] sliderArr = { config.getCriticalsChance().intValue()  };
        if (ImGui.sliderInt("Crit Chance", sliderArr, 0, 100)) {
            config.setCriticalsChance(new ImInt(sliderArr[0]));
        }
    }
}
