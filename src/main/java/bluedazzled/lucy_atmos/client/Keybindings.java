package bluedazzled.lucy_atmos.client;

import bluedazzled.lucy_atmos.networking.UseInhandPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class Keybindings {
    public static final KeyMapping INHAND = new KeyMapping(
            "key.lucy_atmos.inhand",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "key.category.lucy"
            );

    public static void registerMappings(RegisterKeyMappingsEvent event) {
        event.register(INHAND);
    }

    public static void handleKeys(ClientTickEvent.Post event) {
        if (INHAND.consumeClick()) {
            PacketDistributor.sendToServer(new UseInhandPayload(1));
        }
    }

    public static void register() {
        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(EventPriority.HIGH, Keybindings::handleKeys);
    }
}
