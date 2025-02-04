package bluedazzled.mas.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class markiplier extends Block {
    public markiplier() {
        super(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("mas", "markiplier")))
                .strength(3.5F)
                .sound(SoundType.METAL));

    }
}
