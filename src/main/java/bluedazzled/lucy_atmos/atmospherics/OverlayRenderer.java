package bluedazzled.lucy_atmos.atmospherics;

import bluedazzled.lucy_atmos.atmospherics.sim.turf_tile;
import bluedazzled.lucy_atmos.LucyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;


public class OverlayRenderer extends EntityRenderer<turf_tile, EntityRenderState> {
    private static final Logger log = LoggerFactory.getLogger(OverlayRenderer.class);

    private static final ResourceLocation PLASMA_OVERLAY = ResourceLocation.fromNamespaceAndPath(MODID, "gasoverlay/plasma");
    private static final ResourceLocation EXCITED = ResourceLocation.fromNamespaceAndPath(MODID, "gasoverlay/excited");
    private static final ResourceLocation UNEXCITED = ResourceLocation.fromNamespaceAndPath(MODID, "gasoverlay/unexcited");

    public OverlayRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() { //Holy shit, this is a nothing burger. Where the fuck is all my data?
        return null;
    }
//    // This method is called every frame in order to render the block entity. Parameters are:
//    // - blockEntity:   The block entity instance being rendered. Uses the generic type passed to the super interface
//    // - partialTick:   The amount of time, in fractions of a tick (0.0 to 1.0), that has passed since the last tick.
//    // - poseStack:     The pose stack to render to.
//    // - bufferSource:  The buffer source to get vertex buffers from.
//    // - packedLight:   The light value of the block entity.
//    // - packedOverlay: The current overlay value of the block entity, usually OverlayTexture.NO_OVERLAY.
//    @Override
//    public void render(turf_tile tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
//        VertexConsumer buffer = bufferSource.getBuffer(RenderType.text(ResourceLocation.fromNamespaceAndPath(MODID, "textures/atlas/gasoverlays.png")));
//        poseStack.pushPose();
//        poseStack.translate(0.5, 0.5, 0.5);
//        float scale = 0.5f;
//        int opacity = 128;
//        if (LucyConfig.getDebugRenderer()) {
//            if (tile.excited) {
//                for (Direction direction : Direction.values()) {
//                    renderQuad(poseStack, buffer, scale, EXCITED, direction, opacity);
//                }
//            } else {
//                for (Direction direction : Direction.values()) {
//                    renderQuad(poseStack, buffer, scale, UNEXCITED, direction, opacity);
//                }
//            }
//        } else {
//            for (Direction direction : Direction.values()) { //TODO: implement getValidAdjTile() once the first TODO is done
//                renderQuad(poseStack, buffer, scale, PLASMA_OVERLAY, direction, opacity);
//            }
//        }
//        poseStack.popPose();
//    }

    private void renderQuad(PoseStack poseStack, VertexConsumer buffer, float scale, ResourceLocation texture, Direction direction, int opacity) {
        //i really need to start using variables, but what's the point in that if it's only going to be called once? like ok...
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(ResourceLocation.fromNamespaceAndPath(MODID, "textures/atlas/gasoverlays.png")).getSprite(texture);

        int b1 = LightTexture.FULL_BRIGHT >> 16 & 65535;
        int b2 = LightTexture.FULL_BRIGHT & 65535;
        poseStack.pushPose();
        switch (direction) { //TODO: fix this shit, too janky
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
        //fuck ts
        Matrix4f matrix = poseStack.last().pose();
        buffer.addVertex(matrix, -scale, -scale, 0.0f).setColor(255, 255, 255, opacity).setUv(sprite.getU0(), sprite.getV0()).setUv2(b1, b2).setNormal(1, 0, 0);
        buffer.addVertex(matrix, -scale, scale, 0.0f).setColor(255, 255, 255, opacity).setUv(sprite.getU0(), sprite.getV1()).setUv2(b1, b2).setNormal(1, 0, 0);
        buffer.addVertex(matrix, scale, scale, 0.0f).setColor(255, 255, 255, opacity).setUv(sprite.getU1(), sprite.getV1()).setUv2(b1, b2).setNormal(1, 0, 0);
        buffer.addVertex(matrix, scale, -scale, 0.0f).setColor(255, 255, 255, opacity).setUv(sprite.getU1(), sprite.getV0()).setUv2(b1, b2).setNormal(1, 0, 0);
        poseStack.popPose();
    }
}