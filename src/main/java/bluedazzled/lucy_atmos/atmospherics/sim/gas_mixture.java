package bluedazzled.lucy_atmos.atmospherics.sim;

import bluedazzled.lucy_atmos.atmospherics.defines.gas_types;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.*;

import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.*;

public class gas_mixture {
//region Variables
    private static final Logger LOGGER = LogUtils.getLogger();
    turf_tile tile;

    Map<String, double[]> gases;
    ///The temperature of the gas mix in kelvin. Should never be lower than TCMB
    double temperature = TCMB;
    ///Used, like all archived variables, to ensure turf sharing is consistent inside a tick, no matter the other of operations
    double temperature_archived = TCMB; //figure out whatever makes it a tmp, and implement its design
    ///Volume in liters (duh)
    double volume = TILE_VOLUME;
    ///The last tick this gas mixture shared on. A counter that turfs use to manage activity
    double last_share = 0;
    ///Tells us what reactions have happened in our gasmix. Assoc list of reaction - moles reacted each pair
    //ArrayList of something, named reaction_results
    ///Used for analyzer feedback - not initialized until it's used
    //Again, ArrayList of something, named analyzer_results
    ///Whether to call garbage_collect() on the sharer during shares, used for immutable mixtures
    boolean gc_share = false;
    ///When this gas mixture was last touched by pipenet processing
    ///I am sorry
    //Why are you sorry? Future me, why are they sorry?
    int pipenet_cycle = -1;
//endregion
    public gas_mixture(turf_tile tile, double volume) {
        this.tile = tile;
        gases = new HashMap<>();
        //Fuck 49, it can't be null!
        this.volume = volume;
        if (this.volume <= 0) {
            LOGGER.warn("Hey! Real bad man! Tile at {} just tried to create itself with negative or 0 volume!", this.volume);
        }
        //reaction_results = new ArrayList<>();
    }
//region listmos/Returns
    //Completely ditching the old listmos system. Whatever the fuck they were smoking I ain't.
    boolean hasGas(String gasId) {
        return hasGas(gasId, 0);
    }
    boolean hasGas(String gasId, double amount) {
        gases.computeIfAbsent(gasId, k -> new double[2]);
        return amount < gases.get(gasId)[MOLES];
    }
    double heat_capacity() {
        double heatCapacity = 0;
        for (String id : gases.keySet()) {
            heatCapacity += gas_types.getGas(id).getSpecific_heat();
        }
        return heatCapacity;
    }
    double total_moles() {
        return total_moles(MOLES);
    }
    double total_moles(int data) {
        double moles = 0;
        for (Map.Entry<String, double[]> entry : gases.entrySet()) {
            moles += entry.getValue()[data];
        }
        return moles;
    }
    double getPressure() {
        //It should never be negative anyways. That would be fucking wild...
        if (Math.abs(volume) != 0) {
            return total_moles() * GAS_CONSTANT * temperature / volume;
        }
        LOGGER.warn("YO! Tile at {} just tried to return pressure with negative or 0 volume!", tile.getBlockPos());
        return 0;
    }
    double getTemperature() {
        return temperature;
    }
    double getTemperature_archived() {
        return temperature_archived;
    }
    double thermal_energy() {
        return (getTemperature() * heat_capacity());
    }
    void archive() {
        temperature_archived = temperature;
        for (Map.Entry<String, double[]> entry : gases.entrySet()) {
            entry.getValue()[ARCHIVE] = entry.getValue()[MOLES];
        }
    }
//endregion
//region gas handling
    /// Performs air sharing calculations between two gas_mixtures
    /// share() is communitive, which means A.share(B) needs to be the same as B.share(A)
    /// If we don't retain this, we will get negative moles. Don't do it
    /// Returns: amount of gas exchanged (+ if sharer received)
    double share (gas_mixture their, double ourCoeff, double theirCoeff) {
        Map<String, double[]> ourGases = this.gases;
        Map<String, double[]> theirGases = their.gases;

        ArrayList<String> onlyInThem = (ArrayList<String>) theirGases.keySet().stream()
                .filter(id -> !ourGases.containsKey(id))
                .toList();
        ArrayList<String> onlyInUs = (ArrayList<String>) ourGases.keySet().stream()
                .filter(id -> !theirGases.containsKey(id))
                .toList();

        double temperatureDelta = temperature_archived - their.temperature_archived;
        double ourOldHeatCapacity = 0;
        double theirOldHeatCapacity = 0;
        if(Math.abs(temperatureDelta) > MINIMUM_TEMPERATURE_DELTA_TO_CONSIDER) {
            ourOldHeatCapacity = heat_capacity();
            theirOldHeatCapacity = their.heat_capacity();
        }
        double ourHeatCapacityToThem = 0;
        double theirHeatCapacityToUs = 0;

        double movedMoles = 0;
        double movedMolesAbs = 0;

        ///GAS TRANSFER

        //prep
        for (String id : onlyInThem) {
            ourGases.computeIfAbsent(id, k -> new double[2]);
        }
        for (String id : onlyInUs) {
            theirGases.computeIfAbsent(id, k -> new double[2]);
        }


        for (Map.Entry<String, double[]> ourEntry : ourGases.entrySet()) {
            String id = ourEntry.getKey();
            double[] ourGas = ourEntry.getValue();
            double[] theirGas = theirGases.get(id);
            double delta = Math.round(((ourGas[ARCHIVE] - theirGas[ARCHIVE])*MOLAR_ACCURACY)/MOLAR_ACCURACY);

            /// If we have more gas than they do, gas is moving from us to them
            /// This means we want to scale it by our coeff. Vis versa for their case.
            if (delta > 0) delta = delta * ourCoeff;
            else delta = delta * theirCoeff;

            if (Math.abs(temperatureDelta) > MINIMUM_TEMPERATURE_DELTA_TO_CONSIDER) {
                double gasHeatCapacity = delta * gas_types.getGas(id).getSpecific_heat();
                if (delta > 0) ourHeatCapacityToThem += gasHeatCapacity;
                else theirHeatCapacityToUs -= gasHeatCapacity; ///Subtract here instead of adding the absolute value because we know that delta is negative.
            }
            ourGas[MOLES] -= delta;
            theirGas[MOLES] += delta;
            movedMolesAbs += Math.abs(delta);
        }
        last_share = movedMolesAbs;

        ///THERMAL ENERGY TRANSFER
        if (Math.abs(temperatureDelta) > MINIMUM_TEMPERATURE_DELTA_TO_CONSIDER) {
            double ourNewHeatCapacity = ourOldHeatCapacity + theirHeatCapacityToUs - ourHeatCapacityToThem;
            double theirNewHeatCapacity = theirOldHeatCapacity + ourHeatCapacityToThem - theirHeatCapacityToUs;

            if (ourNewHeatCapacity > MINIMUM_HEAT_CAPACITY) {
                temperature = (ourOldHeatCapacity * temperature
                        - ourHeatCapacityToThem * temperature_archived
                        + theirHeatCapacityToUs * their.temperature_archived) / ourNewHeatCapacity;
            }

            if (theirNewHeatCapacity > MINIMUM_HEAT_CAPACITY) {
                their.temperature = (theirOldHeatCapacity * their.temperature
                        - theirHeatCapacityToUs * their.temperature_archived
                        + ourHeatCapacityToThem * temperature_archived) / theirNewHeatCapacity;
            }
            ///thermal energy of the system (us and them) is unchanged
            if (Math.abs(theirOldHeatCapacity) > MINIMUM_HEAT_CAPACITY) {
                if (Math.abs(theirNewHeatCapacity / theirOldHeatCapacity - 1) < 0.1) { /// <10% change in their heat capacity
                    temperature_share(their);
                }
            }
        }
        //garbage collection 414-418

        if (temperatureDelta > MINIMUM_TEMPERATURE_TO_MOVE || Math.abs(movedMoles) > MINIMUM_MOLES_DELTA_TO_MOVE) {
            double ourMoles = total_moles();
            double theirMoles = their.total_moles();
            return (temperature_archived * (ourMoles+movedMoles)
                    - their.temperature_archived * (theirMoles - movedMoles))
                    * GAS_CONSTANT / volume;
        }
        return 0; //turns out we need a return value if the above fails so uh here ya go chat
    }
    void temperature_share(gas_mixture their) {
        their.temperature = their.temperature_archived;
        double temperatureDelta = temperature_archived - their.temperature;
        if (Math.abs(temperatureDelta) > MINIMUM_TEMPERATURE_DELTA_TO_CONSIDER) {
            double ourHeatCapacity = heat_capacity();
            double theirHeatCapacity = their.heat_capacity();
            if ((theirHeatCapacity > MINIMUM_HEAT_CAPACITY) && (ourHeatCapacity > MINIMUM_HEAT_CAPACITY)) {
                //Yes, this is 4 lines now. However, it does make it a bit more readable than ((temperature_delta) * ((heat_capacity_one) * ((heat_capacity_two) / ((heat_capacity_one) + (heat_capacity_two)))))
                //Can you fucking imagine that? That's a parenthesis mess and a half!
                double temperatureDelta4Conduction = OPEN_HEAT_TRANSFER_COEFFICIENT * temperatureDelta;
                double totalHeatCapacity = theirHeatCapacity + ourHeatCapacity;
                double heatCapacityRatio = theirHeatCapacity / totalHeatCapacity;
                double heat = temperatureDelta4Conduction * ourHeatCapacity * heatCapacityRatio;

                temperature = Math.max(temperature - heat/ourHeatCapacity, TCMB);
                their.temperature = Math.max(their.temperature + heat/theirHeatCapacity, TCMB);
                //garbage collection 444-447
            }
        }
        //return their.temperature;
    }
    String compare(gas_mixture their, int index) {
        double molesSum = 0;
        Set<String> allGasIds = new HashSet<>();
        allGasIds.addAll(gases.keySet());
        allGasIds.addAll(their.gases.keySet());
        for (String id : allGasIds) {
            double ourMoles = gases.containsKey(id) && index <
                    gases.get(id).length ? gases.get(id)[index]
                    : 0;
            double theirMoles = their.gases.containsKey(id) && index <
                    their.gases.get(id).length ? gases.get(id)[index]
                    : 0;
            /// Brief explanation. We are much more likely to not pass this first check then pass the first and fail the second
            /// Because of this, double calculating the delta is FASTER then inserting it into a var
            if (Math.abs(ourMoles - theirMoles) > MINIMUM_MOLES_DELTA_TO_MOVE) {
                if (Math.abs(ourMoles - theirMoles) > ourMoles * MINIMUM_AIR_RATIO_TO_MOVE) {
                    return id;
                }
            }
            molesSum += ourMoles;
        }
        if (molesSum > MINIMUM_MOLES_DELTA_TO_MOVE) {
            if(index == ARCHIVE) {
                if (Math.abs(temperature_archived - their.temperature_archived) > MINIMUM_TEMPERATURE_DELTA_TO_SUSPEND) {
                    return "temp";
                }
            }
            else if (Math.abs(temperature - their.temperature) > MINIMUM_TEMPERATURE_DELTA_TO_SUSPEND) {
                return "temp";
            }
        }
        return "";
    }
}
