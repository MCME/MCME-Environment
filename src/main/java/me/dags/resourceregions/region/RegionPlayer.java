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

import me.dags.resourceregions.util.Constants;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author dags_ <dags@dags.me>
 */
public class RegionPlayer
{

    private final UUID uuid;
    private final String currentRegion;
    private final String world;
    private final int xPos;
    private final int zPos;

    public RegionPlayer(Player p)
    {
        uuid = p.getUniqueId();
        world = p.getWorld().getName();
        xPos = p.getLocation().getBlockX();
        zPos = p.getLocation().getBlockZ();
        currentRegion = getCurrent(p);
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public String getWorld()
    {
        return world;
    }

    public int getxPos()
    {
        return xPos;
    }

    public int getzPos()
    {
        return zPos;
    }

    public String getCurrentRegion()
    {
        return currentRegion;
    }

    public static String getCurrent(Player p)
    {
        if (p.hasMetadata(Constants.RESOURCE_REGION) && p.getMetadata(Constants.RESOURCE_REGION).size()>0)
        {
            return p.getMetadata(Constants.RESOURCE_REGION).get(0).asString();
        }
        return "null";
    }

}
