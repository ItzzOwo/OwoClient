package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.gui.OwoMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ForceBlockMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onWorldTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK || !OwoMenu.isClientEnabled() || !OwoMenu.config().getForceGhostBlock().get()) {
            return;
        }
        BlockHitResult hitResult = (BlockHitResult) mc.crosshairTarget;
        BlockPos ghostBlockPos = hitResult.getBlockPos();

        if (OwoMenu.config().getCobblestoneBlocksEnabled().get()) {
            BlockState ghostBlockState = Blocks.COBBLESTONE.getDefaultState();
            ((ClientWorld) (Object) this).setBlockState(ghostBlockPos, ghostBlockState, 19);
        } else if (OwoMenu.config().getSlimeBlocksEnabled().get()) {
            BlockState ghostBlockState = Blocks.SLIME_BLOCK.getDefaultState();
            ((ClientWorld) (Object) this).setBlockState(ghostBlockPos, ghostBlockState, 19);
        } else if (OwoMenu.config().getCommandBlocksEnabled().get()) {
            BlockState ghostBlockState = Blocks.COMMAND_BLOCK.getDefaultState();
            ((ClientWorld) (Object) this).setBlockState(ghostBlockPos, ghostBlockState, 19);
        } else if (OwoMenu.config().getFenceBlocksEnabled().get()) {
            BlockState ghostBlockState = Blocks.OAK_FENCE.getDefaultState();
            ((ClientWorld) (Object) this).setBlockState(ghostBlockPos, ghostBlockState, 19);
        } else if (OwoMenu.config().getAirBlockEnabled().get()) {
            BlockState ghostBlockState = Blocks.AIR.getDefaultState();
            ((ClientWorld) (Object) this).setBlockState(ghostBlockPos, ghostBlockState, 19);
        }
    }
}
