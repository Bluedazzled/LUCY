package bluedazzled.lucy_atmos;

import bluedazzled.lucy_atmos.items.GasAnalyzer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = "lucy_atmos")
public class EventHandler {
    @SubscribeEvent
    public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getLevel().isClientSide) {
            return; //server only biznatch
        }
        ServerPlayer player = (ServerPlayer) event.getEntity();
        ItemStack heldItem = player.getMainHandItem();

        if (heldItem.getItem() instanceof GasAnalyzer) { //call to gas analyzer
//            ((GasAnalyzer) heldItem.getItem()).leftClickedBlock(player, event.getLevel(), event.getPos());
        }
    }
}
