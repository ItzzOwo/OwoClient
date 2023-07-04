package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.gui.OwoMenu;
import net.caffeinemc.phosphor.modules.BlockInjectorModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;


@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        addItem();
    }

    private void addItem() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (OwoMenu.isClientEnabled() && OwoMenu.config().getBlockInjectorEnabled().get()) {
        if (client.player != null) {
            ItemStack grassStack = new ItemStack(Items.GRASS_BLOCK, 64);
            client.player.getInventory().insertStack(grassStack);
        }
    }
}
}