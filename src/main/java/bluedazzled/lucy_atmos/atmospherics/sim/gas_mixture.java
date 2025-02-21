package bluedazzled.lucy_atmos.atmospherics.sim;

import net.minecraft.nbt.CompoundTag;

import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.GAS_CONSTANT;
import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.TCMB;

public class gas_mixture {
    turf_tile tile;
    public gas_mixture(turf_tile tile) {
        this.tile = tile;
    }

    public void updateAll() {
        this.tile.gasMix.put("gasses", this.tile.gasses);
        updateTotalMoles();
        updatePressure();
        updateTileInfo(true);
    }
    public void updateTileInfo(boolean update) {
        this.tile.tileInfo.put("gasMix", this.tile.gasMix);
        this.tile.tileInfo.put("adjacentTiles", this.tile.adjacentTiles);
        this.tile.tileInfo.putBoolean("active", this.tile.active);
        if (update) {
            this.tile.setChanged();
        }
    }
    public void updatePressure() {
        double totalMoles = this.tile.gasMix.getDouble("totalMoles");
        double temperature = this.tile.gasMix.getDouble("temperature");
        int volume = this.tile.gasMix.getInt("volume");
        double pressure = (totalMoles * GAS_CONSTANT * temperature) / volume;

        pressure = Math.round(pressure * 1000.0) / 1000.0;
        this.tile.gasMix.putDouble("pressure", pressure);
    }
    public void updateTotalMoles() {
        double total = 0.0;
        for (String key : this.tile.gasses.getAllKeys()) {
            double gasAmount = this.tile.gasses.getDouble(key);
            total += gasAmount;
        }
        total = Math.round(total * 1000.0) / 1000.0;
        this.tile.gasMix.putDouble("totalMoles", total);
    }

    public CompoundTag getGasMix() {
        return this.tile.gasMix;
    }
    public void addGas(String key, double moles) {
        this.tile.gasses.putDouble(key, moles);
        updateAll();
    }
    public void removeGas(String key) {
        this.tile.gasses.remove(key);
        updateAll();
    }

    public void setTemperature(double temperature) {
        this.tile.gasMix.putDouble("temperature", Math.max(temperature, TCMB));
        updateAll();
    }
    public Double getTemperature() {
        return this.tile.gasMix.getDouble("temperature");
    }

    public Double getPressure() {
        return this.tile.gasMix.getDouble("pressure");
    }
}
