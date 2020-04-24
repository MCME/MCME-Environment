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

import com.mcme.environment.Util.EnvChange;
import com.mcme.environment.Util.RandomCollection;
import com.mcmiddleearth.pluginutil.region.CuboidRegion;
import com.mcmiddleearth.pluginutil.region.PrismoidRegion;
import com.mcmiddleearth.pluginutil.region.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class OceanSound {

    public static void OceanSound(Player pl, Region re) {
        RandomCollection<Boolean> random = new RandomCollection<>();
        random.add(0.1, true);
        random.add(0.9, false);

        RandomCollection<Boolean> random2 = new RandomCollection<>();
        random2.add(0.4, true);
        random2.add(0.6, false);
        Location l = null;

        if (re instanceof CuboidRegion) {
            l = SoundUtil.getRandomLocationYW(((CuboidRegion) re).getMinCorner().getBlockX(), ((CuboidRegion) re).getMaxCorner().getBlockX(), ((CuboidRegion) re).getMinCorner().getBlockZ(), ((CuboidRegion) re).getMaxCorner().getBlockZ(), pl.getWorld(), ((CuboidRegion) re).getMinCorner().getBlockY(), ((CuboidRegion) re).getMaxCorner().getBlockY());
        } else if (re instanceof PrismoidRegion) {
            l = EnvChange.randomLocPrismoid(re, pl.getWorld().getName());
            Block bl = pl.getWorld().getBlockAt(l);
            while (bl.getType() != Material.WATER) {

                l = EnvChange.randomLocPrismoid(re, pl.getWorld().getName());
            }
        }

        Boolean result = random.next();
        Boolean result2 = random2.next();
        Float volume = 1F;
        if (l != null) {
            if (SoundUtil.isOutdoor(pl.getLocation())) {
                volume = 0.4F;

            }
            if (result) {

                pl.playSound(l, SoundsString.WALES.getPath(), SoundCategory.AMBIENT, volume, 1.0F);

            }
            if (result2) {

                pl.playSound(l, SoundsString.OCEAN.getPath(), SoundCategory.AMBIENT, volume, 1.0F);

            }
        }
    }
}
