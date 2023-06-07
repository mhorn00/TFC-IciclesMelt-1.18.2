package xyz.sarcly.tfc_iciclesmelt.mixins;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dries007.tfc.common.blocks.IcicleBlock;
import net.dries007.tfc.common.blocks.ThinSpikeBlock;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

@Mixin(IcicleBlock.class)
public class IcicleBlockMixin extends ThinSpikeBlock {

	public IcicleBlockMixin(Properties properties) {
		super(properties);// required implement
	}

	@Inject(at = @At("HEAD"), method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Ljava/util/Random;)V", cancellable = true)
	private void onRandomTick(BlockState state, ServerLevel level, BlockPos pos, Random random, CallbackInfo info) {
		if (state.getValue(TIP) && state.getValue(FLUID).getFluid() == Fluids.EMPTY && level.getBrightness(LightLayer.BLOCK, pos) > 10 - state.getLightBlock(level, pos)) {
			level.removeBlock(pos, false);
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().setWithOffset(pos, 0, 1, 0);
			final BlockState stateAbove = level.getBlockState(mutable);
			if (Helpers.isBlock(stateAbove, (IcicleBlock)(Object)this)) {
				level.setBlock(mutable, stateAbove.setValue(TIP, true), Block.UPDATE_ALL);
			}
			for (int i = 0; i < 5; i++) {
				mutable.move(0, -1, 0);
				BlockState stateAt = level.getBlockState(mutable);
				if (!stateAt.isAir()) // if we hit a non-air block, we won't be returning
				{
					BlockEntity blockEntity = level.getBlockEntity(mutable);
					if (blockEntity != null) {
						blockEntity.getCapability(Capabilities.FLUID, Direction.UP).ifPresent(cap -> cap.fill(new FluidStack(Fluids.WATER, 100), IFluidHandler.FluidAction.EXECUTE));
					}
					info.cancel();
					return;
				}
			}
		}
	}
}
