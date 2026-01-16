package com.scivent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Duel implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("duel")) {
            Player player = (Player) sender;
            if(args.length < 1)
                player.sendMessage(ChatColor.RED + "Poprawne użycie:\n/duel <gracz>");
        }
        return true;
    }
}
