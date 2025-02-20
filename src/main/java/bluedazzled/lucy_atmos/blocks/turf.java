package bluedazzled.lucy_atmos.blocks;

import bluedazzled.lucy_atmos.atmospherics.environmental.turf_tile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class turf extends Block implements EntityBlock { //this only exists to make AtmosTileEntity work

    public turf() {
        super(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("mas", "atmos_tile_block")))
                .noCollission());
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new turf_tile(pos, state);
    }

//    @SuppressWarnings("unchecked")
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
//        //Yeah so this is magic. What the fuck does this even mean? Who knows?
//        //Anyone who is competent in java does of course!
//        return type == ATMOS_TILE_ENTITY.get() ? AtmosTileEntity::tick : null;
//    }
}
