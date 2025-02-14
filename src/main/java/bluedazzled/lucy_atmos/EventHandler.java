package bluedazzled.lucy_atmos;

import bluedazzled.lucy_atmos.items.GasAnalyzer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        ItemStack heldItem = player.getMainHandItem();

        if (heldItem.getItem() instanceof GasAnalyzer) {
            ((GasAnalyzer) heldItem.getItem()).leftClickedBlock(player, event.getLevel(), event.getPos());
        }
    }
}
