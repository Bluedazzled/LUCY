package bluedazzled.lucy_atmos.networking;

import bluedazzled.lucy_atmos.items.GasAnalyzer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public record GasAnaPacket(double temperature) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GasAnaPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "gasanadata"));

    public static final StreamCodec<ByteBuf, GasAnaPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            GasAnaPacket::temperature,
            GasAnaPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(final GasAnaPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof GasAnalyzer gasAnalyzer) {
                gasAnalyzer.setTemperature(data.temperature);
            }
        });
    }
}