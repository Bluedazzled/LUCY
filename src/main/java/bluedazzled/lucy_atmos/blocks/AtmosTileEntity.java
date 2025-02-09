package bluedazzled.lucy_atmos.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

import static bluedazzled.lucy_atmos.GasConstants.*;
import static bluedazzled.lucy_atmos.Registration.*;

@ParametersAreNonnullByDefault
public class AtmosTileEntity extends BlockEntity {
    public CompoundTag gasses;
    public double temperature;
    public CompoundTag gasMix;

    public int pressure_difference;
    public int pressure_direction;
    public boolean active;

    public CompoundTag tileInfo;

    public AtmosTileEntity(BlockPos pos, BlockState state) {
        super(ATMOS_TILE_ENTITY.get(), pos, state);
        this.gasMix = new CompoundTag();
        this.gasses = new CompoundTag();
        this.tileInfo = new CompoundTag();

        this.temperature = T20C;
        this.gasMix.putDouble("temperature", this.temperature);
        this.gasMix.putInt("volume", TILE_VOLUME);
        //Default molar values of uhh 1m^3 of aaiirrrrrr
        this.gasses.putDouble("oxygen", 8.73);
        this.gasses.putDouble("nitrogen", 32.84);
        this.gasMix.put("gasses", this.gasses);
        updateTotalMoles();
        updatePressure();

        this.tileInfo.put("gasMix", this.gasMix);
        setChanged();
    }

    public CompoundTag getGasMix() {
        return this.gasMix;
    }

    public void updatePressure() {
        double totalMoles = this.gasMix.getDouble("totalMoles");  // Get total moles of gas
        double temperature = this.gasMix.getDouble("temperature"); // Get temperature
        int volume = this.gasMix.getInt("volume");  // Get volume
        double pressure = (totalMoles * GAS_CONSTANT * temperature) / volume;

        pressure = Math.round(pressure * 1000.0) / 1000.0; //See updateTotalMoles()
        this.gasMix.putDouble("pressure", pressure);
        setChanged();
    }

    public void updateTotalMoles() {
        double total = 0.0;
        for (String key : this.gasses.getAllKeys()) {
            double gasAmount = this.gasses.getDouble(key);
            total += gasAmount;
        }
        total = Math.round(total * 1000.0) / 1000.0;
        this.gasMix.putDouble("totalMoles", total);
        setChanged();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.gasMix = tag.getCompound("gasMix");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("gasMix", this.gasMix);
    }
}

