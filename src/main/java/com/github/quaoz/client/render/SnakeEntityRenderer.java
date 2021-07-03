package com.github.quaoz.client.render;

import com.github.quaoz.Scuttle;
import com.github.quaoz.client.ScuttleClient;
import com.github.quaoz.client.model.SnakeEntityModel;
import com.github.quaoz.entity.SnakeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class SnakeEntityRenderer extends MobEntityRenderer<SnakeEntity, SnakeEntityModel> {
	public static final Identifier TEXTURE = Scuttle.id("textures/entity/snake/snake.png");

	public SnakeEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new SnakeEntityModel(context.getPart(ScuttleClient.SNAKE_MODEL_LAYER)), 0.5f);
	}

	@Override
	public Identifier getTexture(SnakeEntity entity) {
		return TEXTURE;
	}
}
