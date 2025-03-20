package bluedazzled.lucy_atmos;

import bluedazzled.lucy_atmos.client.rendering.TileRenderer;
import bluedazzled.lucy_atmos.init.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

import static bluedazzled.lucy_atmos.LucyConfig.CONFIG;
import static bluedazzled.lucy_atmos.LucyConfig.CONFIG_SPEC;
import static bluedazzled.lucy_atmos.init.Entities.TURF_TILE;


/*
    fuck linda bro i want this port to be done with so i can get to the good shit
    grammar for comments vary depending on my mood and i will not be fixing them unless i feel like it
*/
@Mod(lucy_atmos.MODID)
public class lucy_atmos {
    public static final String MODID = "lucy_atmos";
    private static final Logger LOGGER = LogUtils.getLogger();

    public lucy_atmos(IEventBus bus, ModContainer container) {
        //todo: fucking reorganize this wtf?
        container.registerConfig(ModConfig.Type.CLIENT, CONFIG_SPEC);
        bus.addListener(this::registerConfig);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        Blocks.registerBlocks(bus);
        Menus.registerMenus(bus);
        bus.addListener(Menus::registerScreens);
        Items.registerHandlers(bus);
        Entities.registerEntities(bus);
        bus.addListener(Payloads::registerPayloads);
        bus.addListener(Atlasses::registerAtlasses);
        bus.addListener(this::commonSetup);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        EntityRenderers.register(TURF_TILE.get(), TileRenderer::new);
    }
    private void registerConfig(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == CONFIG_SPEC) {
            CONFIG.updateCache();
        }
    }
}
