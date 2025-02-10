package bluedazzled.lucy_atmos.atmospherics;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import org.joml.Matrix4f;

import static net.minecraft.core.Direction.*;


public class OverlayRenderer implements BlockEntityRenderer<AtmosTileEntity> { //Blockstates are a work of art. This. This is bullshit.
    public OverlayRenderer(BlockEntityRendererProvider.Context context) {}
    private static final ResourceLocation PLASMA_OVERLAY = ResourceLocation.fromNamespaceAndPath("lucy_atmos", "textures/gasoverlay/plasma.png");


    // This method is called every frame in order to render the block entity. Parameters are:
    // - blockEntity:   The block entity instance being rendered. Uses the generic type passed to the super interface.
    // - partialTick:   The amount of time, in fractions of a tick (0.0 to 1.0), that has passed since the last tick.
    // - poseStack:     The pose stack to render to.
    // - bufferSource:  The buffer source to get vertex buffers from.
    // - packedLight:   The light value of the block entity.
    // - packedOverlay: The current overlay value of the block entity, usually OverlayTexture.NO_OVERLAY.
    @Override
    public void render(AtmosTileEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(PLASMA_OVERLAY));
        int skyLight = blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos());

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.501);

        renderQuad(poseStack, buffer, packedLight, skyLight, packedOverlay, Direction.UP);
        renderQuad(poseStack, buffer, packedLight, skyLight, packedOverlay, Direction.DOWN);
        renderQuad(poseStack, buffer, packedLight, skyLight, packedOverlay, Direction.NORTH);
        renderQuad(poseStack, buffer, packedLight, skyLight, packedOverlay, Direction.SOUTH);
        renderQuad(poseStack, buffer, packedLight, skyLight, packedOverlay, Direction.EAST);
        renderQuad(poseStack, buffer, packedLight, skyLight, packedOverlay, Direction.WEST);

        poseStack.popPose();

    }

    private void renderQuad(PoseStack poseStack, VertexConsumer buffer, int packedLight, int skyLight, int packedOverlay, Direction direction) {
        poseStack.pushPose();

        switch (direction) { //I just want to preface this by saying do *not* ask me how long it took me to fucking get these numbers.
            case UP -> {
                poseStack.translate(0, 0.5, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            }
            case DOWN -> {
                poseStack.translate(0, -0.5, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
            }
            case NORTH -> {
                poseStack.translate(0, 0, 0.5);
            }
            case SOUTH -> {
                poseStack.translate(0, 0, -0.5); //?
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
            }
            case EAST -> {
                poseStack.translate(0.5, 0, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
            }
            case WEST -> {
                poseStack.translate(-0.5, 0, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(-90));
            }
        }

        Matrix4f matrix = poseStack.last().pose();

        buffer.addVertex(matrix, -.5f, -.5f, 0f)
                .setColor(255, 255, 255, 255)
                .setUv(0, 1)
                .setOverlay(packedOverlay)
                .setUv2(packedLight, skyLight)
                .setNormal(0, 0, 1);

        buffer.addVertex(matrix,  .5f, -.5f, 0f)
                .setColor(255, 255, 255, 255)
                .setUv(1, 1)
                .setOverlay(packedOverlay)
                .setUv2(packedLight, skyLight)
                .setNormal(0, 0, 1);

        buffer.addVertex(matrix,  .5f,  .5f, 0f)
                .setColor(255, 255, 255, 255)
                .setUv(1, 0)
                .setOverlay(packedOverlay)
                .setUv2(packedLight, skyLight)
                .setNormal(0, 0, 1);

        buffer.addVertex(matrix, -.5f,  .5f, 0f)
                .setColor(255, 255, 255, 255)
                .setUv(0, 0)
                .setOverlay(packedOverlay)
                .setUv2(packedLight, skyLight)
                .setNormal(0, 0, 1);
        poseStack.popPose();
    }
}