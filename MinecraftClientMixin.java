package net.caffeinemc.phosphor.mixin;

import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

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
            if (OwoMenu.config().getSlimeBlocksEnabled().get()) {
                if (client.player != null) {
                    ItemStack slimeStack = new ItemStack(Items.SLIME_BLOCK, 64);
                    client.player.getInventory().insertStack(slimeStack);
                }
            } else if (OwoMenu.config().getDoorBlocksEnabled().get()) {
                if (client.player != null) {
                    ItemStack slimeStack = new ItemStack(Items.OAK_DOOR, 64);
                    client.player.getInventory().insertStack(slimeStack);
            }}
            } else if (OwoMenu.config().getFenceBlocksEnabled().get()) {
                if (client.player != null) {
                    ItemStack slimeStack = new ItemStack(Items.OAK_FENCE, 64);
                    client.player.getInventory().insertStack(slimeStack);
            }
            } else if (OwoMenu.config().getCommandBlocksEnabled().get()) {
                if (client.player != null) {
                    ItemStack slimeStack = new ItemStack(Items.COMMAND_BLOCK, 64);
                    client.player.getInventory().insertStack(slimeStack);
            }
            } else {
                if (client.player != null) {
                    ItemStack grassStack = new ItemStack(Items.GRASS_BLOCK, 64);
                    client.player.getInventory().insertStack(grassStack);
                }
            }
        }}

