package com.mcme.environment.runnable;

import com.mcme.environment.Environment;
import com.mcme.environment.Util.UpdateTimePacketUtil;
import com.mcme.environment.data.EnvironmentPlayer;
import com.mcme.environment.data.PluginData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TimeWarpRunnable {

    private static BukkitTask timeWarpTask;

    public static void startTimeWarpTask() {
        stopTimeWarpTask();
        timeWarpTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    EnvironmentPlayer environmentPlayer = PluginData.getOrCreateEnvironmentPlayer(player);
                    if(player.isPlayerTimeRelative() && environmentPlayer.getTimeWarp()>1) {
                        player.setPlayerTime(player.getPlayerTimeOffset()+(environmentPlayer.getTimeWarp()-1),true);
                        UpdateTimePacketUtil.sendTime(player,player.getPlayerTime(),false);
                    }
                });
            }
        }.runTaskTimer(Environment.getPluginInstance(), 1, 1L);
    }

    public static void stopTimeWarpTask() {
        if(timeWarpTask != null && !timeWarpTask.isCancelled()) {
            timeWarpTask.cancel();
        }
    }

}
