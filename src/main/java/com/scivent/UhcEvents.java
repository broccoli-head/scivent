package com.scivent;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitTask;


public class UhcEvents {

    private SCIvent plugin = SCIvent.getInstance();
    private FileConfiguration config = plugin.getConfig();

    private BukkitTask spawnTNTtask;
    private BukkitTask spawnAnvilTask;
    private int tntCounter = 0;
    private int anvilCounter = 0;


    private UhcInterface spawnTNT = (player) -> {
        int spawnInterval = config.getInt("uhc.events.fallingTNT.spawnInterval");
        int repeatCount = config.getInt("uhc.events.fallingTNT.repeatCount");

        spawnTNTtask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Location tntLoc = player.getLocation();
            //spawns primed TNT 5 blocks above a player
            tntLoc.setY(tntLoc.getY() + 5);
            TNTPrimed tnt = player.getWorld().spawn(tntLoc, TNTPrimed.class);
            //TNT explodes after 20 ticks (1 second)
            tnt.setFuseTicks(Constants.SECOND);

            if (tntCounter == repeatCount) {
                spawnTNTtask.cancel();
                tntCounter = 0;
            }
            tntCounter++;
        }, 0, spawnInterval * Constants.SECOND);
    };

    private UhcInterface spawnAnvil = (player) -> {
        int spawnInterval = config.getInt("uhc.events.fallingAnvil.spawnInterval");
        int repeatCount = config.getInt("uhc.events.fallingAnvil.repeatCount");

        spawnAnvilTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Location anvilLoc = player.getLocation();
            //spawns anvil 5 blocks above a player
            anvilLoc.setY(anvilLoc.getY() + 5);
            FallingBlock anvil = player.getWorld().spawnFallingBlock(anvilLoc, Material.ANVIL.createBlockData());
            
            //removes anvil after 2 seconds
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if(anvil.isValid()) anvil.remove();
            }, 2 * Constants.SECOND);

            if (anvilCounter == repeatCount) {
                spawnAnvilTask.cancel();
                anvilCounter = 0;
            }
            anvilCounter++;
        }, 0, spawnInterval * Constants.SECOND);
    };
    

    private UhcInterface[] actions = { spawnTNT, spawnAnvil };

    //chooses a random event to execute
    public void randomizeEvent(ArrayList<Player> alivePlayers) {
        int i = new Random().nextInt(actions.length);
        
        for(Player player : alivePlayers)
            actions[i].executeEvent(player);
    }
}
