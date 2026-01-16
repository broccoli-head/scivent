package com.scivent;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Utils {
    public static void saturatePlayer(Player player) {
        player.setFoodLevel(Constants.MAX_FOOD);
        player.addPotionEffect(new PotionEffect(
            PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, Constants.MAX_AMPLIFIER,
            false, false, false
        ));
    }
}
