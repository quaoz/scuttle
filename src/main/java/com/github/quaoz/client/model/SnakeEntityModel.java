package com.github.quaoz.client.model;

import com.github.quaoz.entity.SnakeEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class SnakeEntityModel extends EntityModel<SnakeEntity> {
	public static final String HEAD = "head";
	public static final String BODY = "body";
	public static final String TAIL = "tail";
	private final Model model;

	public SnakeEntityModel(ModelPart root) {
		this.model = new Model(root.getChild("snake"));
	}

	public static TexturedModelData model() {
		var modelData = new ModelData();
		var root = modelData.getRoot();
		buildModel(root.addChild("snake", new ModelPartBuilder(), ModelTransform.NONE));
		return TexturedModelData.of(modelData, 24, 24);
	}

	private static void buildModel(ModelPartData root) {
		var head = root.addChild(HEAD, new ModelPartBuilder()
						.uv(8, 13)
						.cuboid(-3.0F, -2.0F, -7.0F, 2.0F, 1.0F, 1.0F, false)
						.uv(12, 8)
						.cuboid(-3.0F, -3.0F, -10.0F, 2.0F, 1.0F, 4.0F, false)
						.uv(8, 11)
						.cuboid(-2.0F, -2.0F, -6.0F, 1.0F, 1.0F, 1.0F, false),
				ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		head.addChild("jaw", new ModelPartBuilder()
						.uv(0, 17)
						.cuboid(-1.0F, 1.0F, -4.0F, 2.0F, 1.0F, 3.0F, false),
				ModelTransform.of(-2.0F, -3.0F, -6.0F, 0.0873F, 0.0F, 0.0F));

		root.addChild(BODY, new ModelPartBuilder()
						.uv(12, 13)
						.cuboid(-7.0F, -2.0F, -9.0F, 4.0F, 2.0F, 2.0F, false)
						.uv(20, 4)
						.cuboid(-3.0F, -2.0F, -8.0F, 1.0F, 2.0F, 1.0F, false)
						.uv(10, 17)
						.cuboid(-4.0F, -2.0F, -7.0F, 2.0F, 2.0F, 2.0F, false)
						.uv(20, 7)
						.cuboid(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, false)
						.uv(0, 4)
						.cuboid(-11.0F, -2.0F, -5.0F, 8.0F, 2.0F, 2.0F, false)
						.uv(0, 21)
						.cuboid(-12.0F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, false)
						.uv(18, 17)
						.cuboid(-12.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F, false)
						.uv(4, 21)
						.cuboid(-12.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, false)
						.uv(0, 0)
						.cuboid(-11.0F, -2.0F, -1.0F, 10.0F, 2.0F, 2.0F, false),
				ModelTransform.pivot(6.0F, 24.0F, 2.0F));

		root.addChild(TAIL, new ModelPartBuilder()
						.uv(8, 21)
						.cuboid(4.0F, -2.0F, 5.0F, 1.0F, 2.0F, 1.0F, false)
						.uv(0, 8)
						.cuboid(-1.0F, -2.0F, 6.0F, 7.0F, 2.0F, 1.0F, false)
						.uv(0, 11)
						.cuboid(5.0F, -2.0F, 2.0F, 2.0F, 2.0F, 4.0F, false)
						.uv(0, 11)
						.cuboid(5.0F, -2.0F, 1.0F, 1.0F, 2.0F, 1.0F, false),
				ModelTransform.pivot(0.0F, 24.0F, 0.0F));
	}

	public Model getCurrentModel() {
		return this.model;
	}

	@Override
	public void setAngles(SnakeEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.push();
		this.getCurrentModel().render(matrices, vertices, light, overlay, red, green, blue, alpha);
		matrices.pop();
	}

	public record Model(ModelPart root) {
		public Model(ModelPart root) {
			this.root = root;
			root.getChild(HEAD);
			root.getChild(BODY);
			root.getChild(TAIL);
		}

		public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
			this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		}
	}
}
