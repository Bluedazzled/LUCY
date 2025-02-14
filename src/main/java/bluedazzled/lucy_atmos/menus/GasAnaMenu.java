package bluedazzled.lucy_atmos.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import static bluedazzled.lucy_atmos.Registration.GASANA_MENU;

public class GasAnaMenu extends AbstractContainerMenu {
    public GasAnaMenu(int containerId, Inventory inv) {
        super(GASANA_MENU.get(), containerId);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
        return ItemStack.EMPTY;
    }
}