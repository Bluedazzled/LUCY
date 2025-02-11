package bluedazzled.lucy_atmos.atmospherics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.ParametersAreNonnullByDefault;

import static bluedazzled.lucy_atmos.atmospherics.GasConstants.*;
import static bluedazzled.lucy_atmos.Registration.*;

@ParametersAreNonnullByDefault
public class AtmosTileEntity extends BlockEntity {
    public CompoundTag gasses;
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

        setTemperature(T20C);
        this.gasMix.putDouble("volume", TILE_VOLUME);
        addGas("oxygen", MOLES_O2STANDARD);
        addGas("nitrogen", MOLES_N2STANDARD);
        setActive(false);
    }

    public CompoundTag getGasMix() {
        return this.gasMix;
    }

    public void updateAll() {
        this.gasMix.put("gasses", this.gasses);
        updateTotalMoles();
        updatePressure();
        this.tileInfo.put("gasMix", this.gasMix);
        setChanged();
    }
    public void updateTileInfo() { //Because maybe I don't want to recalculate everything
        this.tileInfo.putBoolean("active", active);
        setChanged();
    }

    public void setTemperature(double temperature) {
        this.gasMix.putDouble("temperature", Math.max(temperature, TCMB));
        updateAll();
    }

    public void clearGasses() { //Why would anyone want this? At all?
        this.gasMix.remove("gasses");
        this.gasses = new CompoundTag();
        updateAll();
    }
    public void addGas(String key, double moles) {
        this.gasses.putDouble(key, moles);
        updateAll();
    }
    public void setGasMoles(String key, double moles) {
        this.gasses.putDouble(key, moles);
        updateAll();
    }
    public void removeGas(String key) {
        this.gasses.remove(key);
        updateAll();
    }

    public void setActive(boolean active) {
        this.active = active;
        updateTileInfo();
    }
    public boolean getActive() {
        return this.active;
    }

    public void updatePressure() {
        double totalMoles = this.gasMix.getDouble("totalMoles");
        double temperature = this.gasMix.getDouble("temperature");
        int volume = this.gasMix.getInt("volume");
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

