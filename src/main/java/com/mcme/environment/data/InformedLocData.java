/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.data;

import com.mcme.environment.SoundPacket.SoundType;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Fraspace5
 */
public class InformedLocData {

    public BukkitTask bcrunnable;

    public String name;

    public SoundType sound;

    public InformedLocData(BukkitTask l, String n, SoundType sou) {
        bcrunnable = l;

        name = n;

        sound = sou;

    }
}
