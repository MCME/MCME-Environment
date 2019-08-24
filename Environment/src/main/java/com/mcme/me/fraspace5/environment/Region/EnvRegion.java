/*
 * ResourceRegions, a regions based texture-switcher
 * Copyright (c) 2014 dags <http://dags.me>
 *
 *   This program is free software: you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *   for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.mcme.me.fraspace5.environment.Region;



import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;



/**
 * @author Fraspace5
 */


public class EnvRegion
{

    @Getter 
    @Setter
    private String name;
    @Getter 
    @Setter
    private String worldName;
    @Getter 
    @Setter
    private String weather;
    @Getter 
    @Setter
    private int time;
    @Getter 
    @Setter
    private boolean thundering;
    @Getter 
    @Setter
    private Region region;

    public EnvRegion(String name, Region region){
        this.name = name;
        this.region = region;
    }

     public boolean contains(Location loc) {
//Logger.getGlobal().info(region.getWorld().getClass().getName() + " "+loc.getWorld().getClass().getName()
//                         + " "+region.getWorld().equals(loc.getWorld()));
        Vector vec = new Vector(loc.getX(),loc.getY(),loc.getZ());
//Logger.getGlobal().info("contains: "+vec.toString()+" "+region.contains(vec));
        return region.getWorld().getName().equals(loc.getWorld().getName()) 
                && region.contains(new Vector(loc.getX(),loc.getY(),loc.getZ()));
    }

    public Map<String, Object> saveToMap() {
        Map<String,Object> result = new HashMap<>();
        result.put("name", name);
        result.put("thundering", thundering);
        result.put("time", time);
        result.put("weather", weather);
        Map<String,Object> regionMap = new HashMap<>();
        regionMap.put("world", region.getWorld().getName());
        if(region instanceof CuboidRegion) {
            regionMap.put("type", "CuboidRegion");
            Vector vec = region.getMinimumPoint();
            regionMap.put("minimumPoint", vec.getBlockX()+","+vec.getBlockY()+","+vec.getBlockZ());
            vec = region.getMaximumPoint();
            regionMap.put("maximumPoint", vec.getBlockX()+","+vec.getBlockY()+","+vec.getBlockZ());
        } else if(region instanceof CylinderRegion) {
            regionMap.put("type", "CylinderRegion");
            Vector vec = ((CylinderRegion)region).getCenter();
            regionMap.put("center", vec.getBlockX()+","+vec.getBlockY()+","+vec.getBlockZ());
            regionMap.put("minY", region.getMinimumPoint().getBlockY());
            regionMap.put("maxY", region.getMaximumPoint().getBlockY());
            regionMap.put("radius", ((CylinderRegion)region).getRadius().getBlockX()+","
                                   +((CylinderRegion)region).getRadius().getBlockZ());
        } else if(region instanceof EllipsoidRegion) {
            regionMap.put("type", "EllipsoidRegion");
            Vector vec = ((EllipsoidRegion)region).getCenter();
            regionMap.put("center", vec.getBlockX()+","+vec.getBlockY()+","+vec.getBlockZ());
            vec = ((EllipsoidRegion)region).getRadius();
            regionMap.put("radius", vec.getBlockX()+","+vec.getBlockY()+","+vec.getBlockZ());
        } else if(region instanceof Polygonal2DRegion) {
            regionMap.put("type", "Polygonal2DRegion");
            regionMap.put("minY", region.getMinimumPoint().getBlockY());
            regionMap.put("maxY", region.getMaximumPoint().getBlockY());
            List<BlockVector2D> points = ((Polygonal2DRegion)region).getPoints();
            List<String> pointData = new ArrayList<>();
            for(BlockVector2D vec: points) {
                pointData.add(vec.getBlockX()+","+vec.getBlockZ());
            }
            regionMap.put("points", pointData);
        }
        result.put("region", regionMap);
        return result;
    }
    
    public static Region loadFromMap(Map<String,Object> data) {
        EnvRegion result;
        String name = (String) data.get("name");
        Map<String,Object> regionData = ((ConfigurationSection)data.get("region")).getValues(true);
        World world = Bukkit.getWorld((String) regionData.get("world"));
        if(world==null) {
            return null;
        }
        String type = (String) regionData.get("type");
        switch(type) {
            case "CuboidRegion":
                Vector minPoint = getVector((String) regionData.get("minimumPoint"));
                Vector maxPoint = getVector((String) regionData.get("maximumPoint"));
                result = new EnvRegion(name,new CuboidRegion(new BukkitWorld(world),minPoint,maxPoint));
                break;
            case "CylinderRegion":
                Vector center = getVector((String) regionData.get("center"));
                BlockVector2D blockRadius = getBlockVector2D((String)regionData.get("radius"));
                Vector2D rad = new Vector2D(blockRadius.getBlockX(),blockRadius.getBlockZ());
                int minY = (Integer) regionData.get("minY");
                int maxY = (Integer) regionData.get("maxY");
                result = new EnvRegion(name, new CylinderRegion(new BukkitWorld(world),
                                                               center.toBlockVector(),
                                                               rad,minY,maxY));
                break;
            case "EllipsoidRegion":
                center = getVector((String) regionData.get("center"));
                Vector radius3D = getVector((String)regionData.get("radius"));
                result = new EnvRegion(name, new EllipsoidRegion(new BukkitWorld(world),center,radius3D));
                break;
            case "Polygonal2DRegion":
                minY = (Integer) regionData.get("minY");
                maxY = (Integer) regionData.get("maxY");
                List<String> pointData = (List<String>) regionData.get("points");
                List<BlockVector2D> points = new ArrayList<>();
                for(String point: pointData) {
                    points.add(getBlockVector2D(point));
                }
                result = new EnvRegion(name, new Polygonal2DRegion(new BukkitWorld(world),points,minY,maxY));
                break;
            default:
                throw new UnsupportedOperationException("Not all region types are supported.");
        }
        
        result.setName((String) data.get("name"));
        result.setThundering((Boolean) data.get("thundering"));
        result.setTime((Integer) data.get("time"));
        result.setWeather((String) data.get("weather"));
        
        return result;
    }
    
    private static Vector getVector(String data) {
        String[] split = data.split(",");
        return new Vector(Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2]));
    }
    private static BlockVector2D getBlockVector2D(String data) {
        String[] split = data.split(",");
        return new BlockVector2D(Integer.parseInt(split[0]),Integer.parseInt(split[1]));
    }
    
    
    
    
    
}
