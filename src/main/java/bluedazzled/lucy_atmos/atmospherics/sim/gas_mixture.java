package bluedazzled.lucy_atmos.atmospherics.sim;

import bluedazzled.lucy_atmos.atmospherics.AtmosTileEntity;
import net.minecraft.nbt.CompoundTag;

import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.GAS_CONSTANT;
import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.TCMB;

public class gas_mixture {

    public static void updateAll(AtmosTileEntity tile) {
        tile.gasMix.put("gasses", tile.gasses);
        updateTotalMoles(tile);
        updatePressure(tile);
        updateTileInfo(tile, true);
    }
    public static void updateTileInfo(AtmosTileEntity tile, boolean update) {
        tile.tileInfo.put("gasMix", tile.gasMix);
        tile.tileInfo.put("adjacentTiles", tile.adjacentTiles);
        if (update) {
            tile.setChanged();
        }
    }
    public static void updatePressure(AtmosTileEntity tile) {
        double totalMoles = tile.gasMix.getDouble("totalMoles");
        double temperature = tile.gasMix.getDouble("temperature");
        int volume = tile.gasMix.getInt("volume");
        double pressure = (totalMoles * GAS_CONSTANT * temperature) / volume;

        pressure = Math.round(pressure * 1000.0) / 1000.0;
        tile.gasMix.putDouble("pressure", pressure);
    }
    public static void updateTotalMoles(AtmosTileEntity tile) {
        double total = 0.0;
        for (String key : tile.gasses.getAllKeys()) {
            double gasAmount = tile.gasses.getDouble(key);
            total += gasAmount;
        }
        total = Math.round(total * 1000.0) / 1000.0;
        tile.gasMix.putDouble("totalMoles", total);
    }

    public static CompoundTag getGasMix(AtmosTileEntity tile) {
        return tile.gasMix;
    }
    public static void addGas(AtmosTileEntity tile, String key, double moles) {
        tile.gasses.putDouble(key, moles);
        updateAll(tile);
    }
    public static void removeGas(AtmosTileEntity tile, String key) {
        tile.gasses.remove(key);
        updateAll(tile);
    }

    public static void setTemperature(AtmosTileEntity tile, double temperature) {
        tile.gasMix.putDouble("temperature", Math.max(temperature, TCMB));
        updateAll(tile);
    }
    public static Double getTemperature(AtmosTileEntity tile) {
        return tile.gasMix.getDouble("temperature");
    }

    public static Double getPressure(AtmosTileEntity tile) {
        return tile.gasMix.getDouble("pressure");
    }
}
