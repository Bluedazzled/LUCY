package bluedazzled.lucy_atmos.atmospherics.defines;

public class LilMaths {
    public static String displayJoules(double units) {
        String[] labels = {"J", "kJ", "MJ", "GJ", "TJ"};
        double scale = .1d;
        for (String label : labels) {
            if (units < scale * 1000) {
                return String.format("%s %s", String.valueOf(units/scale).replaceAll("\\.?0+$", ""), label);
            }
            scale *= 1000;
        }
        return String.format("%s %s", String.valueOf(units / scale).replaceAll("\\.?0+$", ""), "PJ").replaceAll("\\.?0+$", "");
    }
}
