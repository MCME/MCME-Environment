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
package com.mcme.environment.Util;

import com.mcme.environment.Environment;
import com.mcme.environment.data.PluginData;
import com.mcme.environment.data.RegionData;
import com.sk89q.worldedit.regions.Region;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class RegionScanner {

    public static void getChunkSnaphshot(Region region, World b, String name, RegionData redata) {
        new BukkitRunnable() {

            @Override
            public void run() {
                List<ChunkSnapshot> chunklist = new ArrayList<>();

                region.getChunks().forEach(chunk -> chunklist.add(b.getChunkAt(chunk.getX(), chunk.getZ()).getChunkSnapshot()));

                List<Location> waterList = new ArrayList<>();
                List<Location> leavesList = new ArrayList<>();

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        chunklist.forEach(chunk -> {
                            List<Location> waterListAll = new ArrayList<>();
                            List<Location> leavesListAll = new ArrayList<>();

                            for (int i = 0; i <= 16; i++) {
                                for (int j = 0; j <= 16; j++) {
                                    for (int k = 255; k > 0; k--) {
                                        BlockData data = chunk.getBlockData(i, k, j);

                                        if (!data.getMaterial().isAir()) {
                                            if (data.getMaterial().equals(Material.WATER)) {
                                                waterListAll.add(new Location(b, (double) chunk.getX() * 16 + i, (double) k, (double) chunk.getZ() * 16 + j));
                                            } else if (data.getMaterial().equals(Material.ACACIA_LEAVES)
                                                    || data.getMaterial().equals(Material.BIRCH_LEAVES)
                                                    || data.getMaterial().equals(Material.DARK_OAK_LEAVES)
                                                    || data.getMaterial().equals(Material.JUNGLE_LEAVES)
                                                    || data.getMaterial().equals(Material.SPRUCE_LEAVES)
                                                    || data.getMaterial().equals(Material.OAK_LEAVES)) {

                                                leavesListAll.add(new Location(b, (double) chunk.getX() * 16 + i, (double) k, (double) chunk.getZ() * 16 + j));
                                            }

                                        }

                                    }
                                }
                            }

                            Location center = new Location(b, chunk.getX() * 16 + 8, 64, chunk.getZ() * 16 + 8);

                            if (!waterListAll.isEmpty()) {

                                Location loc = waterListAll.get(0);

                                for (Location l : waterListAll) {
                                    if (l.distance(center) < loc.distance(center)) {
                                        loc = l;
                                    }
                                }
                                waterList.add(loc);

                            }

                            if (!leavesListAll.isEmpty()) {

                                Location loc = leavesListAll.get(0);

                                for (Location l : leavesListAll) {
                                    if (l.distance(center) < loc.distance(center)) {
                                        loc = l;
                                    }
                                }

                                leavesList.add(loc);

                            }

                        });
                        if (!leavesList.isEmpty()) {
                            redata.locData.leaves = leavesList;
                        }
                        if (!waterList.isEmpty()) {
                            redata.locData.water = waterList;
                        }
                        System.out.println(waterList.size() + " | " + leavesList.size());

                        try {
                            PluginData.onSave(Environment.getPluginInstance().getEnvFolder());
                        } catch (IOException ex) {
                            Logger.getLogger(RegionScanner.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }.runTaskAsynchronously(Environment.getPluginInstance());

            }
        }.runTaskLater(Environment.getPluginInstance(), 80L);

    }

}
