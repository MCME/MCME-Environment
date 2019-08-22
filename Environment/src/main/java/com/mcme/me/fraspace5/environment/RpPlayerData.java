/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.me.fraspace5.environment;

import com.mcme.me.fraspace5.environment.Region.EnvRegion;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

/**
 *
 * @author Fraspace5
 */
public class RpPlayerData implements Serializable {
    
    private long serialVerionsUID = 1;
            
    @Setter
    @Getter
    private String variant = "light";
    @Setter
    @Getter
    private int resolution = 16;
    @Getter
    @Setter
    private transient EnvRegion currentRegion = null;
    
}