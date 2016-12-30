/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.resourceregions;

import me.dags.resourceregions.util.Constants;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class RRDevCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender cs, Command arg1, String arg2, String[] args) {
        if (cs instanceof Player && !((Player)cs).hasPermission(Constants.REGION_PERM))
        {
            ((Player)cs).sendMessage("Sorry you don't have permission.");
            return true;
        }
        if(args.length>0 && args[0].equalsIgnoreCase("true")) {
            DevUtil.setConsoleOutput(true);
            showDetails(cs);
            return true;
        }
        else if(args.length>0 && args[0].equalsIgnoreCase("false")) {
            DevUtil.setConsoleOutput(false);
            showDetails(cs);
            return true;
        }
        else if(args.length>0) {
            try {
                int level = Integer.parseInt(args[0]);
                DevUtil.setLevel(level);
                showDetails(cs);
                return true;
            }
            catch(NumberFormatException e){};
        }
        if(cs instanceof Player) {
            Player player = (Player) cs;
            if(args.length>0 && args[0].equalsIgnoreCase("r")) {
                DevUtil.remove(player);
                showDetails(cs);
                return true;
            }
            DevUtil.add(player);
            showDetails(cs);
        }
        return false;
    }
    
    private void showDetails(CommandSender cs) {
        cs.sendMessage("DevUtil: Level - "+DevUtil.getLevel()+"; Console - "+DevUtil.isConsoleOutput()+"; ");
        cs.sendMessage("         Developer:");
        for(OfflinePlayer player:DevUtil.getDeveloper()) {
        cs.sendMessage("                "+player.getName());
        }
    }

}

