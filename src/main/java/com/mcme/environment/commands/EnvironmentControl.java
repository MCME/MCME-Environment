/*
 *Copyright (C) 2020 MCME (Fraspace5)
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
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import com.mcmiddleearth.pluginutil.message.MessageType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentControl extends EnvironmentCommand {

    public EnvironmentControl(String... permissionNodes) {
        super(1, true, permissionNodes);
        setShortDescription(": Util and metrics");
        setUsageDescription(": Metrics and util commands for environment plugin");
    }

    @Override
    protected void execute(final CommandSender cs, final String... args) {
        Player pl = (Player) cs;
        if (args[0].equalsIgnoreCase("reload")) {
            try {
                PluginData.loadRegions();

                new BukkitRunnable() {

                    @Override
                    public void run() {
                      
                        PluginData.loadLocations();
                        pl.sendMessage(ChatColor.GREEN + "Reload Completed, no errors found");
                    }

                }.runTaskLater(Environment.getPluginInstance(), 40L);

            } catch (IllegalArgumentException | IllegalStateException e) {
                pl.sendMessage(ChatColor.RED + "Errors Found - " + e.getClass().getName());
            }

        } else if (args[0].equalsIgnoreCase("realtime")) {

            Integer currentPlayerRunnable = getTasksSize();
            Integer totalPlayers = Bukkit.getOnlinePlayers().size();
            Double s = 100.0 * currentPlayerRunnable / totalPlayers;

            FancyMessage message = new FancyMessage(MessageType.INFO_NO_PREFIX, PluginData.getMessageUtils());

            message.addSimple(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Metrics :" + "\n");
            message.addSimple(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Players that are using the sound engine : " + ChatColor.YELLOW + currentPlayerRunnable + "/" + totalPlayers + " [" + Math.round(s) + "%]" + "\n");

            for (int i = 0; i <= 100; i++) {

                if (i <= Math.round(s)) {

                    if (i <= 40) {
                        message.addSimple(ChatColor.GREEN.toString() + "|");
                    } else if (i > 40 && i < 80) {
                        message.addSimple(ChatColor.YELLOW.toString() + "|");

                    } else {
                        message.addSimple(ChatColor.RED.toString() + "|");

                    }

                } else {
                    message.addSimple(ChatColor.GRAY.toString() + "|");
                }

            }

            if (pl.hasPermission("env.staff")) {
                if (Environment.isEngine()) {
                    message.addClickable("\n" + ChatColor.RED.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Shutdown Sound Engine", "/env control shutdown").setRunDirect();

                } else {
                    message.addClickable("\n" + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Enable Sound Engine", "/env control enable").setRunDirect();

                }

            }

            message.send(pl);

        } else if (args[0].equalsIgnoreCase("shutdown")) {

            if (pl.hasPermission("env.staff")) {

                if (Environment.isEngine()) {
                    Environment.setEngine(false);

                    PluginData.getAllRegions().values().forEach((s) -> {
                        s.getInformedLoc().forEach((b) -> {
                            s.cancelAllTasks(b);
                        });
                    });

                    PluginData.getMessageUtils().sendInfoMessage(cs, "Sound engine switched off");
                } else {
                    PluginData.getMessageUtils().sendInfoMessage(cs, "Sound engine is already off ");
                }

            } else {
                sendNoPerm(cs);
            }

        } else if (args[0].equalsIgnoreCase("enable")) {

            if (pl.hasPermission("env.staff")) {

                if (!Environment.isEngine()) {

                    Environment.setEngine(true);
                    PluginData.getMessageUtils().sendInfoMessage(cs, "Sound engine enabled");
                } else {

                    PluginData.getMessageUtils().sendInfoMessage(cs, "Sound engine is already on ");

                }
            } else {
                sendNoPerm(cs);
            }

        }

    }

    private Integer getTasksSize() {
        List<UUID> list = new ArrayList<>();

        PluginData.getAllRegions().values().forEach((region) -> {
            region.getTasks().keySet().forEach((uuid) -> {
                if (!list.contains(uuid)) {
                    list.add(uuid);
                }
            });
        });

        return list.size();
    }

    private void sendNoPerm(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "No permission for this command");

    }

}
