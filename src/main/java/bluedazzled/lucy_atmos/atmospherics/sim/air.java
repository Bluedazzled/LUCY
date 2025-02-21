package bluedazzled.lucy_atmos.atmospherics.sim;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static bluedazzled.lucy_atmos.atmospherics.ChunkTileList.getAllActiveTiles;
import static bluedazzled.lucy_atmos.atmospherics.ChunkTileList.removeFromChunkActiveList;
import static bluedazzled.lucy_atmos.lucy_atmos.MODID;
import static net.minecraft.world.level.Level.OVERWORLD;

@EventBusSubscriber(modid = MODID)
public class air {
    public static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void fire(ServerTickEvent.Pre tick) {
        long time = System.nanoTime();
        process_active_turfs(tick.getServer().getLevel(OVERWORLD));

        //We NEED to make sure this shit stays under 50ms! Even then, we may need to squeeze a *little* more out, as we aren't the only thing hogging up a single tick.
        long totalTime = System.nanoTime() - time;
        if (totalTime > 50_000_000) {
            LOGGER.error("We took too long to process! Total time taken was {}ms.", totalTime / 1_000_000);
        }
    }
    public static void process_active_turfs(Level level) {
        for (BlockPos pos : getAllActiveTiles(level)) {
            if (level.getBlockEntity(pos) instanceof turf_tile tile) {
                tile.process_cell();
            } else {
                LOGGER.warn("Position {} wasn't a tile_turf! Removing this position from the AllList", pos);
            }
        }
    }
    public static void remove_from_active(turf_tile tile) {
        removeFromChunkActiveList((LevelChunk) tile.getLevel().getChunk(tile.getBlockPos()), tile.getBlockPos()); //sorgy
        tile.setActive(false);
        //implement line 453-456 in air.dm
    }
}
