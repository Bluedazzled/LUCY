package bluedazzled.lucy_atmos.items;

import bluedazzled.lucy_atmos.atmospherics.defines.gas_types;
import bluedazzled.lucy_atmos.atmospherics.sim.gas_mixture;
import bluedazzled.lucy_atmos.atmospherics.sim.turf_tile;
import bluedazzled.lucy_atmos.atmospherics.defines.LilMaths;
import bluedazzled.lucy_atmos.menus.GasAnaMenu;
import com.mojang.logging.LogUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Map;

import static bluedazzled.lucy_atmos.Registration.ATMOS_TILE_BLOCK;
import static bluedazzled.lucy_atmos.atmospherics.defines.LilMaths.*;
import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.*;
import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

@MethodsReturnNonnullByDefault
public class GasAnalyzer extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    public GasAnalyzer() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "gas_analyzer")))
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        Player player = context.getPlayer();
        BlockPos blockpos = context.getClickedPos();
        BlockState state = level.getBlockState(blockpos);
        if(!state.is(ATMOS_TILE_BLOCK)) {
            return super.useOn(context);
        }
        BlockEntity blockent = level.getBlockEntity(blockpos);
        if (!(blockent instanceof turf_tile atmosTile)) { //we aren't a tile, fuck off!
            return InteractionResult.PASS;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            scanTile(atmosTile, serverPlayer);
        }
        return InteractionResult.SUCCESS;
    }

    void scanTile(turf_tile tile, ServerPlayer player) {
        gas_mixture mixture = tile.return_air();
        double totalMoles = mixture.total_moles();
        double pressure = mixture.getPressure();
        double volume = mixture.getVolume();
        double temperature = mixture.getTemperature();
        double heatCapacity = mixture.heat_capacity();
        double thermal_energy = mixture.thermal_energy();
        //705 scanners.dm
        ArrayList<String> message = new ArrayList<>();
        message.add("--------RESULTS-------");
        if (totalMoles > 0) {
            message.add(String.format("Moles: %smol", doubleToString(Math.round(totalMoles*1000d)/1000d)));
            message.add(String.format("Volume: %sL", doubleToString(volume)));
            message.add(String.format("Pressure: %skPa", doubleToString(Math.round(pressure*1000d)/1000d)));
            message.add(String.format("Heat Capacity: %s", displayJoules(heatCapacity)));
            message.add(String.format("Thermal Energy: %s", displayJoules(thermal_energy)));
            for (Map.Entry<String, double[]> entry : mixture.getGases().entrySet()){
                double concentration = entry.getValue()[MOLES]/totalMoles;
                message.add(String.format("%s: %s%% %s mol",
                        gas_types.getGas(entry.getKey()).getName(),
                        doubleToString(Math.round(concentration*100d)*100d/100d),
                        doubleToString(Math.round(totalMoles*1000d)/1000d)
                        ));
            }
            message.add(String.format("Temperature: %s°C (%sK)",
                    doubleToString(Math.round((temperature - T0C)*1000d)/1000d),
                    doubleToString(Math.round(temperature*1000d)/1000d)));
        } else message.add("VACUUM. SORRY!");
        message.add("----------END---------");
        for (String text : message) {
            player.sendSystemMessage(Component.literal(text));
        }
    }
    String doubleToString(double number) {
        return Double.toString(number).replaceAll("\\.?0+$", "");
    }
}