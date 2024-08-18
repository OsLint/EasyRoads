package me.tepeshini.easyroads.commands;

import me.tepeshini.easyroads.EasyRoads;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EasyRoadsCommand implements CommandExecutor {

    private final EasyRoads plugin;

    public EasyRoadsCommand(EasyRoads plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        throw new UnsupportedOperationException("Not supported yet.");
      /*
        if (!sender.hasPermission("speedroads.admin"))
            return true;

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("/roads - this page");
            sender.sendMessage("/roads reload - reloads the config");
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.loadConfig();
            sender.sendMessage(plugin.getRoads().size() + " road(s) loaded.");
        } else
            return false;

        return true; */
    }
}
