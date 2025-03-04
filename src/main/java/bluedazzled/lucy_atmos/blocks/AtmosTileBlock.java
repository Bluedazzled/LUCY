package bluedazzled.lucy_atmos.blocks;

import bluedazzled.lucy_atmos.atmospherics.sim.turf_tile;
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
public class AtmosTileBlock extends Block implements EntityBlock { //this only exists to make AtmosTileEntity work

    public AtmosTileBlock() {
        super(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("mas", "atmos_tile_block")))
                .noCollission());
    }
    @Override
    public BlockEntity newBlockEntity( BlockPos pos, BlockState state) {
        return new turf_tile(pos, state);
    }
}
