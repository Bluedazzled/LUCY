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
        player.sendSystemMessage(Component.literal("--------RESULTS-------"));
        if (totalMoles > 0) {
            player.sendSystemMessage(Component.literal("Moles: %smol".formatted(formatNum(totalMoles))));
            player.sendSystemMessage(Component.literal("Volume: %sL".formatted(formatNum(volume))));
            player.sendSystemMessage(Component.literal("Pressure: %skPa".formatted(formatNum(pressure))));
            player.sendSystemMessage(Component.literal("Heat Capacity: %s".formatted(displayJoules(heatCapacity))));
            player.sendSystemMessage(Component.literal("Thermal Energy: %s".formatted(displayJoules(thermal_energy))));
            for (Map.Entry<String, double[]> entry : mixture.getGases().entrySet()) {
                double concentration = entry.getValue()[MOLES] / totalMoles;
                player.sendSystemMessage(Component.literal("%s: %s%% %s mol".formatted(
                        gas_types.getGas(entry.getKey()).getName(),
                        formatNum(concentration * 100),
                        formatNum(entry.getValue()[MOLES])
                )));
            }
            player.sendSystemMessage(Component.literal("Temperature: %s°C (%sK)".formatted(
                    formatNum(temperature - T0C),
                    formatNum(temperature)
            )));
        } else player.sendSystemMessage(Component.literal("VACUUM. SORRY!"));
        player.sendSystemMessage(Component.literal("----------END---------"));
    }
    private String formatNum(double num) {
        return num % 1 == 0 ? String.format("%.0f", num) : String.format("%.2f", num);
    }
}