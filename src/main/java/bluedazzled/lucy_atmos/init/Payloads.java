package bluedazzled.lucy_atmos.init;

import bluedazzled.lucy_atmos.lucy_atmos;
import bluedazzled.lucy_atmos.networking.GasAnaPacket;
import bluedazzled.lucy_atmos.networking.UseInhandPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Payloads {
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(lucy_atmos.MODID);
        registrar.playToServer(UseInhandPayload.TYPE, UseInhandPayload.STREAM_CODEC, UseInhandPayload::handlePayload);
        registrar.playToServer(GasAnaPacket.TYPE, GasAnaPacket.STREAM_CODEC, GasAnaPacket::handlePayload);
    }
}
