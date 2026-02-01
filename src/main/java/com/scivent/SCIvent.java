package com.scivent;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;


public class SCIvent extends JavaPlugin implements Listener {

    private static SCIvent pluginInstance;
    private FileConfiguration pluginConfig;
    private File configFile;

    @Override
    public void onEnable() {
        pluginInstance = this;
        createConfigFile();

        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("duel").setExecutor(new Duel());
        this.getCommand("uhc").setExecutor(new Uhc());
        this.getCommand("lobby").setExecutor(new Lobby());

        getLogger().info("SCIvent plugin loaded correctly!");
        
        if (Lobby.getSpawnLoc() == null) setDefaultSpawnLoc();
    }

    @Override
    public void onDisable() {
        getLogger().info("SCIvent plugin has been unloaded!");
    }


    public static SCIvent getInstance() {
        return pluginInstance;
    }
    public FileConfiguration getConfig() {
        return pluginConfig;
    }
    public File getConfigFile() {
        return this.configFile;
    }

    
    private void createConfigFile() {
        //creates the config file if it doesn't exist
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        //tries to load the config file
        pluginConfig = new YamlConfiguration();
        try {
            pluginConfig.load(configFile);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    //sets a lobby spawn location if haven't been set yet
    private void setDefaultSpawnLoc() {
        World defaultWorld = Bukkit.getWorld("world");
        if (defaultWorld != null) {
            Location defaultSpawn = defaultWorld.getSpawnLocation();
            String path = "general.lobbySpawn";

            pluginConfig.set(path + ".world", defaultWorld.getName());
            pluginConfig.set(path + ".x", defaultSpawn.getX());
            pluginConfig.set(path + ".y", defaultSpawn.getY());
            pluginConfig.set(path + ".z", defaultSpawn.getZ());
            pluginConfig.set(path + ".yaw", defaultSpawn.getYaw());
            pluginConfig.set(path + ".pitch", defaultSpawn.getPitch());
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore())
            event.setJoinMessage(ChatColor.GREEN + "Witaj " + ChatColor.AQUA + player.getDisplayName() + ChatColor.GREEN + " na SCIvencie!");   
        else
            event.setJoinMessage((ChatColor.GREEN + "Witaj ponownie, " + ChatColor.AQUA + player.getDisplayName()));

        Utils.saturatePlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.RED + player.getDisplayName() + ChatColor.GREEN + " sfrajerzył (wyszedł z serwera)");
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        
        //saturates a player 1 tick later after respawning to prevent effect clearing
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Utils.saturatePlayer(player);
        }, 1L);
    }

}