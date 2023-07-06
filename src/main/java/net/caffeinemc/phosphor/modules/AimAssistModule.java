package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;
import net.caffeinemc.phosphor.gui.module.BindableModule;

public class AimAssistModule implements ToggleableModule, RenderableModule, BindableModule  {

    @Override
    public String getName() {
        return "Aim Assist";
    }
    @Override
    public String getTabName() {
        return "Combat";
    }
    @Override
    public int getKeybinding(OwoConfig config) {
        return config.getAimassistKeybinding();
    }
    @Override
    public void setKeybinding(OwoConfig config, int key) {
        config.setAimassistKeybinding(key);
    }

    @Override
    public ImBoolean getToggle(OwoConfig config) {
        return config.getAimAssistEnabled();
    }
    public void render(OwoConfig config) {
        float[] smoothnessArr = { config.getAimAssistSmoothness().floatValue() };
        if (ImGui.sliderFloat("Smoothness", smoothnessArr, 1F, 2F)) {
            config.setAimAssistSmoothness(new ImFloat(smoothnessArr[0]));
        }

        float[] yOffsetArr = { config.getAimAssistYOffset().floatValue()  };
        if (ImGui.sliderFloat("Y Offset", yOffsetArr, -1.0F, 1.0F)) {
            config.setAimAssistYOffset(new ImFloat(yOffsetArr[0]));
        }

        float[] xOffsetArr = { config.getAimAssistXOffset().floatValue() };
        if (ImGui.sliderFloat("X Offset", xOffsetArr, -1.0F, 1.0F)) {
            config.setAimAssistXOffset(new ImFloat(xOffsetArr[0]));
        }

        float[] fovRangeArr = { config.getAimAssistFOVRange().floatValue() };
        if (ImGui.sliderFloat("FOV Range", fovRangeArr, 0F, 360F)) {
            config.setAimAssistFOVRange(new ImFloat(fovRangeArr[0]));
        }

        float[] RangeArr = { config.getAimAssistRange().floatValue() };
        if (ImGui.sliderFloat("Aim Range", RangeArr, 2F, 6F)) {
            config.setAimAssistRange(new ImFloat(RangeArr[0]));
        }
    }
}