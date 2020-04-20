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
public class OceanSound {

    public static void OceanSound(Player pl) {
        RandomCollection<Boolean> random = new RandomCollection<>();
        random.add(0.1, true);
        random.add(0.9, false);

        RandomCollection<Boolean> random2 = new RandomCollection<>();
        random2.add(0.4, true);
        random2.add(0.6, false);

        Location l = Sound.getRandomLocationYW(pl.getLocation().getBlockX() - 50, pl.getLocation().getBlockX() + 50, pl.getLocation().getBlockZ() - 50, pl.getLocation().getBlockZ() + 50, pl.getWorld(), pl.getLocation().getBlockY());

        Boolean result = random.next();
        Boolean result2 = random2.next();

        if (result) {

            pl.playSound(l, SoundsString.WALES.getPath(), 0.7F, 1.0F);

        }
        if (result2) {

            pl.playSound(l, SoundsString.OCEAN.getPath(), 0.7F, 1.0F);

        }

    }
}
