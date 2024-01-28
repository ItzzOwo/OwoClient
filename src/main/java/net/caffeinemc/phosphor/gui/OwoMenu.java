package net.caffeinemc.phosphor.gui;

import com.sun.jna.platform.win32.WinDef;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import lombok.Getter;
import net.caffeinemc.phosphor.config.ConfigManager;
import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.module.BaseModule;
import net.caffeinemc.phosphor.gui.module.BindableModule;
import net.caffeinemc.phosphor.gui.module.ToggleableModule;
import net.caffeinemc.phosphor.gui.module.RenderableModule;
import net.caffeinemc.phosphor.modules.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class OwoMenu implements Renderable {
    private static OwoMenu instance;
    public static WinDef.HWND hwnd;
    private static final ConfigManager<OwoConfig> manager = ConfigManager.create(OwoConfig.class, "owo");
    private static final AtomicBoolean clientEnabled = new AtomicBoolean(true);
    private static final AtomicReference<BindableModule> listeningModule = new AtomicReference<>(null);

    @Getter
    private static final List<BaseModule> modules = List.of(
            new AutoJumpResetModule(),
            new AutoSprintResetModule(),
            new AutoTotem(),
            new Criticals(),
            new StrafeModule(),
            new AutoWalkModule(),
            new ForceGhostBlockModule(),
            new AimAssistModule(),
            new BlockInjectorModule(),
            new GamemodeModule(),
            new TriggerModule(),
            new SpeedModule(),
            new FlyModule(),
            new OwoSettingsModule()
    );

    private static OwoMenu getInstance() {
        if (instance == null) {
            instance = new OwoMenu();
        }
        return instance;
    }

    public static void toggleVisibility() {
        if (ImguiLoader.isRendered(getInstance())) {
            ImguiLoader.queueRemove(getInstance());
        } else {
            ImguiLoader.addRenderable(getInstance());
        }
    }

    public static boolean isClientEnabled() {
        return clientEnabled.get();
    }

    public static void stopClient() {
        clientEnabled.set(false);
    }

    public static boolean isListening() {
        return listeningModule.get() != null;
    }

    public static void setKey(int key) {
        BindableModule module = listeningModule.get();
        if (module != null) {
            module.setKeybinding(config(), key);
            listeningModule.set(null);
        }
    }

    public static OwoConfig config() {
        return manager.getConfig();
    }

    public static void saveConfig() {
        manager.saveConfig();
    }

    @Override
    public String getName() {
        return "Owo";
    }

    @Override
    public void render() {
        ImGui.begin("Owo");
        ImGui.separator();

        // Create a set to store unique tab names
        Set<String> uniqueTabNames = new HashSet<>();

        // Iterate over the modules to collect unique tab names
        for (BaseModule module : modules) {
            String tabName = module.getTabName();
            uniqueTabNames.add(tabName);
        }

        // Begin the tab bar
        if (ImGui.beginTabBar("tabs")) {
            // Iterate over the unique tab names
            for (String tabName : uniqueTabNames) {
                // Start a tab with the tab name
                if (ImGui.beginTabItem(tabName)) {
                    // Iterate over the modules and check if their tab name matches the current tab
                    for (BaseModule module : modules) {
                        if (module.getTabName().equals(tabName)) {
                            if (Util.isSimpleModule(module) && module instanceof ToggleableModule toggleableModule) {
                                ImGui.checkbox(module.getName(), toggleableModule.getToggle(config()));
                                continue;
                            }

                            if (module instanceof ToggleableModule toggleableModule) {
                                ImGui.checkbox(toggleableModule.getToggleText(), toggleableModule.getToggle(config()));
                            }

                            if (ImGui.collapsingHeader(Util.getHeader(module))) {
                                if (module instanceof BindableModule bindableModule) {
                                    String text = bindableModule.isEqual(listeningModule.get()) ? "Press a key..." : Util.getKey(bindableModule.getKeybinding(config()));
                                    if (ImGui.button(text)) {
                                        listeningModule.set(bindableModule);
                                    }
                                    ImGui.sameLine();
                                    ImGui.text(bindableModule.getKeybindingText());
                                }

                                if (module instanceof RenderableModule renderableModule) {
                                    renderableModule.render(config());
                                }
                                ImGui.separator();
                            }
                        }
                    }

                    ImGui.endTabItem();
                }
            }

            // End the tab bar
            ImGui.endTabBar();
        }

        ImGui.end();
    }

    @Override
    public Theme getTheme() {
        return new Theme() {
            @Override
            public void preRender() {
                ImGui.getStyle().setWindowMinSize(280, 500);
                ImGui.getStyle().setFramePadding(4, 2);
                ImGui.getStyle().setItemSpacing(6, 2);
                ImGui.getStyle().setItemInnerSpacing(6, 4);
                ImGui.getStyle().setAlpha(0.95f);
                ImGui.getStyle().setWindowRounding(4.0f);
                ImGui.getStyle().setFrameRounding(2.0f);
                ImGui.getStyle().setIndentSpacing(6.0f);
                ImGui.getStyle().setItemInnerSpacing(2, 4);
                ImGui.getStyle().setColumnsMinSpacing(50.0f);
                ImGui.getStyle().setGrabMinSize(14.0f);
                ImGui.getStyle().setGrabRounding(16.0f);
                ImGui.getStyle().setScrollbarSize(12.0f);
                ImGui.getStyle().setScrollbarRounding(16.0f);
                float[][] colors = ImGui.getStyle().getColors();

                // Using #171717 for an even darker gray color
                colors[ImGuiCol.Text] = new float[]{0.7f, 0.7f, 0.7f, 0.78f}; // Dark gray text
                colors[ImGuiCol.TextDisabled] = new float[]{0.5f, 0.5f, 0.5f, 0.28f}; // Gray text
                colors[ImGuiCol.WindowBg] = new float[]{0.09f, 0.09f, 0.09f, 0.85f}; // Even darker window background
                colors[ImGuiCol.Border] = new float[]{0.24f, 0.24f, 0.24f, 0.00f}; // Dark gray border
                colors[ImGuiCol.BorderShadow] = new float[]{0.00f, 0.00f, 0.00f, 0.00f}; // No border shadow
                colors[ImGuiCol.FrameBg] = new float[]{0.09f, 0.09f, 0.09f, 1.0f}; // Even darker frame background
                colors[ImGuiCol.FrameBgHovered] = new float[]{0.42f, 0.42f, 0.42f, 0.78f}; // Lighter frame background when hovered
                colors[ImGuiCol.FrameBgActive] = new float[]{0.42f, 0.42f, 0.42f, 1.0f}; // Lighter frame background when active
                colors[ImGuiCol.TitleBg] = new float[]{0.09f, 0.09f, 0.09f, 1.0f}; // Even darker title background
                colors[ImGuiCol.TitleBgCollapsed] = new float[]{0.09f, 0.09f, 0.09f, 0.75f}; // Even darker title background when collapsed
                colors[ImGuiCol.TitleBgActive] = new float[]{0.42f, 0.42f, 0.42f, 1.0f}; // Lighter title background when active
                colors[ImGuiCol.MenuBarBg] = new float[]{0.09f, 0.09f, 0.09f, 0.47f}; // Even darker menu bar background
                colors[ImGuiCol.ScrollbarBg] = new float[]{0.09f, 0.09f, 0.09f, 1.0f}; // Even darker scrollbar background
                colors[ImGuiCol.ScrollbarGrab] = new float[]{0.18f, 0.18f, 0.18f, 1.0f}; // Darker scrollbar grab
                colors[ImGuiCol.ScrollbarGrabHovered] = new float[]{0.42f, 0.42f, 0.42f, 0.78f}; // Lighter scrollbar grab when hovered
                colors[ImGuiCol.ScrollbarGrabActive] = new float[]{0.42f, 0.42f, 0.42f, 1.0f}; // Lighter scrollbar grab when active
                colors[ImGuiCol.CheckMark] = new float[]{0.8f, 0.8f, 0.8f, 1.0f}; // Brighter checkmark background
                colors[ImGuiCol.SliderGrab] = new float[]{0.28f, 0.28f, 0.28f, 0.14f}; // Darker slider grab
                colors[ImGuiCol.SliderGrabActive] = new float[]{0.42f, 0.42f, 0.42f, 1.0f}; // Lighter slider grab when active
                colors[ImGuiCol.Button] = new float[]{0.28f, 0.28f, 0.28f, 0.14f}; // Darker button
                colors[ImGuiCol.ButtonHovered] = new float[]{0.42f, 0.42f, 0.42f, 0.86f}; // Lighter button when hovered
                colors[ImGuiCol.ButtonActive] = new float[]{0.42f, 0.42f, 0.42f, 1.0f}; // Lighter button when active
                colors[ImGuiCol.Header] = new float[]{0.42f, 0.42f, 0.42f, 0.76f}; // Lighter header
                colors[ImGuiCol.HeaderHovered] = new float[]{0.42f, 0.42f, 0.42f, 0.86f}; // Lighter header when hovered
                colors[ImGuiCol.HeaderActive] = new float[]{0.42f, 0.42f, 0.42f, 1.0f}; // Lighter header when active
                colors[ImGuiCol.Separator] = new float[]{0.28f, 0.28f, 0.28f, 1.0f}; // Darker separator
                colors[ImGuiCol.SeparatorHovered] = new float[]{0.42f, 0.42f, 0.42f, 0.78f}; // Lighter separator when hovered
                colors[ImGuiCol.SeparatorActive] = new float[]{0.42f, 0.42f, 0.42f, 1.0f}; // Lighter separator when active
                colors[ImGuiCol.ResizeGrip] = new float[]{0.28f, 0.28f, 0.28f, 0.04f}; // Darker resize grip
                colors[ImGuiCol.ResizeGripHovered] = new float[]{0.42f, 0.42f, 0.42f, 0.78f}; // Lighter resize grip when hovered
                colors[ImGuiCol.ResizeGripActive] = new float[]{0.42f, 0.42f, 0.42f, 1.0f}; // Lighter resize grip when active
                colors[ImGuiCol.PlotLines] = new float[]{0.7f, 0.7f, 0.7f, 0.63f}; // Dark gray plot lines
                colors[ImGuiCol.PlotLinesHovered] = new float[]{0.7f, 0.7f, 0.7f, 1.0f}; // Dark gray plot lines when hovered
                colors[ImGuiCol.PlotHistogram] = new float[]{0.7f, 0.7f, 0.7f, 0.63f}; // Dark gray plot histogram
                colors[ImGuiCol.PlotHistogramHovered] = new float[]{0.7f, 0.7f, 0.7f, 1.0f}; // Dark gray plot histogram when hovered
                colors[ImGuiCol.TextSelectedBg] = new float[]{0.7f, 0.7f, 0.7f, 0.43f}; // Dark gray text selected background
                colors[ImGuiCol.PopupBg] = new float[]{0.2f, 0.2f, 0.2f, 0.9f}; // Dark gray popup background
                colors[ImGuiCol.Tab] = new float[]{0.1f, 0.1f, 0.1f, 0.78f}; // Darker gray tab
                colors[ImGuiCol.TabHovered] = new float[]{0.4f, 0.4f, 0.4f, 0.78f}; // Light gray tab when hovered
                colors[ImGuiCol.TabActive] = new float[]{0.5f, 0.5f, 0.5f, 1.0f}; // Dark gray tab when active

                ImGui.getStyle().setColors(colors);

                if (ImguiLoader.getCustomFont() != null) {
                    ImGui.pushFont(ImguiLoader.getCustomFont());
                }
            }

            @Override
            public void postRender() {
                if (ImguiLoader.getCustomFont() != null) {
                    ImGui.popFont();
                }
            }
        };
    }
}