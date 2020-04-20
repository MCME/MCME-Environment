/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.SoundPacket;

import com.mcme.environment.Environment;
import static com.mcme.environment.SoundPacket.Sound.getRandomLocationNW;
import com.mcme.environment.data.PluginData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class BellSound {

    public static void BellSound(Player pl, int time, Location l) {

        new BukkitRunnable() {
            int times = 0;

            @Override
            public void run() {

                if (times <= time) {
                    times += 1;

                    pl.playSound(l, Sound.BLOCK_BELL_USE, 1.0F, 1.0F);

                } else {
                    cancel();

                }

            }

        }.runTaskTimer(Environment.getPluginInstance(), 300L, 40L);

    }

}
