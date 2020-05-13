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

import com.mcme.environment.data.RegionData;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class LeavesSound {

    public static void LeavesSound(Player pl, RegionData re) {

        Location l = re.locData.leaves.get(0);

        for (Location loc : re.locData.leaves) {
            if (l.distanceSquared(pl.getLocation()) > loc.distanceSquared(pl.getLocation())) {
                l = loc;
            }
        }

        Float volume = 0.4F;
        if (SoundUtil.isOutdoor(pl.getLocation())) {
            volume = 3F;

        }

        if (SoundUtil.randomBoolean(0.4, 0.6)) {

            pl.playSound(l, SoundsString.LEAVES.getPath(), SoundCategory.AMBIENT, volume, 1.0F);
           
            System.out.println("leaves vanno");
        }

        if (SoundUtil.randomBoolean(0.2, 0.8)) {
            Float volWind = 0.05F;
            if (SoundUtil.isOutdoor(pl.getLocation())) {
                volWind = 0.7F;
            }

            pl.playSound(l, SoundsString.WIND.getPath(), SoundCategory.AMBIENT, volWind, 1.0F);

        }
    }
}
