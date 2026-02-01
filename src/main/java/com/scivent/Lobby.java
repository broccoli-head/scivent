package com.scivent;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;


public class Lobby implements CommandExecutor, TabCompleter {

    private static String configPath = "general.lobbySpawn";

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
            Location spawnLoc = getSpawnLoc();
            if (spawnLoc == null)
                player.sendMessage(ChatColor.RED + "Lobby spawn location is not set. Use /lobby setSpawn");
            else
                player.teleport(spawnLoc);
        }

        else if(args.length == 1) {
            if (args[0].equals("setSpawn")) {
                setSpawnLoc(player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Lobby spawn has been set!");
            }
            else sendErrorMsg(player);
        }
        else sendErrorMsg(player);

        return true;
    }


    private void sendErrorMsg(Player player) {
        player.sendMessage(ChatColor.RED + "Correct usage:  /lobby  or  /lobby setSpawn");
    }

    private void setSpawnLoc(Location loc) {  
        Utils.setSpawn(configPath, loc);   
    }

    public static Location getSpawnLoc() {
        return Utils.getSpawn(configPath);
    }

}
