package com.github.quaoz.client.render;

import com.github.quaoz.Scuttle;
import com.github.quaoz.client.ScuttleClient;
import com.github.quaoz.client.model.SnakeEntityModel;
import com.github.quaoz.entity.SnakeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SnakeEntityRenderer extends MobEntityRenderer<SnakeEntity, SnakeEntityModel> {
	public final static Identifier TEXTURE = Scuttle.id("textures/entity/snake/");

	public SnakeEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new SnakeEntityModel(context.getPart(ScuttleClient.SNAKE_MODEL_LAYER)), 0.5f);
	}

	@Override
	public void scale(SnakeEntity snakeEntity, MatrixStack matrixStack, float f) {
		if (snakeEntity.isBaby()) {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
		}
	}

	@Override
	public Identifier getTexture(SnakeEntity entity) {
		var spawnBiome = entity.getSpawnBiome();
		return Scuttle.id(TEXTURE.getPath().concat(spawnBiome).concat("_snake.png"));
	}
}
