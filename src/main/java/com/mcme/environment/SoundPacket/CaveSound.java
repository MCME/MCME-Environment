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
import com.mcme.environment.Util.RandomCollection;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class CaveSound {

    public static void CaveSound(Player pl) {

        Location l = getRandomLocationNW(pl.getLocation().getBlockX() - 10, pl.getLocation().getBlockX() + 10, pl.getLocation().getBlockZ() - 10, pl.getLocation().getBlockZ() + 10, pl.getWorld(), pl.getLocation().getBlockY());

        if (SoundUtil.randomBoolean(0.05, 0.95)) {

            pl.playSound(l, SoundsString.CAVES_CRUMBLE.getPath(), SoundCategory.AMBIENT, 0.7F, 1.0F);

        }
        if (SoundUtil.randomBoolean(0.3, 0.7)) {

            pl.playSound(l, SoundsString.CAVES_DROPLETS.getPath(), SoundCategory.AMBIENT, 0.7F, 1.0F);

        }
        if (SoundUtil.randomBoolean(0.05, 0.95)) {

            pl.playSound(l, SoundsString.CAVE_CRICKETS.getPath(), SoundCategory.AMBIENT, 0.6F, 1.0F);

        }
        if (SoundUtil.randomBoolean(0.05, 0.95)) {
            Float volWind = 0.05F;
            if (SoundUtil.isOutdoor(pl.getLocation())) {
                volWind = 0.7F;
            }

            pl.playSound(l, SoundsString.WIND.getPath(), SoundCategory.AMBIENT, volWind, 1.0F);

        }

    }
}
