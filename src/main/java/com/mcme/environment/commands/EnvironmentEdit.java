
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
import org.bukkit.entity.Player;
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
        Player pl = (Player) cs;

        thunder = false;

        if (PluginData.getAllRegions().containsKey(args[0])) {

            if (args[1].equalsIgnoreCase("rain")) {
                weather = "rain";
            } else {
                weather = "sun";
            }

            if (args[2].equalsIgnoreCase("true")) {
                thunder = true;
            } else {
                thunder = false;
            }
            try {
                
                time = parseLong(args[3]);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        String stat = "UPDATE environment_regions_data SET thunders = '" + boolString(thunder) + "', weather = '" + weather + "', time = '" + time + "' WHERE idregion = '" + PluginData.getAllRegions().get(args[0]).getIdregion().toString() + "' ;";

                        try {
                            Environment.getPluginInstance().getConnection().prepareStatement(stat).executeUpdate(stat);
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

    private String boolString(Boolean b) {

        if (b) {
            return "1";

        } else {
            return "0";
        }

    }

    /**
     *
     * @param s The string of the 24h times to be converted to ticks
     * @return
     */
    /*
     private Integer toTicks(String s) {

        String[] l = unserialize(s);
        int minute = 1200;
        int hour = 72000;
        if (l[1].substring(0, 0).equals("0")) {
            int hours = Integer.parseInt(l[0].substring(1)) * hour;
            int minutes = Integer.parseInt(l[1]) * minute;
            System.out.println(minutes + hours);
            return (minutes + hours);
        } else {
            int hours = Integer.parseInt(l[0]) * hour;
            int minutes = Integer.parseInt(l[1]) * minute;
            System.out.println(minutes + hours);
            return (minutes + hours);

        }

    }

    public static String[] unserialize(String line) {
        String[] dataArray = line.split(":");

        return dataArray;

    }
 
     */
}
