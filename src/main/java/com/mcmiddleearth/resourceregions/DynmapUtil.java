/*
 * Copyright (C) 2016 MCME
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
package com.mcmiddleearth.resourceregions;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dags.resourceregions.ResourceRegions;
import me.dags.resourceregions.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

/**
 *
 * @author Eriol_Eandur
 */
public class DynmapUtil {
    
    private static boolean init = false;
    private static final boolean enabled = getRRPlugin().getConfig().getBoolean("dynmap.enabled",false);
    
    private static DynmapAPI dynmapPlugin;
    
    private static MarkerSet markerSet;
    private static int borderColor;
    private static int areaColor;
    private static int borderWidth;
    private static double borderOpacity;
    private static double areaOpacity;

    private static void init() {
        if(!enabled) {
            return;
        }
        Plugin dynmap = Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        if(dynmap==null) {
            Logger.getGlobal().info("Dynmap not found");
        }
        else {
            try{
                dynmapPlugin = (DynmapAPI) dynmap;
                markerSet = dynmapPlugin.getMarkerAPI().createMarkerSet("resourceregions.markerset", "ResourceRegions", null, false);
                markerSet.setHideByDefault(getRRPlugin().getConfig().getBoolean("dynmap.hide",true));
                borderColor = getRRPlugin().getConfig().getColor("dynmap.borderColor",Color.PURPLE).asRGB();
                areaColor = getRRPlugin().getConfig().getColor("dynmap.areaColor",Color.PURPLE).asRGB();
                borderWidth = getRRPlugin().getConfig().getInt("dynmap.borderWidth",2);
                borderOpacity = getRRPlugin().getConfig().getDouble("dynmap.borderOpacity",0.15);
                areaOpacity = getRRPlugin().getConfig().getDouble("dynmap.areaOpacity",0.25);
                getRRPlugin().saveConfig();
                init = true;
            } catch(Exception e) {
                Logger.getLogger(DynmapUtil.class.getName()).log(Level.WARNING, "Dynmap plugin not compatible",e);
            }
        }
    }
    
    private static JavaPlugin getRRPlugin() {
        return (JavaPlugin) ResourceRegions.getPlugin();
    }
    
    public static void clearMarkers() {
        if(!enabled) {
            return;
        }
        if(!init) {
            init();
        }
        if(init) {
            for(AreaMarker marker: markerSet.getAreaMarkers()) {
                marker.deleteMarker();
            }
        }
    }
    
    public static void createMarker(Region region) {
        if(!enabled) {
            return;
        }
        if(!init) {
            init();
        }
        if(init) {
            String newMarkerId = region.getName().toLowerCase()+".marker";
            for (AreaMarker marker : markerSet.getAreaMarkers())
            {
                if (marker.getMarkerID().equals(newMarkerId)) {
    DevUtil.log("Updating Dynmap AreaMarker for region: " + region.getName());
                    marker.setCornerLocations(toDoubleArray(region.getXPoints()),toDoubleArray(region.getZPoints()));
                    return;

                }
            }
    DevUtil.log("Adding Dynmap AreaMarker for region: " + region.getName());
            AreaMarker areaMarker = markerSet.createAreaMarker(newMarkerId, region.getName(), 
                                                               false, region.getWorldName(), 
                                                               toDoubleArray(region.getXPoints()),
                                                               toDoubleArray(region.getZPoints()), false);
            areaMarker.setFillStyle(areaOpacity, areaColor);
            areaMarker.setLineStyle(borderWidth, borderOpacity, borderColor);
            areaMarker.setDescription(region.getName()+": Weight: "+region.getWeight());
        }
    }
    
    private static double[] toDoubleArray(int[] array) {
        double[] result = new double[array.length];
        for(int i=0; i<array.length;i++) {
            result[i]=array[i];
        }
        return result;
    }


}
