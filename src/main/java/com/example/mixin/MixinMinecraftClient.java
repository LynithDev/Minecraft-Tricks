package com.example.mixin;

import com.example.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

	@Inject(method = "<init>", at = @At(value = "NEW", shift = At.Shift.AFTER, target = "(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/render/item/HeldItemRenderer;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/client/render/BufferBuilderStorage;)Lnet/minecraft/client/render/GameRenderer;"))
	public void afterWindowCreated(RunArgs args, CallbackInfo ci) {
		Renderer.INSTANCE.initContext();
		Renderer.INSTANCE.initSkia();
	}

	// TODO: This doesn't work properly and returns the previous resolution
//	@Inject(method = "onResolutionChanged", at = @At("TAIL"))
//	public void afterResolutionChanged(CallbackInfo ci) {
//		Renderer.INSTANCE.onResize();
//	}

	@Unique
	private int prevWidth = 0;

	@Unique
	private int prevHeight = 0;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;draw(II)V", shift = At.Shift.AFTER))
	public void render(CallbackInfo ci) {
		if (prevWidth != MinecraftClient.getInstance().getWindow().getFramebufferWidth() || prevHeight != MinecraftClient.getInstance().getWindow().getFramebufferHeight()) {
			Renderer.INSTANCE.onResize();
			prevWidth = MinecraftClient.getInstance().getWindow().getFramebufferWidth();
			prevHeight = MinecraftClient.getInstance().getWindow().getFramebufferHeight();
		}

		Renderer.INSTANCE.render();
	}

}