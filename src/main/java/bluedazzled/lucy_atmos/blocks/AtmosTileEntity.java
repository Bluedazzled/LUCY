package bluedazzled.lucy_atmos.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

import static bluedazzled.lucy_atmos.Registration.*;

@ParametersAreNonnullByDefault
public class AtmosTileEntity extends BlockEntity {
    public CompoundTag gasInfo;
    public double temperature;
    public CompoundTag tileInfo;

    public AtmosTileEntity(BlockPos pos, BlockState state) {
        super(ATMOS_TILE_ENTITY.get(), pos, state);
        this.tileInfo = new CompoundTag();
        this.gasInfo = new CompoundTag();
        //20C room temperature
        this.temperature = 293.15;
        this.tileInfo.putDouble("temperature", this.temperature);
        //Default molar values of uhh 1m^3 of aaiirrrrrr
        this.gasInfo.putDouble("oxygen", 9.38);
        this.gasInfo.putDouble("nitrogen", 35.27);
        this.tileInfo.put("gasInfo", this.gasInfo);
    }

    public CompoundTag getTileInfo(Level level) {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt, level.registryAccess());
        return nbt;
    }

    @Override
    public void loadAdditional( CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.tileInfo = tag.getCompound("tileInfo");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("tileInfo", this.tileInfo);
    }
}

