package net.caffeinemc.phosphor.gui;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImFloat;
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

    private static final ConfigManager<OwoConfig> manager = ConfigManager.create(OwoConfig.class, "owo");
    private static final AtomicBoolean clientEnabled = new AtomicBoolean(true);
    private static final AtomicReference<BindableModule> listeningModule = new AtomicReference<>(null);


    @Getter
    private static final List<BaseModule> modules = List.of(
            new AutoJumpResetModule(),
            new AutoSprintResetModule(),
            new ForceCrashModule(),
            new AutoWalkModule(),
            new ForceGhostBlockModule(),
            new AimAssistModule(),
            new BlockInjectorModule(),
            new GamemodeModule(),
            new TriggerModule(),
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
        ImGui.text("Welcome to Owo client!");
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
                float[][] colors = ImGui.getStyle().getColors();
                colors[ImGuiCol.Text] = new float[]{0.60f, 0.70f, 0.80f, 1.00f};
                colors[ImGuiCol.TextDisabled] = new float[]{0.40f, 0.50f, 0.60f, 1.00f};
                colors[ImGuiCol.WindowBg] = new float[]{0.15f, 0.19f, 0.25f, 1.00f};
                colors[ImGuiCol.ChildBg] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
                colors[ImGuiCol.PopupBg] = new float[]{0.12f, 0.12f, 0.18f, 0.94f};
                colors[ImGuiCol.Border] = new float[]{0.50f, 0.50f, 0.58f, 0.50f};
                colors[ImGuiCol.BorderShadow] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
                colors[ImGuiCol.FrameBg] = new float[]{0.33f, 0.49f, 0.61f, 0.59f};
                colors[ImGuiCol.FrameBgHovered] = new float[]{0.33f, 0.49f, 0.61f, 0.85f};
                colors[ImGuiCol.FrameBgActive] = new float[]{0.33f, 0.49f, 0.61f, 1.00f};
                colors[ImGuiCol.TitleBg] = new float[]{0.08f, 0.12f, 0.16f, 1.00f};
                colors[ImGuiCol.TitleBgActive] = new float[]{0.18f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.TitleBgCollapsed] = new float[]{0.00f, 0.00f, 0.00f, 0.51f};
                colors[ImGuiCol.MenuBarBg] = new float[]{0.12f, 0.12f, 0.14f, 1.00f};
                colors[ImGuiCol.ScrollbarBg] = new float[]{0.04f, 0.04f, 0.06f, 0.53f};
                colors[ImGuiCol.ScrollbarGrab] = new float[]{0.41f, 0.41f, 0.51f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabHovered] = new float[]{0.51f, 0.51f, 0.61f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabActive] = new float[]{0.61f, 0.61f, 0.71f, 1.00f};
                colors[ImGuiCol.CheckMark] = new float[]{0.33f, 0.49f, 0.61f, 1.00f};
                colors[ImGuiCol.SliderGrab] = new float[]{0.27f, 0.43f, 0.55f, 1.00f};
                colors[ImGuiCol.SliderGrabActive] = new float[]{0.33f, 0.49f, 0.61f, 1.00f};
                colors[ImGuiCol.Button] = new float[]{0.33f, 0.49f, 0.61f, 0.45f};
                colors[ImGuiCol.ButtonHovered] = new float[]{0.33f, 0.49f, 0.61f, 1.00f};
                colors[ImGuiCol.ButtonActive] = new float[]{0.27f, 0.43f, 0.55f, 1.00f};
                colors[ImGuiCol.Header] = new float[]{0.33f, 0.49f, 0.61f, 0.32f};
                colors[ImGuiCol.HeaderHovered] = new float[]{0.33f, 0.49f, 0.61f, 0.73f};
                colors[ImGuiCol.HeaderActive] = new float[]{0.33f, 0.49f, 0.61f, 1.00f};
                colors[ImGuiCol.Separator] = new float[]{0.52f, 0.54f, 0.62f, 1.00f};
                colors[ImGuiCol.SeparatorHovered] = new float[]{0.30f, 0.52f, 0.61f, 0.78f};
                colors[ImGuiCol.SeparatorActive] = new float[]{0.26f, 0.51f, 0.60f, 1.00f};
                colors[ImGuiCol.ResizeGrip] = new float[]{0.33f, 0.49f, 0.61f, 0.21f};
                colors[ImGuiCol.ResizeGripHovered] = new float[]{0.33f, 0.49f, 0.61f, 0.79f};
                colors[ImGuiCol.ResizeGripActive] = new float[]{0.33f, 0.49f, 0.61f, 1.00f};
                colors[ImGuiCol.Tab] = new float[]{0.21f, 0.34f, 0.46f, 0.85f};
                colors[ImGuiCol.TabHovered] = new float[]{0.33f, 0.49f, 0.61f, 0.85f};
                colors[ImGuiCol.TabActive] = new float[]{0.26f, 0.40f, 0.51f, 1.00f};
                colors[ImGuiCol.TabUnfocused] = new float[]{0.07f, 0.15f, 0.21f, 0.97f};
                colors[ImGuiCol.TabUnfocusedActive] = new float[]{0.14f, 0.24f, 0.33f, 1.00f};
                colors[ImGuiCol.DockingPreview] = new float[]{0.33f, 0.49f, 0.61f, 0.70f};
                colors[ImGuiCol.DockingEmptyBg] = new float[]{0.20f, 0.20f, 0.20f, 1.00f};
                colors[ImGuiCol.PlotLines] = new float[]{0.61f, 0.61f, 0.61f, 1.00f};
                colors[ImGuiCol.PlotLinesHovered] = new float[]{0.35f, 0.64f, 0.73f, 1.00f};
                colors[ImGuiCol.PlotHistogram] = new float[]{0.00f, 0.29f, 0.51f, 1.00f};
                colors[ImGuiCol.PlotHistogramHovered] = new float[]{0.00f, 0.40f, 0.60f, 1.00f};
                colors[ImGuiCol.TableHeaderBg] = new float[]{0.20f, 0.20f, 0.21f, 1.00f};
                colors[ImGuiCol.TableBorderStrong] = new float[]{0.35f, 0.35f, 0.39f, 1.00f};
                colors[ImGuiCol.TableBorderLight] = new float[]{0.25f, 0.25f, 0.27f, 1.00f};
                colors[ImGuiCol.TableRowBg] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
                colors[ImGuiCol.TableRowBgAlt] = new float[]{0.06f, 0.06f, 0.06f, 0.97f};
                colors[ImGuiCol.TextSelectedBg] = new float[]{0.33f, 0.49f, 0.61f, 0.35f};
                colors[ImGuiCol.DragDropTarget] = new float[]{0.00f, 0.00f, 1.00f, 0.90f};
                colors[ImGuiCol.NavHighlight] = new float[]{0.33f, 0.49f, 0.61f, 1.00f};
                colors[ImGuiCol.NavWindowingHighlight] = new float[]{1.00f, 1.00f, 1.00f, 0.70f};
                colors[ImGuiCol.NavWindowingDimBg] = new float[]{0.80f, 0.80f, 0.80f, 0.20f};
                colors[ImGuiCol.ModalWindowDimBg] = new float[]{0.80f, 0.80f, 0.80f, 0.35f};
                ImGui.getStyle().setColors(colors);

                ImGui.getStyle().setWindowRounding(8);
                ImGui.getStyle().setFrameRounding(4);
                ImGui.getStyle().setGrabRounding(4);
                ImGui.getStyle().setPopupRounding(4);
                ImGui.getStyle().setScrollbarRounding(4);
                ImGui.getStyle().setTabRounding(4);

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