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
import com.mcmiddleearth.pluginutil.region.Region;
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
public class RegionData {

    @Getter
    private final String name;
    @Getter
    private final UUID idregion;
    @Getter
    private final Region region;
    @Getter
    private final String server;
    @Getter
    private final String type;
    @Getter
    private final String weather;
    @Getter
    private final Boolean thunder;
    @Getter
    private final String time;
    @Getter
    private final Integer weight;
    @Getter
    private final SoundType soundAmbient;
    @Getter
    private final Map<UUID, List<BukkitTask>> tasks;
    @Getter
    private final List<UUID> informedLoc;
    @Setter
    public LocationsData locData;

    /**
     *
     * Create a new region data
     *
     * @param namem Name of the region
     * @param idregi Id of the region
     * @param rn The PluginUtil region
     * @param sr The name of the server as in BungeeCord file
     * @param t Type of the region (Cuboid or Prismoid)
     * @param we Weather type (rain|sun)
     * @param th Thunder boolean
     * @param tm Time in ticks
     * @param weg Weight of the region
     * @param sndAmb Sound type of Ambient
     * 
     *
     */
    public RegionData(String namem, UUID idregi, Region rn, String sr, String t, String we, boolean th, String tm, Integer weg, SoundType sndAmb ) {

        name = namem;

        idregion = idregi;

        region = rn;

        weather = we;

        server = sr;

        type = t;

        thunder = th;

        time = tm;

        weight = weg;

        soundAmbient = sndAmb;

        locData = new LocationsData();

        informedLoc = new ArrayList<>();

        tasks = new HashMap<>();

    }

    public Location getLocation() {
        return region.getLocation();
    }

    public boolean isInside(Location loc) {

        return region.isInside(loc);

    }

    public void addInform(UUID uuid) {
        if (!informedLoc.contains(uuid)) {
            informedLoc.add(uuid);
        }

    }
    
    public void removeInform(UUID uuid) {
        if (informedLoc.contains(uuid)) {
            informedLoc.remove(uuid);
        }
    }

    public void addTask(UUID uuid, BukkitTask task) {
        if (!tasks.containsKey(uuid)) {
            List<BukkitTask> s = new ArrayList<>();
            s.add(task);
            tasks.put(uuid, s);

        } else {
            tasks.get(uuid).add(task);
        }
    }

    public void cancelAllTasks(UUID uuid) {
        if (tasks.containsKey(uuid)) {
            tasks.get(uuid).forEach((s) -> {
                s.cancel();
            });
        }

    }

    public void addInfoTask(UUID uuid, BukkitTask task) {

        addTask(uuid, task);
        addInform(uuid);

    }
}
