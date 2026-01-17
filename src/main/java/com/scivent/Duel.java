package com.scivent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Duel implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Poprawne użycie:  /duel <gracz>");
            return false;
        }

        Player challenger = (Player) sender;    //person who starts the duel
        String opponentName = args[0];
        Player opponent = Bukkit.getPlayerExact(opponentName);   //opponent of the challenger

        if (opponent == null) {
            challenger.sendMessage(ChatColor.RED + "Nie znaleziono gracza " + opponentName);
            return false;
        }

        challenger.sendMessage(ChatColor.GREEN + "Wyzwałeś " + opponentName + " na pojedynek!");
        opponent.sendMessage(ChatColor.GREEN + challenger.getName() + " wyzwał cię na pojedynek!");
        return true;
    }
}
