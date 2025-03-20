package bluedazzled.lucy_atmos.init;

import net.neoforged.bus.api.IEventBus;

public class RegisterHandlers {
    public static void registerHandlers(IEventBus bus) {
        Blocks.registerBlocks(bus);
        Menus.registerMenus(bus);
        bus.addListener(Menus::registerScreens);
        Items.registerHandlers(bus);
        Entities.registerEntities(bus);
    }
}
