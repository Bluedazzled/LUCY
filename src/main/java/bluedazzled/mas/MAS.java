package bluedazzled.mas;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.beans.EventHandler;

@Mod(MAS.MODID)
public class MAS {
    public static final String MODID = "mas";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MAS(IEventBus modBus) {
        Registration.init(modBus);
        LOGGER.info("Done initializing.");
        modBus.addListener(this::commonSetup);
//        modBus.addListener(this::registerCapabilities);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
    }
//    private void registerCapabilities(RegisterCapabilitiesEvent event) {
//        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registration.MEIN_BLOCK_ENTITY.get(), (o, direction) -> o.getItemHandler());
//    }
}
