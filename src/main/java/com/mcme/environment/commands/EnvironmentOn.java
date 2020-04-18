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
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentOn extends EnvironmentCommand {

    public EnvironmentOn(String... permissionNodes) {
        super(0, true, permissionNodes);
        setShortDescription(": Switch on ");
        setUsageDescription(": Your time and weather will change along the fellowship path");
    }

    @Override
    protected void execute(final CommandSender cs, final String... args) {
        Player pl = (Player) cs;

        if (!PluginData.boolPlayers.get(pl.getUniqueId())) {
            PluginData.boolPlayers.remove(pl.getUniqueId());
            PluginData.boolPlayers.put(pl.getUniqueId(), Boolean.TRUE);
            sendDone(cs);
        } else {
            sendAlready(cs);
        }

    }

    private void sendDone(CommandSender cs) {
        PluginData.getMessageUtil().sendInfoMessage(cs, "Switched on!");

    }

    private void sendAlready(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "It's already on");

    }

}
