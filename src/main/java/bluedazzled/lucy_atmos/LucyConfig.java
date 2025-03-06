package bluedazzled.lucy_atmos;

import com.electronwill.nightconfig.core.ConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class LucyConfig extends ConfigSpec {
    public static final LucyConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.ConfigValue<Boolean> debugRenderer;

    private LucyConfig(ModConfigSpec.Builder builder) {
        debugRenderer = builder.define("debug_renderer", false);
    }

    static {
        Pair<LucyConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(LucyConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}
