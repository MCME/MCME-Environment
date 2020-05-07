/*
 * Copyright (C) 2020 MCME (Fraspace5)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mcme.environment.commands;

import com.mcme.environment.data.PluginData;
import com.mcmiddleearth.pluginutil.NumericUtil;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import com.mcmiddleearth.pluginutil.message.MessageType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentList extends EnvironmentCommand {

    public EnvironmentList(String... permissionNodes) {
        super(1, true, permissionNodes);
        setShortDescription(": List of all regions/locations");
        setUsageDescription(": List");
    }
//environment list region|location 1

    @Override
    protected void execute(final CommandSender cs, final String... args) {
        int pageIndex = 0;
        Player pl = (Player) cs;
        if (args.length > 0 && (!NumericUtil.isInt(args[1]))) {

            pageIndex = 1;
        }
        int page = 1;

        if (args.length > pageIndex && NumericUtil.isInt(args[pageIndex])) {
            page = NumericUtil.getInt(args[pageIndex]);
        }
        if (args[0].equalsIgnoreCase("region")) {

            FancyMessage header = new FancyMessage(MessageType.WHITE, PluginData.getMessageUtil())
                    .addSimple(ChatColor.GREEN + "Regions loaded in the network --> " + ChatColor.BOLD + PluginData.AllRegions.size() + "\n");
            List<FancyMessage> messages = new ArrayList<>();

            for (String region : PluginData.AllRegions.keySet()) {
                FancyMessage r = new FancyMessage(MessageType.WHITE, PluginData.getMessageUtil())
                        .addSimple(ChatColor.DARK_GREEN + "- " + region + "\n");
                messages.add(r);
            }

            PluginData.getMessageUtil().sendFancyListMessage((Player) cs, header, messages, "/environment list ", page);
        } else {

            FancyMessage header = new FancyMessage(MessageType.WHITE, PluginData.getMessageUtil())
                    .addSimple(ChatColor.GREEN + "Locations loaded in the network --> " + ChatColor.BOLD + PluginData.locSounds.size() + "\n");
            List<FancyMessage> messages = new ArrayList<>();

            for (String location : PluginData.locSounds.keySet()) {
                Location l = PluginData.locSounds.get(location).loc;

                FancyMessage r = new FancyMessage(MessageType.WHITE, PluginData.getMessageUtil())
                        .addSimple(ChatColor.DARK_GREEN + "- " + location + " " + l.getX() + "x," + l.getY() + "y," + l.getZ() + "z" + "\n");
                messages.add(r);
            }

            PluginData.getMessageUtil().sendFancyListMessage((Player) cs, header, messages, "/environment list ", page);

        }
    }
}
