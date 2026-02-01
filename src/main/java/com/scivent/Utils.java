package com.scivent;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Utils {

    private static SCIvent plugin = SCIvent.getInstance();
    private static FileConfiguration config = plugin.getConfig();
    private static File configFile = plugin.getConfigFile();


    public static void saturatePlayer(Player player) {
        player.setFoodLevel(Constants.MAX_FOOD);
        player.addPotionEffect(new PotionEffect(
            PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, Constants.MAX_AMPLIFIER,
            false, false, false
        ));
    }

    // checks if a spawn location is written to the config file
    private static boolean checkSpawn(String path) {
        if (
            config.get(path + ".world") != null &&
            config.get(path + ".x") != null &&
            config.get(path + ".y") != null &&
            config.get(path + ".z") != null &&
            config.get(path + ".yaw") != null &&
            config.get(path + ".pitch") != null
        ) return true;
        
        else return false;
    }

    // writes a spawn location to the config file
    public static void setSpawn(String path, Location loc) {     
        config.set(path + ".world", loc.getWorld().getName());
        config.set(path + ".x", (float) Math.floor( loc.getX() ) + 0.5f);
        config.set(path + ".y", (float) Math.floor( loc.getY() ) + 0.5f);
        config.set(path + ".z", (float) Math.floor( loc.getZ() ) + 0.5f);
        config.set(path + ".yaw", Math.floor( loc.getYaw() ));
        config.set(path + ".pitch", Math.floor( loc.getPitch() ));

        try {
            config.save(configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // gets a spawn location from the config file
    public static Location getSpawn(String path) {
        if (!checkSpawn(path)) return null;

        return new Location(
            Bukkit.getWorld(config.getString(path + ".world")),
            config.getDouble(path + ".x"),
            config.getDouble(path + ".y"),
            config.getDouble(path + ".z"),
            (float) config.getDouble(path + "yaw"),
            (float) config.getDouble(path + "pitch")
        );
    }
}
