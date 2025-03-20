package bluedazzled.lucy_atmos.items;

import bluedazzled.lucy_atmos.atmospherics.defines.gas_types;
import bluedazzled.lucy_atmos.atmospherics.sim.gas_mixture;
import bluedazzled.lucy_atmos.atmospherics.sim.turf_tile;
import com.mojang.logging.LogUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static bluedazzled.lucy_atmos.atmospherics.defines.LilMaths.*;
import static bluedazzled.lucy_atmos.atmospherics.defines.atmos_core.*;
import static bluedazzled.lucy_atmos.lucy_atmos.MODID;

@MethodsReturnNonnullByDefault
public class GasAnalyzer extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    private double temperature;
    public GasAnalyzer() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "gas_analyzer")))
        );
    }
    public void setTemperature(double temperature) {this.temperature = temperature;}
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) return InteractionResult.PASS;
        Vec3 start = player.getEyePosition(1f);
        Vec3 end = start.add(player.getLookAngle().scale(1f));
        AABB rayAABB = new AABB(start, end).inflate(.1);
        List<Entity> entities = level.getEntities(player, rayAABB, entity -> entity instanceof turf_tile);
        EntityHitResult closestHit = null;
        double closestDistance = Double.MAX_VALUE;
        for (Entity entity : entities) {
            AABB entityAABB = entity.getBoundingBox();
            Optional<Vec3> intersection = entityAABB.clip(start, end); // Check if the ray hits the entity

            if (intersection.isPresent()) {
                double distance = start.distanceTo(intersection.get());

                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestHit = new EntityHitResult(entity, intersection.get());
                }
            }
        }
        if (closestHit != null) {
            Entity hitEntity = closestHit.getEntity();

            if (hitEntity instanceof turf_tile tile) {
                //All the code for interacting with the tile itself. Everything above is just prerequisite to fetch the tile we're targetting
                scanTile(tile, (ServerPlayer) player);
            }
        }
        return InteractionResult.PASS;
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