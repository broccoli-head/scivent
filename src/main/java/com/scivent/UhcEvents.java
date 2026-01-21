package com.scivent;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitTask;

public class UhcEvents {
    SCIvent plugin = SCIvent.getInstance();
    Player player;

    BukkitTask spawnTNTtask;
    int TNTcounter = 0;

    public UhcEvents(Player player) {
        this.player = player;
    }

    UhcInterface spawnTNT = () -> {
        spawnTNTtask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Location TNTloc = player.getLocation();
            //spawns primed TNT 10 blocks above a player
            TNTloc.setY(TNTloc.getY() + 10);
            player.getWorld().spawn(TNTloc, TNTPrimed.class);

            if (TNTcounter == Constants.UHC_TNT_COUNT) {
                spawnTNTtask.cancel();
                TNTcounter = 0;
            }
            TNTcounter++;
        }, 0, Constants.UHC_SPAWN_TNT_PERIOD * Constants.TICK);
    };
    
    UhcInterface[] actions = { spawnTNT };

    //chooses a random event to execute
    public void randomizeEvent() {
        int i = new Random().nextInt(actions.length);
        actions[i].executeEvent();
    }
}
