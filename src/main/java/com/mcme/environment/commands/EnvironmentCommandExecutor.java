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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentCommandExecutor implements CommandExecutor, TabExecutor {

    @Getter
    private final Map<String, EnvironmentCommand> commands = new LinkedHashMap<>();

    private final String permission = "env.user";
    private final String permissionStaff = "env.staff";

    public EnvironmentCommandExecutor() {
        addCommandHandler("create", new EnvironmentCreate(permissionStaff));
        addCommandHandler("remove", new EnvironmentRemove(permissionStaff));
        addCommandHandler("list", new EnvironmentList(permissionStaff));
        addCommandHandler("on", new EnvironmentOn(permissionStaff, permission));
        addCommandHandler("off", new EnvironmentOff(permissionStaff, permission));
        addCommandHandler("edit", new EnvironmentEdit(permissionStaff));
        addCommandHandler("details", new EnvironmentDetails(permissionStaff));
        addCommandHandler("redefine", new EnvironmentRedefine(permissionStaff));
        addCommandHandler("help", new EnvironmentHelp(permissionStaff, permission));
        addCommandHandler("sound", new EnvironmentSound(permissionStaff));
        addCommandHandler("location", new EnvironmentLocation(permissionStaff));
        addCommandHandler("control", new EnvironmentControl(permissionStaff, permission));
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (!string.equalsIgnoreCase("environment") && !string.equalsIgnoreCase("env") && !string.equalsIgnoreCase("en")) {
            return false;
        }
        if (strings == null || strings.length == 0) {
            sendNoSubcommandErrorMessage(cs);
            return true;
        }
        if (commands.containsKey(strings[0].toLowerCase())) {
            commands.get(strings[0].toLowerCase()).handle(cs, Arrays.copyOfRange(strings, 1, strings.length));

        } else {
            sendSubcommandNotFoundErrorMessage(cs);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player pl = (Player) sender;
        List<String> arguments = new ArrayList<>();
        arguments.add("on");
        arguments.add("off");
        if (pl.hasPermission("env.staff")) {
            arguments.add("edit");
            arguments.add("list");
            arguments.add("remove");
            arguments.add("create");
            arguments.add("details");
            arguments.add("sound");
            arguments.add("location");
            arguments.add("redefine");
            arguments.add("control");
        }
        List<String> Flist = new ArrayList<>();
        List<String> areas = new ArrayList<>();
        List<String> fregion = new ArrayList<>();

        PluginData.getAllRegions().keySet().forEach((s) -> {
            areas.add(s);
        });
        List<String> locations = new ArrayList<>();
        List<String> flocation = new ArrayList<>();

        PluginData.getLocSounds().keySet().forEach((s) -> {
            locations.add(s);
        });

        switch (args.length) {
            case 1:
                for (String s : arguments) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        Flist.add(s);
                    }
                }
                return Flist;
            case 2:
                if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("details") || args[0].equalsIgnoreCase("sound") || args[0].equalsIgnoreCase("redefine")) {

                    for (String s : areas) {
                        if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                            fregion.add(s);
                        }
                    }
                    return fregion;

                } else if (args[0].equalsIgnoreCase("location")) {
                    List<String> l = Arrays.asList("add", "remove");
                    return l;

                } else if (args[0].equalsIgnoreCase("list")) {
                    List<String> l = Arrays.asList("region", "location");
                    return l;

                } else if (args[0].equalsIgnoreCase("control")) {
                    List<String> l = Arrays.asList("shutdown", "enable", "realtime", "reload");
                    return l;

                } else {
                    return null;
                }
            case 3:
                if (args[0].equalsIgnoreCase("edit")) {

                    List<String> l = Arrays.asList("rain", "sun");
                    return l;

                } else if (args[0].equalsIgnoreCase("sound")) {

                    List<String> l = Arrays.asList("none", "plain", "cave", "forest", "ocean", "wind", "swampland");
                    return l;

                } else if (args[0].equalsIgnoreCase("location")) {
                    if (args[1].equalsIgnoreCase("remove")) {
                        for (String s : locations) {
                            if (s.toLowerCase().startsWith(args[2].toLowerCase())) {
                                flocation.add(s);
                            }
                        }
                        return flocation;
                    } else {
                        return null;
                    }
                } else {

                    return null;
                }
            case 4:
                if (args[0].equalsIgnoreCase("edit")) {

                    List<String> l = Arrays.asList("true", "false");
                    return l;

                } else if (args[0].equalsIgnoreCase("location")) {
                    if (args[1].equalsIgnoreCase("add")) {
                        List<String> l = Arrays.asList("bell", "none");
                        return l;

                    } else {
                        return null;
                    }
                } else {

                    return null;
                }
            default:
                return null;
        }

    }

    private void sendNoSubcommandErrorMessage(CommandSender cs) {
        //MessageUtil.sendErrorMessage(cs, "You're missing subcommand name for this command.");
        PluginDescriptionFile descr = Environment.getPluginInstance().getDescription();
        PluginData.getMessageUtils().sendErrorMessage(cs, descr.getName() + " - version " + descr.getVersion());
    }

    private void sendSubcommandNotFoundErrorMessage(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "Subcommand not found.");
    }

    private void addCommandHandler(String name, EnvironmentCommand handler) {
        commands.put(name, handler);
    }

}
