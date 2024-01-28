package net.caffeinemc.phosphor.config;

import imgui.type.ImBoolean;
import imgui.type.ImInt;
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
    private ImBoolean autoSprintResetEnabled = new ImBoolean(false);
    private ImBoolean autoJumpResetEnabled = new ImBoolean(false);
    private ImBoolean autoWalkEnabled = new ImBoolean(false);
    private ImBoolean blockInjectorEnabled = new ImBoolean(false);
    private ImBoolean SlimeBlocksEnabled = new ImBoolean(false);
    private ImBoolean DoorBlocksEnabled = new ImBoolean(false);
    private ImBoolean CobblestoneBlocksEnabled = new ImBoolean(false);
    private ImBoolean FenceBlocksEnabled = new ImBoolean(false);
    private ImBoolean AirBlockEnabled = new ImBoolean(false);
    private ImBoolean CommandBlocksEnabled = new ImBoolean(false);
    private ImBoolean GameModeEnabled = new ImBoolean(false);
    private ImBoolean SpectatorEnabled = new ImBoolean(false);
    private ImBoolean AdventureEnabled = new ImBoolean(false);
    private ImBoolean SurvivalEnabled = new ImBoolean(false);
    private ImBoolean CreativeEnabled = new ImBoolean(false);
    private ImBoolean triggerEnabled = new ImBoolean(false);
    private ImBoolean permTriggerEnabled = new ImBoolean(true);
    private ImBoolean triggerWeaponOnly = new ImBoolean( true);
    private ImBoolean triggerHitselect = new ImBoolean( true);
    private ImBoolean SpeedEnabled = new ImBoolean( false);
    private ImInt SpeedValue = new ImInt(1);
    private ImFloat aimAssistSmooth = new ImFloat(2.0F);
    private ImFloat aimAssistYMultiplier = new ImFloat(1.0F);
    private ImFloat aimAssistXMultiplier = new ImFloat(1.0F);
    private ImFloat aimAssistYOffset = new ImFloat(0F);
    private ImFloat aimAssistXOffset = new ImFloat(0F);
    private ImFloat aimAssistFOVRange = new ImFloat(120F);
    private ImFloat aimAssistRange = new ImFloat(4.5F);
    private ImBoolean aimAssistEnabled = new ImBoolean(false);
    private ImBoolean aimAssistPlayersOnly = new ImBoolean(false);
    private ImBoolean CriticalsEnabled = new ImBoolean(false);
    private ImBoolean CriticalsPCrit = new ImBoolean(false);
    private ImInt CriticalsChance = new ImInt(50);
    private ImBoolean StrafeEnabled = new ImBoolean(false);
    private ImBoolean ForceGhostBlock = new ImBoolean(false);
    private ImBoolean AutoTotemEnabled = new ImBoolean(false);
    private ImBoolean FlyEnabled = new ImBoolean(false);
    private ImInt FlyValue = new ImInt(1);
    private int triggerKeybinding = GLFW.GLFW_KEY_X;
    private int visibilityKeybinding = GLFW.GLFW_KEY_F8;
    private int AimassistKeybinding = GLFW.GLFW_KEY_U;
    private int SpeedKeybinding = GLFW.GLFW_KEY_I;
}