/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.SoundPacket;

import com.mcme.environment.Util.RandomCollection;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class Sound {

    public static void BirdSound(Player pl) {
        RandomCollection<Boolean> random = new RandomCollection<>();
        random.add(0.4, true);
        random.add(0.6, false);

        Boolean result = random.next();
        if (result) {

            pl.playSound(pl.getLocation(), "generic.tree.day", 0.7F, 1.0F);

        }
    }

}
