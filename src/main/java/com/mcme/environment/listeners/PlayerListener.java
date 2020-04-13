/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.mcme.environment.Environment;
import com.mcme.environment.data.PluginData;
import com.mcme.environment.data.RegionData;
import com.mcme.environment.event.EnterRegionEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.WeatherType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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

        if (PluginData.boolPlayers.containsKey(e.getPlayer().getUniqueId())) {

            if (PluginData.boolPlayers.get(e.getPlayer().getUniqueId())) {

                for (String region : PluginData.AllRegions.keySet()) {

                    if (PluginData.AllRegions.get(region).region.isInside(e.getPlayer().getLocation())) {

                        //trigger event 
                        EnterRegionEvent event = new EnterRegionEvent(e.getPlayer(), region);

                    }

                }

            }

        }

    }

    @EventHandler
    public void onEnterRegion(EnterRegionEvent e) {

        RegionData re = PluginData.AllRegions.get(e.getNameRegion());

        if (re.weather.equalsIgnoreCase("rain")) {
            e.getPlayer().setPlayerWeather(WeatherType.DOWNFALL);

        } else {
            e.getPlayer().setPlayerWeather(WeatherType.CLEAR);
        }
        if (re.thunder) {
            PacketContainer thunder = Environment.getPluginInstance().manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
            thunder.getIntegers().
                    write(0, 1).
                    write(1, 1).
                    write(2, (int) e.getPlayer().getLocation().getX()).
                    write(3, (int) e.getPlayer().getLocation().getY()).
                    write(4, (int) e.getPlayer().getLocation().getZ());

            thunder.getDataWatcherModifier().
                    write(0, Environment.getPluginInstance().getThunderWatcher());

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(e.getPlayer(), thunder);
            } catch (InvocationTargetException es) {
                es.printStackTrace();
            }
// 11  18

// 18  11
        }

        if (e.getPlayer().getPlayerTime() <= Integer.getInteger(re.time)) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (Integer.getInteger(re.time) <= e.getPlayer().getPlayerTime()) {

                        cancel();
                    }
                    e.getPlayer().setPlayerTime(e.getPlayer().getPlayerTime() + 50, false);

                }
            }.runTaskTimerAsynchronously(Environment.getPluginInstance(), 0L, 1L);

        } else {
            new BukkitRunnable() {

                @Override
                public void run() {
                    int i = Integer.getInteger(re.time) - 60;
                    if (e.getPlayer().getPlayerTime() >= 23900 && e.getPlayer().getPlayerTime() <= 24000) {

                        e.getPlayer().setPlayerTime(0, false);
                    } else if (e.getPlayer().getPlayerTime() > Integer.getInteger(re.time) && e.getPlayer().getPlayerTime() < 23900) {
                        e.getPlayer().setPlayerTime(e.getPlayer().getPlayerTime() + 50, false);
                    } else if (e.getPlayer().getPlayerTime() < i) {
                        e.getPlayer().setPlayerTime(e.getPlayer().getPlayerTime() + 50, false);
                    } else if (e.getPlayer().getPlayerTime() >= i && e.getPlayer().getPlayerTime() <= Integer.getInteger(re.time)) {
                        e.getPlayer().setPlayerTime(Integer.getInteger(re.time), false);
                        cancel();
                    }

                    e.getPlayer().setPlayerTime(e.getPlayer().getPlayerTime() + 50, false);

                }
            }.runTaskTimerAsynchronously(Environment.getPluginInstance(), 0L, 1L);
        }

    }

}
