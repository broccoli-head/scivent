package com.scivent;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Lobby {
    private static SCIvent plugin = SCIvent.getInstance();
    private static FileConfiguration config = plugin.getConfig();

    public static boolean checkLobbyLoc() {
        String path = "general.lobbySpawn";
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

    // public static void teleportAll() {
    //     for(Player player : Bukkit.getOnlinePlayers()) {
    //         // player.teleport();
    //     }
    // }

    // public static void setSpawn() {
        
    // }

}
