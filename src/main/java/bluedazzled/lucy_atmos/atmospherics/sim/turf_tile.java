package bluedazzled.lucy_atmos.atmospherics.sim;

import bluedazzled.lucy_atmos.atmospherics.ChunkTileList;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.common.EventBusSubscriber;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static bluedazzled.lucy_atmos.Registration.ATMOS_TILE_ENTITY;
import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.*;
import static bluedazzled.lucy_atmos.atmospherics.sim.air.remove_from_active;
import static bluedazzled.lucy_atmos.atmospherics.sim.gas_mixture.*;
import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

@ParametersAreNonnullByDefault
public class turf_tile extends BlockEntity {
    public gas_mixture gas_mixture = new gas_mixture(this);
    private static final Logger LOGGER = LogUtils.getLogger();
    protected CompoundTag gasses;
    protected CompoundTag gasMix;
    protected CompoundTag adjacentTiles;
    protected CompoundTag tileInfo;

    protected boolean active;

    public turf_tile(BlockPos pos, BlockState state) {
        super(ATMOS_TILE_ENTITY.get(), pos, state);
        this.gasMix = new CompoundTag();
        this.gasses = new CompoundTag();
        this.tileInfo = new CompoundTag();
        this.adjacentTiles = new CompoundTag();

        this.gasMix.putDouble("temperature", T20C);
        this.gasMix.putDouble("volume", TILE_VOLUME);
        this.gas_mixture.addGas("oxygen", MOLES_O2STANDARD);
        this.gas_mixture.addGas("nitrogen", MOLES_N2STANDARD);
    }
    public void setAdjTile(Direction direction, BlockPos pos, boolean updateTile) {
        CompoundTag target = new CompoundTag();
        target.putInt("X", pos.getX());
        target.putInt("Y", pos.getY());
        target.putInt("Z", pos.getZ());
        this.adjacentTiles.put(direction.getName(), target);
        this.gas_mixture.updateTileInfo(updateTile);
    }
    public void removeAdjTile(Direction direction, boolean updateTile) {
        this.adjacentTiles.remove(direction.getName());
        this.gas_mixture.updateTileInfo(updateTile);
    }
    public boolean doesAdjTileExist(Direction direction) {
        return (this.adjacentTiles.getAllKeys().contains(direction.getName()));
    }
    public CompoundTag getAdjTileTag(Direction direction) {
        return (this.adjacentTiles);
    }
    public BlockPos getAdjTilePos(Direction direction) {
            CompoundTag tileTag = getAdjTileTag(direction);
            return new BlockPos(tileTag.getInt("X"), tileTag.getInt("Y"), tileTag.getInt("Z"));
    }
    public void tellAdjTilesWeDontExistAnymore(Level level, BlockPos pos) {
        //Will come up with a better name later
        for (String key : this.adjacentTiles.getAllKeys()) {
            //What kind of fucking sanity check must I put to get IntelliJ to shut up about this goddamn thing? It won't even run if getAllKeys() returns nothing!
            BlockPos neighbor = pos.relative(Objects.requireNonNull(Direction.byName(key)));
            if (level.getBlockEntity(neighbor) instanceof turf_tile atmosTile) { //Sanity check part 2 because I also need that atmosTile definition
                atmosTile.removeAdjTile(Objects.requireNonNull(Direction.byName(key)).getOpposite(), false); //Once again, I fucking hate IntelliJ sometimes. Shut up about your stupid NullPointerException!
            }
        }
        setChanged();
    }

    public void checkNearbyTiles(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            if (level.getBlockEntity(neighbor) instanceof turf_tile atmosTile) {
                this.setAdjTile(direction, neighbor, false);
                //Tell our neighbor to set us as their neighbor relative to where we are to them. Make sense?
                atmosTile.setAdjTile(direction.getOpposite(), pos, false);
            }
        }
        setChanged();
    }

    public void setActive(boolean active) {
        this.active = active;
        gas_mixture.updateAll();
    }
    public boolean getActive() {
        return this.active;
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
        //level not null and not client sided
        if (this.level != null && !this.level.isClientSide) {
            if (!ChunkTileList.getChunkAllList(this.level.getChunkAt(this.getBlockPos())).contains(this.getBlockPos())) {
                checkNearbyTiles(this.level, this.getBlockPos());
                setActive(true);
                ChunkTileList.addToAllList(this.level.getChunkAt(this.getBlockPos()), this);
            }
        }
    }
    @Override
    public void setRemoved() {
        super.setRemoved();
        //level not null, not client sided, and is loaded
        if (this.level != null && !this.level.isClientSide && this.level.isLoaded(this.getBlockPos())) {
            if (ChunkTileList.getChunkAllList(this.level.getChunkAt(this.getBlockPos())).contains(this.getBlockPos())) {
                tellAdjTilesWeDontExistAnymore(this.level, this.getBlockPos());
                ChunkTileList.removeFromAllList(this.level.getChunkAt(this.getBlockPos()), this.getBlockPos());
            } else {
                //This will never get called, I thought to myself.
                LOGGER.warn("Tried to remove tile at {} from chunk {}; however, we aren't there!", this.getBlockPos(), this.level.getChunk(this.getBlockPos()).getPos());
            }
        }
    }

    public void process_cell() {
        remove_from_active(this);
    }

}