package bluedazzled.lucy_atmos;

import bluedazzled.lucy_atmos.atmospherics.sim.turf_tile;
import bluedazzled.lucy_atmos.blocks.markiplier;
import bluedazzled.lucy_atmos.items.GasAnalyzer;
import bluedazzled.lucy_atmos.menus.GasAnaMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class Registration {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, MODID);

    //blocks
    public static final DeferredBlock<markiplier> MARKIPLIER = BLOCKS.register(
            "markiplier",
            markiplier::new);

    //items
    public static final DeferredItem<Item> MARKIPLIER_ITEM = ITEMS.register(
            "markiplier",
            () -> new BlockItem(MARKIPLIER.get(),
                    new Item.Properties()
                            .useBlockDescriptionPrefix()
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "markiplier")))
                )
            );
    public static final DeferredItem<GasAnalyzer> GAS_ANALYZER = ITEMS.register(
            "gas_analyzer",
            GasAnalyzer::new);
    //menu (menus eventually)
    public static final Supplier<MenuType<GasAnaMenu>> GASANA_MENU = MENUS.register(
            "gasana_menu",
            () -> new MenuType<>(
                    GasAnaMenu::new,
                    FeatureFlags.DEFAULT_FLAGS)
    );
    //entities
    public static final Supplier<EntityType<turf_tile>> TURF_TILE = ENTITIES.register(
            "turf_tile",
            () -> EntityType.Builder.of(turf_tile::new, MobCategory.MISC)
                    .sized(1f,1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MODID,"turf_tile"))
            )
        );
    //the meat
    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        MENUS.register(modEventBus);
        ENTITIES.register(modEventBus);
    }
}