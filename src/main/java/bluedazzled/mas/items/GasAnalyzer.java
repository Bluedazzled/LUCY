package bluedazzled.mas.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.TagType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bluedazzled.mas.Registration.*;

public class GasAnalyzer extends Item {
    CompoundTag lastScannedTile;
    public GasAnalyzer() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("mas", "gas_analyzer")))
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        BlockEntity blockent = level.getBlockEntity(blockpos);
        BlockState state = level.getBlockState(blockpos);

        if (!level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if(!state.is(ATMOS_TILE_BLOCK)) {
            return super.useOn(context);
        } else {
            CompoundTag nbt = blockent.saveWithoutMetadata(level.registryAccess());
            CompoundTag tileInfo = nbt.getCompound("tileInfo");

            player.displayClientMessage(Component.literal(Double.toString(tileInfo.getDouble("temperature"))), false);
            return InteractionResult.SUCCESS;
        }
    }
}
