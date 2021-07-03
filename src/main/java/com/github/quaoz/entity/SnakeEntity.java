package com.github.quaoz.entity;

import com.github.quaoz.registry.ScuttleRegistry;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SnakeEntity extends AnimalEntity {
	public SnakeEntity(EntityType<? extends SnakeEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createSnakeAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .3f)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0);
	}

	public static boolean canSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		var spawnBlock = world.getBlockState(pos.down());
		return world.getBaseLightLevel(pos, 0) > 8 && spawnBlock.isIn(ScuttleRegistry.SNAKE_SPAWN_BLOCKS);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData,
								 @Nullable NbtCompound entityNbt) {
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}

	@Override
	protected void initGoals() {
		super.initGoals();

		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.2));
		this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.7));
		this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.f));
		this.goalSelector.add(4, new LookAroundGoal(this));
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
	}

	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return null;
	}
}
