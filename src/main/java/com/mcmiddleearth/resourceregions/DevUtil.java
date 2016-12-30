/*
 * Copyright (C) 2016 MCME
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
package com.mcmiddleearth.resourceregions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class DevUtil {
    
    private static final List<UUID> developer = new ArrayList<UUID>();
    
    private static String msgColor = ""+ChatColor.GOLD;
    
    private static String plugin = "[ResourceRegions] ";
    
    private static boolean consoleOutput = false;

    private static int level = 1;
    
    public static void setConsoleOutput(boolean output) {
        consoleOutput = output;
    }
    
    public static boolean isConsoleOutput() {
        return consoleOutput;
    }
    
    public static void setLevel(int newLevel) {
        level = newLevel;
    }
    
    public static int getLevel() {
        return level;
    }
    
    public static void log(String message) {
        log(1,message);
    }
    public static void log(int msglevel, String message) {
        if(level<msglevel) {
            return;
        }
        
        for(UUID uuid:developer) {
            Player player = Bukkit.getPlayer(uuid);
            if(player!=null) {
                player.sendMessage(msgColor+plugin+message);
            }
        }
        if(consoleOutput) {
            Logger.getGlobal().info(plugin+message);
        }
    }
    
    public static void add(Player player) {
        for(UUID search: developer) {
            if(search.equals(player.getUniqueId())) {
                return;
            }
        }
        developer.add(player.getUniqueId());
    }
    
    public static void remove(Player player) {
        developer.remove(player.getUniqueId());
    }
    
    public static List<OfflinePlayer> getDeveloper() {
        List<OfflinePlayer> devs = new ArrayList<OfflinePlayer>();
        for(UUID search: developer) {
            devs.add(Bukkit.getOfflinePlayer(search));
        }
        return devs;
    }

}
