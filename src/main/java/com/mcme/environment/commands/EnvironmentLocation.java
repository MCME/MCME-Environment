/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.commands;

import com.mcme.environment.Environment;
import com.mcme.environment.SoundPacket.SoundType;
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
public class EnvironmentLocation extends EnvironmentCommand {

    public EnvironmentLocation(String... permissionNodes) {
        super(2, true, permissionNodes);
        setShortDescription(": Add/remove a location for a sound ");
        setUsageDescription(": you can add o remove a location for a sound");
    }

    //environment location add|remove locName soundtype
    private SoundType soundLocated;

    @Override
    protected void execute(final CommandSender cs, final String... args) {
        Player pl = (Player) cs;

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 3) {

                if (!PluginData.locSounds.containsKey(args[1])) {
                    if (args[2].equalsIgnoreCase("bell")) {
                        soundLocated = getSoundLocated(args[2]);
                    }

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            String stat = "INSERT INTO " + Environment.getPluginInstance().database + ".environment_locations_data (location, server, name, idlocation, sound) VALUES ('" + pl.getLocation().getWorld().getName().toString() + ";" + pl.getLocation().getX() + ";" + pl.getLocation().getY() + ";" + pl.getLocation().getZ() + "','" + Environment.nameserver + "','" + args[1] + "','" + PluginData.createId() + "','" + soundLocated + "') ;";
                            try {
                                Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                                sendDone(cs);
                                PluginData.loadLocations();

                            } catch (SQLException ex) {
                                Logger.getLogger(EnvironmentEdit.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }

                    }.runTaskAsynchronously(Environment.getPluginInstance());

                } else {
                    sendAlready(cs);
                }
            } else {
                sendNotEnough(cs);
            }

        } else if (args[0].equalsIgnoreCase("remove")) {
            if (PluginData.locSounds.containsKey(args[1])) {

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        String stat = "DELETE FROM " + Environment.getPluginInstance().database + ".environment_locations_data WHERE idlocation = '" + PluginData.locSounds.get(args[1]).id + "' ;";
                        try {
                            Environment.getPluginInstance().con.prepareStatement(stat).executeUpdate();
                            PluginData.loadLocations();
                            sendDel(cs);
                        } catch (SQLException ex) {
                            Logger.getLogger(EnvironmentRemove.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }.runTaskAsynchronously(Environment.getPluginInstance());

            } else {
                sendNo(cs);
            }
        }

    }

    private SoundType getSoundLocated(String arg) {
        SoundType sound = SoundType.NONE;

        if (arg.equalsIgnoreCase("bell")) {
            sound = SoundType.BELL;

        } else {
            sound = SoundType.NONE;
        }
        return sound;
    }

    private void sendDone(CommandSender cs) {
        PluginData.getMessageUtil().sendInfoMessage(cs, "New location added!");

    }
    private void sendDel(CommandSender cs) {
        PluginData.getMessageUtil().sendInfoMessage(cs, "Location removed!");

    }

    private void sendAlready(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "This location already exists");

    }

    private void sendNo(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "This location doesn't exists");

    }

    private void sendNotEnough(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "No enough arguments");

    }
}