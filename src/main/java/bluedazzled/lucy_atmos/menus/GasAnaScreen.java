package bluedazzled.lucy_atmos.menus;

import bluedazzled.lucy_atmos.networking.GasAnaPacket;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class GasAnaScreen extends AbstractContainerScreen<GasAnaMenu> {

    private Player player;
    private BlockEntity blockent;

    public GasAnaScreen(GasAnaMenu menu, Inventory inv, Component title) {
        super(menu, inv, Component.literal("gasana_screen"));

    }
    EditBox box;
    @Override
    public void init(){
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.box = new EditBox(Minecraft.getInstance().font.self(),
                this.leftPos, this.topPos,
                64, 16,
                Component.literal(""));

        this.addRenderableWidget(Button.builder(Component.literal("setTemp"), button -> {onButtonPressed();})
                .bounds(this.leftPos + 64, this.topPos, 64, 16)
                .build());

        this.addRenderableWidget(box);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick){
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBg(GuiGraphics graphics, float p1, int p2, int p3) {
        graphics.blit(
                RenderType::guiTextured,
                MENU_BACKGROUND,
                this.leftPos, this.topPos,
                0, 0,
                this.imageWidth, this.imageHeight,
                256, 256
        );
    }

    private void onButtonPressed(){
        this.minecraft.player.displayClientMessage(Component.literal(this.box.getValue()), false);
        PacketDistributor.sendToServer(new GasAnaPacket(Double.parseDouble(this.box.getValue())));
        System.out.println("Sent data to the server");
    }

    @Override
    public void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        //Draw the string once I get the gui base done
//        graphics.drawString(this.getFont(), "Gas Analyzer")
    }
}
