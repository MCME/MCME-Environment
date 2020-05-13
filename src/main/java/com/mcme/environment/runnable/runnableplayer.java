/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Fraspace5
 */
public class runnableplayer {

    public static void runnableLocationsPlayers() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (PluginData.getBoolPlayers().get(pl.getUniqueId()) && Environment.isEngine()) {

                        for (Map.Entry<String, LocatedSoundData> entry : PluginData.getLocSounds().entrySet()) {

                            LocatedSoundData data = entry.getValue();

                            if (pl.getWorld().equals(data.getLoc().getWorld())) {

                                if (data.playerInRange(pl.getLocation())) {

                                    if (!data.isInformed(pl.getUniqueId())) {
                                        System.out.println(pl.getLocation().distanceSquared(data.getLoc()) + "  ||  " + data.getSound().getDistanceTrigger());

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

                }
            }

        }.runTaskTimer(Environment.getPluginInstance(), 100L, 175L);
    }

    public static void runnableRegionsPlayers() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player pl : Bukkit.getOnlinePlayers()) {

                    List<String> regions = new ArrayList<>();
                    List<String> totalRegions = new ArrayList<>();
                    List<String> informed = new ArrayList<>();

                    if (PluginData.getBoolPlayers().get(pl.getUniqueId()) && Environment.isEngine()) {

                        for (String region : PluginData.getAllRegions().keySet()) {

                            RegionData re = PluginData.getAllRegions().get(region);

                            if (re.region.isInside(pl.getLocation())) {
                                totalRegions.add(region);
                            }

                            if (re.region.isInside(pl.getLocation())
                                    && !PluginData.getInformedRegion().get(re.idr).contains(pl.getUniqueId())) {
                                regions.add(region);

                            } else if (re.region.isInside(pl.getLocation())
                                    && PluginData.getInformedRegion().get(re.idr).contains(pl.getUniqueId())) {
                                informed.add(region);

                            }

                        }

                        if (totalRegions.isEmpty()) {
                            for (Map.Entry<UUID, List<UUID>> r : PluginData.getInformedRegion().entrySet()) {

                                if (r.getValue().contains(pl.getUniqueId())) {

                                    LeaveRegionEvent event = new LeaveRegionEvent(pl, PluginData.getNameFromUUID(r.getKey()));
                                    Bukkit.getPluginManager().callEvent(event);
                                    r.getValue().remove(pl.getUniqueId());

                                }

                            }

                        }

                        if (!regions.isEmpty()) {
                            String weightMax = regions.get(0);

                            for (String re : regions) {
                                if (PluginData.getAllRegions().get(re).weight > PluginData.getAllRegions().get(weightMax).weight) {
                                    weightMax = re;
                                }
                            }

                            if (!informed.contains(weightMax)) {
                                String ll = "";
                                if (!informed.isEmpty()) {
                                    ll = informed.get(0);
                                    PluginData.getInformedRegion().get(PluginData.getAllRegions().get(ll).idr).remove(pl.getUniqueId());
                                }
                                if (PluginData.getPlayersRunnable().containsKey(pl.getUniqueId())) {
                                    for (BukkitTask b : PluginData.getPlayersRunnable().get(pl.getUniqueId())) {
                                        b.cancel();
                                    }
                                }
                                if (!PluginData.getInformedRegion().get(PluginData.getAllRegions().get(weightMax).idr).contains(pl.getUniqueId())) {
                                    PluginData.getInformedRegion().get(PluginData.getAllRegions().get(weightMax).idr).add(pl.getUniqueId());
                                }

                                EnterRegionEvent event = new EnterRegionEvent(pl, weightMax);
                                Bukkit.getPluginManager().callEvent(event);
                            }
                        }
                    }
                }

            }

        }.runTaskTimer(Environment.getPluginInstance(), 200L, 100L);
    }

    private static int setTime(Location loc) {

        int time = 12000;
        for (Map.Entry<String, RegionData> r : PluginData.getAllRegions().entrySet()) {
            if (r.getValue().isInside(loc)) {
                time = parseInt(r.getValue().time);
            }
        }
        return time;

    }
}
