package com.github.quaoz.registry;

import com.github.quaoz.entity.SnakeEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tag.Tag;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

import static com.github.quaoz.Scuttle.id;

public final class ScuttleRegistry {

	public static final SpawnEggItem SNAKE_SPAWN_EGG_ITEM;

	public static final EntityType<SnakeEntity> SNAKE_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, id("snake"),
			FabricEntityTypeBuilder.<SnakeEntity>createMob()
					.spawnGroup(SpawnGroup.AMBIENT)
					.entityFactory(SnakeEntity::new)
					.defaultAttributes(SnakeEntity::createSnakeAttributes)
					.dimensions(EntityDimensions.changing(1f, 0.2f))
					.spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SnakeEntity::canSpawn)
					.build()
	);

	public static final Tag<Block> SNAKE_SPAWN_BLOCKS = TagRegistry.block(id("snake_spawn_blocks"));

	private static <T extends Item> T register(String name, T item) {
		return Registry.register(Registry.ITEM, id(name), item);
	}

	public static void init() {
	}

	static {
		SNAKE_SPAWN_EGG_ITEM = register("snake_spawn_egg", new SpawnEggItem(SNAKE_ENTITY_TYPE, 0x0a3001, 0x2d2b00,
				new FabricItemSettings().group(ItemGroup.MISC)));
	}
}
