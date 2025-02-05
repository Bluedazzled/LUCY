package bluedazzled.mas;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(MAS.MODID)
public class MAS {
    public static final String MODID = "mas";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MAS(IEventBus modBus) {
        Registration.init(modBus);
        modBus.addListener(this::commonSetup);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {}
}
