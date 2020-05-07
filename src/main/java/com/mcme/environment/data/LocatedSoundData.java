/*
 * Copyright (C) 2020 MCME (Fraspace5)
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
