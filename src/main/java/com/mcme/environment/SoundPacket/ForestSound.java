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

import static com.mcme.environment.SoundPacket.SoundUtil.getRandomLocationNW;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class ForestSound {

    public static void ForestSound(Player pl, Long time) {

        Location l = getRandomLocationNW(pl.getLocation().getBlockX() - 20, pl.getLocation().getBlockX() + 20, pl.getLocation().getBlockZ() - 20, pl.getLocation().getBlockZ() + 20, pl.getWorld(), pl.getLocation().getBlockY());

        Float volume = 0.2F;

        if (SoundUtil.isOutdoor(pl.getLocation())) {
            volume = 1.0F;

        }

        if (SoundUtil.getTimeString(time).equalsIgnoreCase("day")) {

            if (SoundUtil.randomBoolean(0.3, 0.7)) {

                pl.playSound(l, SoundsString.FOREST_BIRD_DAY.getPath(), SoundCategory.AMBIENT, volume, 1.0F);

            }
            if (SoundUtil.randomBoolean(0.3, 0.7)) {

                pl.playSound(l, SoundsString.FOREST_INSECT_DAY.getPath(), SoundCategory.AMBIENT, volume, 1.0F);

            }

        } else if (SoundUtil.getTimeString(time).equalsIgnoreCase("morning")) {
            if (SoundUtil.randomBoolean(0.3, 0.7)) {

                pl.playSound(l, SoundsString.FOREST_BIRD_MORNING.getPath(), SoundCategory.AMBIENT, volume, 1.0F);

            }
            if (SoundUtil.randomBoolean(0.3, 0.7)) {

                pl.playSound(l, SoundsString.FOREST_INSECT_MORNING.getPath(), SoundCategory.AMBIENT, volume, 1.0F);

            }

        } else {
            if (SoundUtil.randomBoolean(0.3, 0.7)) {

                pl.playSound(l, SoundsString.FOREST_BIRD_NIGHT.getPath(), SoundCategory.AMBIENT, volume, 1.0F);

            }
            if (SoundUtil.randomBoolean(0.3, 0.7)) {

                pl.playSound(l, SoundsString.FOREST_INSECT_NIGHT.getPath(), SoundCategory.AMBIENT, volume, 1.0F);

            }

        }

    }
}
