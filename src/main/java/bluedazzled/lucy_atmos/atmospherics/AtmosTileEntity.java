package bluedazzled.lucy_atmos.atmospherics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Objects;

import static bluedazzled.lucy_atmos.atmospherics.GasConstants.*;
import static bluedazzled.lucy_atmos.Registration.*;

@ParametersAreNonnullByDefault
public class AtmosTileEntity extends BlockEntity {
    private CompoundTag gasses;
    private CompoundTag gasMix;
    private CompoundTag adjacentTiles;

    private boolean active;

    private CompoundTag tileInfo;

    public AtmosTileEntity(BlockPos pos, BlockState state) {
        super(ATMOS_TILE_ENTITY.get(), pos, state);
        this.gasMix = new CompoundTag();
        this.gasses = new CompoundTag();
        this.tileInfo = new CompoundTag();
        this.adjacentTiles = new CompoundTag();

        setTemperature(T20C);
        this.gasMix.putDouble("volume", TILE_VOLUME);
        addGas("oxygen", MOLES_O2STANDARD);
        addGas("nitrogen", MOLES_N2STANDARD);
        setActive(false);
    }
    public void setAdjTile(Direction direction, BlockPos pos, boolean updateTile) {
        CompoundTag target = new CompoundTag();
        target.putInt("X", pos.getX());
        target.putInt("Y", pos.getY());
        target.putInt("Z", pos.getZ());
        this.adjacentTiles.put(direction.getName(), target);
        updateTileInfo(updateTile);
    }
    public void removeAdjTile(Direction direction, boolean updateTile) {
        this.adjacentTiles.remove(direction.getName());
        updateTileInfo(updateTile);
    }
    public void tellAdjTilesWeDontExistAnymore(Level level, BlockPos pos) {
        //Will come up with a better name later
        for (String key : this.adjacentTiles.getAllKeys()) {
            //What kind of fucking sanity check must I put to get IntelliJ to shut up about this goddamn thing? It won't even run if getAllKeys() returns nothing!
            BlockPos neighbor = pos.relative(Objects.requireNonNull(Direction.byName(key)));
            if (level.getBlockEntity(neighbor) instanceof AtmosTileEntity atmosTile) { //Sanity check part 2 because I also need that atmosTile definition
                atmosTile.removeAdjTile(Objects.requireNonNull(Direction.byName(key)).getOpposite(), false); //Once again, I fucking hate IntelliJ sometimes. Shut up about your stupid NullPointerException!
            }
        }
        setChanged();
    }
    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockent) {
        //tbd
    }

    public void checkNearbyTiles(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            if (level.getBlockEntity(neighbor) instanceof AtmosTileEntity atmosTile) {
                this.setAdjTile(direction, neighbor, false);
                //Tell our neighbor to set us as their neighbor relative to where we are to them. Make sense?
                atmosTile.setAdjTile(direction.getOpposite(), pos, false);
            }
        }
        setChanged();
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
    public void updateTileInfo(boolean update) { //Because maybe I don't want to recalculate everything
        this.tileInfo.putBoolean("active", this.active);
        this.tileInfo.put("adjacentTiles", this.adjacentTiles);
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
        updateTileInfo(true);
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

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide) {
            checkNearbyTiles(this.level, this.getBlockPos());
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level != null && !this.level.isClientSide) {
            tellAdjTilesWeDontExistAnymore(this.level, getBlockPos());
        }
    }
}

