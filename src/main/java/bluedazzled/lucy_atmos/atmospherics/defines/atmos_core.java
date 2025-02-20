package bluedazzled.lucy_atmos.atmospherics.defines;

//All of these values are ripped shamelessly straight from code/__DEFINES/atmospherics/
//Also I'm not porting all of these all at once because it may be months (Yes, months.) before I use them
public final class atmos_core {
    /* __DEFINES/flags.dm */ //Sorry I really need this for compability
    public static final byte NONE = 0;
    /* __DEFINES/atmospherics/atmos_core.dm */
    ///kPa*L/(K*mol)
    public static final double GAS_CONSTANT = 8.31;
    ///kPa
    public static final double ONE_ATMOSPHERE = 101.325;

    ///-270.3decC
    //Lowest temperature we can go, 'sposed to be space but we aren't in space (yet!)
    public static final double TCMB = 2.7;
    ///0degC
    public static final double T0C = 273.15;
    ///20degC
    public static final double T20C = 293.15;
    ///Liters
    public static final double TILE_VOLUME = 1000;

    /**
     *I feel the need to document what happens here. Basically this is used
     *catch rounding errors, and make gas go away in small portions.
     *People have raised it to higher levels in the past, do not do this. Consider this number a soft limit
     *If you're making gasmixtures that have unexpected behavior related to this value, you're doing something wrong.
     *
     *On an unrelated note this may cause a bug that creates negative gas, related to round(). When it has a second arg it will round up.
     *So for instance round(0.5, 1) == 1. I've hardcoded a fix for this into share, by forcing the garbage collect.
     *Any other attempts to fix it just killed atmos. I leave this to a greater man then I
     */
    /// The minimum heat capacity of a gas
    public static final double MINIMUM_HEAT_CAPACITY = 0.0003;
    /// Minimum mole count of a gas
    public static final double MINIMUM_MOLE_COUNT = 0.01;
    /// Molar accuracy to round to
    public static final double MOLAR_ACCURACY = 1E-4;
    /// Types of gases (based on gaslist_cache)
//    public static final double GAS_TYPE_COUNT GLOB.gaslist_cache.len //TODO: ask about GLOB.gaslist_cache
    /// Maximum error caused by QUANTIZE when removing gas (roughly, in reality around 2 * MOLAR_ACCURACY less)
//    public static final double MAXIMUM_ERROR_GAS_REMOVAL (MOLAR_ACCURACY * GAS_TYPE_COUNT)

    ///moles in a 1 m^3 cell at 101.325 Pa and 20 degC (103 or so)
    public static final double MOLES_CELLSTANDARD = (Math.round(ONE_ATMOSPHERE*TILE_VOLUME/(T20C*GAS_CONSTANT)*1000))/1000d;
    ///percentage of o2 in normal air
    public static final double O2STANDARD = .21;
    ///percentage of n2 in normal air
    public static final double N2STANDARD = .79;
    ///o2 standard moles
    public static final double MOLES_O2STANDARD = (Math.round((MOLES_CELLSTANDARD*O2STANDARD)*1000))/1000d;
    ///n2 standard moles
    public static final double MOLES_N2STANDARD = (Math.round((MOLES_CELLSTANDARD*N2STANDARD)*1000))/1000d;

    ///EXCITED GROUPS
    /** yeah can IntelliJ fuck off about this
     * Some further context on breakdown. Unlike dismantle, the breakdown ticker doesn't reset itself when a tile is added
     * This is because we cannot expect maps to have small spaces, so we need to even ourselves out often
     * We do this to avoid equalizing a large space in one tick, with some significant amount of say heat diff
     * This way large areas don't suddenly all become cold at once, it acts more like a wave
     *
     * Because of this and the behavior of share(), the breakdown cycles value can be tweaked directly to effect how fast we want gas to move
     */
    /// number of FULL air controller ticks before an excited group breaks down (averages gas contents across turfs)
    public static final int EXCITED_GROUP_BREAKDOWN_CYCLES = 4;
    /// number of FULL air controller ticks before an excited group dismantles and removes its turfs from active
    public static final int EXCITED_GROUP_DISMANTLE_CYCLES = (EXCITED_GROUP_BREAKDOWN_CYCLES * 2) + 1; ///Reset after 2 breakdowns
    /// Ratio of air that must move to/from a tile to reset group processing
    public static final double MINIMUM_AIR_RATIO_TO_SUSPEND = 0.1;
    /// Minimum ratio of air that must move to/from a tile
    public static final double MINIMUM_AIR_RATIO_TO_MOVE = 0.001;
    /// Minimum amount of air that has to move before a group processing can be suspended (Round about 10)
    public static final double MINIMUM_AIR_TO_SUSPEND = (MOLES_CELLSTANDARD*MINIMUM_AIR_RATIO_TO_SUSPEND);
    /// Either this must be active (round about 0.1) //Might need to raise this a tad to better support space leaks. we'll see
    public static final double MINIMUM_MOLES_DELTA_TO_MOVE = (MOLES_CELLSTANDARD*MINIMUM_AIR_RATIO_TO_MOVE);
    /// or this (or both, obviously)
    public static final double MINIMUM_TEMPERATURE_TO_MOVE = (T20C+100);
    /// Minimum temperature difference before group processing is suspended
    public static final int MINIMUM_TEMPERATURE_DELTA_TO_SUSPEND = 4;
    /// Minimum temperature difference before the gas temperatures are just set to be equal
    public static final double MINIMUM_TEMPERATURE_DELTA_TO_CONSIDER = 0.5;
    ///Minimum temperature to continue superconduction once started
    public static final double MINIMUM_TEMPERATURE_FOR_SUPERCONDUCTION = (T20C+80);
    ///Minimum temperature to start doing superconduction calculations
    public static final double MINIMUM_TEMPERATURE_START_SUPERCONDUCTION = (T20C+400);

    ///HEAT TRANSFER COEFFICIENTS
    ///Must be between 0 and 1. Values closer to 1 equalize temperature faster
    ///Should not exceed 0.4 else strange heat flow occur
    public static final double WALL_HEAT_TRANSFER_COEFFICIENT = 0.0; //Uh probably not going to be used but here nontheless
    public static final double OPEN_HEAT_TRANSFER_COEFFICIENT = 0.4;
    /// a hack for now
    public static final double WINDOW_HEAT_TRANSFER_COEFFICIENT = 0.1; //See WALL_HEAT_TRANSFER_COEFFICIENT
    /// a hack to help make vacuums "cold", sacrificing realism for gameplay
    public static final double HEAT_CAPACITY_VACUUM = 7000;
}
