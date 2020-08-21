/*
 *Copyright (C) 2020 MCME (Fraspace5)
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
package com.mcme.environment.Util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.mcme.environment.Environment;
import com.mcme.environment.SoundPacket.SoundUtil;
import com.mcme.environment.data.PluginData;
import com.mcmiddleearth.pluginutil.region.CuboidRegion;
import com.mcmiddleearth.pluginutil.region.PrismoidRegion;
import com.mcmiddleearth.pluginutil.region.Region;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 *
 * @author Fraspace5
 */
public class EnvChange {

    /**
     * It spawn a thunderstorm 20% thunder 15% sound
     *
     * @param pl Player
     * @param bol Sounds on
     * @param reg
     *
     */
    public static void spawnThunderstorm(Player pl, boolean bol, Region reg) {
        
        Location l = pl.getLocation();
        if (SoundUtil.randomBoolean(0.2, 0.8)) {
            
            String w = reg.getWorld().getName();
            
            if (reg instanceof CuboidRegion) {
                l = randomLocCuboid(reg, w);
            } else {
                l = randomLocPrismoid(reg, w);
            }
            
            PacketContainer thunder = Environment.getPluginInstance().getManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
            thunder.getIntegers().
                    write(0, randomReturn()).
                    write(1, 1);
            thunder.getDoubles().
                    write(0, l.getX()).
                    write(1, l.getY()).
                    write(2, l.getZ());
            
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(pl, thunder);
            } catch (InvocationTargetException es) {
            }
        }
        
        if (bol && SoundUtil.randomBoolean(0.15, 0.85)) {
            
            pl.playSound(l, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.7F, 1.0F);
            
        }
        
    }
    
    private static int randomReturn() {
        Random rnd = new Random();
        int result = (rnd.nextInt(0xFF) + 1) << 8 * 3;
        result += rnd.nextInt(0x1000000);
        
        return result;
    }
    
    public static void resetAll(Player pl) {
        if (pl.getPlayerWeather() != WeatherType.CLEAR) {
            pl.setPlayerWeather(WeatherType.CLEAR);
            PluginData.getBossbar().removePlayer(pl);
        }
        
        pl.resetPlayerTime();
        
        PluginData.getAllRegions().values().forEach((s) -> {
            s.cancelAllTasks(pl.getUniqueId());
        });
        
    }
    
    private static Location randomLocCuboid(Region r, String world) {
        if (r instanceof CuboidRegion) {
            Vector min = ((CuboidRegion) r).getMinCorner();
            Vector max = ((CuboidRegion) r).getMaxCorner();
            
            Location range = new Location(Bukkit.getWorld(world), Math.abs(max.getX() - min.getX()), min.getY(), Math.abs(max.getZ() - min.getZ()));
            return new Location(Bukkit.getWorld(world), (Math.random() * range.getX()) + (min.getX() <= max.getX() ? min.getX() : max.getX()), range.getY(), (Math.random() * range.getZ()) + (min.getZ() <= max.getZ() ? min.getZ() : max.getZ()));
            
        } else {
            return null;
        }
        
    }
    
    private static Location randomLocPrismoid(Region r, String world) {
        if (r instanceof PrismoidRegion) {
            List<Integer> X = Arrays.asList(((PrismoidRegion) r).getXPoints());
            List<Integer> Z = Arrays.asList(((PrismoidRegion) r).getZPoints());
            
            Integer minX = getMin(X);
            Integer minZ = getMin(Z);
            Integer maxZ = getMax(Z);
            Integer maxX = getMax(X);
            Location range = new Location(Bukkit.getWorld(world), Math.abs(maxX - minX), ((PrismoidRegion) r).getMinY(), Math.abs(maxZ - minZ));
            Location l = new Location(Bukkit.getWorld(world), (Math.random() * range.getX()) + (minX <= maxX ? minX : maxX), range.getY(), (Math.random() * range.getZ()) + (minZ <= maxZ ? minZ : maxZ));
            do {
                
                l = new Location(Bukkit.getWorld(world), (Math.random() * range.getX()) + (minX <= maxX ? minX : maxX), range.getY(), (Math.random() * range.getZ()) + (minZ <= maxZ ? minZ : maxZ));
                
            } while (r.isInside(l));
            
            return l;
            
        } else {
            return null;
        }
        
    }
    
    private static Integer getMin(List<Integer> list) {
        
        Integer min = Integer.MAX_VALUE;
        
        for (Integer i : list) {
            
            if (min > i) {
                min = i;
            }
        }
        
        return min;
    }
    
    private static Integer getMax(List<Integer> list) {
        
        Integer max = Integer.MIN_VALUE;
        
        for (Integer i : list) {
            
            if (max < i) {
                max = i;
            }
        }
        
        return max;
    }
    
}
