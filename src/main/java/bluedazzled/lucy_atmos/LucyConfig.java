package bluedazzled.lucy_atmos;

import com.electronwill.nightconfig.core.ConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class LucyConfig extends ConfigSpec {
    public static final LucyConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private final ModConfigSpec.ConfigValue<Boolean> DEBUG_RENDERER;
    private static boolean debugRenderer;

    private LucyConfig(ModConfigSpec.Builder builder) {
        super();
        DEBUG_RENDERER = builder.define("debug_renderer", false);
    }

    static {
        Pair<LucyConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(LucyConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    public void updateCache() {
        debugRenderer = DEBUG_RENDERER.get();
    }

    public static boolean getDebugRenderer() {
        return debugRenderer;
    }
}
