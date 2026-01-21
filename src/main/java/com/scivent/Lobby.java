package com.scivent;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;


public class Lobby implements CommandExecutor, TabCompleter {

    private static SCIvent plugin = SCIvent.getInstance();
    private static FileConfiguration config = plugin.getConfig();
    private File configFile = plugin.getConfigFile();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return null;
        if (args.length != 1) return List.of();

        return List.of(Constants.LOBBY_OPTIONS);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot use this command in the server console!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            Location spawnLoc = getSpawn();
            if (spawnLoc == null)
                player.sendMessage(ChatColor.RED + "Lobby spawn location is not set. Use /lobby setSpawn");
            else
                player.teleport(spawnLoc);
        }

        else if(args.length == 1) {
            if (args[0].equals("setSpawn")) {
                setSpawn(player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Lobby spawn has been set!");
            }
            else sendErrorMsg(player);
        }
        else sendErrorMsg(player);

        return true;
    }


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

    private void sendErrorMsg(Player player) {
        player.sendMessage(ChatColor.RED + "Correct usage:  /lobby  or  /lobby setSpawn");
    }

    private void setSpawn(Location loc) {     
        config.set("general.lobbySpawn.world", loc.getWorld().getName());
        config.set("general.lobbySpawn.x", (float) Math.floor( loc.getX() ) + 0.5f);
        config.set("general.lobbySpawn.y", (float) Math.floor( loc.getY() ) + 0.5f);
        config.set("general.lobbySpawn.z", (float) Math.floor( loc.getZ() ) + 0.5f);
        config.set("general.lobbySpawn.yaw", Math.floor( loc.getYaw() ));
        config.set("general.lobbySpawn.pitch", Math.floor( loc.getPitch() ));

        try {
            config.save(configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Location getSpawn() {
        if(!checkLobbyLoc()) return null;

        String path = "general.lobbySpawn";
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
