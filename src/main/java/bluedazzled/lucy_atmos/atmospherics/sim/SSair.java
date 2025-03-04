package bluedazzled.lucy_atmos.atmospherics.sim;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

import java.util.ArrayList;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;
import static net.minecraft.world.level.Level.OVERWORLD;
import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.*;

@EventBusSubscriber(modid = MODID)
public class SSair {
//region Variables
    private static long lastTick = -1;
    public static final Logger LOGGER = LogUtils.getLogger();

    static ArrayList<turf_tile.excitedGroup> excitedGroups = new ArrayList<>();
    static ArrayList<turf_tile> active_turfs = new ArrayList<>();

    static ArrayList<turf_tile> high_pressure_delta = new ArrayList<>();

    static boolean display_all_groups = false;

    static ArrayList<Object> currentrun = new ArrayList<>(); //This surely won't go wrong!
    static byte currentpart = SSAIR_PIPENETS;
//endregion
    @SubscribeEvent
    public static void fire(ServerTickEvent.Pre tick) {
        ServerLevel level = tick.getServer().getLevel(OVERWORLD);
        long currentTick = level.getGameTime();
        if (lastTick == currentTick) {
            return;
        }
        long time = System.nanoTime();
        //PIPENETS
        //MACHINERY
        //ACTIVE TURFS
        //HOTSPOTS (i think fire, like the light or whatever)
        //EXCITED GROUPS ^^
        //HIGH PRESSURE DELTAS ^^
        //SUPERCONDUCTIVITY (idfk)
        //We NEED to make sure this shit stays under 50ms! Even then, we may need to squeeze a *little* more out, as we aren't the only thing hogging up a single tick.
        long totalTime = System.nanoTime() - time;
        if (totalTime > 50_000_000) {
            LOGGER.error("We took too long to process! Total time taken was {}ms.", totalTime / 1_000_000);
        }
        lastTick = currentTick;
    }

    public static void remove_from_active(turf_tile tile) {}
    public static void add_to_active(turf_tile tile) {}
}
