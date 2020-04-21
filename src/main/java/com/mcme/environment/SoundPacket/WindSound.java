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
public class WindSound {

    public static void WindSound(Player pl) {
        RandomCollection<Boolean> random = new RandomCollection<>();
        random.add(0.4, true);
        random.add(0.6, false);

        Location l = getRandomLocationNW(pl.getLocation().getBlockX() - 10, pl.getLocation().getBlockX() + 10, pl.getLocation().getBlockZ() - 10, pl.getLocation().getBlockZ() + 10, pl.getWorld(), pl.getLocation().getBlockY());

        Boolean result = random.next();

        if (result) {

            pl.playSound(l, SoundsString.WIND.getPath(), 0.7F, 1.0F);

        }

    }
}
