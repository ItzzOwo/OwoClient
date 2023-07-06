package net.caffeinemc.phosphor.config;

import imgui.type.ImBoolean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import imgui.type.ImFloat;
import org.lwjgl.glfw.GLFW;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwoConfig {
    private ImBoolean ForceCrashEnabled = new ImBoolean((false));
    private ImBoolean autoSprintResetEnabled = new ImBoolean(false);
    private ImBoolean autoJumpResetEnabled = new ImBoolean(false);
    private ImBoolean autoWalkEnabled = new ImBoolean(false);
    private ImBoolean blockInjectorEnabled = new ImBoolean(false);
    private ImBoolean SlimeBlocksEnabled = new ImBoolean(false);
    private ImBoolean DoorBlocksEnabled = new ImBoolean(false);
    private ImBoolean FenceBlocksEnabled = new ImBoolean(false);
    private ImBoolean CommandBlocksEnabled = new ImBoolean(false);
    private ImBoolean GameModeEnabled = new ImBoolean(false);
    private ImBoolean SpectatorEnabled = new ImBoolean(false);
    private ImBoolean AdventureEnabled = new ImBoolean(false);
    private ImBoolean SurvivalEnabled = new ImBoolean(false);
    private ImBoolean CreativeEnabled = new ImBoolean(false);
    private ImBoolean triggerEnabled = new ImBoolean(false);
    private ImBoolean permTriggerEnabled = new ImBoolean(true);
    private ImBoolean triggerWeaponOnly = new ImBoolean( true);
    private ImFloat aimAssistSmoothness = new ImFloat(1F);
    private ImFloat aimAssistYOffset = new ImFloat(0F);
    private ImFloat aimAssistXOffset = new ImFloat(0F);
    private ImFloat aimAssistFOVRange = new ImFloat(360F);
    private ImFloat aimAssistRange = new ImFloat(3.5F);
    private ImBoolean aimAssistEnabled = new ImBoolean(false);
    private int triggerKeybinding = GLFW.GLFW_KEY_X;
    private int visibilityKeybinding = GLFW.GLFW_KEY_F8;
    private int AimassistKeybinding = GLFW.GLFW_KEY_U;
}