package me.tepeshini.easyroads;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import me.tepeshini.easyroads.commands.EasyRoadsCommand;
import me.tepeshini.easyroads.models.Road;
import me.tepeshini.easyroads.tasks.EasyRoadsTask;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;


public final class EasyRoads extends JavaPlugin {

    private Set<Road> roads;
    private double speedIncreaseRate = 0.01D;
    private double speedDecayRate = 1D;
    private String onRoadMessage;
    private String noPermissionMessage;
    private String reloadSuccessMessage;
    private String listHeaderMessage;
    private String helpHeaderMessage;
    private String helpReloadMessage;
    private String helpListMessage;
    private String helpHelpMessage;
    private String invalidCommandMessage;
    private final Set<Class<? extends LivingEntity>> affectedEntities = new HashSet<>();

    @Override
    public void onEnable() {
        //create and load config
        if (!getDataFolder().exists()) {
            saveDefaultConfig();
        }
        loadConfig();

        //register commands
        getCommand("easyroads").setExecutor(new EasyRoadsCommand(this));

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
        speedDecayRate = getConfig().getDouble("speedDecayRate", 1D);
        onRoadMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.onRoad", "&cYou are on a road!"));
        noPermissionMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.noPermission", "&4You do not have permission to use this command."));
        reloadSuccessMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.reloadSuccess", "&aConfiguration reloaded successfully."));
        listHeaderMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.listHeader", "&6Roads:"));
        helpHeaderMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.help.header", "&bEasyRoads commands:"));
        helpReloadMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.help.reload", "&7/easyroads reload - Reload the EasyRoads configuration."));
        helpListMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.help.list", "&7/easyroads list - List all roads."));
        helpHelpMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.help.help", "&7/easyroads help - Display this help message."));
        invalidCommandMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.invalidCommand", "&cInvalid subcommand. Use /easyroads help for available commands."));


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
        roads = roadSection.getKeys(false).stream().map(key -> new Road(
                roadSection.getConfigurationSection(key), getLogger())).collect(Collectors.toSet());


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

    // Getters for messages
    public String getOnRoadMessage() {
        return onRoadMessage;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public String getReloadSuccessMessage() {
        return reloadSuccessMessage;
    }

    public String getListHeaderMessage() {
        return listHeaderMessage;
    }

    public String getHelpHeaderMessage() {
        return helpHeaderMessage;
    }

    public String getHelpReloadMessage() {
        return helpReloadMessage;
    }

    public String getHelpListMessage() {
        return helpListMessage;
    }

    public String getHelpHelpMessage() {
        return helpHelpMessage;
    }

    public String getInvalidCommandMessage() {
        return invalidCommandMessage;
    }

    public Set<Class<? extends LivingEntity>> getAffectedEntities() {
        return affectedEntities;
    }


}
