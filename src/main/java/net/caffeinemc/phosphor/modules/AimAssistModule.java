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
    public void render(OwoConfig config) {
        ImGui.checkbox("Players Only", config.getAimAssistPlayersOnly());

        float[] smoothnessArr = { config.getAimAssistSmoothness()[0].floatValue(), config.getAimAssistSmoothness()[1].floatValue()  };
        float smoothnessMinValue = 1F;
        float smoothnessMaxValue = 2F;
        if (ImGui.sliderFloat2("Smoothness", smoothnessArr, smoothnessMinValue, smoothnessMaxValue, "%.3f", ImGuiSliderFlags.AlwaysClamp)) {
            smoothnessArr[0] = Math.min(smoothnessArr[0], smoothnessArr[1]);
            smoothnessArr[1] = Math.max(smoothnessArr[1], smoothnessArr[0]);

            if (Math.abs(smoothnessArr[0] - 1.000) < 0.050) {
                smoothnessArr[0] = 1.000f;
            }
            if (Math.abs(smoothnessArr[1] - 1.000) < 0.050) {
                smoothnessArr[1] = 1.000f;
            }

            config.setAimAssistSmoothness(new ImFloat[] { new ImFloat(smoothnessArr[0]), new ImFloat(smoothnessArr[1]) });
        }

        float[] yMultiplierArr = { config.getAimAssistYMultiplier()[0].floatValue(), config.getAimAssistYMultiplier()[1].floatValue()  };
        float yMultiplierMinValue = 0F;
        float yMultiplierMaxValue = 1.2F;
        if (ImGui.sliderFloat2("Y Multiplier", yMultiplierArr, yMultiplierMinValue, yMultiplierMaxValue, "%.3f", ImGuiSliderFlags.AlwaysClamp)) {
            yMultiplierArr[0] = Math.min(yMultiplierArr[0], yMultiplierArr[1]);
            yMultiplierArr[1] = Math.max(yMultiplierArr[1], yMultiplierArr[0]);

            if (Math.abs(yMultiplierArr[0] - 1.000) < 0.050) {
                yMultiplierArr[0] = 1.000f;
            }
            if (Math.abs(yMultiplierArr[1] - 1.000) < 0.050) {
                yMultiplierArr[1] = 1.000f;
            }

            config.setAimAssistYMultiplier(new ImFloat[] { new ImFloat(yMultiplierArr[0]), new ImFloat(yMultiplierArr[1]) });
        }

        float[] xMultiplierArr = { config.getAimAssistXMultiplier()[0].floatValue(), config.getAimAssistXMultiplier()[1].floatValue()  };
        float xMultiplierMinValue = 0F;
        float xMultiplierMaxValue = 1.2F;
        if (ImGui.sliderFloat2("X Multiplier", xMultiplierArr, xMultiplierMinValue, xMultiplierMaxValue, "%.3f", ImGuiSliderFlags.AlwaysClamp)) {
            xMultiplierArr[0] = Math.min(xMultiplierArr[0], xMultiplierArr[1]);
            xMultiplierArr[1] = Math.max(xMultiplierArr[1], xMultiplierArr[0]);

            if (Math.abs(xMultiplierArr[0] - 1.000) < 0.050) {
                xMultiplierArr[0] = 1.000f;
            }
            if (Math.abs(xMultiplierArr[1] - 1.000) < 0.050) {
                xMultiplierArr[1] = 1.000f;
            }

            config.setAimAssistXMultiplier(new ImFloat[] { new ImFloat(xMultiplierArr[0]), new ImFloat(xMultiplierArr[1]) });
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

        float[] fovRangeArr = { config.getAimAssistRange().floatValue() };
        if (ImGui.sliderFloat("FOV Range", fovRangeArr, 0F, 360F)) {
            float minAngle = (float) Math.round(fovRangeArr[0] / 45) * 45;
            if (Math.abs(fovRangeArr[0] - minAngle) < 0.05) {
                fovRangeArr[0] = minAngle;
            }

            config.setAimAssistRange(new ImFloat(fovRangeArr[0]));
        }
    }
}