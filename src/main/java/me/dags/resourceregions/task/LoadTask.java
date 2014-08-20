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
import me.dags.resourceregions.region.Region;
import me.dags.resourceregions.region.RegionManager;
import me.dags.resourceregions.util.FileUtil;
import me.dags.resourceregions.util.JsonUtil;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dags_ <dags@dags.me>
 */

public class LoadTask extends BukkitRunnable
{

    private final File regionsDir;

    public LoadTask(Plugin p)
    {
        regionsDir = FileUtil.getFile(p.getDataFolder(), "regions/");
    }

    @Override
    public void run()
    {
        Set<Region> regions = new HashSet<Region>();
        File[] contents = regionsDir.listFiles();
        if (contents != null)
        {
            for (File f : contents)
            {
                if (f.getName().endsWith(".json"))
                {
                    Region r = JsonUtil.loadRegion(f);
                    if (r != null)
                    {
                        ResourceRegions.log("Loaded region: " + r.getName() + " (" + r.getWorldName() + ")");
                        r.init();
                        regions.add(r);
                    }
                }
            }
        }
        if (!regions.isEmpty())
        {
            RegionManager.setRegions(regions);
            ResourceRegions.log("Loaded: " + regions.size() + " regions!");
        }
    }

}
