/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
     */
    public RegionData(String namem, UUID idregion, Region rn, String sr, String t, String we, boolean th, String tm) {

        name = namem;

        idr = idregion;

        region = rn;

        weather = we;

        server = sr;

        type = t;

        thunder = th;

        time = tm;

    }

    public Location getLocation() {
        return region.getLocation();
    }

    public boolean isInside(Location loc) {

        return region.isInside(loc);

    }
}
