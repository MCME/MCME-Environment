/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author Fraspace5
 */
public class Sound {

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

    /**
     * This method plays a custom sound packet
     *
     * @param s SoundType
     * @param pl Player
     * @param time The time of the region
     * @param loc Location
     * @param i For bell, it is the number of tocks
     */
    public static void playSound(SoundType s, Player pl, Long time, Location loc, int i) {
        switch (s) {
            case WIND:

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!PluginData.SoundPlayer.contains(pl.getUniqueId())) {
                            WindSound.WindSound(pl);
                        } else {
                            cancel();
                            PluginData.SoundPlayer.remove(pl.getUniqueId());
                        }

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);

                break;
            case CAVES:
                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!PluginData.SoundPlayer.contains(pl.getUniqueId())) {
                            CaveSound.CaveSound(pl);
                        } else {
                            cancel();
                            PluginData.SoundPlayer.remove(pl.getUniqueId());
                        }

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);

                break;
            case FOREST:

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!PluginData.SoundPlayer.contains(pl.getUniqueId())) {
                            ForestSound.ForestSound(pl, time);
                        } else {
                            cancel();
                            PluginData.SoundPlayer.remove(pl.getUniqueId());
                        }

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                break;
            case OCEAN:

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!PluginData.SoundPlayer.contains(pl.getUniqueId())) {
                            OceanSound.OceanSound(pl);
                        } else {
                            cancel();
                            PluginData.SoundPlayer.remove(pl.getUniqueId());
                        }

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);

                break;
            case PLAINS:

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!PluginData.SoundPlayer.contains(pl.getUniqueId())) {
                            PlainSound.PlainSound(pl, time);
                        } else {
                            cancel();
                            PluginData.SoundPlayer.remove(pl.getUniqueId());
                        }

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                break;
            case SWAMPLAND:

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!PluginData.SoundPlayer.contains(pl.getUniqueId())) {
                            SwamplandSound.SwampLandSound(pl, time);
                        } else {
                            cancel();
                            PluginData.SoundPlayer.remove(pl.getUniqueId());
                        }

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
                break;
            case BELL:

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!PluginData.SoundPlayer.contains(pl.getUniqueId())) {
                            BellSound.BellSound(pl, i, loc);
                        } else {
                            cancel();
                            PluginData.SoundPlayer.remove(pl.getUniqueId());
                        }

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 6000L);

                break;
            default:

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!PluginData.SoundPlayer.contains(pl.getUniqueId())) {
                            PlainSound.PlainSound(pl, time);
                        } else {
                            cancel();
                            PluginData.SoundPlayer.remove(pl.getUniqueId());
                        }

                    }

                }.runTaskTimer(Environment.getPluginInstance(), 30L, 6000L);
                break;
        }

    }

}
