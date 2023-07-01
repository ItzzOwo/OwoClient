package net.caffeinemc.phosphor.config;

import imgui.ImGui;
import imgui.flag.ImGuiComboFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwoConfig {
    private ImBoolean autoJumpResetEnabled = new ImBoolean(false);
    private int visibilityKeybinding = GLFW.GLFW_KEY_F8;
}