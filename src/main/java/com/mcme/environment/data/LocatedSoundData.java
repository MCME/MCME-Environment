/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.data;

import com.mcme.environment.SoundPacket.SoundType;
import java.util.UUID;
import org.bukkit.Location;

/**
 *
 * @author Fraspace5
 */
public class LocatedSoundData {

    public Location loc;

    public String server;

    public String name;

    public SoundType sound;

    public UUID id;

    /**
     *
     * @param l Location
     * @param n Name of the location
     * @param s Server name
     * @param sou SoundType of the sound
     * @param uuid UUID of the locSound
     */
    public LocatedSoundData(Location l, String n, String s, SoundType sou, UUID uuid) {
        loc = l;

        server = s;

        name = n;

        sound = sou;

        id = uuid;
    }

}
