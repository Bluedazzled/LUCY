package bluedazzled.lucy_atmos.networking;

import bluedazzled.lucy_atmos.client.gui.GasAnaMenu;
import bluedazzled.lucy_atmos.items.GasAnalyzer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public record UseInhandPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GasAnaPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "gasanadata"));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(IPayloadContext context) {
        Player player = context.player();
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GasAnalyzer) {
            player.openMenu(new SimpleMenuProvider((id, inv, pl) -> new GasAnaMenu(id, inv), Component.literal("")));
        }
    }
}
