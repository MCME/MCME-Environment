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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.mcme.environment.SoundPacket.Sound;
import org.bukkit.scheduler.BukkitRunnable;

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

        String s = "";
        List<String> regions = new ArrayList<>();

        if (PluginData.boolPlayers.get(e.getPlayer().getUniqueId())) {

            for (String region : PluginData.AllRegions.keySet()) {

                RegionData re = PluginData.AllRegions.get(region);

                if (re.region.isInside(e.getPlayer().getLocation())
                        && !PluginData.informedRegion.get(re.idr).contains(e.getPlayer().getUniqueId())) {
                    regions.add(region);
                }

            }

        }
        if (regions.isEmpty()) {

        }

        if (!regions.isEmpty()) {
            String weightMax = regions.get(0);

            for (String re : regions) {
                if (PluginData.AllRegions.get(re).weight > PluginData.AllRegions.get(weightMax).weight) {
                    weightMax = re;
                }
            }

            for (String r : PluginData.AllRegions.keySet()) {
                if (PluginData.informedRegion.get(PluginData.AllRegions.get(r).idr).contains(e.getPlayer().getUniqueId()) && !r.equals(weightMax)) {
                    PluginData.informedRegion.get(PluginData.AllRegions.get(r).idr).remove(e.getPlayer().getUniqueId());
                    s = r;
                }
            }

            if (PluginData.AllRegions.containsKey(s)) {
                if (!PluginData.AllRegions.get(weightMax).thunder && PluginData.AllRegions.get(s).thunder) {
                    PluginData.EntityPlayer.add(e.getPlayer().getUniqueId());

                }
            }
            if (PluginData.AllRegions.containsKey(s)) {
                if (!PluginData.AllRegions.get(weightMax).sound.equalsIgnoreCase("none") && !PluginData.AllRegions.get(s).sound.equalsIgnoreCase(PluginData.AllRegions.get(weightMax).sound)) {
                    PluginData.SoundPlayer.add(e.getPlayer().getUniqueId());

                }
            }

            if (!PluginData.informedRegion.get(PluginData.AllRegions.get(weightMax).idr).contains(e.getPlayer().getUniqueId())) {
                PluginData.informedRegion.get(PluginData.AllRegions.get(weightMax).idr).add(e.getPlayer().getUniqueId());
            }

            EnterRegionEvent event = new EnterRegionEvent(e.getPlayer(), weightMax);
            Bukkit.getPluginManager().callEvent(event);

        }

    }

    @EventHandler
    public void onEnterRegion(EnterRegionEvent e) {

        RegionData re = PluginData.AllRegions.get(e.getNameRegion());
        System.out.println("evento triggered");
        if (re.weather.equalsIgnoreCase("rain")) {
            e.getPlayer().setPlayerWeather(WeatherType.DOWNFALL);

        } else {
            e.getPlayer().setPlayerWeather(WeatherType.CLEAR);
        }

        if (re.thunder) {
            new BukkitRunnable() {

                @Override
                public void run() {

                    if (!PluginData.EntityPlayer.contains(e.getPlayer().getUniqueId())) {
                        EnvChange.spawnThunderstorm(e.getPlayer(), true, re.region);
                    } else {
                        cancel();
                        PluginData.EntityPlayer.remove(e.getPlayer().getUniqueId());
                    }

                }

            }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);

        }

        EnvChange.changePlayerTime(e.getPlayer(), parseLong(re.time));

        if (!re.sound.equalsIgnoreCase("none")) {
            new BukkitRunnable() {

                @Override
                public void run() {

                    if (!PluginData.SoundPlayer.contains(e.getPlayer().getUniqueId())) {
                        Sound.BirdSound(e.getPlayer());
                    } else {
                        cancel();
                        PluginData.SoundPlayer.remove(e.getPlayer().getUniqueId());
                    }

                }

            }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);
        }

    }

}
