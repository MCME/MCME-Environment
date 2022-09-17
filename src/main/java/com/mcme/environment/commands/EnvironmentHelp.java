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

import com.mcme.environment.data.PluginData;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentHelp extends EnvironmentCommand {

    public EnvironmentHelp(String... permissionNodes) {
        super(0, true, permissionNodes);
        setShortDescription(": Help for Guidebook.");
        setUsageDescription(" [subcommand]: Without a given [subcommand] shows short help messages for all Guidebook commands. With additional argument shows detailed help for [subcommand].");
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        sendHelpStartMessage(cs);
        Map<String, EnvironmentCommand> commands = ((EnvironmentCommandExecutor) Bukkit.getPluginCommand("environment")
                .getExecutor()).getCommands();
        if (args.length > 0) {
            EnvironmentCommand command = commands.get(args[0]);
            if (command == null) {
                sendNoSuchCommandMessage(cs, args[0]);
            } else {
                String description = command.getUsageDescription();
                if (description == null) {
                    description = command.getShortDescription();
                }
                if (description != null) {
                    sendDescriptionMessage(cs, args[0], description);
                } else {
                    sendNoDescriptionMessage(cs, args[0]);
                }
            }
        } else {
            Set<String> keys = commands.keySet();
            keys.forEach((key) -> {
                String description = commands.get(key).getShortDescription();
                if (description != null) {
                    sendDescriptionMessage(cs, key, description);
                } else {
                    sendNoDescriptionMessage(cs, key);
                }
            });
        }
        sendManualMessage(cs);
    }

    private void sendHelpStartMessage(CommandSender cs) {
        PluginData.getMessageUtils().sendInfoMessage(cs, "Help for Environment plugin.");
    }

    private void sendNoSuchCommandMessage(CommandSender cs, String arg) {
        PluginData.getMessageUtils().sendNoPrefixInfoMessage(cs, "/environment " + arg + ": There is no such command.");
    }

    private void sendDescriptionMessage(CommandSender cs, String arg, String description) {
        PluginData.getMessageUtils().sendNoPrefixInfoMessage(cs, "/environment " + arg + description);
    }

    private void sendNoDescriptionMessage(CommandSender cs, String arg) {
        PluginData.getMessageUtils().sendNoPrefixInfoMessage(cs, "/environment " + arg + ": There is no help for this command.");
    }

    private void sendManualMessage(CommandSender cs) {
        PluginData.getMessageUtils().sendNoPrefixInfoMessage(cs, "Manual: https://www.mcmiddleearth.com/community/resources/mcme-environment-plugin.149/ ");
    }
}
