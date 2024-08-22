package me.tepeshini.easyroads.commands;

import me.tepeshini.easyroads.EasyRoads;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the commands for the EasyRoads plugin.
 * This class implements {@link CommandExecutor} and processes player commands related to EasyRoads.
 */
public class EasyRoadsCommand implements CommandExecutor {

    private final EasyRoads plugin;

    /**
     * Constructs an instance of EasyRoadsCommand with the given plugin.
     *
     * @param plugin the EasyRoads plugin instance
     */
    public EasyRoadsCommand(EasyRoads plugin) {
        this.plugin = plugin;
    }
    /**
     * Executes the command sent by the player.
     *
     * @param sender      the sender of the command
     * @param cmd         the command that was executed
     * @param commandLabel the alias used to execute the command
     * @param args        the arguments passed to the command
     * @return true if the command was handled successfully, false otherwise
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        // Check if no arguments or 'help' is requested
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            player.sendMessage(plugin.getHelpHeaderMessage());
            //check for permissions
            if (player.hasPermission("easyroads.reload")) {
                player.sendMessage(plugin.getHelpReloadMessage());
            }

            if (player.hasPermission("easyroads.list")) {
                player.sendMessage(plugin.getHelpListMessage());
            }

            player.sendMessage(plugin.getHelpHelpMessage());

            return true;
        }

        // Handle the 'reload' command
        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("easyroads.reload")) {
                player.sendMessage(plugin.getNoPermissionMessage());
                return true;
            }
            plugin.reloadConfig();
            plugin.loadConfig();
            player.sendMessage(plugin.getReloadSuccessMessage());
            return true;
        }


        // Handle the 'list' command
        if (args[0].equalsIgnoreCase("list")) {
            if (!player.hasPermission("easyroads.list")) {
                player.sendMessage(plugin.getNoPermissionMessage());
                return true;
            }
            player.sendMessage(plugin.getListHeaderMessage());
            plugin.getRoads().forEach(road -> player.sendMessage(road.toString()));
            return true;
        }

        // If command not recognized, show invalid command message
        player.sendMessage(plugin.getInvalidCommandMessage());
        return true;
    }
}
