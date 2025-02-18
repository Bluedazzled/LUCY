package bluedazzled.lucy_atmos.menus;

import bluedazzled.lucy_atmos.networking.GasAnaPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

import static bluedazzled.lucy_atmos.atmospherics.Definitions.T20C;
import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class GasAnaScreen extends AbstractContainerScreen<GasAnaMenu> {
    private static final ResourceLocation EYE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/tgui/eye.png");

    public GasAnaScreen(GasAnaMenu menu, Inventory inv, Component title) {
        super(menu, inv, Component.literal("gasana_screen"));

    }
    EditBox box;
    @Override
    public void init(){
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        //temperature edit box
        this.box = new EditBox(Minecraft.getInstance().font.self(),
                this.leftPos + 32, this.topPos + 96,
                64, 16,
                Component.literal("")); //default to empty
        //temperature button
        this.addRenderableWidget(Button.builder(Component.literal("setTemp"), button -> {onButtonPressed();})
                .bounds(this.leftPos + 32, this.topPos + 112, 64, 16)
                .build());
        //wtf did i add this for again?
        this.addRenderableWidget(box);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick){
        super.render(graphics, mouseX, mouseY, partialTick);
        //eye
        graphics.blit(
                RenderType::guiTextured,
                EYE,
                this.leftPos, this.topPos,
                0, 0,
                8, 8,
                8, 8);
    }

    @Override
    public void renderBg(GuiGraphics graphics, float p1, int p2, int p3) {
        graphics.blit(
                RenderType::guiTextured,
                MENU_BACKGROUND,
                this.leftPos, this.topPos,
                0, 0,
                this.imageWidth, this.imageHeight,
                256, 256);
    }

    private void onButtonPressed(){
        this.minecraft.player.displayClientMessage(Component.literal(this.box.getValue()), false);
        try {
            PacketDistributor.sendToServer(new GasAnaPacket(Double.parseDouble(this.box.getValue())));
        } catch (NumberFormatException e) {//Man, who would've thought we'd crash if we put in a string for a number?
            PacketDistributor.sendToServer(new GasAnaPacket(T20C)); //sets the default to T20C
        }
    }

    @Override
    public void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        //Draw the string once I get the gui base done (sike bitch using placehgolder for now :3)
        graphics.drawCenteredString(this.getFont(), "Gas Analyzer", 85, 0, -1);
    }
}
