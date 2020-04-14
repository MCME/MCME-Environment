/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.commands;

import com.mcme.environment.data.PluginData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentOff extends EnvironmentCommand {

    public EnvironmentOff(String... permissionNodes) {
        super(1, true, permissionNodes);
        setShortDescription(": Switch off");
        setUsageDescription(": Your time and weather won't change along the fellowship path");
    }

     @Override
    protected void execute(final CommandSender cs, final String... args) {
        Player pl = (Player) cs;

        if (PluginData.boolPlayers.get(pl.getUniqueId())) {
            PluginData.boolPlayers.remove(pl.getUniqueId());
            PluginData.boolPlayers.put(pl.getUniqueId(), Boolean.FALSE);
        } else {
            sendAlready(cs);
        }

    }

    private void sendDone(CommandSender cs) {
        PluginData.getMessageUtil().sendInfoMessage(cs, "Switched off!");

    }

    private void sendAlready(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "It's already off");

    }
}
