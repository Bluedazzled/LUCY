package bluedazzled.lucy_atmos.items;

import bluedazzled.lucy_atmos.atmospherics.AtmosTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static bluedazzled.lucy_atmos.Registration.*;

public class GasAnalyzer extends Item {
    public GasAnalyzer() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("lucy_atmos", "gas_analyzer")))
        );
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
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
