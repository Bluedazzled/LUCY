package bluedazzled.lucy_atmos.client.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import static bluedazzled.lucy_atmos.init.Menus.GASANA_MENU;

//i hate how barebones this is but it's unfortunately necessary
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