package com.github.quaoz.registry;

import com.github.quaoz.entity.SnakeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
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

public class ScuttleRegistry {
	public static final EntityType<SnakeEntity> SNAKE_ENTITY = FabricEntityTypeBuilder.<SnakeEntity>createMob()
			.spawnGroup(SpawnGroup.CREATURE)
			.entityFactory(SnakeEntity::new)
			.dimensions(EntityDimensions.changing(1f, 0.2f))
			.spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
					SnakeEntity::canSpawn)
			.build();

	/* Tags */
	public static final Tag<Item> SNAKE_BREEDING_ITEMS = TagRegistry.item(id("snake_breeding_items"));

	public static void init() {
		/* Entities */
		Registry.register(Registry.ENTITY_TYPE, id("snake"), SNAKE_ENTITY);

		/* Items */
		Registry.register(Registry.ITEM, id("snake_spawn_egg"), new SpawnEggItem(SNAKE_ENTITY,
				0xff0a3001, 0xff2d2b00, new Item.Settings().group(ItemGroup.MISC)));

		/* Attributes */
		FabricDefaultAttributeRegistry.register(SNAKE_ENTITY, SnakeEntity.createSnakeAttributes());
	}
}
