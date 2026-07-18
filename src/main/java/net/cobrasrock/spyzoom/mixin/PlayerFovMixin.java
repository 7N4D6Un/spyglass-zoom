package net.cobrasrock.spyzoom.mixin;

import net.cobrasrock.spyzoom.SpyZoom;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class PlayerFovMixin {
	//overrides the FOV modifier when using a spyglass
	@Inject(at = @At("HEAD"), method = "getFieldOfViewModifier", cancellable = true)
	private void overrideSpyglassZoom(boolean firstPerson, float effectScale, CallbackInfoReturnable<Float> cir) {
		AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
		if (firstPerson && player.isScoping()) {
			cir.setReturnValue(SpyZoom.zoom);
		}
	}
}
