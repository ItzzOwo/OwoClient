package net.caffeinemc.phosphor.gui.module;

import net.caffeinemc.phosphor.config.OwoConfig;

public non-sealed interface RenderableModule extends BaseModule {
    void render(OwoConfig config);
}
