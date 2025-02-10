package bluedazzled.lucy_atmos;

import bluedazzled.lucy_atmos.atmospherics.OverlayRenderer;
import bluedazzled.lucy_atmos.Registration;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMaterialAtlasesEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(lucy_atmos.MODID)
public class lucy_atmos {
    public static final String MODID = "lucy_atmos";
    private static final Logger LOGGER = LogUtils.getLogger();

    public lucy_atmos(IEventBus modBus) {
        Registration.init(modBus);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::registerAtlas);

    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        BlockEntityRenderers.register(Registration.ATMOS_TILE_ENTITY.get(), OverlayRenderer::new);

    }
    private void registerAtlas(final RegisterMaterialAtlasesEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath("lucy_atmos", "textures/atlas/gasoverlays"), ResourceLocation.fromNamespaceAndPath("lucy_atmos", "gasoverlays"));
    }
}
