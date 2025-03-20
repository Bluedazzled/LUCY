package bluedazzled.lucy_atmos.init;

import bluedazzled.lucy_atmos.client.gui.GasAnaMenu;
import bluedazzled.lucy_atmos.client.gui.GasAnaScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class Menus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);

    public static final Supplier<MenuType<GasAnaMenu>> GASANA_MENU = MENUS.register("gasana_menu", () -> new MenuType<>(GasAnaMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GASANA_MENU.get(), GasAnaScreen::new);
    }
    public static void registerMenus(IEventBus bus) {
        MENUS.register(bus);
    }
}
