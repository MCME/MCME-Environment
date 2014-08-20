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

package me.dags.resourceregions.event;

import me.dags.resourceregions.region.Region;
import me.dags.resourceregions.region.RegionPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author dags_ <dags@dags.me>
 */

public class RegionChangeEvent extends Event
{

    private static final HandlerList handlers = new HandlerList();
    private final RegionPlayer playerWrapper;
    private final Region region;

    public RegionChangeEvent(RegionPlayer pw, Region r)
    {
        playerWrapper = pw;
        region = r;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public OfflinePlayer getPlayer()
    {
        return Bukkit.getOfflinePlayer(playerWrapper.getUuid());
    }


    public Region getRegion()
    {
        return region;
    }

}
