/*
 * ResourceRegions, a regions based texture-switcher
 * Copyright (c) 2014 dags <http://dags.me>
 *
 *   This program is free software: you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *   for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.dags.resourceregions.region;

import me.dags.resourceregions.ResourceRegions;
import me.dags.resourceregions.task.CheckTask;
import me.dags.resourceregions.task.LoadTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dags_ <dags@dags.me>
 */
public class RegionManager
{

    private static RegionManager instance;

    private final Object lock = new Object();
    private volatile boolean ready = false;
    private Set<Region> regions;
    private CheckLoop checkLoop;
    private CheckTask asyncChecks;

    private RegionManager()
    {
        regions = new HashSet<Region>();
    }

    private static RegionManager i()
    {
        if (instance == null)
        {
            instance = new RegionManager();
        }
        return instance;
    }

    public static void setRegions(Set<Region> set)
    {
        i().setRegionMap(set);
    }

    public static void loadRegions(Plugin plugin)
    {
        LoadTask lt = new LoadTask(plugin);
        lt.runTaskAsynchronously(ResourceRegions.getPlugin());
    }

    public static boolean isReady()
    {
        return i().ready;
    }

    public static void runChecks()
    {
        i().runCheckLoop();
    }

    public static void stopChecks()
    {
        i().stopCheckLoop();
    }

    public static void clear()
    {
        i().ready = false;
        i().regions.clear();
    }

    private void setRegionMap(Set<Region> map)
    {
        synchronized (lock)
        {
            regions = map;
            ready = true;
        }
    }

    private void runCheckLoop()
    {
        stopCheckLoop();
        checkLoop = new CheckLoop();
        checkLoop.runTaskTimer(ResourceRegions.getPlugin(), 0L, 20L);
    }

    private void stopCheckLoop()
    {
        if (asyncChecks != null && asyncChecks.isRunning())
        {
            Bukkit.getScheduler().cancelTask(asyncChecks.getTaskId());
        }
        if (checkLoop != null && checkLoop.isRunning)
        {
            Bukkit.getScheduler().cancelTask(checkLoop.getTaskId());
        }
    }

    private class CheckLoop extends BukkitRunnable
    {
        public boolean isRunning = false;
        public void run()
        {
            isRunning = true;
            Set<Region> regionSet = new HashSet<Region>();
            regionSet.addAll(regions);
            List<RegionPlayer> players = new ArrayList<RegionPlayer>();
            for (Player p : Bukkit.getOnlinePlayers())
            {
                RegionPlayer pw = new RegionPlayer(p);
                players.add(pw);
            }
            if (regionSet.isEmpty())
            {
                RegionManager.stopChecks();
                return;
            }
            asyncChecks = new CheckTask(players, regionSet);
            asyncChecks.runTaskAsynchronously(ResourceRegions.getPlugin());
        }
    }

}
