package bluedazzled.lucy_atmos.init;

import bluedazzled.lucy_atmos.blocks.markiplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class Blocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<markiplier> MARKIPLIER = BLOCKS.register("markiplier", markiplier::new);

    public static void registerBlocks(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
