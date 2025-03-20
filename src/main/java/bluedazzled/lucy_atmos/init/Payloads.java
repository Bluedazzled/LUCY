package bluedazzled.lucy_atmos.init;

import bluedazzled.lucy_atmos.lucy_atmos;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Payloads {
    private Payloads() {}
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(lucy_atmos.MODID);
    }
}
