/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Fraspace5
 */
public class EnterRegionEvent extends Event implements Cancellable {

  
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    private Player pl;
    private String name;

    public EnterRegionEvent(Player player, String region) {
        this.isCancelled = false;
        this.pl = player;
        this.name = region;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Player getPlayer() {
        return pl;
    }

    public String getNameRegion() {
        return name;
    }

}
