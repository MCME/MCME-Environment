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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentSound extends EnvironmentCommand {

    public EnvironmentSound(String... permissionNodes) {
        super(3, true, permissionNodes);
        setShortDescription(": Edit sounds ");
        setUsageDescription("<nameRegion> <ambientSoundType> <LocatedSoundType>: With this command you can edit sounds of that region");
    }
//environment sound nameRegion ambientsound addedsound
    //               0            1              2

    private SoundType soundAmbient;

    private SoundType soundLocated;

    @Override
    protected void execute(final CommandSender cs, final String... args) {
        if (PluginData.AllRegions.containsKey(args[0])) {
            Player pl = (Player) cs;

            soundAmbient = getSoundAmbient(args[1]);

            soundLocated = getSoundLocated(args[2]);

            new BukkitRunnable() {

                @Override
                public void run() {
                    String stat = "UPDATE " + Environment.getPluginInstance().database + ".environment_regions_data SET sound = '" + soundAmbient.name().toUpperCase() + ";" + soundLocated.name().toUpperCase() + "', info_sound = '" + pl.getLocation().getWorld().getName() + ";" + pl.getLocation().getX() + ";" + pl.getLocation().getY() + ";" + pl.getLocation().getZ() + "' WHERE idregion = '" + PluginData.getAllRegions().get(args[0]).idr.toString() + "' ;";

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
    }

    private SoundType getSoundAmbient(String arg) {
        SoundType sound = SoundType.NONE;

        if (arg.equalsIgnoreCase("plain")) {
            sound = SoundType.PLAIN;

        } else if (arg.equalsIgnoreCase("cave")) {
            sound = SoundType.CAVE;

        } else if (arg.equalsIgnoreCase("forest")) {
            sound = SoundType.FOREST;

        } else if (arg.equalsIgnoreCase("ocean")) {
            sound = SoundType.OCEAN;

        } else if (arg.equalsIgnoreCase("wind")) {
            sound = SoundType.WIND;

        } else if (arg.equalsIgnoreCase("swampland")) {
            sound = SoundType.SWAMPLAND;

        } else {
            sound = SoundType.NONE;
        }
        return sound;
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
        PluginData.getMessageUtil().sendInfoMessage(cs, "Region updated!");

    }

    private void sendNo(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "This region doesn't exist.Type before /environment create areaName weight");

    }
}
