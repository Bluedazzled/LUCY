package bluedazzled.lucy_atmos.items;

import bluedazzled.lucy_atmos.atmospherics.AtmosTileEntity;
import bluedazzled.lucy_atmos.blocks.markiplier;
import bluedazzled.lucy_atmos.menus.GasAnaMenu;
import bluedazzled.lucy_atmos.menus.GasAnaScreen;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static bluedazzled.lucy_atmos.Registration.*;
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
    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void leftClickedBlock(ServerPlayer player, Level level, BlockPos pos) {
        BlockEntity blockent = level.getBlockEntity(pos);

        if (player.hasPermissions(2)) {
            if (player.isCrouching()) {
                player.openMenu(new SimpleMenuProvider(
                        (containerId, playerInventory, serverPlayer) -> new GasAnaMenu(containerId, playerInventory),
                        Component.translatable("menu.title.lucy_atmos.gasanamenu")
                ));
            } else {
                if (blockent instanceof AtmosTileEntity atmosTile) {
                    atmosTile.setTemperature(this.temperature);
                }
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        Player player = context.getPlayer();
        BlockPos blockpos = context.getClickedPos();
        BlockState state = level.getBlockState(blockpos);
        if(!state.is(ATMOS_TILE_BLOCK)) {
            return super.useOn(context);
        }
        BlockEntity blockent = level.getBlockEntity(blockpos);
        if (!(blockent instanceof AtmosTileEntity atmosTile)) {
            return InteractionResult.PASS;
        }

        CompoundTag gasMix = atmosTile.getGasMix();
        CompoundTag gasses = gasMix.getCompound("gasses");

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(Component.literal("temperature: " + gasMix.getDouble("temperature") + "K"));
            serverPlayer.sendSystemMessage(Component.literal("total moles: " + gasMix.getDouble("totalMoles")));
            serverPlayer.sendSystemMessage(Component.literal("pressure: " + gasMix.getDouble("pressure") + "kPa"));
            for (String key : gasses.getAllKeys()) {
                serverPlayer.sendSystemMessage(Component.literal(key + ": " + gasses.getDouble(key) + " moles"));
            }
        }
        return InteractionResult.SUCCESS;
    }


}