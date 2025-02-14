package bluedazzled.lucy_atmos.atmospherics.gas;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;

public abstract class GasBehaviour implements FeatureElement { //No, bro, I swear. I didn't just rip this straight from BlockBehaviour.class, totally.
    protected final String name;
    protected final String description;
    protected final FeatureFlagSet requiredFeatures;
    protected Properties properties;


    public GasBehaviour(Properties properties) {
        this.name = properties.name;
        this.description = properties.description;
        this.requiredFeatures = properties.requiredFeatures;
        this.properties = properties;
    }

    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    public Properties properties() {
        return this.properties;
    }

    public static class Properties {
        String name;
        String description;
        FeatureFlagSet requiredFeatures;
        private ResourceKey<Gas> id;

        public static Properties of() {
            return new Properties();
        }
        public Properties setId(ResourceKey<Gas> id) {
            this.id = id;
            return this;
        }
        public Properties name(String name) {
            this.name = name;
            return this;
        }
        public Properties description(String description) {
            this.description = description;
            return this;
        }
    }


}
