package bluedazzled.lucy_atmos.atmospherics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

import static bluedazzled.lucy_atmos.atmospherics.GasConstants.*;
import static bluedazzled.lucy_atmos.Registration.*;

@ParametersAreNonnullByDefault
public class AtmosTileEntity extends BlockEntity {
    public CompoundTag gasses;
    public double temperature;
    public CompoundTag gasMix;

    public int pressure_difference;
    public int pressure_direction;
    public boolean active = false;

    public float plasmaOpacity;

    public CompoundTag tileInfo;

    public AtmosTileEntity(BlockPos pos, BlockState state) {
        super(ATMOS_TILE_ENTITY.get(), pos, state);
        this.gasMix = new CompoundTag();
        this.gasses = new CompoundTag();
        this.tileInfo = new CompoundTag();

        this.temperature = T20C;
        this.gasMix.putDouble("temperature", this.temperature);
        this.gasMix.putDouble("volume", TILE_VOLUME);
        this.gasses.putDouble("oxygen", MOLES_O2STANDARD);
        this.gasses.putDouble("nitrogen", MOLES_N2STANDARD);
        this.gasMix.put("gasses", this.gasses);
        updateTotalMoles();
        updatePressure();
        this.tileInfo.putBoolean("active", this.active);
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

        pressure = Math.round(pressure * 1000.0) / 1000.0;
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

    public void setPlasmaOpacity(float opacity) {
        this.plasmaOpacity = opacity;
    }

    public float getPlasmaOpacity(){
        return plasmaOpacity;
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

