package bluedazzled.lucy_atmos.networking;

import bluedazzled.lucy_atmos.client.gui.GasAnaMenu;
import bluedazzled.lucy_atmos.items.GasAnalyzer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public record UseInhandPayload(int fuckoff) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UseInhandPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "useinhand"));

    public static final StreamCodec<ByteBuf, UseInhandPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, //no this GENUINELY only exists for now so it'll let me actually fucking register it
            UseInhandPayload::fuckoff,
            UseInhandPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handlePayload(IPayloadContext context) {
        Player player = context.player();
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GasAnalyzer) {
            player.openMenu(new SimpleMenuProvider((id, inv, pl) -> new GasAnaMenu(id, inv), Component.literal("")));
        }
    }
}
