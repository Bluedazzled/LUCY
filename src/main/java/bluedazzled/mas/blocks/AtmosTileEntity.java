package bluedazzled.mas.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static bluedazzled.mas.Registration.*;

public class AtmosTileEntity extends BlockEntity {
    public CompoundTag gasInfo = new CompoundTag();
    public double temperature;
    public CompoundTag tileInfo = new CompoundTag();

    public AtmosTileEntity(BlockPos pos, BlockState state) {
        super(ATMOS_TILE_ENTITY.get(), pos, state);
        this.temperature = 293.15;
        this.tileInfo.putDouble("temperature", this.temperature);
        this.gasInfo.putDouble("oxygen", 20.5);
        this.gasInfo.putDouble("nitrogen", 79.5);
        this.tileInfo.put("gasInfo", this.gasInfo);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.tileInfo = tag.getCompound("tileInfo");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("tileInfo", this.tileInfo);
    }
}
