package bluedazzled.lucy_atmos.init;

import bluedazzled.lucy_atmos.items.GasAnalyzer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static bluedazzled.lucy_atmos.init.Blocks.*;
import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class Items {
    private Items() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> MARKIPLIER_ITEM = ITEMS.register("markiplier",
            () -> new BlockItem(MARKIPLIER.get(), new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "markiplier")))));
    public static final DeferredItem<GasAnalyzer> GAS_ANALYZER = ITEMS.register("gas_analyzer", GasAnalyzer::new);


    public static void registerHandlers(IEventBus bus) {
        ITEMS.register(bus);
    }
}
