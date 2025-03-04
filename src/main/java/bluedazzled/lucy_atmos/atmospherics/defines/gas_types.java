package bluedazzled.lucy_atmos.atmospherics.defines;

import java.util.HashMap;
import java.util.Map;

public final class gas_types {
    private static final Map<String, gas> GAS_MAP = new HashMap<>();

    public static final gas oxygen = new gas(
            "oxygen",
            20,
            "Oxygen",
            false,
            900,
            "The gas most life forms need to be able to survive. Also an oxidizer."
    ) {};
    public static final gas nitrogen = new gas(
            "nitrogen",
            20,
            "Nitrogen",
            false,
            1000,
            "A very common gas that used to pad artificial atmospheres to habitable pressure."
    ) {};
    /// what the fuck is this?
    public static final gas carbon_dioxide = new gas(
            "co2",
            30,
            "Carbon Dioxide",
            false,
            700,
            "What the fuck is carbon dioxide?"
    ) {};
    public static final gas plasma = new gas(
            "plasma",
            200,
            "Plasma",
            true,
            800,
            "A flammable gas with many other curious properties. Its research is one of NanoTrasen's- Wait, wrong game."
    ) {};
    //Other gases will be ported later. Just want the basic 4

    static {
        GAS_MAP.put(oxygen.id, oxygen);
        GAS_MAP.put(nitrogen.id, nitrogen);
        GAS_MAP.put(carbon_dioxide.id, carbon_dioxide);
        GAS_MAP.put(plasma.id, plasma);
    }

    public static gas getGas(String id) {
        return GAS_MAP.get(id);
    }
}
