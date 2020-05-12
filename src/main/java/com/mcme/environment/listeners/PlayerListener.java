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
package com.mcme.environment.listeners;

import com.mcme.environment.Environment;
import com.mcme.environment.Util.EnvChange;
import com.mcme.environment.data.PluginData;
import com.mcme.environment.data.RegionData;
import com.mcme.environment.event.EnterRegionEvent;
import static java.lang.Long.parseLong;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.mcme.environment.SoundPacket.SoundUtil;
import com.mcme.environment.SoundPacket.SoundType;
import com.mcme.environment.data.LocatedSoundData;
import com.mcme.environment.event.LeaveRegionEvent;
import static java.lang.Integer.parseInt;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Fraspace5
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (Environment.nameserver.equalsIgnoreCase("default")) {
                    Environment.getPluginInstance().sendNameServer(e.getPlayer());
                }
            }

        }.runTaskLater(Environment.getPluginInstance(), 150L);

        System.out.println("Env " + Environment.getNameserver());

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    String statement = "SELECT * FROM " + Environment.getPluginInstance().database + ".environment_players WHERE uuid = '" + e.getPlayer().getUniqueId().toString() + "' ;";

                    final ResultSet r = Environment.getPluginInstance().con.prepareStatement(statement).executeQuery();

                    if (r.first()) {

                        PluginData.boolPlayers.put(e.getPlayer().getUniqueId(), r.getBoolean("bool"));

                    } else {

                        String stat = "INSERT INTO " + Environment.getPluginInstance().database + ".environment_players (bool, uuid) VALUES (1,'" + e.getPlayer().getUniqueId().toString() + "') ; ";
                        Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                        PluginData.boolPlayers.put(e.getPlayer().getUniqueId(), Boolean.TRUE);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskAsynchronously(Environment.getPluginInstance());

    }

    @EventHandler

    public void onQuit(PlayerQuitEvent e) {

        if (PluginData.boolPlayers.containsKey(e.getPlayer().getUniqueId())) {

            if (PluginData.boolPlayers.get(e.getPlayer().getUniqueId())) {
                new BukkitRunnable() {

                    @Override
                    public void run() {

                        String stat = "UPDATE " + Environment.getPluginInstance().database + ".environment_players SET bool = '1' WHERE uuid = '" + e.getPlayer().getUniqueId().toString() + "' ;";
                        try {
                            Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                            PluginData.boolPlayers.remove(e.getPlayer().getUniqueId());
                        } catch (SQLException ex) {
                            Logger.getLogger(PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }.runTaskAsynchronously(Environment.getPluginInstance());
            } else {
                new BukkitRunnable() {

                    @Override
                    public void run() {

                        String stat = "UPDATE " + Environment.getPluginInstance().database + ".environment_players SET bool = '0' WHERE uuid = '" + e.getPlayer().getUniqueId().toString() + "' ;";
                        try {
                            Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                            PluginData.boolPlayers.remove(e.getPlayer().getUniqueId());
                        } catch (SQLException ex) {
                            Logger.getLogger(PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }.runTaskAsynchronously(Environment.getPluginInstance());
            }

        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        List<String> regions = new ArrayList<>();
        List<String> totalRegions = new ArrayList<>();
        List<String> informed = new ArrayList<>();

        if (PluginData.boolPlayers.get(e.getPlayer().getUniqueId()) && Environment.isEngine()) {

            for (String region : PluginData.AllRegions.keySet()) {

                RegionData re = PluginData.AllRegions.get(region);

                if (re.region.isInside(e.getPlayer().getLocation())) {
                    totalRegions.add(region);
                }

                if (re.region.isInside(e.getPlayer().getLocation())
                        && !PluginData.informedRegion.get(re.idr).contains(e.getPlayer().getUniqueId())) {
                    regions.add(region);

                } else if (re.region.isInside(e.getPlayer().getLocation())
                        && PluginData.informedRegion.get(re.idr).contains(e.getPlayer().getUniqueId())) {
                    informed.add(region);

                }

            }

            if (totalRegions.isEmpty()) {
                for (Entry<UUID, List<UUID>> r : PluginData.informedRegion.entrySet()) {

                    if (r.getValue().contains(e.getPlayer().getUniqueId())) {

                        LeaveRegionEvent event = new LeaveRegionEvent(e.getPlayer(), PluginData.getNameFromUUID(r.getKey()));
                        Bukkit.getPluginManager().callEvent(event);
                        r.getValue().remove(e.getPlayer().getUniqueId());

                    }

                }

            }

            if (!regions.isEmpty()) {
                String weightMax = regions.get(0);

                for (String re : regions) {
                    if (PluginData.AllRegions.get(re).weight > PluginData.AllRegions.get(weightMax).weight) {
                        weightMax = re;
                    }
                }

                if (!informed.contains(weightMax)) {
                    String ll = "";
                    if (!informed.isEmpty()) {
                        ll = informed.get(0);
                        PluginData.informedRegion.get(PluginData.AllRegions.get(ll).idr).remove(e.getPlayer().getUniqueId());
                    }
                    if (PluginData.PlayersRunnable.containsKey(e.getPlayer().getUniqueId())) {
                        for (BukkitTask b : PluginData.PlayersRunnable.get(e.getPlayer().getUniqueId())) {
                            b.cancel();
                        }
                    }
                    if (!PluginData.informedRegion.get(PluginData.AllRegions.get(weightMax).idr).contains(e.getPlayer().getUniqueId())) {
                        PluginData.informedRegion.get(PluginData.AllRegions.get(weightMax).idr).add(e.getPlayer().getUniqueId());
                    }

                    EnterRegionEvent event = new EnterRegionEvent(e.getPlayer(), weightMax);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
        }

    }

    @EventHandler
    public void onEnterRegion(EnterRegionEvent e) {

        RegionData re = PluginData.AllRegions.get(e.getNameRegion());

        if (re.weather.equalsIgnoreCase("rain")) {
            e.getPlayer().setPlayerWeather(WeatherType.DOWNFALL);

        } else if (re.weather.equalsIgnoreCase("sun")) {
            e.getPlayer().setPlayerWeather(WeatherType.CLEAR);
        }

        if (re.thunder) {

            BukkitTask bRunnable = new BukkitRunnable() {

                @Override
                public void run() {

                    EnvChange.spawnThunderstorm(e.getPlayer(), true, re.region);

                }

            }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);

            PluginData.addBukkitTask(e.getPlayer(), bRunnable);

        }

        if (!re.time.equalsIgnoreCase("default")) {
            e.getPlayer().setPlayerTime(parseLong(re.time), false);
        }

        if (!re.soundAmbient.equals(SoundType.NONE)) {

            SoundUtil.playSoundAmbient(re.soundAmbient, e.getPlayer(), parseLong(re.time), re.region, re);

        }

        if (!re.locData.leaves.isEmpty()) {
            SoundUtil.playSoundAmbient(SoundType.LEAVES, e.getPlayer(), parseLong(re.time), re.region, re);
        }

    }

    @EventHandler
    public void onLeaveRegion(LeaveRegionEvent e) {

        EnvChange.resetAll(e.getPlayer());

    }

    @EventHandler
    public void onMoveLocation(PlayerMoveEvent e) {

        if (PluginData.boolPlayers.get(e.getPlayer().getUniqueId()) && Environment.isEngine()) {
            Player pl = e.getPlayer();

            for (Entry<String, LocatedSoundData> entry : PluginData.locSounds.entrySet()) {

                if (pl.getWorld().equals(entry.getValue().loc.getWorld())) {
                    System.out.println("world is good");
                }

                if (pl.getLocation().distanceSquared(entry.getValue().loc) <= entry.getValue().sound.getDistanceTrigger()) {

                    System.out.println(pl.getLocation().distanceSquared(entry.getValue().loc) + "  ||  " + entry.getValue().sound.getDistanceTrigger());

                    if (!PluginData.informedLocation.get(entry.getValue().id).contains(pl.getUniqueId())) {
                        int time = 12000;
                        for (Entry<String, RegionData> r : PluginData.AllRegions.entrySet()) {
                            if (r.getValue().isInside(entry.getValue().loc)) {
                                time = parseInt(r.getValue().time);
                            }
                        }

                        SoundUtil.playSoundLocated(entry.getValue().sound, pl, time, entry.getValue().loc);
                        PluginData.informedLocation.get(entry.getValue().id).add(pl.getUniqueId());
                    }

                } else {

                    if (PluginData.informedLocation.get(entry.getValue().id).contains(pl.getUniqueId())) {

                        PluginData.informedLocation.get(entry.getValue().id).remove(pl.getUniqueId());

                        for (BukkitTask s : PluginData.PlayersRunnableLocation.get(pl.getUniqueId())) {
                            s.cancel();
                        }

                    }

                }

            }

        }

    }

}

/*
     new BukkitRunnable() {

                @Override
                public void run() {

                    if (!PluginData.SoundPlayer.contains(e.getPlayer().getUniqueId())) {
                       Sound.playSound(re.sound, e.getPlayer(), parseLong(re.time));
                    } else {
                        cancel();
                        PluginData.SoundPlayer.remove(e.getPlayer().getUniqueId());
                    }

                }

            }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
    
 */
