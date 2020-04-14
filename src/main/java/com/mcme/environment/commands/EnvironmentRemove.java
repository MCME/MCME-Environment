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

                    String stat = "DELETE FROM " + Environment.getPluginInstance().database + ".mcmeproject_regions_data WHERE name = '" + args[0] + "' ;";
                    try {
                        Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate();
                    } catch (SQLException ex) {
                        Logger.getLogger(EnvironmentRemove.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    PluginData.loadRegions();

                    sendDel(cs);

                }

            }.runTaskAsynchronously(Environment.getPluginInstance());

        } else {
            sendNoRegion(cs);
        }

    }

    private void sendDel(CommandSender cs) {
        PluginData.getMessageUtil().sendInfoMessage(cs, "Region deleted!");

    }

    private void sendNoRegion(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "This region doesn't exists!");

    }
}
