package com.scivent;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
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
    private File configFile;
    private FileConfiguration pluginConfig;

    @Override
    public void onEnable() {
        getLogger().info("SCIvent plugin loaded correctly!");

        pluginInstance = this;
        createConfigFile();

        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("duel").setExecutor(new Duel());
    }

    @Override
    public void onDisable() {
        getLogger().info("SCIvent plugin has been unloaded!");
    }

    public static SCIvent getInstance() {
        return pluginInstance;
    }

    public FileConfiguration getConfig() {
        return this.pluginConfig;
    }

    private void createConfigFile() {
        //creating and loading plugin config file
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        pluginConfig = new YamlConfiguration();
        try {
            pluginConfig.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
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
        
        //saturates player 1 tick later after respawning to prevent effect clearing
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Utils.saturatePlayer(player);
        }, 1L);
    }

}