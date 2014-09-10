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

package me.dags.resourceregions.commands;

import me.dags.resourceregions.ResourceRegions;
import me.dags.resourceregions.region.Region;
import me.dags.resourceregions.util.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * @author dags_ <dags@dags.me>
 */

public class RegionCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] a)
    {
        if (!(cs instanceof Player))
        {
            return true;
        }
        Player p = (Player) cs;
        if (!p.hasPermission(Constants.REGION_PERM))
        {
            p.sendMessage("Ain't got perms fo' dat!");
            return true;
        }
        if (a.length == 0)
        {
            p.sendMessage("/rr <create | bounds | url | weight | save | load | reload>");
            return true;
        }
        else if (a[0].equalsIgnoreCase("create"))
        {
            if (a.length == 2)
            {
                p.sendMessage("Building new region " + a[1] + "...");
                Region r = new Region(true);
                r.setName(a[1]);
                r.setWorldName(p.getWorld().getName());
                if (WEUtil.worldEditExists() && WEUtil.hasSelection(p))
                {
                    p.sendMessage("Setting current selection as region bounds...");
                    RegionUtil.attachRegionBuilder(p, r);
                    WEUtil.setWERegion(p);
                }
                RegionUtil.attachRegionBuilder(p, r);
                return true;
            }
            p.sendMessage("/rr create <name>");
            return true;
        }
        else if (a[0].equalsIgnoreCase("bounds"))
        {
            if (!WEUtil.worldEditExists())
            {
                p.sendMessage("Please install worldedit to save regions!");
                return true;
            }
            if (!WEUtil.hasSelection(p))
            {
                p.sendMessage("Please make a worldedit selection first!");
                return true;
            }
            Region r = RegionUtil.getRegionBuilder(p);
            if (r == null)
            {
                p.sendMessage("You are not currently building a region!");
                return true;
            }
            p.sendMessage("Setting current selection as region bounds...");
            WEUtil.setWERegion(p);
            return true;
        }
        else if (a[0].equalsIgnoreCase("url"))
        {
            if (a.length == 2)
            {
                Region r = RegionUtil.getRegionBuilder(p);
                if (r == null)
                {
                    p.sendMessage("You are not currently building a region!");
                    return true;
                }
                r.setPackUrl(a[1]);
                p.sendMessage("Resource url set!");
                return true;
            }
            p.sendMessage("/rr url <resource_url>");
            return true;
        }
        else if (a[0].equalsIgnoreCase("weight"))
        {
            if (a.length == 2)
            {
                Region r = RegionUtil.getRegionBuilder(p);
                if (r == null)
                {
                    p.sendMessage("You are not currently building a region!");
                    return true;
                }
                if (isInt(a[1]))
                {
                    int i = Integer.parseInt(a[1]);
                    r.setWeight(i);
                    p.sendMessage("Weight set!");
                    return true;
                }
                p.sendMessage(a[1] + " is not a number!");
                return true;
            }
            p.sendMessage("/rr weight <#weight>");
            return true;
        }
        else if (a[0].equalsIgnoreCase("save"))
        {
            Region r = RegionUtil.getRegionBuilder(p);
            if (r == null)
            {
                p.sendMessage("You are not currently building a region!");
                return true;
            }
            if (!r.isValid())
            {
                p.sendMessage("You have not completed the region!");
                return true;
            }
            r.save();
            p.sendMessage("Region saved!");
            RegionUtil.removeRegionBuilder(p);
            return true;
        }
        else if (a[0].equalsIgnoreCase("load"))
        {
            if (a.length == 2)
            {
                File rf = FileUtil.getRegionToLoad(a[1]);
                if (rf == null)
                {
                    p.sendMessage("Region could not be found, searching for possible matches:");
                    p.sendMessage(FileUtil.getMatches(a[1]));
                    return true;
                }
                Region r = JsonUtil.loadRegion(rf);
                RegionUtil.attachRegionBuilder(p, r);
                p.sendMessage("Region " + r.getName() + " loaded successfully!");
                return true;
            }
            p.sendMessage("/rr load <fileName>");
            return true;
        }
        else if (a[0].equalsIgnoreCase("reload"))
        {
            p.sendMessage("Reloading regions...");
            ResourceRegions.reload();
            return true;
        }
        return false;
    }

    private boolean isInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}
