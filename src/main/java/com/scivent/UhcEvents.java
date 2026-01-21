package com.scivent;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitTask;

public class UhcEvents {
    SCIvent plugin = SCIvent.getInstance();
    Player player;

    BukkitTask spawnTNTtask;
    BukkitTask spawnAnvilTask;
    int TNTcounter = 0;
    int anvilCounter = 0;

    public UhcEvents(Player player) {
        this.player = player;
    }

    UhcInterface spawnTNT = () -> {
        spawnTNTtask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Location TNTloc = player.getLocation();
            //spawns primed TNT 10 blocks above a player
            TNTloc.setY(TNTloc.getY() + 10);
            player.getWorld().spawn(TNTloc, TNTPrimed.class);

            if (TNTcounter == Constants.UHC_SPAWNED_ENTITY_COUNT) {
                spawnTNTtask.cancel();
                TNTcounter = 0;
            }
            TNTcounter++;
        }, 0, Constants.UHC_SPAWN_ENTITY_PERIOD * Constants.TICK);
    };

    UhcInterface spawnAnvil = () -> {
        spawnAnvilTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Location anvilLoc = player.getLocation();
            //spawns anvil 10 blocks above a player
            anvilLoc.setY(anvilLoc.getY() + 10);
            FallingBlock anvil = player.getWorld().spawnFallingBlock(anvilLoc, Material.ANVIL.createBlockData());
            
            //removes anvil after 5 seconds
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if(anvil.isValid()) anvil.remove();
            }, 5 * Constants.TICK);

            if (anvilCounter == Constants.UHC_SPAWNED_ENTITY_COUNT) {
                spawnAnvilTask.cancel();
                anvilCounter = 0;
            }
            anvilCounter++;
        }, 0, Constants.UHC_SPAWN_ENTITY_PERIOD * Constants.TICK);
    };
    

    UhcInterface[] actions = { spawnTNT, spawnAnvil };

    //chooses a random event to execute
    public void randomizeEvent() {
        int i = new Random().nextInt(actions.length);
        actions[i].executeEvent();
    }
}
