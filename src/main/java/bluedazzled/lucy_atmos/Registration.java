package bluedazzled.lucy_atmos;

import bluedazzled.lucy_atmos.atmospherics.sim.turf_tile;
import bluedazzled.lucy_atmos.blocks.AtmosTileBlock;
import bluedazzled.lucy_atmos.blocks.markiplier;
import bluedazzled.lucy_atmos.items.GasAnalyzer;
import bluedazzled.lucy_atmos.menus.GasAnaMenu;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class Registration {
    //I'm about 🤏 this close to splitting this bitch up.
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    //blocks
    public static final DeferredBlock<markiplier> MARKIPLIER = BLOCKS.register(
            "markiplier",
            markiplier::new);
    public static final DeferredBlock<AtmosTileBlock> ATMOS_TILE_BLOCK = BLOCKS.register(
            "atmos_tile_block",
            AtmosTileBlock::new);

    //items
    public static final DeferredItem<Item> MARKIPLIER_ITEM = ITEMS.register(
            "markiplier",
            () -> new BlockItem(MARKIPLIER.get(),
                    new Item.Properties()
                            .useBlockDescriptionPrefix()
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "markiplier")))
                )
            );
    public static final DeferredItem<Item> ATMOS_TILE_BLOCK_ITEM = ITEMS.register(
            "atmos_tile_block",
            () -> new BlockItem(ATMOS_TILE_BLOCK.get(),
                    new Item.Properties()
                            .useBlockDescriptionPrefix()
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "atmos_tile_block")))
                )
    );
    public static final DeferredItem<GasAnalyzer> GAS_ANALYZER = ITEMS.register(
            "gas_analyzer",
            GasAnalyzer::new);
    //block entity (maybe entities one day)
    public static final Supplier<BlockEntityType<turf_tile>> ATMOS_TILE_ENTITY = BLOCK_ENTITY_TYPES.register(
            "atmos_tile_entity",
            () -> new BlockEntityType<>(
                    turf_tile::new,
                    Registration.ATMOS_TILE_BLOCK.get()
            )
    );
    //menu (menus eventually)
    public static final Supplier<MenuType<GasAnaMenu>> GASANA_MENU = MENUS.register(
            "gasana_menu",
            () -> new MenuType<>(
                    GasAnaMenu::new,
                    FeatureFlags.DEFAULT_FLAGS)
    );
    //codec definition(s)
    public static final Codec<BlockPos> BLOCKPOS_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(BlockPos::getX),
            Codec.INT.fieldOf("y").forGetter(BlockPos::getY),
            Codec.INT.fieldOf("z").forGetter(BlockPos::getZ)
    ).apply(instance, BlockPos::new));

    //attachment types
    public static final Supplier<AttachmentType<List<BlockPos>>> CHUNK_ALLTILES = ATTACHMENT_TYPES.register(
            "chunk_alltiles", () -> AttachmentType
                    .<List<BlockPos>>builder(() -> new ArrayList<>())
                    .serialize(Codec.list(BLOCKPOS_CODEC))
                    .build()
    );
    public static final Supplier<AttachmentType<List<ChunkPos>>> GLOBAL_TILECHUNKS = ATTACHMENT_TYPES.register(
            "global_tilechunks", () -> AttachmentType
                    .<List<ChunkPos>>builder(() -> new ArrayList<>())
                    .serialize(Codec.list(ChunkPos.CODEC))
                    .build()
    );

    //the meat
    public static void init(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENUS.register(modEventBus);
    }
}