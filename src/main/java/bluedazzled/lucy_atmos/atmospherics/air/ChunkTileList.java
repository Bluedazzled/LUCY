package bluedazzled.lucy_atmos.atmospherics.air;

import bluedazzled.lucy_atmos.atmospherics.AtmosTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.List;

import static bluedazzled.lucy_atmos.Registration.CHUNK_ATMOSTILES;
public class ChunkTileList {

    public static List<BlockPos> getChunkTileList(LevelChunk chunk) {
        return chunk.getData(CHUNK_ATMOSTILES);
    }
    public static void setChunkTileList(LevelChunk chunk, List<BlockPos> list) {
            chunk.setData(CHUNK_ATMOSTILES, list);
            System.out.println("current list:");
            for (BlockPos tile : list) {
                System.out.println(tile);
            }
            chunk.markUnsaved();
    }
    public static void addTileToList(LevelChunk chunk, AtmosTileEntity tile) {
        List<BlockPos> list = new ArrayList<>(getChunkTileList(chunk));

        list.add(tile.getBlockPos());
        setChunkTileList(chunk, list);
    }
    public static void removeTileFromList(LevelChunk chunk, AtmosTileEntity tile) {
        List<BlockPos> list = new ArrayList<>(getChunkTileList(chunk));

        list.remove(tile.getBlockPos());
        setChunkTileList(chunk, list);
    }
}
