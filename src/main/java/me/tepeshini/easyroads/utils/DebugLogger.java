package me.tepeshini.easyroads.utils;

import me.tepeshini.easyroads.EasyRoads;

public class DebugLogger {

    private static DebugLogger instance;
    private final EasyRoads plugin;
    private Boolean debug;

    private DebugLogger(EasyRoads plugin, Boolean debug) {
        this.plugin = plugin;
    }

    public static void init(EasyRoads plugin, Boolean debug) {
        if (instance == null) {
            instance = new DebugLogger(plugin, debug);
        }
    }

    /**
     *
     * @return instance of the debug logger
     */
    public static DebugLogger debugLog() {
        return instance;
    }


    public void info(String message) {
        if (debug) {
            plugin.getLogger().info(message);
        }
    }

    public void warning(String message) {
        if (debug) {
            plugin.getLogger().warning(message);
        }

    }

    public void severe(String message) {
        if (debug) {
            plugin.getLogger().severe(message);
        }
    }

}
