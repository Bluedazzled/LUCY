package bluedazzled.lucy_atmos;

import bluedazzled.lucy_atmos.atmospherics.OverlayRenderer;
import bluedazzled.lucy_atmos.blocks.markiplier;
import bluedazzled.lucy_atmos.blocks.AtmosTileBlock;
import bluedazzled.lucy_atmos.atmospherics.AtmosTileEntity;
import bluedazzled.lucy_atmos.items.GasAnalyzer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class Registration {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final DeferredItem<GasAnalyzer> GAS_ANALYZER = ITEMS.register("gas_analyzer", GasAnalyzer::new);

    public static final DeferredBlock<markiplier> MARKIPLIER = BLOCKS.register("markiplier", markiplier::new);
    public static final DeferredBlock<AtmosTileBlock> ATMOS_TILE_BLOCK = BLOCKS.register("atmos_tile_block", AtmosTileBlock::new);

    public static final DeferredItem<Item> MARKIPLIER_ITEM = ITEMS.register("markiplier",
            () -> new BlockItem(MARKIPLIER.get(),
                    new Item.Properties()
                            .useBlockDescriptionPrefix()
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "markiplier")))
                )
            );
    public static final DeferredItem<Item> ATMOS_TILE_BLOCK_ITEM = ITEMS.register("atmos_tile_block",
            () -> new BlockItem(ATMOS_TILE_BLOCK.get(),
                    new Item.Properties()
                            .useBlockDescriptionPrefix()
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "atmos_tile_block")))
                )
            );

    public static final Supplier<BlockEntityType<AtmosTileEntity>> ATMOS_TILE_ENTITY = BLOCK_ENTITY_TYPES.register(
            "atmos_tile_entity",
            () -> new BlockEntityType<>(
                    AtmosTileEntity::new,
                    Registration.ATMOS_TILE_BLOCK.get()
            )
    );

    @SubscribeEvent
    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                Registration.ATMOS_TILE_ENTITY.get(),
                OverlayRenderer::new
        );
    }

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}