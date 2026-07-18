package net.cobrasrock.spyzoom.mixin;

import net.cobrasrock.spyzoom.SpyZoom;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.SmoothDouble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
	@Shadow private SmoothDouble smoothTurnX;
	@Shadow private SmoothDouble smoothTurnY;
	@Shadow private double accumulatedDX;
	@Shadow private double accumulatedDY;

	//replicates the scoping branch in turnPlayer with FOV compensation applied
	@Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
	private void onTurnPlayer(CallbackInfo ci) {
		if (SpyZoom.instance.player == null) return;
		if (!SpyZoom.instance.player.isScoping() || !SpyZoom.instance.options.getCameraType().isFirstPerson()) return;

		double compensation = SpyZoom.zoom / 0.1f;
		double ss = (SpyZoom.instance.options.sensitivity().get().doubleValue() * 0.6000000238418579D) + 0.20000000298023224D;
		double sensitivityMod = ss * ss * ss * compensation;

		this.smoothTurnX.reset();
		this.smoothTurnY.reset();
		double xo = this.accumulatedDX * sensitivityMod;
		double yo = this.accumulatedDY * sensitivityMod;

		SpyZoom.instance.getTutorial().onMouse(xo, yo);
		if (SpyZoom.instance.player != null) {
			SpyZoom.instance.player.turn(
				SpyZoom.instance.options.invertMouseX().get().booleanValue() ? -xo : xo,
				SpyZoom.instance.options.invertMouseY().get().booleanValue() ? -yo : yo
			);
		}
		ci.cancel();
	}
}
