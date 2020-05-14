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
package com.mcme.environment.runnable;

import com.mcme.environment.Environment;
import com.mcme.environment.SoundPacket.SoundUtil;
import com.mcme.environment.data.LocatedSoundData;
import com.mcme.environment.data.PluginData;
import com.mcme.environment.data.RegionData;
import com.mcme.environment.event.EnterRegionEvent;
import com.mcme.environment.event.LeaveRegionEvent;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class RunnablePlayer {

    public static void runnableLocationsPlayers() {
        new BukkitRunnable() {

            @Override
            public void run() {

                Bukkit.getOnlinePlayers().forEach((pl) -> {
                    if (PluginData.getBoolPlayers().get(pl.getUniqueId()) && Environment.isEngine()) {

                        for (Map.Entry<String, LocatedSoundData> entry : PluginData.getLocSounds().entrySet()) {

                            LocatedSoundData data = entry.getValue();

                            if (pl.getWorld().equals(data.getLoc().getWorld())) {

                                if (data.playerInRange(pl.getLocation())) {

                                    if (!data.isInformed(pl.getUniqueId())) {

                                        data.addInformed(pl.getUniqueId());

                                        int time = setTime(data.getLoc());

                                        SoundUtil.playSoundLocated(data.getSound(), pl, time, data.getLoc(), data.getId());

                                    }

                                } else if (data.isInformed(pl.getUniqueId())) {
                                    data.removeInformed(pl.getUniqueId());

                                }

                            }

                        }

                    }

                });
            }

        }.runTaskTimer(Environment.getPluginInstance(), 100L, 175L);
    }

    public static void runnableRegionsPlayers() {
        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach((pl) -> {
                    List<String> regions = new ArrayList<>();
                    List<String> totalRegions = new ArrayList<>();
                    List<String> informed = new ArrayList<>();
                    if (PluginData.getBoolPlayers().get(pl.getUniqueId()) && Environment.isEngine()) {
                        PluginData.getAllRegions().keySet().forEach((region) -> {
                            RegionData re = PluginData.getAllRegions().get(region);

                            if (re.getRegion().isInside(pl.getLocation())) {
                                totalRegions.add(region);
                            }

                            if (re.getRegion().isInside(pl.getLocation())
                                    && !re.getInformedLoc().contains(pl.getUniqueId())) {
                                regions.add(region);

                            } else if (re.getRegion().isInside(pl.getLocation())
                                    && re.getInformedLoc().contains(pl.getUniqueId())) {
                                informed.add(region);

                            }
                        });
                        if (totalRegions.isEmpty()) {
                            PluginData.getAllRegions().values().forEach((r) -> {

                                if (r.getInformedLoc().contains(pl.getUniqueId())) {

                                    LeaveRegionEvent event = new LeaveRegionEvent(pl, r.getName());
                                    Bukkit.getPluginManager().callEvent(event);
                                    r.cancelAllTasks(pl.getUniqueId());

                                }
                            });

                        }
                        if (!regions.isEmpty()) {
                            String weightMax = regions.get(0);
                            for (String re : regions) {
                                if (PluginData.getAllRegions().get(re).getWeight() > PluginData.getAllRegions().get(weightMax).getWeight()) {
                                    weightMax = re;
                                }
                            }
                            if (!informed.contains(weightMax)) {
                                String ll = "";
                                if (!informed.isEmpty()) {
                                    ll = informed.get(0);
                                    PluginData.getAllRegions().get(PluginData.getAllRegions().get(ll).getName()).cancelAllTasks(pl.getUniqueId());

                                }

                                PluginData.getAllRegions().values().forEach((b) -> {
                                    b.cancelAllTasks(pl.getUniqueId());
                                });

                                PluginData.getAllRegions().get(weightMax).addInform(pl.getUniqueId());

                                EnterRegionEvent event = new EnterRegionEvent(pl, weightMax);
                                Bukkit.getPluginManager().callEvent(event);
                            }
                        }
                    }
                });

            }

        }.runTaskTimer(Environment.getPluginInstance(), 200L, 75L);
    }

    private static int setTime(Location loc) {

        int time = 12000;
        for (Map.Entry<String, RegionData> r : PluginData.getAllRegions().entrySet()) {
            if (r.getValue().isInside(loc)) {
                time = parseInt(r.getValue().getTime());
            }
        }
        return time;

    }
}
