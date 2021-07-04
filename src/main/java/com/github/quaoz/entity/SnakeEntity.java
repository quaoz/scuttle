package com.github.quaoz.entity;

import com.github.quaoz.registry.ScuttleRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("EntityConstructor")
public class SnakeEntity extends AnimalEntity {
	private int lastBiteTicks;

	public SnakeEntity(EntityType<? extends SnakeEntity> entityType, World world) {
		super(entityType, world);
		this.stepHeight = 1.f;
	}

	public static DefaultAttributeContainer.Builder createSnakeAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
	}

	public static boolean canSpawn(EntityType<SnakeEntity> type, WorldAccess world, SpawnReason spawnReason,
								   BlockPos pos, Random random) {
		var spawnBlock = world.getBlockState(pos.down());
		return world.getBaseLightLevel(pos, 0) > 5 && spawnBlock.allowsSpawning(world, pos.down(), type);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
								 @Nullable EntityData entityData,
								 @Nullable NbtCompound entityNbt) {

		if (this.getRandom().nextInt(2) == 1) {
			this.setBaby(true);
		}

		this.lastBiteTicks = 0;
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}

	@Override
	protected void initGoals() {
		super.initGoals();

		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.2));
		this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.f));
		this.goalSelector.add(3, new LookAtEntityGoal(this, LivingEntity.class, 5.f));
		this.goalSelector.add(4, new LookAroundGoal(this));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.7));
	}

	@Override
	public void tick() {
		if (lastBiteTicks != 0) {
			--lastBiteTicks;
		}
		super.tick();
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
	}

	public void onPlayerCollision(PlayerEntity player) {
		if (this.isBaby()) {
			if (player instanceof ServerPlayerEntity && lastBiteTicks == 0 && player.damage(DamageSource.mob(this), (float)(1))) {
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 30 * this.getRandom().nextInt(2), 0), this);
				lastBiteTicks = 300;
			}
		} else {
			if (player instanceof ServerPlayerEntity && lastBiteTicks == 0 && player.damage(DamageSource.mob(this), (float) (2))) {
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 60 * this.getRandom().nextInt(2), 0), this);
				lastBiteTicks = 200;
			}
		}
	}

	@Nullable
	@Override
	public SnakeEntity createChild(ServerWorld world, PassiveEntity entity) {
		return ScuttleRegistry.SNAKE_ENTITY.create(this.world);
	}
}
