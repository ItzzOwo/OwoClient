package net.caffeinemc.phosphor.gui;

import imgui.ImFont;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class ImguiLoader {
    private static final Set<Renderable> renderstack = new HashSet<>();
    private static final Set<Renderable> toRemove = new HashSet<>();

    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    @Getter
    private static ImFont customFont;

    public static void onGlfwInit(long handle) {
        initializeImGui();
        imGuiGlfw.init(handle,true);
        imGuiGl3.init();
    }

    public static void onFrameRender() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        //user render code

        for (Renderable renderable : renderstack) {
            MinecraftClient.getInstance().getProfiler().push("ImGui Render/"+renderable.getName());
            renderable.getTheme().preRender();
            renderable.render();
            renderable.getTheme().postRender();
            MinecraftClient.getInstance().getProfiler().pop();
        }

        //end of user code

        ImGui.render();
        endFrame();
    }

    private static void initializeImGui() {
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename(null);                               // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);     // Enable Docking
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);   // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(true);


        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        fontAtlas.addFontDefault();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

        try (InputStream is = ImguiLoader.class.getClassLoader().getResourceAsStream("JetBrainsMono-Regular.ttf")) {
            if (is != null) {
                customFont = fontAtlas.addFontFromMemoryTTF(is.readAllBytes(), 16);
            }
        } catch (IOException ignored) {
            // do nothing, we already have font :3
        }

        fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
        fontConfig.setPixelSnapH(true);

        fontConfig.destroy();

        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final ImGuiStyle style = ImGui.getStyle();
            style.setWindowRounding(0.0f);
            style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
        }
    }

    private static void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }

        if (!toRemove.isEmpty()) {
            toRemove.forEach(renderstack::remove);
            toRemove.clear();
        }
    }

    public static void addRenderable(Renderable renderable) {
        renderstack.add(renderable);
    }

    public static void queueRemove(Renderable renderable) {
        toRemove.add(renderable);
    }

    public static boolean isRendered(Renderable renderable) {
        return renderstack.contains(renderable);
    }

    private ImguiLoader() {}
}
