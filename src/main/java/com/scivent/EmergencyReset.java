package com.scivent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class EmergencyReset implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender.isOp()) {
            if (cmd.getName().equalsIgnoreCase("reset")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.getInventory().clear();
                    player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(Constants.MAX_HEALTH);     //set heart count to default
                    player.setHealth(Constants.MAX_HEALTH);
                    
                    for (PotionEffect effect : player.getActivePotionEffects())
                        player.removePotionEffect(effect.getType());
                }
            }
        }

        else sender.sendMessage(ChatColor.RED + "Komenda tylko dla adminów!");
        return true;
    }
}

