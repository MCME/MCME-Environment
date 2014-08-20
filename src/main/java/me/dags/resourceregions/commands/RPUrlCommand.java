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
import me.dags.resourceregions.util.Constants;
import me.dags.resourceregions.util.RegionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author dags_ <dags@dags.me>
 */

public class RPUrlCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] a)
    {
        if (!cs.hasPermission(Constants.RPURL_PERM))
        {
            cs.sendMessage("Ain't got perms fo' dat!");
            return false;
        }
        if (a.length == 2)
        {
            String pack = a[0];
            String url = a[1];
            if (!RegionUtil.validPackUrl(url))
            {
                cs.sendMessage("That does not appear to be a valid URL!");
                return true;
            }
            ResourceRegions.setPackUrl(pack, url);
        }
        return false;
    }
}
