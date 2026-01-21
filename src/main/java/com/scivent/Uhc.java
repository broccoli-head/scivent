package com.scivent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;


public class Uhc implements CommandExecutor, TabCompleter {
    
    SCIvent plugin = SCIvent.getInstance();
    FileConfiguration config = plugin.getConfig();
    File configFile = plugin.getConfigFile();

    ArrayList<Player> alivePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
    int changesCounter = 0;
    BukkitTask uhcTask;

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
            Location spawnLoc = getSpawn();
            if (spawnLoc == null)
                sender.sendMessage(ChatColor.RED + "Player spawn location is not set. Use /uhc setSpawn");
            else startUhc(spawnLoc);  
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
    private Location getSpawn() {
        String path = "uhc.spawn";

        if (
            config.contains(path + ".world") && config.contains(path + ".x") &&
            config.contains(path + ".y") && config.contains(path + ".z") &&
            config.contains(path + ".yaw") && config.contains(path + ".pitch")
        ) {
            return new Location(
                Bukkit.getWorld(config.getString("uhc.spawn.world")),
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z"),
                (float) config.getDouble(path + "yaw"),
                (float) config.getDouble(path + "pitch")
            );
        }
        else return null;
    }


    private void startUhc(Location spawnLoc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(spawnLoc);
            player.setRespawnLocation(spawnLoc);
            player.sendTitle(
                ChatColor.AQUA + "UHC" + ChatColor.GOLD + "się rozpoczęło!",
                "", 10, 50, 10
            );
        }

        uhcTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            changesCounter++;
            Bukkit.broadcastMessage(ChatColor.GREEN + "" + changesCounter + ". change");

            for(Player player : Bukkit.getOnlinePlayers()) {
                UhcEvents events = new UhcEvents(player);
                events.randomizeEvent();
            }
        }, Constants.TICK * 30, Constants.TICK * 30);
    }
    
    private void stopUhc() {
        //stops repeating task timer from startUhc()
        uhcTask.cancel();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        //strikes a lightning at the player's death location
        Location deathLocation = player.getLastDeathLocation();
        player.getWorld().strikeLightning(deathLocation);

        //respawns the player as a spectator
        player.spigot().respawn();
        player.teleport(deathLocation);
        player.setGameMode(GameMode.SPECTATOR);
    }
}
