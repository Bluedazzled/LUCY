package bluedazzled.lucy_atmos;

import bluedazzled.lucy_atmos.atmospherics.OverlayRenderer;
import bluedazzled.lucy_atmos.menus.GasAnaScreen;
import bluedazzled.lucy_atmos.networking.GasAnaPacket;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMaterialAtlasesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static bluedazzled.lucy_atmos.LucyConfig.CONFIG;
import static bluedazzled.lucy_atmos.LucyConfig.CONFIG_SPEC;
import static bluedazzled.lucy_atmos.Registration.GASANA_MENU;


/*
    EVERYTHING IN THE ATMOSPHERICS FOLDER WILL NOT BE FOLLOWING NORMAL NAMING CONVENTIONS!
    THIS IS SO IT'S A MORE OR LESS 1:1 PORT SO IT MAKES DEBUGGING A *LITTLE* EASIER. NOT BY MUCH, JUST A LITTLE.

    All ye who enter here, a few notes I should state, and this WILL be updated eventually
    A comment with /// is copied (or based upon) a comment contained within LINDA's code (y'know, our base?)
    There *may* be a **few** comments that vent, sorry! It is what it is.
    Oh, and priorities for to dos are ambiguous.
    grammar for comments vary depending on my mood and i will not be fixing them unless i feel like it
*/
@Mod(lucy_atmos.MODID)
public class lucy_atmos {
    public static final String MODID = "lucy_atmos";
//    private static final Logger LOGGER = LogUtils.getLogger();

    public lucy_atmos(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, CONFIG_SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        Registration.init(modBus);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::registerAtlas);
        modBus.addListener(this::registerScreens);
        modBus.addListener(this::registerPackets);
        modBus.addListener(this::registerConfig);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        BlockEntityRenderers.register(Registration.ATMOS_TILE_ENTITY.get(), OverlayRenderer::new);
    }
    private void registerAtlas(final RegisterMaterialAtlasesEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(MODID, "textures/atlas/gasoverlays.png"), ResourceLocation.fromNamespaceAndPath(MODID, "gasoverlays"));
        event.register(ResourceLocation.fromNamespaceAndPath(MODID, "textures/atlas/tgui.png"), ResourceLocation.fromNamespaceAndPath(MODID, "tgui"));
    }
    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GASANA_MENU.get(), GasAnaScreen::new);
    }
    //dude i have no fucking idea how this works todo: figure ts out priorirty: 3
    private void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.NETWORK);
        registrar.playBidirectional(
                GasAnaPacket.TYPE,
                GasAnaPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                    GasAnaPacket.ClientPayloadHandler::handleDataOnNetwork,
                    GasAnaPacket.ServerPayloadHandler::handleDataOnNetwork
                )
        );
    }
    private void registerConfig(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == CONFIG_SPEC) {
            CONFIG.updateCache();
        }
    }
}
