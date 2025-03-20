package bluedazzled.lucy_atmos.init;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterMaterialAtlasesEvent;

import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

public class Atlasses {
    private Atlasses(){}
    public static void registerAtlasses(final RegisterMaterialAtlasesEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(MODID, "textures/atlas/gasoverlays.png"), ResourceLocation.fromNamespaceAndPath(MODID, "gasoverlays"));
        event.register(ResourceLocation.fromNamespaceAndPath(MODID, "textures/atlas/tgui.png"), ResourceLocation.fromNamespaceAndPath(MODID, "tgui"));
    }
}
