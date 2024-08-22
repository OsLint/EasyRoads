package me.tepeshini.easyroads.commands;

import me.tepeshini.easyroads.EasyRoads;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EasyRoadsCommand implements CommandExecutor {

    private final EasyRoads plugin;

    public EasyRoadsCommand(EasyRoads plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        // Check if no arguments or 'help' is requested
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            player.sendMessage("EasyRoads commands:");
            //check for permissions
            if (player.hasPermission("easyroads.reload")){
                player.sendMessage("/easyroads reload - Reload the EasyRoads configuration.");
            }

            if (player.hasPermission("easyroads.list")){
                player.sendMessage("/easyroads list - List all roads.");
            }

            player.sendMessage("/easyroads help - Display this help message.");

            return true;
        }

        // Handle the 'reload' command
        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("easyroads.reload")) {
                player.sendMessage("You do not have permission to use this command.");
                return true;
            }
            plugin.reloadConfig();
            plugin.loadConfig();
            player.sendMessage("EasyRoads configuration reloaded.");
            return true;
        }


        // Handle the 'list' command
        if (args[0].equalsIgnoreCase("list")) {
            if (!player.hasPermission("easyroads.list")) {
                player.sendMessage("You do not have permission to use this command.");
                return true;
            }
            player.sendMessage("Roads:");
            plugin.getRoads().forEach(road -> player.sendMessage(road.toString()));
            return true;
        }

        // If command not recognized, show help
        player.sendMessage("Invalid subcommand. Use /easyroads help for available commands.");
        return true;
    }
}
