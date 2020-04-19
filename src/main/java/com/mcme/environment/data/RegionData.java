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
package com.mcme.environment.data;

import com.mcmiddleearth.pluginutil.region.Region;
import java.util.UUID;
import org.bukkit.Location;

/**
 *
 * @author Fraspace5
 */
public class RegionData {

    public final String name;

    public final UUID idr;

    public final Region region;

    public final String server;

    public final String type;

    public final String weather;

    public final Boolean thunder;

    public final String time;

    public final Integer weight;
    
    public final String sound;

    /**
     *
     * Create a new region data
     *
     * @param namem Name of the region
     * @param idregion Id of the region
     * @param rn The PluginUtil region
     * @param sr The name of the server as in BungeeCord file
     * @param t Type of the region (Cuboid or Prismoid)
     * @param we Weather type (rain|sun)
     * @param th Thunder boolean
     * @param tm Time in ticks
     * @param weg Weight of the region
     * @param snd Sound type
     */
    public RegionData(String namem, UUID idregion, Region rn, String sr, String t, String we, boolean th, String tm, Integer weg,String snd) {

        name = namem;

        idr = idregion;

        region = rn;

        weather = we;

        server = sr;

        type = t;

        thunder = th;

        time = tm;

        weight = weg;
        
        sound = snd;

    }

    public Location getLocation() {
        return region.getLocation();
    }

    public boolean isInside(Location loc) {

        return region.isInside(loc);

    }
}
