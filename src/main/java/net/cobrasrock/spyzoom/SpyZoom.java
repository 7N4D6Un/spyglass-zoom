package net.cobrasrock.spyzoom;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;

public class SpyZoom implements ModInitializer {
	public static final float MIN_ZOOM = 0.1f;
	public static final float MAX_ZOOM = 1.0f;

	public static float zoom = MIN_ZOOM;

	public static Minecraft instance = Minecraft.getInstance();

	public static void scroll(int steps) {
		zoom = Math.clamp(zoom * (float) Math.pow(0.9, steps), MIN_ZOOM, MAX_ZOOM);
	}

	@Override
	public void onInitialize() {}
}
