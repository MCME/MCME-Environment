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

import com.mcme.environment.Environment;
import com.mcme.environment.Util.RandomCollection;
import com.mcme.environment.data.RegionData;
import com.mcmiddleearth.pluginutil.region.Region;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Fraspace5
 */
public class SoundUtil {

    public static Location getRandomLocationNW(int minX, int maxX, int minZ, int maxZ, World world, int y) {
        Random r = new Random();
        int x = r.nextInt(maxX - minX + 1) + minX;
        int z = r.nextInt(maxZ - minZ + 1) + minZ;
        Location rLoc = new Location(world, x, y, z);
        Block bl = world.getBlockAt(rLoc);

        while (bl.getType() == Material.WATER) {
            int x2 = r.nextInt(maxX - minX + 1) + minX;
            int z2 = r.nextInt(maxZ - minZ + 1) + minZ;
            rLoc = new Location(world, x2, y, z2);

        }

        return rLoc;
    }

    public static String getTimeString(Long time) {
        if (time >= 0 && time <= 7000) {
            return "morning";
        } else if (time > 7000 && time <= 12500) {
            return "day";
        } else if (time > 12500) {
            return "night";
        } else {
            return "day";
        }
    }

    /**
     * It returns a boolean with the given possibility
     *
     * @param tr True percentage
     * @param fl False percentage
     * @return
     */
    public static Boolean randomBoolean(Double tr, Double fl) {
        RandomCollection<Boolean> random = new RandomCollection<>();
        random.add(tr, true);
        random.add(fl, false);

        return random.next();

    }

    public static Boolean isOutdoor(Location l) {

        int y = l.getBlockY();
        int worldMaximumY = l.getWorld().getHighestBlockYAt(l);

        return y >= worldMaximumY;

    }

    /**
     * This method plays a custom sound packet
     *
     * @param s SoundType
     * @param pl Player
     * @param time The time of the region
     * @param r RegionData
     * @param re Region
     */
    public static void playSoundAmbient(SoundType s, Player pl, Long time, Region re, RegionData r) {
        UUID uuid = pl.getUniqueId();
        switch (s) {
            case WIND:

                BukkitTask bRunnable = new BukkitRunnable() {

                    @Override
                    public void run() {

                        WindSound.WindSound(pl);
                        if (!r.locData.getLeaves().isEmpty()) {
                            LeavesSound.LeavesSound(pl, r);
                        }
                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 100L);

                r.addInfoTask(uuid, bRunnable);
                break;
            case CAVE:
                BukkitTask bRunnable1 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        CaveSound.CaveSound(pl);
                        if (!r.locData.getLeaves().isEmpty()) {
                            LeavesSound.LeavesSound(pl, r);
                        }
                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 40L);
                r.addInfoTask(uuid, bRunnable1);
                break;
            case FOREST:

                BukkitTask bRunnable2 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        ForestSound.ForestSound(pl, time);
                        if (!r.locData.getLeaves().isEmpty()) {
                            LeavesSound.LeavesSound(pl, r);
                        }
                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                r.addInfoTask(uuid, bRunnable2);
                break;
            case OCEAN:

                BukkitTask bRunnable3 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        OceanSound.OceanSound(pl, r);
                        if (!r.locData.getLeaves().isEmpty()) {
                            LeavesSound.LeavesSound(pl, r);
                        }
                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 40L);
                r.addInfoTask(uuid, bRunnable3);
                break;
            case PLAIN:

                BukkitTask bRunnable4 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        PlainSound.PlainSound(pl, time);
                        System.out.println("doverbbe andarea ora");
                        if (!r.locData.getLeaves().isEmpty()) {
                            LeavesSound.LeavesSound(pl, r);
                        }
                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                r.addInfoTask(uuid, bRunnable4);
                break;
            case SWAMPLAND:

                BukkitTask bRunnable5 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        SwamplandSound.SwampLandSound(pl, time);
                        if (!r.locData.getLeaves().isEmpty()) {
                            LeavesSound.LeavesSound(pl, r);
                        }
                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                r.addInfoTask(uuid, bRunnable5);
                break;

            default:

                BukkitTask bRunnable6 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        PlainSound.PlainSound(pl, time);
                        if (!r.locData.getLeaves().isEmpty()) {
                            LeavesSound.LeavesSound(pl, r);
                        }
                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                r.addInfoTask(uuid, bRunnable6);
                break;
        }

    }

    public static void playSoundLocated(SoundType s, Player pl, Integer time, Location loc, UUID id) {
        switch (s) {

            case BELL:

                BellSound.BellSound(pl, time, loc, id);

                break;

        }

    }

}
