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

import com.mcme.environment.Util.EnvChange;
import com.mcme.environment.data.PluginData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentOff extends EnvironmentCommand {

    public EnvironmentOff(String... permissionNodes) {
        super(0, true, permissionNodes);
        setShortDescription(": Switch off");
        setUsageDescription(": Your time and weather won't change along the fellowship path");
    }

    @Override
    protected void execute(final CommandSender cs, final String... args) {
        Player pl = (Player) cs;

        if (PluginData.getBoolPlayers().get(pl.getUniqueId())) {
            PluginData.getBoolPlayers().remove(pl.getUniqueId());
            PluginData.getBoolPlayers().put(pl.getUniqueId(), Boolean.FALSE);
            EnvChange.resetAll(pl);
            sendDone(cs);
        } else {
            sendAlready(cs);
        }

    }

    private void sendDone(CommandSender cs) {
        PluginData.getMessageUtils().sendInfoMessage(cs, "Switched off!");

    }

    private void sendAlready(CommandSender cs) {
        PluginData.getMessageUtils().sendErrorMessage(cs, "It's already off");

    }

}
