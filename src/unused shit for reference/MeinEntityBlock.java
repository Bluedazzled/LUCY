package bluedazzled.mas.blocks;

import bluedazzled.mas.blockentities.MeinBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MeinEntityBlock extends Block implements EntityBlock {
    public MeinEntityBlock() {
        super(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("mas", "mein_entity_block"))));
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MeinBlockEntity(pos, state);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            // We don't have anything to do on the client side
            return null;
        } else {
            // Server side we delegate ticking to our block entity
            return (lvl, pos, st, blockEntity) -> {
                MeinBlockEntity be = (MeinBlockEntity) blockEntity;
                be.tickServer();
            };
        }
    }
}