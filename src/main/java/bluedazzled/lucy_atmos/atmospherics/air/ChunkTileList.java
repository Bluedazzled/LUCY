package bluedazzled.lucy_atmos.atmospherics.air;

import bluedazzled.lucy_atmos.atmospherics.AtmosTileEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.fml.common.EventBusSubscriber;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static bluedazzled.lucy_atmos.Registration.*;
import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

@EventBusSubscriber(modid = MODID)
public class ChunkTileList {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static List<BlockPos> getChunkAllList(LevelChunk chunk) {
        return chunk.getData(CHUNK_ALLTILES);
    }
    public static void setChunkAllList(LevelChunk chunk, List<BlockPos> list) {
            chunk.setData(CHUNK_ALLTILES, list);
            chunk.markUnsaved();
    }
    public static void addToAllList(LevelChunk chunk, AtmosTileEntity tile) {
        List<BlockPos> list = new ArrayList<>(getChunkAllList(chunk));
        list.add(tile.getBlockPos());
        setChunkAllList(chunk, list);
    }
    public static void removeFromAllList(LevelChunk chunk, BlockPos pos) {
        List<BlockPos> list = new ArrayList<>(getChunkAllList(chunk));

        list.remove(pos);
        setChunkAllList(chunk, list);
    }

    public static List<BlockPos> getChunkActiveList(LevelChunk chunk) {
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
            if (chunk.getBlockEntity(tilePos) instanceof AtmosTileEntity tile) {
                if (tile.getActive()) {
                    activeList.add(tilePos);
                }
            } else {
                LOGGER.warn("Position {} wasn't an AtmosTileEntity! Removing this position from the AllList", tilePos);
                removeFromAllList(chunk, tilePos);
            }
        }
        setChunkActiveList(chunk, activeList);
    }

}
