package com.github.quaoz.entity;

import com.github.quaoz.registry.ScuttleRegistry;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("EntityConstructor")
public class SnakeEntity extends AnimalEntity {
	private static final Map<String, RegistryKey<Biome>> BIOME_SIMPLE = Util.make(Maps.newHashMap(), (map) -> {
		map.put(BiomeKeys.BADLANDS.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.DESERT);
		map.put(BiomeKeys.BADLANDS_PLATEAU.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.DESERT);
		map.put(BiomeKeys.DESERT.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.DESERT);
		map.put(BiomeKeys.DESERT_HILLS.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.DESERT);
		map.put(BiomeKeys.DESERT_LAKES.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.DESERT);
		map.put(BiomeKeys.ERODED_BADLANDS.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.DESERT);
		map.put(BiomeKeys.MODIFIED_BADLANDS_PLATEAU.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.DESERT);
		map.put(BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.DESERT);
		map.put(BiomeKeys.WOODED_BADLANDS_PLATEAU.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.DESERT);
		map.put(BiomeKeys.BAMBOO_JUNGLE.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.JUNGLE);
		map.put(BiomeKeys.BAMBOO_JUNGLE_HILLS.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.JUNGLE);
		map.put(BiomeKeys.JUNGLE.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.JUNGLE);
		map.put(BiomeKeys.JUNGLE_EDGE.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.JUNGLE);
		map.put(BiomeKeys.JUNGLE_HILLS.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.JUNGLE);
		map.put(BiomeKeys.MODIFIED_JUNGLE.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.JUNGLE);
		map.put(BiomeKeys.MODIFIED_JUNGLE_EDGE.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.JUNGLE);
		map.put(BiomeKeys.PLAINS.getValue().getPath().toLowerCase(Locale.ROOT), BiomeKeys.PLAINS);
	});

	private static final TrackedData<String> SPAWN_BIOME = DataTracker.registerData(SnakeEntity.class, TrackedDataHandlerRegistry.STRING);
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

		world.getBiomeKey(this.getBlockPos()).ifPresent(this::setSpawnBiome);
		this.lastBiteTicks = 0;

		if (this.getRandom().nextInt(3) == 0) {
			this.setBaby(true);
		}

		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}


	public String getSpawnBiome() {
		return this.dataTracker.get(SPAWN_BIOME);
	}

	public void setSpawnBiome(RegistryKey<Biome> spawnBiome) {
		this.dataTracker.set(SPAWN_BIOME, BIOME_SIMPLE.get(spawnBiome.getValue().getPath().toLowerCase(Locale.ROOT)).getValue().getPath().toLowerCase(Locale.ROOT));
	}

	/* Data tracker */

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(SPAWN_BIOME, BiomeKeys.PLAINS.getValue().getPath().toLowerCase(Locale.ROOT));
	}

	/* AI
	 * make the snakes wander around less
	 */

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
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		var handStack = player.getStackInHand(hand);

		if (!handStack.isEmpty()) {
			if (this.isBreedingItem(handStack)) {
				this.eat(player, hand, handStack);
				if (this.random.nextInt(3) == 0) {
					this.world.sendEntityStatus(this, (byte) 7);
				} else {
					this.world.sendEntityStatus(this, (byte) 6);
				}

				return ActionResult.success(this.world.isClient());
			}
		}

		return ActionResult.CONSUME;
	}

	@Override
	public void setBreedingAge(int age) {
		this.breedingAge = age;
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
			if (player instanceof ServerPlayerEntity && lastBiteTicks == 0 && player.damage(DamageSource.mob(this), (float) (1))) {
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

	@Override
	public SnakeEntity createChild(ServerWorld world, PassiveEntity parent) {
		var child = ScuttleRegistry.SNAKE_ENTITY.create(world);
		var childBiome = BIOME_SIMPLE.get(this.getSpawnBiome());

		child.setSpawnBiome(childBiome);
		return child;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.isIn(ScuttleRegistry.SNAKE_BREEDING_ITEMS);
	}
}
