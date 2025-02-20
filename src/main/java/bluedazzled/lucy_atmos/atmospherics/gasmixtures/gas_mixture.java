package bluedazzled.lucy_atmos.atmospherics.gasmixtures;

import java.util.ArrayList;

import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.*;
import static java.lang.Boolean.*;

public interface gas_mixture {
    ArrayList gases = new ArrayList(); //TODO: FIX THIS SHIT, FIGURE OUT WHAT VARIABLE TYPE TO USE FOR THIS. I CANNOT RAWDOG THIS. INTELLIJ. WILL. BITCH.
    /// The temperature of the gas mix in kelvin. Should never be lower then TCMB
    double temperature = TCMB;
    /// Used, like all archived variables, to ensure turf sharing is consistent inside a tick, no matter
    /// The order of operations
    double temperature_archived = TCMB;
    /// Volume in liters (duh)
    double volume = TILE_VOLUME;
    /// The last tick this gas mixture shared on. A counter that turfs use to manage activity
    int last_share = 0;
    /// Tells us what reactions have happened in our gasmix. Assoc list of reaction - moles reacted pair.
    ArrayList reaction_results = new ArrayList(); //see gases
    /// Used for analyzer feedback - not initialized until its used
    ArrayList analyzer_results = new ArrayList(); //see gases
    /// Whether to call garbage_collect() on the sharer during shares, used for immutable mixtures
    boolean gc_share = FALSE; //😬 not happening
    /// When this gas mixture was last touched by pipenet processing
    /// I am sorry
    int pipenet_cycle = -1;
}
