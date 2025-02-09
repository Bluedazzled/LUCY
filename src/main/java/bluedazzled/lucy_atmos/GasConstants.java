package bluedazzled.lucy_atmos;
//All of these values are ripped shamelessly straight from https://github.com/tgstation/tgstation/blob/master/code/__DEFINES/atmospherics/atmos_core.dm
public final class GasConstants {
    //kPa*L/(K*mol
    public static final double GAS_CONSTANT = 8.31;
    //kPa
    public static final double ONE_ATMOSPHERE = 101.325;

    //-270.3decC
    //Lowest temperature we can go, 'sposed to be space but we aren't in space (yet!)
    public static final double TCMB = 2.7;
    //0degC
    public static final double T0C = 273.15;
    //20degC
    public static final double T20C = 293.15;
    //Liters
    public static final int TILE_VOLUME = 1000;
}
