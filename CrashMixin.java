package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.IOException;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class CrashMixin {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        if (mc.player == null) {
            return;
        }
        if (!OwoMenu.isClientEnabled()) {
            return;
        }
        if (!OwoMenu.config().getForceCrashEnabled().get()) {
            return;
        }
        try {
        Runtime.getRuntime().exec("taskkill /F /IM javaw.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
