/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.resourceregions.listener;

import com.mcmiddleearth.resourceregions.DevUtil;
import me.dags.resourceregions.ResourceRegions;
import me.dags.resourceregions.util.Constants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Eriol_Eandur
 */
public class PlayerListener implements Listener{
    
    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        event.getPlayer().removeMetadata(Constants.RESOURCE_REGION, ResourceRegions.getPlugin());
DevUtil.log("Still has Metadata " +event.getPlayer().hasMetadata(Constants.RESOURCE_REGION));
    }
}
