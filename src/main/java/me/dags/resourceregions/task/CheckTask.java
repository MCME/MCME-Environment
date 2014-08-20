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

package me.dags.resourceregions.task;

import me.dags.resourceregions.ResourceRegions;
import me.dags.resourceregions.event.RegionChangeEvent;
import me.dags.resourceregions.region.Region;
import me.dags.resourceregions.region.RegionPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dags_ <dags@dags.me>
 */

public class CheckTask extends BukkitRunnable
{

    private Collection<Region> regions;
    private List<RegionPlayer> players;
    private volatile boolean running;

    public CheckTask(List<RegionPlayer> wrapped, Collection<Region> reg)
    {
        regions = reg;
        players = wrapped;
        running = false;
    }

    public void run()
    {
        try
        {
            Set<RegionChangeEvent> events = new HashSet<RegionChangeEvent>();
            running = true;
            for (RegionPlayer pw : players)
            {
                Region newRegion = new Region(true);
                for (Region r : regions)
                {
                    if (!r.getWorldName().equals(pw.getWorld()))
                    {
                        continue;
                    }
                    if (r.contains(pw.getxPos(), pw.getzPos()))
                    {
                        if (r.getName().equals(pw.getCurrentRegion()))
                        {
                            if (r.getWeight() >= newRegion.getWeight())
                            {
                                newRegion = r;
                            }
                            continue;
                        }
                        if (r.getWeight() >= newRegion.getWeight())
                        {
                            newRegion = r;
                        }
                    }
                }
                String rName = newRegion.getName();
                if (!rName.equals("") && !rName.equals(pw.getCurrentRegion()))
                {
                    events.add(new RegionChangeEvent(pw, newRegion));
                }
            }
            if (!events.isEmpty())
            {
                SyncEvents se = new SyncEvents(events);
                se.runTask(ResourceRegions.getPlugin());
            }
        }
        finally
        {
            running = false;
        }
    }

    public boolean isRunning()
    {
        return running;
    }

    private class SyncEvents extends BukkitRunnable
    {

        private final Set<RegionChangeEvent> events;

        public SyncEvents(Set<RegionChangeEvent> e)
        {
            events = e;
        }

        public void run()
        {
            PluginManager pm = Bukkit.getPluginManager();
            for (Event e : events)
            {
                pm.callEvent(e);
            }
        }
    }

}
