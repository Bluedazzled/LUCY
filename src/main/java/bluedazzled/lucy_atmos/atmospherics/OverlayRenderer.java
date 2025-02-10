package bluedazzled.lucy_atmos.atmospherics;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;


public class OverlayRenderer implements BlockEntityRenderer<AtmosTileEntity> { //Blockstates are a work of art. This. This is bullshit.
    public OverlayRenderer(BlockEntityRendererProvider.Context context) {}
    private static final ResourceLocation PLASMA_OVERLAY = ResourceLocation.fromNamespaceAndPath("lucy_atmos", "gasoverlay/plasma");


    @Override
    public void render(AtmosTileEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.text(ResourceLocation.fromNamespaceAndPath("lucy_atmos", "textures/atlas/gasoverlays.png")));
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        float scale = 0.5f;
        int opacity = 128;

        //I just want to preface this by saying this shit is jank and I'm sorry but I can't think of another way of doing this
        renderQuad(poseStack, buffer, scale, PLASMA_OVERLAY, Direction.UP,opacity);
        renderQuad(poseStack, buffer, scale, PLASMA_OVERLAY, Direction.DOWN, opacity);
        renderQuad(poseStack, buffer, scale, PLASMA_OVERLAY, Direction.NORTH,opacity);
        renderQuad(poseStack, buffer, scale, PLASMA_OVERLAY, Direction.SOUTH,opacity);
        renderQuad(poseStack, buffer, scale, PLASMA_OVERLAY, Direction.EAST,opacity);
        renderQuad(poseStack, buffer, scale, PLASMA_OVERLAY, Direction.WEST,opacity);
        poseStack.popPose();
    }

    private void renderQuad(PoseStack poseStack, VertexConsumer buffer, float scale, ResourceLocation texture, Direction direction, int opacity) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(ResourceLocation.fromNamespaceAndPath("lucy_atmos", "textures/atlas/gasoverlays.png")).getSprite(texture);

        int b1 = LightTexture.FULL_BRIGHT >> 16 & 65535;
        int b2 = LightTexture.FULL_BRIGHT & 65535;
        poseStack.pushPose();
        switch (direction) { //Please see line 44 TODO: Force myself to constantly update this line in order to make sure I actually fix this jank
            case UP -> {
                poseStack.translate(0, scale, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
            }
            case DOWN -> {
                poseStack.translate(0, -scale, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            }
            case NORTH -> {
                poseStack.translate(0, 0, scale);
                poseStack.mulPose(Axis.YP.rotationDegrees(-180));
            }
            case SOUTH -> {
                poseStack.translate(0, 0, -scale);
            }
            case EAST -> {
                poseStack.translate(scale, 0, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(-90));
            }
            case WEST -> {
                poseStack.translate(-scale, 0, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
            }
        }
        Matrix4f matrix = poseStack.last().pose();
        buffer.addVertex(matrix, -scale, -scale, 0.0f).setColor(255, 255, 255, opacity).setUv(sprite.getU0(), sprite.getV0()).setUv2(b1, b2).setNormal(1, 0, 0);
        buffer.addVertex(matrix, -scale, scale, 0.0f).setColor(255, 255, 255, opacity).setUv(sprite.getU0(), sprite.getV1()).setUv2(b1, b2).setNormal(1, 0, 0);
        buffer.addVertex(matrix, scale, scale, 0.0f).setColor(255, 255, 255, opacity).setUv(sprite.getU1(), sprite.getV1()).setUv2(b1, b2).setNormal(1, 0, 0);
        buffer.addVertex(matrix, scale, -scale, 0.0f).setColor(255, 255, 255, opacity).setUv(sprite.getU1(), sprite.getV0()).setUv2(b1, b2).setNormal(1, 0, 0);
        poseStack.popPose();
    }
}