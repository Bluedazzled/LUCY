package bluedazzled.lucy_atmos.atmospherics.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.ArrayList;
import java.util.Objects;

import static bluedazzled.lucy_atmos.Registration.ATMOS_TILE_ENTITY;
import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.*;
import static java.lang.Double.POSITIVE_INFINITY;

@ParametersAreNonnullByDefault
public class turf_tile extends BlockEntity {
//region Variables
    // /turf (LINDA_tile_turf.dm AND turf.dm
    ///used for temperature calculations in superconduction
    //What's a superconduction? :troll:
    double thermal_conductivity = .5d;
    double heat_capacity = POSITIVE_INFINITY;
    double temperature_archived;

    ///list of turfs adjacent to us that air can flow onto
    ArrayList<turf_tile> atmosAdjTiles = new ArrayList<>();

    ///used to determine whether we should archive
    //*Pretty* sure these are for the subsystem system for BYOND which we aren't fucking using thank you very much
    int archived_cycle = 0;
    int current_cycle = 0;

    ///How hot the turf is, in kelvin
    double temperature = T20C;

    // /turf/open
    ///used for spacewind
    ///Pressure difference between two turfs
    double pressure_difference = 0d;
    ///Where the difference come from (from higher pressure to lower pressure)
    Direction pressure_direction = null;
    ///Excited group we are part of
    excitedGroup excitedGroup;
    ///Are we active?
    boolean excited = false;
    ///Our gas mix
    gas_mixture air;
    ///air will slowly revert to initial_gas_mix
    boolean planetary_atmos = false;
    ///once our paired turfs are finished with all other shares, do one 100% share
    ///exists so things like space can ask to take 100% of a tile's gas
    boolean run_later = false;

    ///gas IDs of current active gas overlays
    int significant_share_ticker = 0;
    //46-51
//endregion
    public turf_tile(BlockPos pos, BlockState state) {
        super(ATMOS_TILE_ENTITY.get(), pos, state);
        air = create_gas_mixture(); //todo: just move the entirety of create_gas_mixture() up here
        //todo: 57-60
    }

//region Gas mixture methods
    ///Copies all gas info from the turf into a new gas_mixture, along with our temperature
    ///Returns the create gas_mixture
    gas_mixture create_gas_mixture() {
        gas_mixture mix = new gas_mixture(this, TILE_VOLUME); //todo: parse default gas string

        ///accounts for changes in temperature
        //todo: 79-82
        return mix;
    }

    public gas_mixture return_air() {
        return air;
    }

//endregion
//region Simulation
    void process_cell(int fire_count) { //The MEAT
        SSair.remove_from_active(this);

        if(archived_cycle < fire_count) {
            //LINDA_CYCLE_ARCHIVE(this);
        }
        this.current_cycle = fire_count;
        int cached_ticker = significant_share_ticker;
        cached_ticker += 1;

        ///cache for sanic speed
        ArrayList<turf_tile> adjacentTurfs = atmosAdjTiles;
        excitedGroup our_excited_group = this.excitedGroup;
        double our_share_coeff = 1d / (adjacentTurfs.isEmpty() ? 1 : adjacentTurfs.size() + 1);

        gas_mixture our_air = this.air;


        for (turf_tile enemy_tile : adjacentTurfs) {
            if(!(enemy_tile instanceof  turf_tile)) {
                continue;
            }

            if (fire_count <= enemy_tile.current_cycle) {
                continue;
            }

        //region group handling
            boolean should_share_air = false;
            gas_mixture enemy_air = enemy_tile.air;

            ///cache for sanic speed
            excitedGroup enemyExcitedGroup = enemy_tile.excitedGroup;

            ///If we are both in an excited group, and they aren't the same, merge.
            ///If we are both in an excited group, and you're active, share
            ///If we pass compare, and if we're not already both in a group, lets join up
            ///If we both pass compare, add to active and share
            if (our_excited_group != null && enemyExcitedGroup != null) {
                if (our_excited_group != enemyExcitedGroup) {
                    ///combine groups (this also handles updating the excited_group var of all involved turfs)
                    our_excited_group.merge_groups(enemyExcitedGroup);
                    our_excited_group = this.excitedGroup; ///update our cache
                }
            }
            if (our_excited_group != null && enemyExcitedGroup != null && enemy_tile.excited) {
                should_share_air = true;
            } else if (Objects.equals(our_air.compare(enemy_air, ARCHIVE), "")) { ///Let's see if you're up for it
                SSair.add_to_active(enemy_tile);
                excitedGroup existing_group = (our_excited_group != null ? enemyExcitedGroup : new excitedGroup());
                if (our_excited_group == null) {
                    existing_group.add_turf(this);
                }
                if(enemyExcitedGroup == null) {
                    existing_group.add_turf(enemy_tile);
                }
                our_excited_group = this.excitedGroup;
                should_share_air = true;
            }
            //air sharing
            if (should_share_air) {
                double difference = our_air.share(enemy_air, our_share_coeff, 1d / (adjacentTurfs.isEmpty() ? 1d : adjacentTurfs.size() + 1) + 1);
                if (difference != 0) {
                    if(difference > 0) {
                        this.consider_pressure_difference(enemy_tile, difference);
                    }
                    else {
                        enemy_tile.consider_pressure_difference(this, -difference);
                    }
                }
            }
        }
    }
//endregion
//region saving/loading
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }
    @Override
    public void setRemoved() {
        super.setRemoved();
    }
//endregion
//region spacewind
    void consider_pressure_difference(turf_tile target_turf, double difference) {
        BlockPos diff = target_turf.getBlockPos().subtract(this.getBlockPos());
        SSair.high_pressure_delta.add(this);
        if (difference > pressure_difference) {
            pressure_direction = Direction.getApproximateNearest((float)diff.getX(), (float)diff.getY(), (float)diff.getZ());
            pressure_difference = difference;
        }
    }
    //383 - 408
//endregion
//region excited groups
    class excitedGroup {
        ///Stores a reference to the turfs we are controlling
        ArrayList<turf_tile> turf_list = new ArrayList<>();
        ///If this is over EXCITED_GROUP_BREAKDOWN_CYCLES we call self_breakdown()
        int breakdown_cooldown = 0;
        ///If this is over EXCITED_GROUP_DISMANTLE_CYCLES we call dismantle()
        int dismantle_cooldown = 0;
        ///Used for debug to show excited groups active and their turfs
        boolean should_display = false;
        ///Id of the index color of the displayed group
        int display_id = 0;
        ///Wrapping loop of the index colors
        static int wrapping_id = 0;
        ///All turf reaction flags we have received
        byte turf_reactions = 0;

        excitedGroup() {
            SSair.excitedGroups.add(excitedGroup);
        }
        void add_turf(turf_tile target_tile) {
            turf_list.add(target_tile);
            target_tile.excitedGroup = excitedGroup;
            dismantle_cooldown = 0;
            //435
        }
        //Don't forget to cough up the should_display and the likes
        void merge_groups(excitedGroup target_group) {
            if (turf_list.size() > target_group.turf_list.size()) { //If we are the bigger one. We are supreme.
                SSair.excitedGroups.remove(target_group);
                for (turf_tile group_member : target_group.turf_list) {
                    group_member.excitedGroup = excitedGroup;
                    turf_list.add(group_member);
                }
                should_display = target_group.should_display | should_display;
//                if (should_display || SSair.display_all_groups) {
//                    target_group.hide_turfs();
//                    display_turfs();
//                }
                breakdown_cooldown = Math.min(breakdown_cooldown, target_group.breakdown_cooldown); ///Take the smaller of the two options
                dismantle_cooldown = 0;
            }
            else { //We lost.
                SSair.excitedGroups.remove(excitedGroup); //Goodbye, cruel world.
                for (turf_tile group_member : turf_list) {
                    group_member.excitedGroup = target_group;
                    target_group.turf_list.add(group_member);
                }
                target_group.should_display = target_group.should_display | should_display;
//                if (target_group.should_display || SSair.display_all_groups) {
//                    hide_turfs();
//                    target_group.display_turfs();
//                }
                target_group.breakdown_cooldown = Math.min(breakdown_cooldown, target_group.breakdown_cooldown);
                target_group.dismantle_cooldown = 0;
            }
        }
        void reset_cooldowns() {
            breakdown_cooldown = 0;
            dismantle_cooldown = 0;
        }
        void self_breakdown() {self_breakdown(false);} //Default to false
        void self_breakdown(boolean poke_turfs) {

        }
        void dismantle() {
            for (turf_tile current_turf : turf_list) {
                current_turf.excited = false;
                current_turf.significant_share_ticker = 0;
                SSair.active_turfs.remove(current_turf);
                //528-530
            }
            garbage_collect();
        }
        void garbage_collect() {
            if (display_id != 0) {
//                hide_turfs();
            }
            for (turf_tile current_turf : turf_list) {
                current_turf.excitedGroup = null;
            }
            turf_list.clear();
            SSair.excitedGroups.remove(excitedGroup);
            if (SSair.currentpart == SSAIR_EXCITEDGROUPS) {
                SSair.currentrun.remove(excitedGroup);
            }
        }
        //SOMEHOW modify OverlayRenderer. Good luck!
        void display_turfs() {

        }
        void hide_turfs() {

        }
    }
//endregion
}