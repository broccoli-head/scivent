package com.scivent;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Duel implements CommandExecutor, TabCompleter {
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return null;
        if (args.length != 1) return List.of();
        
        List<String> players = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName());
        }
        return players;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot use this command in the server console!");
            return true;
        }

        if(args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Correct usage:  /duel <player>");
            return true;
        }

        Player challenger = (Player) sender;    //person who starts the duel
        String opponentName = args[0];
        Player opponent = Bukkit.getPlayerExact(opponentName);   //opponent of the challenger

        if (opponent == null) {
            challenger.sendMessage(
                ChatColor.RED + "Player " + ChatColor.BOLD + opponentName +
                ChatColor.RESET + ChatColor.RED + " was not found!"
            );
            return true;
        }
        else if (challenger.getName().equals(args[0])) {
            challenger.sendMessage(ChatColor.RED + "You cannot duel yourself!");
            return true;
        }

        challenger.sendMessage(ChatColor.GREEN + "You challenged " + opponentName + " to a duel!");
        opponent.sendMessage(ChatColor.GREEN + challenger.getName() + " challenged you to a duel!");
        return true;
    }
}
