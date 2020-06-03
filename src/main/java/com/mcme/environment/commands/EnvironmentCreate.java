
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
import com.mcme.environment.Util.RegionScanner;
import com.mcme.environment.data.PluginData;
import com.mcmiddleearth.pluginutil.region.CuboidRegion;
import com.mcmiddleearth.pluginutil.region.PrismoidRegion;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import static java.lang.Integer.parseInt;
import java.sql.Statement;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentCreate extends EnvironmentCommand {

    public EnvironmentCreate(String... permissionNodes) {
        super(2, true, permissionNodes);
        setShortDescription(": Create a new region");
        setUsageDescription(" <areaName> <weight>: Create a new region");
    }

    private Region weRegion;

    @Override
    protected void execute(final CommandSender cs, final String... args) {

        Player pl = (Player) cs;
        final Location loc = pl.getLocation();
        if (!PluginData.getAllRegions().containsKey(args[0])) {

            try {
                WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

                weRegion = worldEdit.getSession(pl).getSelection(worldEdit.getSession(pl).getSelectionWorld());

                if (!(weRegion instanceof com.sk89q.worldedit.regions.CuboidRegion || weRegion instanceof Polygonal2DRegion)) {
                    sendInvalidSelection(pl);

                } else if (weRegion instanceof Polygonal2DRegion) {

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            PrismoidRegion r = new PrismoidRegion(loc, (com.sk89q.worldedit.regions.Polygonal2DRegion) weRegion);
                            try {
                                String stat = "INSERT INTO environment_regions_data (idregion, name, type, xlist, zlist, ymin, ymax, location, server, weather, thunders, time, sound, weight, info_sound ) VALUES ('" + PluginData.createId().toString() + "','" + args[0] + "','prismoid','" + serialize(r.getXPoints()) + "','" + serialize(r.getZPoints()) + "','" + r.getMinY() + "','" + r.getMaxY() + "','" + pl.getLocation().getWorld().getName() + ";" + pl.getLocation().getX() + ";" + pl.getLocation().getY() + ";" + pl.getLocation().getZ() + "','" + Environment.getNameserver() + "','default','0','default','" + SoundType.NONE.name().toUpperCase() + "','" + parseInt(args[1]) + "', '" + pl.getLocation().getWorld().getName() + ";" + pl.getLocation().getX() + ";" + pl.getLocation().getY() + ";" + pl.getLocation().getZ() + "' ) ;";

                                Statement statm = Environment.getPluginInstance().getConnection().prepareStatement(stat);
                                statm.setQueryTimeout(10);
                                statm.executeUpdate(stat);

                                PluginData.loadRegions();

                                RegionScanner.getChunkSnaphshot((Region) weRegion, loc.getWorld(), args[0]);

                                sendDone(cs);
                            } catch (SQLException | NumberFormatException ex) {
                                if (ex instanceof NumberFormatException) {
                                    PluginData.getMessageUtils().sendErrorMessage(cs, "It should be an integer number");
                                } else if (ex instanceof SQLException) {
                                    Logger.getLogger(EnvironmentCreate.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }

                        }

                    }.runTaskAsynchronously(Environment.getPluginInstance());

                } else if (weRegion instanceof com.sk89q.worldedit.regions.CuboidRegion) {

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            CuboidRegion r = new CuboidRegion(loc, (com.sk89q.worldedit.regions.CuboidRegion) weRegion);
                            Vector minCorner = r.getMinCorner();
                            Vector maxCorner = r.getMaxCorner();
                            try {
                                String stat = "INSERT INTO environment_regions_data (idregion, name, type, xlist, zlist, ymin, ymax, location, server, weather, thunders, time, sound, weight,info_sound ) VALUES ('" + PluginData.createId().toString() + "','" + args[0] + "','cuboid','" + minCorner.getBlockX() + ";" + maxCorner.getBlockX() + "','" + minCorner.getBlockZ() + ";" + maxCorner.getBlockZ() + "','" + minCorner.getBlockY() + "','" + maxCorner.getBlockY() + "','" + pl.getLocation().getWorld().getName() + ";" + pl.getLocation().getX() + ";" + pl.getLocation().getY() + ";" + pl.getLocation().getZ() + "','" + Environment.getNameserver() + "','default','0','default','" + SoundType.NONE.name().toUpperCase() + "','" + parseInt(args[1]) + "', '" + pl.getLocation().getWorld().getName() + ";" + pl.getLocation().getX() + ";" + pl.getLocation().getY() + ";" + pl.getLocation().getZ() + "' ) ;";

                                Statement statm = Environment.getPluginInstance().getConnection().prepareStatement(stat);
                                statm.setQueryTimeout(10);
                                statm.executeUpdate(stat);

                                PluginData.loadRegions();
                                RegionScanner.getChunkSnaphshot((Region) weRegion, loc.getWorld(), args[0]);

                                sendDone(cs);
                            } catch (SQLException | NumberFormatException ex) {
                                if (ex instanceof NumberFormatException) {
                                    PluginData.getMessageUtils().sendErrorMessage(cs, "It should be an integer number");
                                } else if (ex instanceof SQLException) {
                                    Logger.getLogger(EnvironmentCreate.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }

                        }

                    }.runTaskAsynchronously(Environment.getPluginInstance());

                }
            } catch (IncompleteRegionException | NullPointerException ex) {

                Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
                if (ex instanceof NullPointerException) {
                    sendInvalidSelection(pl);
                } else if (ex instanceof IncompleteRegionException) {
                    sendInvalidSelection(pl);

                }

            }
        } else {
            sendAlready(cs, args[0]);
        }

    }

    public String serialize(Integer[] intlist) {

        StringBuilder builder = new StringBuilder();

        for (Integer intlist1 : intlist) {
            builder.append(String.valueOf(intlist1)).append(";");
        }

        return builder.toString();

    }

    private void sendDone(CommandSender cs) {
        PluginData.getMessageUtils().sendInfoMessage(cs, "Region created!");

    }

    private void sendAlready(CommandSender cs, String name) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "The region " + name + " already exists!");

    }

    private void sendInvalidSelection(Player player) {
        PluginData.getMessageUtils().sendErrorMessage(player, "For a cuboid or polygonal area make a valid WorldEdit selection first.");
    }

}
