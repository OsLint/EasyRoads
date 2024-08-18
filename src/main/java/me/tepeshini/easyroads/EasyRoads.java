package me.tepeshini.easyroads;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import me.tepeshini.easyroads.models.Road;
import me.tepeshini.easyroads.tasks.EasyRoadsTask;
import me.tepeshini.easyroads.utils.DebugLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import static me.tepeshini.easyroads.utils.DebugLogger.debugLog;

public final class EasyRoads extends JavaPlugin {

    private Set<Road> roads;
    private double speedIncreaseRate = 0.01D;
    private double speedDecayRate = 1D;
    private final Set<Class<? extends LivingEntity>> affectedEntities = new HashSet<>();

    @Override
    public void onEnable() {
        //create and load config
        if (!getDataFolder().exists()) {
            saveDefaultConfig();
        }
        loadConfig();

        //register commands
        // Objects.requireNonNull(getCommand("roads")).setExecutor(new EasyRoadsCommand(this));

        //init logger
        Boolean debug = getConfig().getBoolean("debug", false);
        DebugLogger.init(this, debug);
        debugLog().warning("Debug mode enabled");

        //start task
        new EasyRoadsTask(this).runTaskTimer(this, 1L, 1L);


        getLogger().info("EasyRoads enabled");
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getLogger().warning("EasyRoads disabled");
    }


    public void loadConfig() {
        affectedEntities.clear();
        speedIncreaseRate = getConfig().getDouble("speedIncreaseRate", 0.01D);

        //load affected entities
        for (String s : getConfig().getStringList("affectedEntities")) {
            EntityType type = EntityType.valueOf(s);

            if (type == EntityType.PLAYER)
                continue;

            Class<? extends Entity> clazz = type.getEntityClass();

            assert clazz != null;
            if (LivingEntity.class.isAssignableFrom(clazz))
                affectedEntities.add(clazz.asSubclass(LivingEntity.class));
        }

        //load roads
        ConfigurationSection roadSection = getConfig().getConfigurationSection("roads");
        assert roadSection != null;
        roads = roadSection.getKeys(false).stream().map(key -> new Road(Objects.requireNonNull(
                roadSection.getConfigurationSection(key)))).collect(Collectors.toSet());


        getLogger().info("--------------------");
        getLogger().info("Loaded roads:");
        getLogger().info("--------------------");
        int i = 1;
        for (Road r : roads) {
            getLogger().info(i + ") " + r);
            i++;
        }
        getLogger().info("--------------------");

    }

    public Set<Road> getRoads() {
        return roads;
    }

    public double getSpeedIncreaseRate() {
        return speedIncreaseRate;
    }

    public double getSpeedDecayRate() {
        return speedDecayRate;
    }

    public Set<Class<? extends LivingEntity>> getAffectedEntities() {
        return affectedEntities;
    }


}
