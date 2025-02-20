package bluedazzled.lucy_atmos.atmospherics.environmental;

import bluedazzled.lucy_atmos.atmospherics.gasmixtures.gas_mixture;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

import static bluedazzled.lucy_atmos.Registration.*;
import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.*;
import static java.lang.Boolean.*; //??
import static java.lang.Float.POSITIVE_INFINITY;

/*
todo: copy all variables from LINDA_turf_tile here (/turf AND /turf/open, we're merging the two
refer: https://cdn.discordapp.com/attachments/901257151111102505/1341893763035037816/8asm99.png
I wish I could embed images.
 */
@ParametersAreNonnullByDefault
public class turf_tile extends BlockEntity implements gas_mixture {
    public static final Logger LOGGER = LogUtils.getLogger();
    /* /turf */
    ///used for temperature calculations in superconduction
    protected double thermal_conductivity = 0.05;
    protected double heat_capacity = POSITIVE_INFINITY; ///This should be opt in rather then opt out
    protected double temperature_archived;
    ///list of turfs adjacent to us that air can flow onto
    protected ArrayList<BlockPos> atmos_adjacent_turfs;
    ///used to determine whether we should archive
    protected int archived_cycle = 0;
    /// Not copying initial_gas_mix for now
    /* /turf/open */
    ///used for spacewind
    ///Pressure difference between two turfs
    protected double pressure_difference;
    ///Where the difference come from (from higher pressure to lower pressure)
    protected Direction pressure_direction;

    ///Excited group we are part of
    excited_group excited_group;
    ///Are we active?
    protected boolean excited = FALSE;
    ///Our gas mix
    //whatever will be equivalent to var/datum/gas_mixture/turf/air
    /* NOTE:
    While we're on the subject, `/datum/gas_mixture` has two subtypes.
    The first is `/datum/gas_mixture/turf`, which exists for literally one purpose. When a turf is empty, we want it to have the same heat capacity as space. This lets us achieve that by overriding `heat_capacity()`
     */

    ///If there is an active hotspot on us store a reference to it here
    //whatever will be equivalent to var/obj/effect/hotspot/active_hotspot
    /// air will slowly revert to initial_gas_mix
    boolean planetary_atmos = FALSE;
    /// once our paired turfs are finished with all other shares, do one 100% share
    /// exists so things like space can ask to take 100% of a tile's gas
    boolean run_later = FALSE;

    ///gas IDs of current active gas overlays
    ArrayList atmos_overlay_types = new ArrayList();
    int significant_share_ticker = 0;


    /* EXCITED GROUPS */
    protected static class excited_group {
        ///Stores a reference to the turfs we are controlling
        ArrayList<BlockPos> turf_list = new ArrayList<>();
        ///If this is over EXCITED_GROUP_BREAKDOWN_CYCLES we call self_breakdown()
        int breakdown_cycle = 0;
        ///If this is over EXCITED_GROUP_DISMANTLE_CYCLES we call dismantle()
        int dismantle_cooldown = 0;
        ///Used for debug to show the excited groups active and their turfs
        boolean should_display = FALSE;
        ///Id of the index color of the displayed group
        int display_id = 0;
        ///Wrapping loop of the index colors
        static int wrapping_id = 0;
        ///All turf reaction flags we have received.
        byte turf_reactions = NONE;
    }

    public turf_tile(BlockPos pos, BlockState state) {
        super(ATMOS_TILE_ENTITY.get(), pos, state);
    }
}