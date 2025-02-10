package bluedazzled.lucy_atmos;

import bluedazzled.lucy_atmos.atmospherics.OverlayRenderer;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(lucy_atmos.MODID)
public class lucy_atmos {
    public static final String MODID = "lucy_atmos";
    private static final Logger LOGGER = LogUtils.getLogger();

    public lucy_atmos(IEventBus modBus) {
        Registration.init(modBus);
        modBus.addListener(this::commonSetup);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        BlockEntityRenderers.register(Registration.ATMOS_TILE_ENTITY.get(), OverlayRenderer::new);
    }
}
