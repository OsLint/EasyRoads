package me.tepeshini.easyroads.models;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;

import static me.tepeshini.easyroads.utils.DebugLogger.debugLog;

public class Road {
    private final double speed;
    private final List<BlockData> blockData = new ArrayList<>();

    public Road(ConfigurationSection config) {
        this.speed = config.getDouble("speed", 0.2D);

        config.getStringList("blocks").forEach(a -> {
            if (a.equalsIgnoreCase("EMPTY") || a.equalsIgnoreCase("NULL") || a.equalsIgnoreCase("ANY")) {
                blockData.add(null);
                return;
            }

            int index = a.indexOf('[');

            if (index == -1)
                index = a.length();

            String material = a.substring(0, index).trim();
            String data = a.substring(index);
            Material mat = Material.matchMaterial(material);

            if (mat == null) {
                mat = Material.matchMaterial(material, true);

                if (mat != null) {
                    debugLog().warning("Found legacy material in road. You should update it to the new name to avoid any potential issues.");
                    debugLog().warning(String.format("Input string: %s -> %s", material, mat.name()));
                }
            }

            if (mat == null) {
                debugLog().severe("Invalid road block defined, skipping. Make sure to specify a valid material!");
                debugLog().severe(String.format("Input string: %s", a));
                return;
            }

            blockData.add(Bukkit.createBlockData(mat, data));
        });
    }

    public boolean isRoadBlock(Block block) {
        if (blockData.isEmpty())
            return false;

        for (int i = 0; i < blockData.size(); i++) {
            if (blockData.get(i) == null)
                continue;

            if (!block.getRelative(0, -i, 0).getBlockData().matches(blockData.get(i)))
                return false;
        }

        return true;
    }

    public double getSpeedMod() {
        return speed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Road: ");
        sb.append("Speed=");
        sb.append(speed);
        sb.append("BlockData=");
        for (BlockData blockData : blockData) {
            sb.append(blockData.getAsString());
        }
        return sb.toString();
    }
}