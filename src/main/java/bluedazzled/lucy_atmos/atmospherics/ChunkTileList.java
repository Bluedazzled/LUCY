package bluedazzled.lucy_atmos.atmospherics;

import bluedazzled.lucy_atmos.atmospherics.sim.turf_tile;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static bluedazzled.lucy_atmos.Registration.*;
import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class ChunkTileList {
    //list of all tiles per chunk
    private static final Logger LOGGER = LogUtils.getLogger();
    public static List<BlockPos> getChunkAllList(LevelChunk chunk) {
        return chunk.getData(CHUNK_ALLTILES);
    }
    public static void setChunkAllList(LevelChunk chunk, List<BlockPos> list) {
        chunk.setData(CHUNK_ALLTILES, list);
        ChunkPos pos = chunk.getPos();
        Level level = chunk.getLevel();
        if (!getGlobalTileChunks(level).contains(pos)) { //if this chunk isn't in the global chunk list
            addChunkToGlobalList(level, pos);
        }
        if (getChunkAllList(chunk).isEmpty()) { //if the chunk is empty take us out of the global chunk list
            removeChunkFromGlobalList(level, pos);
        }
        chunk.markUnsaved();
    }
    public static void addToAllList(LevelChunk chunk, turf_tile tile) {
        List<BlockPos> list = new ArrayList<>(getChunkAllList(chunk));
        list.add(tile.getBlockPos());
        setChunkAllList(chunk, list);
    }
    public static void removeFromAllList(LevelChunk chunk, BlockPos pos) {
        List<BlockPos> list = new ArrayList<>(getChunkAllList(chunk));
        list.remove(pos);
        setChunkAllList(chunk, list);
    }
    //list of all active tiles per chunk
    public static List<BlockPos> getChunkActiveList(LevelChunk chunk) {
        if (chunk == null) {
            return new ArrayList<>();
        }
        return chunk.getData(CHUNK_ACTIVETILES);

    }
    public static void setChunkActiveList(LevelChunk chunk, List<BlockPos> list) {
        chunk.setData(CHUNK_ACTIVETILES, list);
        chunk.markUnsaved();
    }
    public static void updateChunkActiveList(LevelChunk chunk) {
        List<BlockPos> allList = getChunkAllList(chunk);
        List<BlockPos> activeList = new ArrayList<>();

        setChunkActiveList(chunk, new ArrayList<>());
        for (BlockPos tilePos : allList) {
            if (chunk.getBlockEntity(tilePos) instanceof turf_tile tile) {
                if (tile.getActive()) {
                    activeList.add(tilePos);
                } else {
                    return;
                }
            } else {
                LOGGER.warn("Position {} wasn't a turf_tile! Removing this position from the AllList", tilePos);
                removeFromAllList(chunk, tilePos);
            }
        }
        setChunkActiveList(chunk, activeList);
    }
    public static void removeFromChunkActiveList(LevelChunk chunk, BlockPos pos) {
        List<BlockPos> currentList = getChunkActiveList(chunk);
        currentList.remove(pos);
        setChunkActiveList(chunk, currentList);
    }
    //list of all chunks with tiles stored at 0, 0 because i couldn't be bothered to rig together a SavedData. this will DEFINITELY break when done in another dimension. oh well!
    public static List<ChunkPos> getGlobalTileChunks(Level level) {
        LevelChunk chunk = level.getChunk(0,0); //may 0 0 have mercy on us all!
        return chunk.getData(GLOBAL_TILECHUNKS);
    }
    public static void setGlobalTileChunks(Level level, List<ChunkPos> list) {
        LevelChunk chunk = level.getChunk(0,0);
        chunk.setData(GLOBAL_TILECHUNKS, list);
    }
    public static void addChunkToGlobalList(Level level, ChunkPos pos) {
        List<ChunkPos> list = new ArrayList<>(getGlobalTileChunks(level));
        list.add(pos);
        setGlobalTileChunks(level, list);
    }
    public static void removeChunkFromGlobalList(Level level, ChunkPos pos) {
        List<ChunkPos> list = new ArrayList<>(getGlobalTileChunks(level));
        list.remove(pos);
        setGlobalTileChunks(level, list);
    }
    //returns a list of *every* active turf_tile.
    public static List<BlockPos> getAllActiveTiles(Level level) {
        if (level == null) {
            return null;
        }
        List<BlockPos> activeTiles = new ArrayList<>();
        for (ChunkPos pos : getGlobalTileChunks(level)) {
            activeTiles.addAll(getChunkActiveList(level.getChunkSource().getChunk(pos.x, pos.z, false)));
        }
        return activeTiles;
    }
}
