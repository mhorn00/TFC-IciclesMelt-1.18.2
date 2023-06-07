package xyz.sarcly.tfc_iciclesmelt.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dries007.tfc.util.EnvironmentHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

@Mixin(value = EnvironmentHelpers.class, remap=false)
public final class EnvironmentHelpersMixin {
	@Inject(at = @At("HEAD"), method = "doIcicles(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;F)V", cancellable = true)
    private static void onDoIcicles(Level level, BlockPos lcgPos, float temperature, CallbackInfo info) {
		if (level.getBrightness(LightLayer.BLOCK, lcgPos) > 10) {
			info.cancel();
		}
    }
}
