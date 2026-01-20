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


public class Uhc implements CommandExecutor, TabCompleter {
    
    SCIvent plugin = SCIvent.getInstance();
    FileConfiguration config = plugin.getConfig();
    File configFile = plugin.getConfigFile();
    int changesCounter = 0;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return null;
        if (args.length != 1) return List.of();
        
        return List.of(Constants.UHC_OPTIONS);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if(args.length != 1) {
            sendErrorMsg(sender);
            return true;
        }
        
        //disables option to set a spawn location by the console operator
        if (!(sender instanceof Player) && args[0].equals("setSpawn")) {
            sender.sendMessage("You cannot use this command in the server console!");
            return true;
        }

        if (args[0].equals("setSpawn")) {   
            Player player = (Player) sender;
            setSpawn(player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Player spawn location has been successfully set!");
            return true;
        }
        
        else if (args[0].equals("start")) {
            Location spawnLoc = getSpawn(sender);
            if (spawnLoc == null) return true;

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(spawnLoc);
                player.setRespawnLocation(spawnLoc);
                player.sendTitle("UHC started!", "", 10, 50, 10);
            }

            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                changesCounter++;
                Bukkit.broadcastMessage(ChatColor.GREEN + "" + changesCounter + ". change");
            }, Constants.TICK * 30, Constants.TICK * 30);  
            return true;
        }

        else {
            sendErrorMsg(sender);
            return true;
        }
    }

    private void sendErrorMsg(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Correct usage:  /uhc start  or  /uhc setSpawn");
    }

    //saves the player spawn location to config file
    private void setSpawn(Location loc) {     
        config.set("uhc.spawn.world", loc.getWorld().getName());
        config.set("uhc.spawn.x", (float) Math.floor( loc.getX() ) + 0.5f);
        config.set("uhc.spawn.y", (float) Math.floor( loc.getY() ) + 0.5f);
        config.set("uhc.spawn.z", (float) Math.floor( loc.getZ() ) + 0.5f);
        config.set("uhc.spawn.yaw", Math.floor( loc.getYaw() ));
        config.set("uhc.spawn.pitch", Math.floor( loc.getPitch() ));

        try {
            config.save(configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //gets the player spawn location from a config file
    private Location getSpawn(CommandSender sender) {
        if (
            config.contains("uhc.spawn.world") &&
            config.contains("uhc.spawn.x") &&
            config.contains("uhc.spawn.y") &&
            config.contains("uhc.spawn.y") &&
            config.contains("uhc.spawn.pitch") &&
            config.contains("uhc.spawn.yaw")
        ) {
            Location loc = new Location(
                Bukkit.getWorld(config.getString("uhc.spawn.world")),
                config.getDouble("uhc.spawn.x"),
                config.getDouble("uhc.spawn.y"),
                config.getDouble("uhc.spawn.z"),
                (float) config.getDouble("uhc.spawn.yaw"),
                (float) config.getDouble("uhc.spawn.pitch")
            );

            return loc;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Player spawn location is not set. Use /uhc setSpawn");
            return null;
        }
    }
}
