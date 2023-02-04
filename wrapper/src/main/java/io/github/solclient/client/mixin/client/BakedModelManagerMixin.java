package io.github.solclient.client.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import io.github.solclient.client.util.MinecraftUtils;
import net.minecraft.client.render.block.BlockModelShapes;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;

@Mixin(BakedModelManager.class)
public class BakedModelManagerMixin {

	@Redirect(method = "reload", at = @At(value = "NEW", target = "net/minecraft/client/render/model/ModelLoader"))
	public ModelLoader captureModelBakery(ResourceManager resourceManager, SpriteAtlasTexture atlas,
			BlockModelShapes blockModelShapes) {
		return MinecraftUtils.modelLoader = new ModelLoader(resourceManager, atlas, blockModelShapes);
	}

}
