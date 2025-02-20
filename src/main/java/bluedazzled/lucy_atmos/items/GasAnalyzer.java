package bluedazzled.lucy_atmos.items;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

@MethodsReturnNonnullByDefault
public class GasAnalyzer extends Item {
    private double temperature;

    public GasAnalyzer() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "gas_analyzer")))
                //.component()
        );
    }
    ///COMMENTING ALL OF THIS OUT UNTIL I REWORK AtmosTileEntity INTO turf_tile AND JUST STRAIGHT UP COMPLETELY REDO ALL METHODS (PROCS IG)
    /// SO FOR NOW THIS ITEM DOES JACK SHIT
//    @Override
//    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
//        //sword behavior ahh
//        return !player.isCreative();
//    }
//    public void setTemperature(double temperature) {
//        this.temperature = temperature;
//    }
//
//    public void leftClickedBlock(ServerPlayer player, Level level, BlockPos pos) {
//        BlockEntity blockent = level.getBlockEntity(pos);
//
//        if (player.hasPermissions(2)) {
//            if (player.isCrouching()) {
//                player.openMenu(new SimpleMenuProvider(
//                        (containerId, playerInventory, serverPlayer) -> new GasAnaMenu(containerId, playerInventory),
//                        Component.translatable("menu.title.lucy_atmos.gasanamenu")
//                ));
//            } else {
//                if (blockent instanceof turf_tile atmosTile) { //This entire function is, essentially, disabled. Entirely.
//                    atmosTile.setTemperature(this.temperature);
//                }
//            }
//        }
//    }
//
//    @Override
//    public InteractionResult useOn(UseOnContext context) {
//        Level level = context.getLevel();
//        if (level.isClientSide) {
//            return InteractionResult.SUCCESS;
//        }
//        Player player = context.getPlayer();
//        BlockPos blockpos = context.getClickedPos();
//        BlockState state = level.getBlockState(blockpos);
//        if(!state.is(ATMOS_TILE_BLOCK)) {
//            return super.useOn(context);
//        }
//        BlockEntity blockent = level.getBlockEntity(blockpos);
//        if (!(blockent instanceof turf_tile atmosTile)) {
//            return InteractionResult.PASS;
//        }
//
//        CompoundTag gasMix = getGasMix(atmosTile);
//        CompoundTag gasses = gasMix.getCompound("gasses");
//
//        if (player instanceof ServerPlayer serverPlayer) {
//            serverPlayer.sendSystemMessage(Component.literal("temperature: " + gasMix.getDouble("temperature") + "K"));
//            serverPlayer.sendSystemMessage(Component.literal("total moles: " + gasMix.getDouble("totalMoles")));
//            serverPlayer.sendSystemMessage(Component.literal("pressure: " + gasMix.getDouble("pressure") + "kPa"));
//            for (String key : gasses.getAllKeys()) {
//                serverPlayer.sendSystemMessage(Component.literal(key + ": " + gasses.getDouble(key) + " moles"));
//            }
//        }
//        return InteractionResult.SUCCESS;
//    }


}