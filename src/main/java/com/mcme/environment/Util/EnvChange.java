/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.Util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.mcme.environment.Environment;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
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
     *
     */
    public static void spawnThunderstorm(Player pl) {
        PacketContainer thunder = Environment.getPluginInstance().manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
        thunder.getIntegers().
                write(0, randomReturn()).
                write(1, 1).
                write(2, (int) pl.getLocation().getX()).
                write(3, (int) pl.getLocation().getY()).
                write(4, (int) pl.getLocation().getZ());

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(pl, thunder);
        } catch (InvocationTargetException es) {
            es.printStackTrace();
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

        if (pl.getPlayerTime() <= time) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (time <= pl.getPlayerTime()) {

                        cancel();
                    }
                    pl.setPlayerTime(pl.getPlayerTime() + 50, false);

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
                        pl.setPlayerTime(pl.getPlayerTime() + 50, false);
                    } else if (pl.getPlayerTime() < i) {
                        pl.setPlayerTime(pl.getPlayerTime() + 50, false);
                    } else if (pl.getPlayerTime() >= i && pl.getPlayerTime() <= time) {
                        pl.setPlayerTime(time, false);
                        cancel();
                    }

                    pl.setPlayerTime(pl.getPlayerTime() + 50, false);

                }
            }.runTaskTimer(Environment.getPluginInstance(), 0L, 1L);
        }

    }

}
