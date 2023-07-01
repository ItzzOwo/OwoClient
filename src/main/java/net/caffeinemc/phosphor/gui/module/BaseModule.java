package net.caffeinemc.phosphor.gui.module;

public sealed interface BaseModule permits BindableModule, RenderableModule, ToggleableModule {
    String getName();
}
