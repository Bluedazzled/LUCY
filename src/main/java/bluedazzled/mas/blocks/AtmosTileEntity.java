package bluedazzled.mas.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static bluedazzled.mas.Registration.*;

public class AtmosTileEntity extends BlockEntity {
    public CompoundTag tileInfo = new CompoundTag();
    public double temperature;

    public AtmosTileEntity(BlockPos pos, BlockState state) {
        super(ATMOS_TILE_ENTITY.get(), pos, state);
    }


}
