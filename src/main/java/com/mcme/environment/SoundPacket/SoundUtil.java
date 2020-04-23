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
import com.mcme.environment.data.PluginData;
import java.util.Random;
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

// NW no water 
    public static Location getRandomLocationNW(int minX, int maxX, int minZ, int maxZ, World world, int y) {
        Random rand = new Random();
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
// only water 

    public static Location getRandomLocationYW(int minX, int maxX, int minZ, int maxZ, World world, int y) {
        Random rand = new Random();
        Random r = new Random();
        int x = r.nextInt(maxX - minX + 1) + minX;
        int z = r.nextInt(maxZ - minZ + 1) + minZ;
        Location rLoc = new Location(world, x, y, z);
        Block bl = world.getBlockAt(rLoc);

        while (bl.getType() != Material.WATER) {
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

    public static Boolean isOutdoor(Location l) {
        int y = l.getBlockY();
        int worldMaximumY = l.getWorld().getHighestBlockYAt(l);

        if (y < worldMaximumY) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * This method plays a custom sound packet
     *
     * @param s SoundType
     * @param pl Player
     * @param time The time of the region
     *
     */
    public static void playSoundAmbient(SoundType s, Player pl, Long time) {
        switch (s) {
            case WIND:

                BukkitTask bRunnable = new BukkitRunnable() {

                    @Override
                    public void run() {

                        WindSound.WindSound(pl);

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 100L);
                PluginData.addBukkitTask(pl, bRunnable);
                break;
            case CAVE:
                BukkitTask bRunnable1 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        CaveSound.CaveSound(pl);

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 40L);
                PluginData.addBukkitTask(pl, bRunnable1);
                break;
            case FOREST:

                BukkitTask bRunnable2 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        ForestSound.ForestSound(pl, time);

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                PluginData.addBukkitTask(pl, bRunnable2);
                break;
            case OCEAN:

                BukkitTask bRunnable3 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        OceanSound.OceanSound(pl);

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 40L);
                PluginData.addBukkitTask(pl, bRunnable3);
                break;
            case PLAIN:

                BukkitTask bRunnable4 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        PlainSound.PlainSound(pl, time);

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                PluginData.addBukkitTask(pl, bRunnable4);
                break;
            case SWAMPLAND:

                BukkitTask bRunnable5 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        SwamplandSound.SwampLandSound(pl, time);

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                PluginData.addBukkitTask(pl, bRunnable5);
                break;
            default:

                BukkitTask bRunnable7 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        PlainSound.PlainSound(pl, time);

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                PluginData.addBukkitTask(pl, bRunnable7);
                break;
        }

    }

    public static void playSoundLocated(SoundType s, Player pl, Long time, Location loc, int i) {
        switch (s) {

            case BELL:

                BukkitTask bRunnable6 = new BukkitRunnable() {

                    @Override
                    public void run() {

                        BellSound.BellSound(pl, i, loc);

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 6000L);
                PluginData.addBukkitTask(pl, bRunnable6);
                break;

        }

    }

}
