/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.SoundPacket;

import static com.mcme.environment.SoundPacket.SoundUtil.getRandomLocationNW;
import com.mcme.environment.Util.RandomCollection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class CaveSound {

    public static void CaveSound(Player pl) {
        RandomCollection<Boolean> random = new RandomCollection<>();
        random.add(0.2, true);
        random.add(0.8, false);

        RandomCollection<Boolean> random2 = new RandomCollection<>();
        random2.add(0.3, true);
        random2.add(0.7, false);

        RandomCollection<Boolean> random3 = new RandomCollection<>();
        random3.add(0.05, true);
        random3.add(0.95, false);
        Location l = getRandomLocationNW(pl.getLocation().getBlockX() - 10, pl.getLocation().getBlockX() + 10, pl.getLocation().getBlockZ() - 10, pl.getLocation().getBlockZ() + 10, pl.getWorld(), pl.getLocation().getBlockY());

        Boolean result = random.next();
        Boolean result2 = random2.next();
        Boolean result3 = random3.next();
        if (result) {

            pl.playSound(l, SoundsString.CAVES_CRUMBLE.getPath(), 0.7F, 1.0F);

        }
        if (result2) {

            pl.playSound(l, SoundsString.CAVES_DROPLETS.getPath(), 0.7F, 1.0F);

        }
        if (result3) {

            pl.playSound(l, SoundsString.CAVE_CRICKETS.getPath(), 0.6F, 1.0F);

        }

    }
}
