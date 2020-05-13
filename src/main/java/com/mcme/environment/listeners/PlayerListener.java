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

                        PluginData.getBoolPlayers().put(e.getPlayer().getUniqueId(), r.getBoolean("bool"));

                    } else {

                        String stat = "INSERT INTO " + Environment.getPluginInstance().database + ".environment_players (bool, uuid) VALUES (1,'" + e.getPlayer().getUniqueId().toString() + "') ; ";
                        Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                        PluginData.getBoolPlayers().put(e.getPlayer().getUniqueId(), Boolean.TRUE);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskAsynchronously(Environment.getPluginInstance());

    }

    @EventHandler

    public void onQuit(PlayerQuitEvent e) {

        if (PluginData.getBoolPlayers().containsKey(e.getPlayer().getUniqueId())) {

            if (PluginData.getBoolPlayers().get(e.getPlayer().getUniqueId())) {
                new BukkitRunnable() {

                    @Override
                    public void run() {

                        String stat = "UPDATE " + Environment.getPluginInstance().database + ".environment_players SET bool = '1' WHERE uuid = '" + e.getPlayer().getUniqueId().toString() + "' ;";
                        try {
                            Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                            PluginData.getBoolPlayers().remove(e.getPlayer().getUniqueId());
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
                            PluginData.getBoolPlayers().remove(e.getPlayer().getUniqueId());
                        } catch (SQLException ex) {
                            Logger.getLogger(PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }.runTaskAsynchronously(Environment.getPluginInstance());
            }

        }

    }

    @EventHandler
    public void onEnterRegion(EnterRegionEvent e) {

        RegionData re = PluginData.getAllRegions().get(e.getNameRegion());

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
            if (re.time != "default") {
                SoundUtil.playSoundAmbient(re.soundAmbient, e.getPlayer(), parseLong(re.time), re.region, re);
            }
        }

        if (!re.locData.leaves.isEmpty()) {
            if (re.time != "default") {
                SoundUtil.playSoundAmbient(SoundType.LEAVES, e.getPlayer(), parseLong(re.time), re.region, re);

            }
        }
    }

    @EventHandler
    public void onLeaveRegion(LeaveRegionEvent e) {

        EnvChange.resetAll(e.getPlayer());

    }

}
