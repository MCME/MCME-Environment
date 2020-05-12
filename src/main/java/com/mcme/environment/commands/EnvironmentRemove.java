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

import com.mcme.environment.Environment;
import com.mcme.environment.data.PluginData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentRemove extends EnvironmentCommand {

    public EnvironmentRemove(String... permissionNodes) {
        super(1, true, permissionNodes);
        setShortDescription(": Remove a region");
        setUsageDescription(" <areaName>: Remove a region");
    }
//environment remove nameRegion

    @Override
    protected void execute(final CommandSender cs, final String... args) {

        if (PluginData.AllRegions.containsKey(args[0])) {

            new BukkitRunnable() {

                @Override
                public void run() {

                    String stat = "DELETE FROM " + Environment.getPluginInstance().database + ".environment_regions_data WHERE idregion = '" + PluginData.AllRegions.get(args[0]).idr.toString() + "' ;";
                    try {
                        Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate();
                        PluginData.loadRegions();
                        sendDel(cs);
                    } catch (SQLException ex) {
                        Logger.getLogger(EnvironmentRemove.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }.runTaskAsynchronously(Environment.getPluginInstance());

        } else {
            sendNoRegion(cs);
        }

    }

    private void sendDel(CommandSender cs) {
        PluginData.getMessageUtils().sendInfoMessage(cs, "Region deleted!");

    }

    private void sendNoRegion(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "This region doesn't exists!");

    }
}
