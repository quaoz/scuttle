package com.github.quaoz.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;

public class SpawnInit {
	public static void init() {
		BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.JUNGLE),
				SpawnGroup.CREATURE, ScuttleRegistry.SNAKE_ENTITY, 10, 1, 2);
		BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.DESERT),
				SpawnGroup.CREATURE, ScuttleRegistry.SNAKE_ENTITY, 8, 1, 1);
	}
}
