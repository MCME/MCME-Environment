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

package me.dags.resourceregions.listener;

import com.mcmiddleearth.resourceregions.DevUtil;
import me.dags.resourceregions.ResourceRegions;
import me.dags.resourceregions.event.RegionChangeEvent;
import me.dags.resourceregions.region.Region;
import me.dags.resourceregions.region.RegionManager;
import me.dags.resourceregions.region.RegionPlayer;
import me.dags.resourceregions.util.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

/**
 * @author dags_ <dags@dags.me>
 */

public class RegionListener implements Listener
{

    @EventHandler
    public void onRegionChange(RegionChangeEvent e)
    {
DevUtil.log("REGION change event");
        if (e.getPlayer().isOnline())
        {
DevUtil.log("is Online - "+e.getPlayer().getUniqueId());
            Player p = e.getPlayer().getPlayer();
            Region currentRegion = RegionManager.getRegion(p.getLocation().getWorld().getName(),
                                                           RegionPlayer.getCurrent(p));
            if((currentRegion==null) || (!e.getRegion().getPackUrl().equals(currentRegion.getPackUrl()))) {
DevUtil.log("switch pack - "+e.getPlayer().getUniqueId());
                p.setResourcePack(e.getRegion().getPackUrl());
            }
            setRegion(p, e.getRegion());
        }
    }

    private void setRegion(Player p, Region r)
    {
        MetadataValue mv = new FixedMetadataValue(ResourceRegions.getPlugin(), r.getName());
        p.setMetadata(Constants.RESOURCE_REGION, mv);
    }

}
