package me.tepeshini.easyroads.models;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Logger;

/**
 * Represents a road in the EasyRoads plugin.
 * A road is defined by its speed modifier and the blocks that make up the road.
 */
public class Road {
    private final double speed;


    private final List<BlockData> blockData = new ArrayList<>();

    /**
     * Constructs a Road instance using the given configuration section.
     *
     * @param config the configuration section containing road data
     * @param log    the logger to log warnings and errors
     */
    public Road(ConfigurationSection config, Logger log) {
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
                    log.warning("Found legacy material in road. You should update it to the new name to avoid any potential issues.");
                    log.warning(String.format("Input string: %s -> %s", material, mat.name()));
                }
            }

            if (mat == null) {
                log.severe("Invalid road block defined, skipping. Make sure to specify a valid material!");
                log.severe(String.format("Input string: %s", a));
                return;
            }

            blockData.add(Bukkit.createBlockData(mat, data));
        });
    }

    /**
     * Checks if the given block matches the road's block data.
     *
     * @param block the block to check
     * @return true if the block matches the road's block data, false otherwise
     */
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

    /**
     * Gets the speed modifier for this road.
     *
     * @return the speed modifier
     */
    public double getSpeedModifier() {
        return speed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Road: ");
        sb.append("Speed=");
        sb.append(speed);
        sb.append(" BlockData=");
        for (BlockData blockData : blockData) {
            if (blockData == null) {
                continue;
            }
            sb.append(blockData.getAsString());
        }
        return sb.toString();
    }
}