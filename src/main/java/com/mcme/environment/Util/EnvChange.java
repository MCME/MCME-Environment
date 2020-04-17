/*
 *Copyright (C) 2020 MCME (Fraspace5)
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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.mcme.environment.Environment;
import com.mcme.environment.data.PluginData;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class EnvChange {

    /**
     * It spawn a thunderstorm with the player in the center
     *
     * @param pl Player
     * @param bol Sounds on
     */
    public static void spawnThunderstorm(Player pl, boolean bol) {
        PacketContainer thunder = Environment.getPluginInstance().manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
        thunder.getIntegers().
                write(0, randomReturn()).
                write(1, 1);
        thunder.getDoubles().
                write(2, pl.getLocation().getX()).
                write(3, pl.getLocation().getY()).
                write(4, pl.getLocation().getZ());

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(pl, thunder);
        } catch (InvocationTargetException es) {
            es.printStackTrace();
        }

        if (bol) {
            RandomCollection<Boolean> random = new RandomCollection<>();
            random.add(0.05, true);
            random.add(0.95, false);

            Boolean result = random.next();
            if (result) {
                pl.playSound(pl.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5F, 1.0F);
            }
        }

    }

    private static int randomReturn() {
        Random rnd = new Random();
        int result = (rnd.nextInt(0xFF) + 1) << 8 * 3;
        result += rnd.nextInt(0x1000000);

        return result;
    }

    /**
     * This method changes the player time with an animation
     *
     * @param pl Player
     * @param time Time in ticks
     */
    public static void changePlayerTime(Player pl, int time) {
// 10 18
        if (pl.getPlayerTime() <= time) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (time <= pl.getPlayerTime()) {

                        cancel();

                    }
                    System.out.println("time" + time + " playertime " + pl.getPlayerTime() + " playertime offset" + pl.getPlayerTimeOffset());
                    pl.setPlayerTime(pl.getPlayerTime() + 20, false);

                }
            }.runTaskTimer(Environment.getPluginInstance(), 0L, 1L);

        } else {
            new BukkitRunnable() {

                @Override
                public void run() {
                    int i = time - 60;
                    if (pl.getPlayerTime() >= 23900 && pl.getPlayerTime() <= 24000) {

                        pl.setPlayerTime(0, false);
                    } else if (pl.getPlayerTime() > time && pl.getPlayerTime() < 23900) {
                        pl.setPlayerTime(pl.getPlayerTime() + 20, false);
                    } else if (pl.getPlayerTime() < i) {
                        pl.setPlayerTime(pl.getPlayerTime() + 20, false);
                    } else if (pl.getPlayerTime() >= i && pl.getPlayerTime() <= time) {
                        pl.setPlayerTime(time, false);
                        cancel();
                    }
                    System.out.println("time" + time + " playertime " + pl.getPlayerTime() + " playertime offset" + pl.getPlayerTimeOffset());
                    pl.setPlayerTime(pl.getPlayerTime() + 20, false);

                }
            }.runTaskTimer(Environment.getPluginInstance(), 0L, 1L);
        }

    }

    public static void resetAll(Player pl) {
        pl.setPlayerWeather(WeatherType.CLEAR);
        pl.setPlayerTime(12000, false);
        PluginData.EntityPlayer.add(pl.getUniqueId());
    }

}
