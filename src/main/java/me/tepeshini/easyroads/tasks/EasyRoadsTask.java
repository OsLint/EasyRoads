package me.tepeshini.easyroads.tasks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.tepeshini.easyroads.EasyRoads;
import me.tepeshini.easyroads.models.Road;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import java.util.logging.Logger;
import org.bukkit.scheduler.BukkitRunnable;


/**
 * A task that periodically updates the movement speed attribute of entities
 * on roads defined in the EasyRoads plugin.
 * This task runs periodically using Bukkit's scheduler to adjust the speed
 * of entities based on their location on roads and their current speed
 * modifier.
 */
public class EasyRoadsTask extends BukkitRunnable {
    private static final int UPDATE_ON_ROAD_DIVIDER = 20;
    private static final int UPDATE_ENTITY_CACHE = 100;
    private static final UUID MODIFIER_UUID = UUID.fromString("0d2d4303-c228-4075-9f94-00fa3036f40c");
    private static final String MODIFIER_NAME = "EasyRoads";
    private static final AttributeModifier EMPTY_MODIFIER = new AttributeModifier(
            MODIFIER_UUID, MODIFIER_NAME, 0, Operation.ADD_SCALAR);
    private final EasyRoads plugin;

    private final Map<UUID, Double> currentSpeedMap = new HashMap<>();
    private final Map<UUID, Double> targetSpeedMap = new HashMap<>();

    private final Map<World, Collection<Entity>> affectedEntitiesMap = new HashMap<>();
    private long tickCounter = 0;

    private final Logger log;

    /**
     * Constructs an EasyRoadsTask with the given plugin instance.
     *
     * @param plugin the EasyRoads plugin instance
     */
    public EasyRoadsTask(EasyRoads plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
    }

    /**
     * Executes the task. This method is called periodically by the Bukkit scheduler.
     * It updates the speed attributes for online players and entities affected by the roads,
     * and refreshes the cache of affected entities.
     */
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(this::applyAttributeToLivingEntity);
        affectedEntitiesMap.forEach((w, a) -> a.forEach(this::applyAttributeToEntity));
        if (tickCounter++ % UPDATE_ENTITY_CACHE == 0 && !plugin.getAffectedEntities().isEmpty()) {
            Bukkit.getWorlds().forEach(a -> affectedEntitiesMap.put(a, a.getEntitiesByClasses(
                    plugin.getAffectedEntities().toArray(new Class[0]))));
        }
    }

    /**
     * Applies speed attribute changes to the given entity if it is entity valid living entity.
     *
     * @param entity the entity to which the speed attribute will be applied
     */
    private void applyAttributeToEntity(Entity entity) {
        if (entity.isValid() && entity instanceof LivingEntity) {
            applyAttributeToLivingEntity((LivingEntity) entity);
        }
    }

     /**
     * Applies speed attribute changes to the given living entity.
     * The entity's movement speed is adjusted towards the target speed defined by the roads.
     *
     * @param livingEntity the living entity to which the speed attribute will be applied
     */
    private void applyAttributeToLivingEntity(LivingEntity livingEntity) {
        double currentSpeedMod = currentSpeedMap.getOrDefault(livingEntity.getUniqueId(), 0D);
        double targetSpeedMod = getTargetSpeed(livingEntity);


        // no need to update attribute if we're at the target already
        if (currentSpeedMod == targetSpeedMod)
            return;

        AttributeInstance attrib = livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        attrib.removeModifier(EMPTY_MODIFIER);

        if (targetSpeedMod >= currentSpeedMod) {
            currentSpeedMod = Math.min(currentSpeedMod + plugin.getSpeedIncreaseRate(), targetSpeedMod);
        } else {
            currentSpeedMod = Math.max(currentSpeedMod - plugin.getSpeedDecayRate(), 0);
        }

        attrib.addModifier(new AttributeModifier(MODIFIER_UUID, MODIFIER_NAME, currentSpeedMod, Operation.ADD_SCALAR));

        currentSpeedMap.put(livingEntity.getUniqueId(), currentSpeedMod);
    }

    /**
     * Retrieves the target speed modifier for the given living entity based on its location.
     * The target speed is determined by the roads in the plugin and is updated periodically.
     *
     * @param livingEntity the living entity whose target speed is to be retrieved
     * @return the target speed modifier for the entity
     */
    private double getTargetSpeed(LivingEntity livingEntity) {
        // distribute updated entities roughly evenly across the ticks
        if (tickCounter % UPDATE_ON_ROAD_DIVIDER == livingEntity.getEntityId() % UPDATE_ON_ROAD_DIVIDER) {
            double targetSpeedMod = Double.NEGATIVE_INFINITY;

            for (Road r : plugin.getRoads())
                if (r.getSpeedModifier() > targetSpeedMod && r.isRoadBlock(livingEntity.getLocation().getBlock())) {
                    targetSpeedMod = r.getSpeedModifier();


                    if (livingEntity instanceof Player p) {
                        p.spigot().sendMessage(
                                ChatMessageType.ACTION_BAR, new TextComponent(plugin.getOnRoadMessage()));
                    }
                }

            if (targetSpeedMod == Double.NEGATIVE_INFINITY) {
                targetSpeedMod = 0.0;
            }

            targetSpeedMap.put(livingEntity.getUniqueId(), targetSpeedMod);
        }

        return targetSpeedMap.getOrDefault(livingEntity.getUniqueId(), 0D);
    }
}