package com.github.quaoz;

import com.github.quaoz.registry.ScuttleRegistry;
import com.github.quaoz.registry.SpawnInit;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
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
		INSTANCE = this;

		ScuttleRegistry.init();
		SpawnInit.init();

		this.log("its awake");
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
