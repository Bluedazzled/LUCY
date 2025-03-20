package bluedazzled.lucy_atmos.init;

import bluedazzled.lucy_atmos.atmospherics.sim.turf_tile;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class Entities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public static final Supplier<EntityType<turf_tile>> TURF_TILE = ENTITIES.register(
            "turf_tile",
            () -> EntityType.Builder.of(turf_tile::new, MobCategory.MISC)
                    .sized(1f,1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MODID,"turf_tile"))
                    )
    );
    public static void registerEntities(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
