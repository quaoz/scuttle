package com.github.quaoz;

import com.github.quaoz.registry.ScuttleRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Scuttle implements ModInitializer {
	public static final String MODID = "scuttle";
	private static Scuttle INSTANCE;
	public final Logger logger = LogManager.getLogger(MODID);

	public static Scuttle get() {
		return INSTANCE;
	}

	@Override
	public void onInitialize() {
		ScuttleRegistry.init();
		INSTANCE = this;

		this.log("its awake");

		BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.JUNGLE),
				SpawnGroup.AMBIENT, ScuttleRegistry.SNAKE_ENTITY_TYPE, 10, 1, 1);
	}

	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}

	public void log(String info) {
		this.logger.info("[Scuttle/INFO] " + info);
	}

	public void warn(String info) {
		this.logger.warn("[Scuttle/WARN] " + info);
	}
}
