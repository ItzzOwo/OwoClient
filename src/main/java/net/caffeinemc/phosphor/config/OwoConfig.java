package net.caffeinemc.phosphor.config;

import imgui.type.ImBoolean;
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
    private ImBoolean autoSprintResetEnabled = new ImBoolean(false);
    private ImBoolean autoJumpResetEnabled = new ImBoolean(false);
    private ImBoolean blockInjectorEnabled = new ImBoolean(false);
    private ImBoolean GameModeEnabled = new ImBoolean(false);
    private ImBoolean triggerEnabled = new ImBoolean(false);
    private ImBoolean permTriggerEnabled = new ImBoolean(true);
    private ImBoolean triggerWeaponOnly = new ImBoolean( true);
    private int triggerKeybinding = GLFW.GLFW_KEY_X;
    private int visibilityKeybinding = GLFW.GLFW_KEY_F8;
}