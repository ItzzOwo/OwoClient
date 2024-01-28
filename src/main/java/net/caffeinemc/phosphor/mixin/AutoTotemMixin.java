package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.modules.AutoTotem;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.caffeinemc.phosphor.utils.InventoryUtils;
import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class AutoTotemMixin {

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
         int totemCount = 0;
         boolean clickBlank = false;
         boolean move = false;
        if (mc.player == null) {
            return;
        }
        if (!OwoMenu.isClientEnabled()) {
            return;
        }
        if (!OwoMenu.config().getAutoTotemEnabled().get()) {
            return;
        }
        if (mc.player.isBlocking()) {
            return;
        }
        if (mc.player.isUsingItem()) {
            return;
        }
        if (mc.currentScreen instanceof HandledScreen) {
            return;
        }
        if(mc.currentScreen instanceof GenericContainerScreen) return;
        totemCount = InventoryUtils.amountInInventory(Items.TOTEM_OF_UNDYING);

        if(move) {
            mc.interactionManager.clickSlot(0, 45, 0, SlotActionType.PICKUP, mc.player);
            move = false;
            if(!mc.player.currentScreenHandler.getCursorStack().isEmpty()) clickBlank = true;
            return;
        }

        if(clickBlank) {
            int index = InventoryUtils.getBlank();
            if(index == -1) return;
            mc.interactionManager.clickSlot(0, InventoryUtils.getSlotIndex(index), 0, SlotActionType.PICKUP, mc.player);
            clickBlank = false;
        }

        if(mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {


            if(totemCount == 0) return;
            int index = InventoryUtils.findItem(Items.TOTEM_OF_UNDYING);
            if(index == -1) return;
            mc.interactionManager.clickSlot(0, InventoryUtils.getSlotIndex(index), 0, SlotActionType.PICKUP, mc.player);
            move = true;
        }
    }
}
