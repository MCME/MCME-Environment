/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.commands;

import com.mcme.environment.Environment;
import com.mcme.environment.data.PluginData;
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
        setUsageDescription(": With this command you can edit a region, then set his weather and time");
    }
//environment edit nameRegion rain|sun true|false time
    //               0

    private boolean rain;
    private boolean sun;
    private boolean thunder;

    @Override
    protected void execute(final CommandSender cs, final String... args) {
        Player pl = (Player) cs;
        rain = false;
        sun = false;
        thunder = false;

        if (PluginData.AllRegions.containsKey(args[0])) {

            if (args[1].equalsIgnoreCase("rain")) {
                rain = true;
            } else {
                sun = true;
            }

            if (args[2].equalsIgnoreCase("true")) {
                thunder = true;
            } else {
                thunder = false;
            }

            if (rain) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        String stat = "UPDATE " + Environment.getPluginInstance().database + ".environment_regions_data SET thunders = '" + boolString(thunder) + "', weather = 'rain', time = '" + toTicks(args[3]) + "', WHERE idregion = '" + PluginData.getAllRegions().get(args[0]).idr + "' ;";

                        try {
                            Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                            sendDone(cs);
                            PluginData.loadRegions();
                        } catch (SQLException ex) {
                            Logger.getLogger(EnvironmentEdit.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }.runTaskAsynchronously(Environment.getPluginInstance());
            } else {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        String stat = "UPDATE " + Environment.getPluginInstance().database + ".environment_regions_data SET thunders = '" + boolString(thunder) + "', weather = 'sun', time = '" + toTicks(args[3]) + "', WHERE idregion = '" + PluginData.getAllRegions().get(args[0]).idr + "' ;";

                        try {
                            Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                            sendDone(cs);
                            PluginData.loadRegions();
                        } catch (SQLException ex) {
                            Logger.getLogger(EnvironmentEdit.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }.runTaskAsynchronously(Environment.getPluginInstance());
            }

        } else {
            sendNo(cs);
        }

    }

    private void sendDone(CommandSender cs) {
        PluginData.getMessageUtil().sendInfoMessage(cs, "Region updated!");

    }

    private void sendNo(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "This region doesn't exists.Type before /environment create areaName");

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
}
