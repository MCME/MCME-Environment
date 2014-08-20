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

package me.dags.resourceregions.util;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import me.dags.resourceregions.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author dags_ <dags@dags.me>
 */

public class WEUtil
{

    public static boolean hasSelection(Player p)
    {
        LocalSession ls = getLocalSession(p);
        return ls.isSelectionDefined(ls.getSelectionWorld());
    }

    public static void setWERegion(Player p)
    {
        if (!RegionUtil.hasRegionBuilder(p))
        {
            return;
        }
        Region r = RegionUtil.getRegionBuilder(p);
        WorldEditAPI w = new WorldEditAPI((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit"));
        LocalSession ls = w.getSession(p);
        try
        {
            com.sk89q.worldedit.regions.Region selection = ls.getSelection(ls.getSelectionWorld());
            if (selection.getLength() < 2)
            {
                p.sendMessage("Not enough points selected!");
                return;
            }
            r.resetBounds();
            if (selection instanceof Polygonal2DRegion)
            {
                Polygonal2DRegion p2r = (Polygonal2DRegion) selection;
                for (BlockVector2D bv : p2r.getPoints())
                {
                    r.addPoint(bv.getBlockX(), bv.getBlockZ());
                }
                return;
            }
            Vector vMax = selection.getMaximumPoint();
            Vector vMin = selection.getMinimumPoint();
            r.addPoint(vMax.getBlockX(), vMax.getBlockZ());
            r.addPoint(vMax.getBlockX(), vMin.getBlockZ());
            r.addPoint(vMin.getBlockX(), vMin.getBlockZ());
            r.addPoint(vMin.getBlockX(), vMax.getBlockZ());
        }
        catch (IncompleteRegionException e)
        {
            p.sendMessage("Something went wrong whilst saving that region! D:");
        }
    }

    public static boolean worldEditExists()
    {
        return Bukkit.getPluginManager().getPlugin("WorldEdit") != null;
    }

    private static WorldEditAPI getWorldEditApi()
    {
        return new WorldEditAPI((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit"));
    }

    private static LocalSession getLocalSession(Player p)
    {
        return getWorldEditApi().getSession(p);
    }
}
