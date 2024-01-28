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

    @Override
    public void onTick() {

    }

    @Override
    public String getInfo() {
        return null;
    }

    public void render(OwoConfig config) {
        ImGui.checkbox("Players Only", config.getAimAssistPlayersOnly());

        float[] smoothnessArr = { config.getAimAssistSmooth().floatValue() };
        float smoothnessMinValue = 1F;
        float smoothnessMaxValue = 2F;
        if (ImGui.sliderFloat("Smoothness", smoothnessArr, smoothnessMinValue, smoothnessMaxValue, "%.3f", ImGuiSliderFlags.AlwaysClamp)) {

            if (Math.abs(smoothnessArr[0] - 1.000) < 0.050) {
                smoothnessArr[0] = 1.000f;
            }

            config.setAimAssistSmooth(new ImFloat(smoothnessArr[0]));
        }

        float[] yMultiplierArr = { config.getAimAssistYMultiplier().floatValue()  };
        float yMultiplierMinValue = 0F;
        float yMultiplierMaxValue = 1.2F;
        if (ImGui.sliderFloat("Y Multiplier", yMultiplierArr, yMultiplierMinValue, yMultiplierMaxValue, "%.3f", ImGuiSliderFlags.AlwaysClamp)) {

            if (Math.abs(yMultiplierArr[0] - 0) < 0.050) {
                yMultiplierArr[0] = 0f;
            }

            config.setAimAssistYMultiplier(new ImFloat(yMultiplierArr[0]));
        }

        float[] xMultiplierArr = { config.getAimAssistXMultiplier().floatValue()  };
        float xMultiplierMinValue = 0F;
        float xMultiplierMaxValue = 1.2F;
        if (ImGui.sliderFloat("X Multiplier", xMultiplierArr, xMultiplierMinValue, xMultiplierMaxValue, "%.3f", ImGuiSliderFlags.AlwaysClamp)) {

            if (Math.abs(xMultiplierArr[0] - 0) < 0.050) {
                xMultiplierArr[0] = 0f;
            }

            config.setAimAssistXMultiplier(new ImFloat(xMultiplierArr[0]));
        }

        float[] yOffsetArr = { config.getAimAssistYOffset().floatValue()  };
        if (ImGui.sliderFloat("Y Offset", yOffsetArr, -1.0F, 1.0F)) {

            if (Math.abs(yOffsetArr[0] - 0) < 0.050) {
                yOffsetArr[0] = 0f;
            }

            config.setAimAssistYOffset(new ImFloat(yOffsetArr[0]));
        }

        float[] xOffsetArr = { config.getAimAssistXOffset().floatValue() };
        if (ImGui.sliderFloat("X Offset", xOffsetArr, -1.0F, 1.0F)) {

            if (Math.abs(xOffsetArr[0] - 0) < 0.050) {
                xOffsetArr[0] = 0f;
            }

            config.setAimAssistXOffset(new ImFloat(xOffsetArr[0]));
        }

        float[] fovRangeArr = { config.getAimAssistFOVRange().floatValue() };
        if (ImGui.sliderFloat("FOV Range", fovRangeArr, 0F, 360F)) {
            float minAngle = (float) Math.round(fovRangeArr[0] / 45) * 45;
            if (Math.abs(fovRangeArr[0] - minAngle) < 0.05) {
                fovRangeArr[0] = minAngle;
            }
            config.setAimAssistFOVRange(new ImFloat(fovRangeArr[0]));
        }

        float[] rangeArr = { config.getAimAssistRange().floatValue() };
        if (ImGui.sliderFloat("Max Distance", rangeArr, 0F, 360F)) {
            float minAngle = (float) Math.round(rangeArr[0] / 45) * 45;
            if (Math.abs(rangeArr[0] - minAngle) < 0.05) {
                rangeArr[0] = minAngle;
            }

            config.setAimAssistRange(new ImFloat(rangeArr[0]));
        }
    }
}