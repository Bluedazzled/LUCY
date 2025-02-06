package bluedazzled.mas.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import static bluedazzled.mas.Registration.*;

public class GasAnalyzer extends Item {
    public GasAnalyzer() {
        super(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("mas", "gas_analyzer")))
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        if (!level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if(!level.getBlockState(blockpos).is(ATMOS_TILE_BLOCK)) {
            return super.useOn(context);
        } else {
            player.displayClientMessage(Component.literal("FUCK"), false);
            return InteractionResult.SUCCESS;
        }
    }
}
