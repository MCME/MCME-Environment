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
import static java.lang.Integer.parseInt;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
                    if (PluginData.boolPlayers.get(pl.getUniqueId()) && Environment.isEngine()) {

                        for (Map.Entry<String, LocatedSoundData> entry : PluginData.locSounds.entrySet()) {

                            if (pl.getWorld().equals(entry.getValue().loc.getWorld())) {

                                if (pl.getLocation().distanceSquared(entry.getValue().loc) <= entry.getValue().sound.getDistanceTrigger()) {

                                    if (!PluginData.informedLocation.get(entry.getValue().id).contains(pl.getUniqueId())) {
                                        PluginData.informedLocation.get(entry.getValue().id).add(pl.getUniqueId());
                                        System.out.println(pl.getLocation().distanceSquared(entry.getValue().loc) + "  ||  " + entry.getValue().sound.getDistanceTrigger());

                                        int time = 12000;
                                        for (Map.Entry<String, RegionData> r : PluginData.AllRegions.entrySet()) {
                                            if (r.getValue().isInside(entry.getValue().loc)) {
                                                time = parseInt(r.getValue().time);
                                            }
                                        }

                                        SoundUtil.playSoundLocated(entry.getValue().sound, pl, time, entry.getValue().loc, entry.getValue().id);

                                    } 

                                } else if (PluginData.informedLocation.get(entry.getValue().id).contains(pl.getUniqueId())) {
                                    PluginData.informedLocation.get(entry.getValue().id).remove(pl.getUniqueId());

                                }

                            }

                        }

                    }

                }
            }

        }.runTaskTimer(Environment.getPluginInstance(), 100L, 175L);
    }
}
