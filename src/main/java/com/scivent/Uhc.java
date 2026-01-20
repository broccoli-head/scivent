package com.scivent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Uhc implements CommandExecutor{
    
    SCIvent instance = SCIvent.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length != 1) {
            sendErrorMsg(sender);
            return false;
        }
        
        Player cmdSender = (Player) sender;

        if (args[0].equals("setSpawn")) {
            setSpawn(cmdSender.getLocation());
            return true;
        }
        
        else if (args[0].equals("start")) {

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport()
            }

            Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
                Bukkit.broadcastMessage("Zmiana");
            }, 0, 180);  
            return true;
        }

        else {
            sendErrorMsg(sender);
            return false;
        }
    }

    private void sendErrorMsg(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Poprawne użycie:  /uhc start  lub  /uhc setSpawn <świat>");
    }

    //saves players spawn location to config file
    private void setSpawn(Location loc) {
        String world = loc.getWorld().getName();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float pitch = loc.getPitch();
        float yaw = loc.getYaw();
        
        instance.getConfig().set("uhc.spawn.world", world);
        instance.getConfig().set("uhc.spawn.x", x);
        instance.getConfig().set("uhc.spawn.y", y);
        instance.getConfig().set("uhc.spawn.z", z);
        instance.getConfig().set("uhc.spawn.pitch", pitch);
        instance.getConfig().set("uhc.spawn.yaw", yaw);
    }

    private void getSpawn() {
        Location loc = new Location(
            instance.getConfig().getString("uhc.spawn.world"),
            instance.getConfig().getString("uhc.spawn.x"),
            instance.getConfig().getString("uhc.spawn.y"),
            instance.getConfig().getString("uhc.spawn.z"),
            instance.getConfig().getString("uhc.spawn.pitch"),
            instance.getConfig().getString("uhc.spawn.yaw")
        )
    }
}
