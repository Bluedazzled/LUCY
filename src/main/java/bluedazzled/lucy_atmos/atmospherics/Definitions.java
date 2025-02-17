package bluedazzled.lucy_atmos.atmospherics;

//All of these values are ripped shamelessly straight from relevant files within __DEFINES at https://github.com/tgstation/tgstation/blob/master/code/__DEFINES/
public final class Definitions {
    ///kPa*L/(K*mol
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

    ///moles in a 1 m^3 cell at 101.325 Pa and 20 degC (103 or so)
    public static final double MOLES_CELL_STANDARD = (Math.round(ONE_ATMOSPHERE*TILE_VOLUME/(T20C*GAS_CONSTANT)*1000))/1000d;
    ///percentage of o2 in normal air
    public static final double O2STANDARD = .21;
    ///percentage of n2 in normal air
    public static final double N2STANDARD = .79;
    ///o2 standard moles
    public static final double MOLES_O2STANDARD = (Math.round((MOLES_CELL_STANDARD*O2STANDARD)*1000))/1000d;
    ///n2 standard moles
    public static final double MOLES_N2STANDARD = (Math.round((MOLES_CELL_STANDARD*N2STANDARD)*1000))/1000d;
}
