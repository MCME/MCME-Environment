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
import com.mcme.environment.data.PluginData;
import com.mcmiddleearth.pluginutil.region.CuboidRegion;
import com.mcmiddleearth.pluginutil.region.PrismoidRegion;
import com.mcmiddleearth.pluginutil.region.Region;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
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
     * @param world
     */
    public static void spawnThunderstorm(Player pl, boolean bol, Region reg) {
        RandomCollection<Boolean> r = new RandomCollection<>();
        r.add(0.2, true);
        r.add(0.8, false);
        Boolean result1 = r.next();
        Location l = pl.getLocation();
        if (result1) {

            String w = reg.getWorld().getName();
            if (reg instanceof CuboidRegion) {
                l = randomLocCuboid(reg, w);
            } else if (reg instanceof PrismoidRegion) {
                l = randomLocPrismoid(reg, w);
            }
            PacketContainer thunder = Environment.getPluginInstance().manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
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
                es.printStackTrace();
            }
        }

        if (bol) {
            RandomCollection<Boolean> random = new RandomCollection<>();
            random.add(0.15, true);
            random.add(0.85, false);

            Boolean result = random.next();
            if (result) {
                pl.playSound(l, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.7F, 1.0F);
            }
        }

    }

    private static int randomReturn() {
        Random rnd = new Random();
        int result = (rnd.nextInt(0xFF) + 1) << 8 * 3;
        result += rnd.nextInt(0x1000000);

        return result;
    }

    /**
     * This method changes the player time with an animation
     *
     * @param pl Player
     * @param time Time in ticks
     */
    public static void changePlayerTime(Player pl, Long time) {
// 10 18
        Long playertime = pl.getPlayerTime();

        Double ptime = playertime.doubleValue() / 24000.0;
        long ptime1 = Math.round(ptime);
        double pp = ptime1 * 24000.0;
        ptime = pl.getPlayerTime() - pp;
        pl.setPlayerTime(ptime.longValue(), false);
        Long startTime = ptime.longValue();
        System.out.println("ptime " + ptime + " ptime long value " + ptime.longValue() + " pp " + pp + " time " + time);

        if (startTime <= time) {
            System.out.println("1 scelto ");

            new BukkitRunnable() {
                long effectiveTime = startTime;

                @Override
                public void run() {

                    if (effectiveTime >= time) {
                        cancel();
                        System.out.println("trigger0 " + (pl.getPlayerTime()) + " " + time);
                    }

                    effectiveTime += 50;
                    pl.setPlayerTime(effectiveTime, false);
                    System.out.println("vediamo il playertime " + effectiveTime);
                }
            }.runTaskTimer(Environment.getPluginInstance(), 0L, 2L);

        } else {
            System.out.println("2 scelto ");
            new BukkitRunnable() {
                long effectiveTime = startTime;

                @Override
                public void run() {
                    Long i = time - 60;
                    if (effectiveTime >= 23900 && effectiveTime <= 24000) {

                        pl.setPlayerTime(0L, false);
                        effectiveTime = 0L;
                        System.out.println("trigger1 " + (effectiveTime));
                    } else if (effectiveTime > time && effectiveTime < 23900) {
                        effectiveTime += 50;
                        pl.setPlayerTime(effectiveTime, false);
                        System.out.println("trigger2 " + (effectiveTime));
                    } else if (effectiveTime < i) {
                        effectiveTime += 50;
                        pl.setPlayerTime(effectiveTime, false);
                        System.out.println("trigger3 " + (effectiveTime));
                    } else if (effectiveTime >= i && effectiveTime <= time) {
                        pl.setPlayerTime(time, false);
                        cancel();
                        System.out.println("trigger4(cancel) " + (effectiveTime));
                    } else {
                        effectiveTime += 50;
                        pl.setPlayerTime(effectiveTime, false);
                    }

                }
            }.runTaskTimer(Environment.getPluginInstance(), 0L, 2L);
        }

    }

    public static void resetAll(Player pl) {
        pl.setPlayerWeather(WeatherType.CLEAR);
        pl.setPlayerTime(12000, false);
        PluginData.EntityPlayer.add(pl.getUniqueId());
        PluginData.SoundPlayer.add(pl.getUniqueId());
        for (UUID region : PluginData.informedRegion.keySet()) {
            if (PluginData.informedRegion.get(region).contains(pl.getUniqueId())) {
                PluginData.informedRegion.get(region).remove(pl.getUniqueId());
            }

        }

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

    public static Integer getMin(List<Integer> list) {

        Integer min = Integer.MAX_VALUE;

        for (Integer i : list) {

            if (min > i) {
                min = i;
            }
        }

        return min;
    }

    public static Integer getMax(List<Integer> list) {

        Integer max = Integer.MIN_VALUE;

        for (Integer i : list) {

            if (max < i) {
                max = i;
            }
        }

        return max;
    }

}
