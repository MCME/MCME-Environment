/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.commands;

import com.mcme.environment.data.PluginData;
import com.mcmiddleearth.pluginutil.NumericUtil;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import com.mcmiddleearth.pluginutil.message.MessageType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentList extends EnvironmentCommand {

    public EnvironmentList(String... permissionNodes) {
        super(1, true, permissionNodes);
        setShortDescription(": List of all regions");
        setUsageDescription(": List");
    }
//environment list 1

    @Override
    protected void execute(final CommandSender cs, final String... args) {
        int pageIndex = 0;
        Player pl = (Player) cs;
        if (args.length > 0 && (!NumericUtil.isInt(args[0]))) {

            pageIndex = 1;
        }
        int page = 1;

        if (args.length > pageIndex && NumericUtil.isInt(args[pageIndex])) {
            page = NumericUtil.getInt(args[pageIndex]);
        }

        FancyMessage header = new FancyMessage(MessageType.WHITE, PluginData.getMessageUtil())
                .addSimple(ChatColor.GREEN + "Project opens (click for details)" + "\n");
        List<FancyMessage> messages = new ArrayList<>();
       
        for (String region : PluginData.AllRegions.keySet()) {
            FancyMessage r = new FancyMessage(MessageType.WHITE, PluginData.getMessageUtil())
                    .addSimple(ChatColor.DARK_GREEN + region + "\n");
        }
        
        PluginData.getMessageUtil().sendFancyListMessage((Player) cs, header, messages, "/environment list ", page);
    }
}
