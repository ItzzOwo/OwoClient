package net.caffeinemc.phosphor.gui.module;

import net.caffeinemc.phosphor.config.OwoConfig;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.caffeinemc.phosphor.gui.Util;

import java.util.Objects;

public non-sealed interface BindableModule extends BaseModule {
    int getKeybinding(OwoConfig config);

    void setKeybinding(OwoConfig config, int key);

    default void onKeybindingPressed() {
        if (this instanceof ToggleableModule toggleableModule) {
            Util.reverse(toggleableModule.getToggle(OwoMenu.config()));
        }
    }

    default boolean canBeUsedInMenus() {
        return false;
    }

    default boolean showKeybinding() {
        return true;
    }

    default String getKeybindingText() {
        return getName() + " Keybinding";
    }

    default boolean isEqual(BindableModule other) {
        return other != null && Objects.equals(getName(), other.getName());
    }
}
