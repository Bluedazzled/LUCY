package bluedazzled.lucy_atmos.atmospherics;

import bluedazzled.lucy_atmos.atmospherics.gas.Gas;
import bluedazzled.lucy_atmos.atmospherics.gas.GasBehaviour;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class Gasses {
    public static final Gas OXYGEN;
    public static final Gas NITROGEN;

    private static Gas register(ResourceKey<Gas> resourceKey, Function<GasBehaviour.Properties, Gas> factory, GasBehaviour.Properties properties) {
        Gas gas = (Gas)factory.apply(properties.setId(resourceKey));
        return (Gas) Registry.register();
    }
    private static ResourceKey<Block> lucyGasId(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(name));
    }
    private static Block register(String name, Function<GasBehaviour.Properties, Gas> factory, GasBehaviour.Properties properties) {
        return register(vanillaBlockId(name), factory, properties);
    }
    private static Gas register(ResourceKey<Gas> name, GasBehaviour.Properties properties) {
        return register(name, Gas::new, properties);
    }

    static {
        OXYGEN = register("oxygen", GasBehaviour.Properties.of().name("oxygen").description("what"));
        NITROGEN = register("nitrogen", GasBehaviour.Properties.of().name("nitrogen").description("yeah"));
    }
}
