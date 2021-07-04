package com.github.quaoz.client;

import com.github.quaoz.Scuttle;
import com.github.quaoz.client.model.SnakeEntityModel;
import com.github.quaoz.client.render.SnakeEntityRenderer;
import com.github.quaoz.registry.ScuttleRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

@Environment(EnvType.CLIENT)
public class ScuttleClient implements ClientModInitializer {
	public static final EntityModelLayer SNAKE_MODEL_LAYER = new EntityModelLayer(Scuttle.id("snake"), "main");

	@SuppressWarnings({"UnstableApiUsage", "deprecation"})
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(ScuttleRegistry.SNAKE_ENTITY, SnakeEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(SNAKE_MODEL_LAYER, SnakeEntityModel::model);
	}
}
