/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.SoundPacket;

import static com.mcme.environment.SoundPacket.Sound.getRandomLocationNW;
import com.mcme.environment.Util.RandomCollection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class SwamplandSound {

    public static void SwampLandSound(Player pl, Long time) {
        RandomCollection<Boolean> random = new RandomCollection<>();
        random.add(0.4, true);
        random.add(0.6, false);

        RandomCollection<Boolean> random2 = new RandomCollection<>();
        random2.add(0.4, true);
        random2.add(0.6, false);

        RandomCollection<Boolean> random3 = new RandomCollection<>();
        random3.add(0.2, true);
        random3.add(0.8, false);
        Location l = getRandomLocationNW(pl.getLocation().getBlockX() - 20, pl.getLocation().getBlockX() + 20, pl.getLocation().getBlockZ() - 20, pl.getLocation().getBlockZ() + 20, pl.getWorld(), pl.getLocation().getBlockY());

        Boolean result = random.next();
        Boolean result2 = random2.next();
        Boolean result3 = random3.next();

        if (Sound.getTimeString(time).equalsIgnoreCase("day") || Sound.getTimeString(time).equalsIgnoreCase("morning")) {

            if (result) {

                pl.playSound(l, SoundsString.SWAMPLAND_BIRDS_DAY.getPath(), 0.7F, 1.0F);

            }
            if (result2) {

                pl.playSound(l, SoundsString.SWAMPLAND_CRICKETS_DAY.getPath(), 0.7F, 1.0F);

            }

        } else {
            if (result) {

                pl.playSound(l, SoundsString.SWAMPLAND_FROGS_NIGHT.getPath(), 0.7F, 1.0F);

            }
            if (result2) {

                pl.playSound(l, SoundsString.SWAMPLAND_CRICKETS_NIGHT.getPath(), 0.7F, 1.0F);

            }
            if (result3) {

                pl.playSound(l, SoundsString.SWAMPLAND_BIRDS_NIGHT.getPath(), 0.6F, 1.0F);

            }

        }
    }
}
