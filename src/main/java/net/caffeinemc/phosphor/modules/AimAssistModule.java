package net.caffeinemc.phosphor.modules;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;
import net.caffeinemc.phosphor.gui.module.BindableModule;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static List<String> items = new ArrayList<>(Arrays.asList());
    public static ImString target = new ImString(256);
    public static ImInt currentStyle = new ImInt(0); // currentStyle is a single integer


    public void render(OwoConfig config) {
        ImGui.checkbox("Players Only", config.getAimAssistPlayersOnly());
        ImGui.checkbox("Anti-Bot (Broken)", config.getAimAssistAntiBot());

        renderSliderFloat2("Smoothness", config.getAimAssistSmoothness(), 1.0f, 2.0f);
        renderSliderFloat2("Y-Axis Multiplier", config.getAimAssistYMultiplier(), 0.0f, 1.2f);
        renderSliderFloat2("X-Axis Multiplier", config.getAimAssistXMultiplier(), 0.0f, 1.2f);
        renderSliderFloat("Y-Axis Offset", config.getAimAssistYOffset(), -1.0f, 1.0f);
        renderSliderFloat("X-Axis Offset", config.getAimAssistXOffset(), -1.0f, 1.0f);
        renderSliderFloat("FOV Range", config.getAimAssistFOVRange(), 0.0f, 360.0f);
        renderSliderFloat("Aim Range", config.getAimAssistAimRange(), 3.5f, 12.0f);

        ImGui.inputText("Target Player", target, 256);
        for (String item : items) {
            if (item.startsWith(target.get())) {
                ImGui.text(item); // Suggest this item
            }
        }

        String[] styles = {"Normal", "Dynamic"};
        ImGui.combo("Style", currentStyle, styles);
    }

    private void renderSliderFloat(String label, ImFloat value, float minValue, float maxValue) {
        float[] arr = {value.get()};
        if (ImGui.sliderFloat(label, arr, minValue, maxValue)) {
            value.set(MathHelper.clamp(arr[0], minValue, maxValue));
        }
    }

    private void renderSliderFloat2(String label, ImFloat[] values, float minValue, float maxValue) {
        float[] arr = {values[0].get(), values[1].get()};
        if (ImGui.sliderFloat2(label, arr, minValue, maxValue, "%.3f", ImGuiSliderFlags.AlwaysClamp)) {
            values[0].set(MathHelper.clamp(arr[0], minValue, maxValue));
            values[1].set(MathHelper.clamp(arr[1], minValue, maxValue));

            if (Math.abs(values[0].get() - 1.0) < 0.05) {
                values[0].set(1.0f);
            }
            if (Math.abs(values[1].get() - 1.0) < 0.05) {
                values[1].set(1.0f);
            }
        }
    }

}