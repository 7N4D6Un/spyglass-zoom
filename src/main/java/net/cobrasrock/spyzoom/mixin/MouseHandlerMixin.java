package net.cobrasrock.spyzoom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.cobrasrock.spyzoom.SpyZoom;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
	@ModifyExpressionValue(method = "onScroll", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/ScrollWheelHandler;onMouseScroll(DD)Lorg/joml/Vector2i;"))
	private Vector2i spyzoomScroll(Vector2i wheel) {
		if (SpyZoom.instance == null || SpyZoom.instance.player == null) return wheel;
		if (!SpyZoom.instance.player.isScoping() || !SpyZoom.instance.options.getCameraType().isFirstPerson()) return wheel;
		if (SpyZoom.instance.getCameraEntity() != SpyZoom.instance.player) return wheel;

		SpyZoom.scroll(wheel.y == 0 ? -wheel.x : wheel.y);
		wheel.zero();
		return wheel;
	}

	@WrapOperation(method = "turnPlayer", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
	private void spyzoomSensitivity(LocalPlayer player, double yaw, double pitch, Operation<Void> turn) {
		if (SpyZoom.instance == null || SpyZoom.instance.player == null) {
			turn.call(player, yaw, pitch);
			return;
		}
		if (!player.isScoping()
			|| !SpyZoom.instance.options.getCameraType().isFirstPerson()
			|| SpyZoom.instance.getCameraEntity() != player
			|| SpyZoom.instance.options.smoothCamera) {
			turn.call(player, yaw, pitch);
			return;
		}
		double compensation = SpyZoom.zoom / SpyZoom.MIN_ZOOM;
		turn.call(player, yaw * compensation, pitch * compensation);
	}
}
