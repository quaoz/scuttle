package com.github.quaoz.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;

@SuppressWarnings("deprecation")
public class SpawnInit {
	public static void init() {
		addSpawn(Biome.Category.JUNGLE, SpawnGroup.CREATURE, ScuttleRegistry.SNAKE_ENTITY, 10, 1, 2);
		addSpawn(Biome.Category.DESERT, SpawnGroup.CREATURE, ScuttleRegistry.SNAKE_ENTITY, 8, 1, 1);
		addSpawn(Biome.Category.MESA, SpawnGroup.CREATURE, ScuttleRegistry.SNAKE_ENTITY, 2, 1, 1);
		addSpawn(Biome.Category.PLAINS, SpawnGroup.CREATURE, ScuttleRegistry.SNAKE_ENTITY, 3, 1, 1);
	}

	private static void addSpawn(Biome.Category biome,
								 SpawnGroup spawnGroup,
								 EntityType entityType,
								 int weight,
								 int mineGroupSize,
								 int maxGroupSize) {
		BiomeModifications.addSpawn(BiomeSelectors.categories(biome),
				spawnGroup, entityType, weight, mineGroupSize, maxGroupSize);
	}

}
