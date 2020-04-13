/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    private final String permissionReload = "project.reload";

    public EnvironmentCommandExecutor() {
        addCommandHandler("create", new EnvironmentCreate(permissionStaff));
        addCommandHandler("remove", new EnvironmentRemove(permissionStaff));
        addCommandHandler("list", new EnvironmentList(permissionStaff));
        addCommandHandler("on", new EnvironmentOn(permissionStaff, permission));
        addCommandHandler("off", new EnvironmentOff(permissionStaff, permission));
        addCommandHandler("edit", new EnvironmentEdit(permissionStaff));
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (!string.equalsIgnoreCase("project")) {
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
        }
        List<String> Flist = new ArrayList<String>();
        List<String> areas = new ArrayList<String>();
        List<String> fregion = new ArrayList<>();

        for (String s : PluginData.AllRegions.keySet()) {
            areas.add(s);

        }

        if (args.length == 1) {
            for (String s : arguments) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    Flist.add(s);
                }
            }
            return Flist;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("edit") || args[1].equalsIgnoreCase("remove")) {

                for (String s : areas) {
                    if (s.toLowerCase().startsWith(args[2].toLowerCase())) {
                        fregion.add(s);
                    }
                }
                return fregion;

            } else {
                return null;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("edit")) {

                List<String> l = Arrays.asList("rain", "sun");
                return l;

            } else {

                return null;
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("edit")) {

                List<String> l = Arrays.asList("true", "false");
                return l;

            } else {

                return null;
            }
        } else {
            return null;
        }

    }

    private void sendNoSubcommandErrorMessage(CommandSender cs) {
        //MessageUtil.sendErrorMessage(cs, "You're missing subcommand name for this command.");
        PluginDescriptionFile descr = Environment.getPluginInstance().getDescription();
        PluginData.getMessageUtil().sendErrorMessage(cs, descr.getName() + " - version " + descr.getVersion());
    }

    private void sendSubcommandNotFoundErrorMessage(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "Subcommand not found.");
    }

    private void addCommandHandler(String name, EnvironmentCommand handler) {
        commands.put(name, handler);
    }

}
