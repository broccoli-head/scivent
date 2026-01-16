package com.scivent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;


public class App extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("SCIvent plugin loaded correctly!");

        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("duel").setExecutor(new Duel());
    }

    @Override
    public void onDisable() {
        getLogger().info("SCIvent plugin has been unloaded!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore())
            event.setJoinMessage(ChatColor.GREEN + "Witaj " + ChatColor.AQUA + player.getDisplayName() + ChatColor.GREEN + " na SCIvencie!");   
        else
            event.setJoinMessage((ChatColor.GREEN + "Witaj ponownie, " + ChatColor.AQUA + player.getDisplayName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.RED + player.getDisplayName() + ChatColor.GREEN + " sfrajerzył (wyszedł z serwera)");
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Utils.saturatePlayer(player);
    }

}