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
import com.mcme.environment.Util.UpdateTimePacketUtil;
import com.mcme.environment.commands.PTimeCommand;
import com.mcme.environment.data.PluginData;
import com.mcme.environment.data.RegionData;
import com.mcme.environment.event.EnterRegionEvent;
import static java.lang.Long.parseLong;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.mcme.environment.SoundPacket.SoundUtil;
import com.mcme.environment.SoundPacket.SoundType;
import com.mcme.environment.event.LeaveRegionEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Fraspace5
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setPlayerTime(6000, false);
        UpdateTimePacketUtil.sendTime(p, p.getPlayerTime(), true);
        /*new BukkitRunnable() {

            @Override
            public void run() {
                if (Environment.getNameserver().equalsIgnoreCase("default")) {
                    Environment.getPluginInstance().sendNameServer(e.getPlayer());

                }
            }

        }.runTaskLater(Environment.getPluginInstance(), 150L);

        new BukkitRunnable() {

            @Override
            public void run() {
                try {

                    Environment.getSelectPlayer().setString(1, e.getPlayer().getUniqueId().toString());

                    final ResultSet r = Environment.getSelectPlayer().executeQuery();

                    if (r.first()) {

                        PluginData.getBoolPlayers().put(e.getPlayer().getUniqueId(), r.getBoolean("bool"));

                    } else {

                        Environment.getInsertPlayerBool().setString(1, e.getPlayer().getUniqueId().toString());
                        Environment.getInsertPlayerBool().executeUpdate();

                        PluginData.getBoolPlayers().put(e.getPlayer().getUniqueId(), Boolean.TRUE);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskAsynchronously(Environment.getPluginInstance());*/

    }

    @EventHandler

    public void onQuit(PlayerQuitEvent e) {
        PluginData.removeEnvironmentPlayer(e.getPlayer());
        if (PluginData.getBoolPlayers().containsKey(e.getPlayer().getUniqueId())) {

            if (PluginData.getBoolPlayers().get(e.getPlayer().getUniqueId())) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        try {
                            Environment.getUpdatePlayerBool().setBoolean(1, true);
                            Environment.getUpdatePlayerBool().setString(2, e.getPlayer().getUniqueId().toString());
                            Environment.getUpdatePlayerBool().executeUpdate();

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
                        try {
                            Environment.getUpdatePlayerBool().setBoolean(1, false);
                            Environment.getUpdatePlayerBool().setString(2, e.getPlayer().getUniqueId().toString());
                            Environment.getUpdatePlayerBool().executeUpdate();

                            PluginData.getBoolPlayers().remove(e.getPlayer().getUniqueId());
                        } catch (SQLException ex) {
                            Logger.getLogger(PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }.runTaskAsynchronously(Environment.getPluginInstance());
            }

        }

        PluginData.getAllRegions().values().forEach((r) -> {
            r.cancelAllTasks(e.getPlayer().getUniqueId());
        });

    }

    @EventHandler
    public void onEnterRegion(EnterRegionEvent e) {

        RegionData re = PluginData.getAllRegions().get(e.getNameRegion());

        if (re.getWeather().equalsIgnoreCase("rain")) {
            e.getPlayer().setPlayerWeather(WeatherType.DOWNFALL);

            if (PluginData.getBossbar() != null) {
                PluginData.getBossbar().addPlayer(e.getPlayer());
            }
        } else if (re.getWeather().equalsIgnoreCase("sun")) {
            e.getPlayer().setPlayerWeather(WeatherType.CLEAR);
        }

        if (re.getThunder()) {

            BukkitTask bRunnable = new BukkitRunnable() {

                @Override
                public void run() {

                    EnvChange.spawnThunderstorm(e.getPlayer(), true, re.getRegion());

                }

            }.runTaskTimer(Environment.getPluginInstance(), 30L, 20L);

            re.addInfoTask(e.getPlayer().getUniqueId(), bRunnable);

        }

        if (!re.getTime().equalsIgnoreCase("default")) {
            e.getPlayer().setPlayerTime(parseLong(re.getTime()), false);
        }

        if (!re.getSoundAmbient().equals(SoundType.NONE) && !re.getTime().equals("default")) {

            SoundUtil.playSoundAmbient(re.getSoundAmbient(), e.getPlayer(), parseLong(re.getTime()), re.getRegion(), re);
System.out.println("parte1");
        }

    }

    @EventHandler
    public void onLeaveRegion(LeaveRegionEvent e) {

        EnvChange.resetAll(e.getPlayer());

    }

}
