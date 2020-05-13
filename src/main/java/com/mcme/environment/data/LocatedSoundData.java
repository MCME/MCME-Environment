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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Fraspace5
 */
public class LocatedSoundData {

    @Getter
    @Setter
    private Location loc;
    @Getter
    @Setter
    private String server;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private SoundType sound;
    @Getter
    @Setter
    private UUID id;
    @Setter
    private List<UUID> informed;

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

        informed = new ArrayList<>();
    }

    public Boolean playerInRange(Location location) {
        return location.distanceSquared(loc) <= sound.getDistanceTrigger();
    }

    public Boolean isInformed(UUID uuid) {
        return informed.contains(uuid);
    }

    public void addInformed(UUID uuid) {

        if (!informed.contains(uuid)) {
            informed.add(uuid);
        }

    }

    public void removeInformed(UUID uuid) {

        if (informed.contains(uuid)) {
            informed.remove(uuid);
        }

    }

}
