package bluedazzled.lucy_atmos.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

@ParametersAreNonnullByDefault
public class markiplier extends Block {
    public markiplier() {
        super(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MODID, "markiplier")))
                .strength(3.5F)
                .sound(SoundType.METAL));

    }
    @NotNull
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (!level.isClientSide) {
            level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 10, true, Level.ExplosionInteraction.TNT);
        }
        return InteractionResult.SUCCESS;
    }
}
