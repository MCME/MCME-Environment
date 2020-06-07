
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
import static java.lang.Long.parseLong;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentEdit extends EnvironmentCommand {

    public EnvironmentEdit(String... permissionNodes) {
        super(4, true, permissionNodes);
        setShortDescription(": Edit a region ");
        setUsageDescription("<nameRegion> rain|sun true|false time: With this command you can edit a region, then set his weather and time");
    }

    private String weather;
    private boolean thunder;
    private Long time;

    @Override
    protected void execute(final CommandSender cs, final String... args) {

        thunder = false;

        if (PluginData.getAllRegions().containsKey(args[0])) {

            if (args[1].equalsIgnoreCase("rain")) {
                weather = "rain";
            } else {
                weather = "sun";
            }

            thunder = args[2].equalsIgnoreCase("true");

            try {

                time = parseLong(args[3]);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {

                            Environment.getEditRegion().setBoolean(1, thunder);
                            Environment.getEditRegion().setString(2, weather);
                            Environment.getEditRegion().setString(3, time.toString());
                            Environment.getEditRegion().setString(4, PluginData.getAllRegions().get(args[0]).getIdregion().toString());

                            Environment.getEditRegion().executeUpdate();

                            sendDone(cs);
                            PluginData.loadRegions();
                        } catch (SQLException ex) {
                            Logger.getLogger(EnvironmentEdit.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }.runTaskAsynchronously(Environment.getPluginInstance());
            } catch (NumberFormatException e) {

            }
        } else {
            sendNo(cs);
        }

    }

    private void sendDone(CommandSender cs) {
        PluginData.getMessageUtils().sendInfoMessage(cs, "Region updated!");

    }

    private void sendNo(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "This region doesn't exist.Type before /environment create areaName");

    }

}
