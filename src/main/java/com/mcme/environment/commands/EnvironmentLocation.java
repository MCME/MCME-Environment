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
import com.mcme.environment.SoundPacket.SoundType;
import com.mcme.environment.data.PluginData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
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

                if (!PluginData.getLocSounds().containsKey(args[1])) {
                    if (args[2].equalsIgnoreCase("bell")) {
                        soundLocated = getSoundLocated(args[2]);
                    }
                    if (checkOtherSounds(pl.getLocation(), soundLocated)) {
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                String stat = "INSERT INTO environment_locations_data (location, server, name, idlocation, sound) VALUES ('" + pl.getLocation().getWorld().getName() + ";" + pl.getLocation().getX() + ";" + pl.getLocation().getY() + ";" + pl.getLocation().getZ() + "','" + Environment.getNameserver() + "','" + args[1] + "','" + PluginData.createId() + "','" + soundLocated + "') ;";
                                try {
                                    Environment.getPluginInstance().getConnection().prepareStatement(stat).executeUpdate(stat);
                                    sendDone(cs);

                                    PluginData.loadLocations();

                                } catch (SQLException ex) {
                                    Logger.getLogger(EnvironmentEdit.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }

                        }.runTaskAsynchronously(Environment.getPluginInstance());
                    } else {

                        sendNear(cs);
                    }
                } else {
                    sendAlready(cs);
                }
            } else {
                sendNotEnough(cs);
            }

        } else if (args[0].equalsIgnoreCase("remove")) {
            if (PluginData.getLocSounds().containsKey(args[1])) {

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        String stat = "DELETE FROM environment_locations_data WHERE idlocation = '" + PluginData.getLocSounds().get(args[1]).getId() + "' ;";
                        try {
                            Environment.getPluginInstance().getConnection().prepareStatement(stat).executeUpdate();
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

        }
        return sound;
    }

    private boolean checkOtherSounds(Location loc, SoundType p) {
        Boolean bool = true;

        for (String s : PluginData.getLocSounds().keySet()) {
            if (!PluginData.getLocSounds().get(s).getLoc().equals(loc)) {
                if (PluginData.getLocSounds().get(s).playerInRange(loc)) {
                    bool = false;
                }
            }

        }

        return bool;

    }

    private void sendDone(CommandSender cs) {
        PluginData.getMessageUtils().sendInfoMessage(cs, "New location added!");

    }

    private void sendNear(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "This sound is near another sound!");

    }

    private void sendDel(CommandSender cs) {
        PluginData.getMessageUtils().sendInfoMessage(cs, "Location removed!");

    }

    private void sendAlready(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "This location already exists");

    }

    private void sendNo(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "This location doesn't exists");

    }

    private void sendNotEnough(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "No enough arguments");

    }
}
