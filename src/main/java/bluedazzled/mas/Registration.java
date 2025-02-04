package bluedazzled.mas;

//import bluedazzled.mas.blockentities.MeinBlockEntity;
//import bluedazzled.mas.blocks.MeinEntityBlock;
import bluedazzled.mas.blocks.markiplier;
import bluedazzled.mas.items.GasAnalyzer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Registration {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MAS.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MAS.MODID);
//    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
//            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MAS.MODID);

    public static final DeferredItem<GasAnalyzer> GAS_ANALYZER = ITEMS.register("gas_analyzer", GasAnalyzer::new);

    public static final DeferredBlock<markiplier> MARKIPLIER = BLOCKS.register("markiplier", markiplier::new);
//    public static final DeferredBlock<MeinEntityBlock> MEIN_ENTITY_BLOCK = BLOCKS.register("mein_entity_block", MeinEntityBlock::new);
//
    public static final DeferredItem<Item> MARKIPLIER_ITEM = ITEMS.register("markiplier",
            () -> new BlockItem(MARKIPLIER.get(),
                    new Item.Properties()
                            .useBlockDescriptionPrefix()
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("mas", "markiplier")))
                )
            );
//    public static final DeferredItem<Item> MEIN_ENTITY_BLOCK_ITEM = ITEMS.register("mein_entity_block",
//            () -> new BlockItem(MEIN_ENTITY_BLOCK.get(),
//                    new Item.Properties()
//                            .useBlockDescriptionPrefix()
//                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("mas", "mein_entity_block")))
//                )
//            );
//
//    public static final Supplier<BlockEntityType<MeinBlockEntity>> MEIN_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
//            "mein_block_entity",
//            () -> new BlockEntityType<>(
//                    MeinBlockEntity::new,
//                    Registration.MEIN_ENTITY_BLOCK.get()
//            )
//    );

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
//        BLOCK_ENTITY_TYPES.register(modEventBus);
    }

//    static void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
//            event.accept(SIMPLE_BLOCK_ITEM);
//        }
//    }
}
