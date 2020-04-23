/*
 * Copyright (C) 2020 MCME (Fraspace5)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mcme.environment.SoundPacket;

import com.mcme.environment.Environment;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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
                Float volume = 1F;
                if (SoundUtil.isOutdoor(pl.getLocation())) {
                    volume = 0.4F;

                }

                if (times <= time) {
                    times += 1;

                    pl.playSound(l, Sound.BLOCK_BELL_USE, SoundCategory.AMBIENT, volume, 0.7F);

                } else {
                    cancel();

                }

            }

        }.runTaskTimer(Environment.getPluginInstance(), 300L, 60L);

    }

}
