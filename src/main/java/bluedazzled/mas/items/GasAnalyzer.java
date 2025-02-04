package bluedazzled.mas.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class GasAnalyzer extends Item {
    public GasAnalyzer() {
        super(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("mas", "gas_analyzer")))
        );
    }
}
