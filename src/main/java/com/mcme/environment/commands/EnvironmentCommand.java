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
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public abstract class EnvironmentCommand {

    private final String[] permissionNodes;

    @Getter
    private final int minArgs;

    private boolean playerOnly = true;

    @Getter
    @Setter
    private String usageDescription, shortDescription;

    public EnvironmentCommand(int minArgs, boolean playerOnly, String... permissionNodes) {
        this.minArgs = minArgs;
        this.playerOnly = playerOnly;
        this.permissionNodes = permissionNodes;
    }

    public void handle(CommandSender cs, String... args) {
        Player p = null;
        if (cs instanceof Player) {
            p = (Player) cs;
        }

        if (p == null && playerOnly) {
            sendPlayerOnlyErrorMessage(cs);
            return;
        }

        if (p != null && !hasPermissions(p)) {
            sendNoPermsErrorMessage(p);
            return;
        }

        if (args.length < minArgs) {
            sendMissingArgumentErrorMessage(cs);
            return;
        }

        execute(cs, args);
    }

    protected abstract void execute(CommandSender cs, String... args);

    private void sendPlayerOnlyErrorMessage(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "You have to be logged in to run this command.");
    }

    private void sendNoPermsErrorMessage(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "You don't have permission to run this command.");
    }

    protected void sendMissingArgumentErrorMessage(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "You're missing arguments for this command.");
    }

    protected boolean hasPermissions(Player p) {
        if (permissionNodes != null) {
            int l = permissionNodes.length;
            switch (l) {
                case 1:
                    if (p.hasPermission(permissionNodes[0])) {
                        return true;
                    }
                    return false;
                case 2:
                    if (p.hasPermission(permissionNodes[0]) || p.hasPermission(permissionNodes[1])) {
                        return true;
                    }
                    return false;

                case 3:
                    if (p.hasPermission(permissionNodes[0]) || p.hasPermission(permissionNodes[1]) || p.hasPermission(permissionNodes[2])) {
                        return true;
                    }
                    return false;
                case 4:
                    if (p.hasPermission(permissionNodes[0]) || p.hasPermission(permissionNodes[1]) || p.hasPermission(permissionNodes[2]) || p.hasPermission(permissionNodes[3])) {
                        return true;
                    }
                    return false;

            }

        }
        return true;
    }

}
