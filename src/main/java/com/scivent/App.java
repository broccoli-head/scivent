package com.scivent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;


public class App extends JavaPlugin
{
    @Override
    public void onEnable() {
        getLogger().info("SCIvent plugin loaded correctly!");
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
}