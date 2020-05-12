/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        Location l = null;
        if (!re.locData.leaves.isEmpty()) {
            l = re.locData.leaves.get(0);

            for (Location loc : re.locData.leaves) {
                if (l.distanceSquared(pl.getLocation()) > loc.distanceSquared(pl.getLocation())) {
                    l = loc;
                }
            }

        }

        Float volume = 0.4F;
        if (SoundUtil.isOutdoor(pl.getLocation())) {
            volume = 2F;

        }
        if (l != null) {
            if (SoundUtil.randomBoolean(0.3, 0.7)) {

                pl.playSound(l, SoundsString.OCEAN.getPath(), SoundCategory.AMBIENT, volume, 1.0F);

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
}
