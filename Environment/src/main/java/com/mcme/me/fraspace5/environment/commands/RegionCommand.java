/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.me.fraspace5.environment.commands;

import com.mcme.me.fraspace5.environment.Region.EnvRegion;
import com.mcme.me.fraspace5.environment.util.RegionUtil;
import com.mcme.me.fraspace5.environment.util.WEUtil;
import com.mcme.me.fraspace5.environment.Environment;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class RegionCommand implements CommandExecutor {
 
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] a)
    {
    Player player = (Player) cs;
        
        if (player instanceof Player){
          
            if (a.length == 0){
            
            player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+"/env create|bounds");
            return true;
            }
            if (a[0].equalsIgnoreCase("create")){
               if (player.hasPermission("environment.admin")){
               if (a.length == 3){
               
               if (WEUtil.worldEditExists() && WEUtil.hasSelection(player))
                {
                    EnvRegion r = new EnvRegion(true);
                    
                    
                    
                    
                    
                    player.sendMessage("Setting current selection as region bounds...");
                    RegionUtil.attachRegionBuilder(player, r);
                    WEUtil.setWERegion(player);

                
                
                }else {
               player.sendMessage("You have to make a selection first");
               
               }
                   
                   
                   
                   
               }else {
               player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+"Not enough argouments");
               }

               }else {
               
               player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+"No permissions to use this command!");
               
               }
            
            
            
            
            
            }
                
                
                
                
                
            
            
        
        
        
        
        
        
        }else{
        System.out.println("You can't use this command in console");
        
        
        }
    
    
    
    
    
    
    
    
    
    
    return false;
    }
    
    
    
    
}
