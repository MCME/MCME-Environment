/*
 *Copyright (C) 2020 MCME (Fraspace5)
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
package com.mcme.environment.SoundPacket;

/**
 *
 * @author Fraspace5
 */
public enum SoundsString {

    GENERIC_TREE_DAY("generic.tree.day"),
    //  Plains
    PLAINS_BIRD_DAY("plains.bird.day"),
    PLAINS_BIRD_MORNING("plains.bird.morning"),
    PLAINS_BIRD_NIGHT("plains.bird.night"),
    PLAINS_INSECT_DAY("plains.insect.day"),
    PLAINS_INSECT_MORNING("plains.insect.morning"),
    PLAINS_INSECT_NIGHT("plains.insect.night"),
    //  Forest
    FOREST_BIRD_DAY("forest.bird.day"),
    FOREST_BIRD_MORNING("forest.bird.morning"),
    FOREST_BIRD_NIGHT("forest.bird.night"),
    FOREST_INSECT_DAY("forest.insect.day"),
    FOREST_INSECT_MORNING("forest.insect.morning"),
    FOREST_INSECT_NIGHT("forest.insect.night"),
    //  SwampLand
    SWAMPLAND_FROGS_NIGHT("swampland.frogs.night"),
    SWAMPLAND_CRICKETS_DAY("swampland.crickets.day"),
    SWAMPLAND_CRICKETS_NIGHT("swampland.crickets.night"),
    SWAMPLAND_BIRDS_DAY("swampland.birds.day"),
    SWAMPLAND_BIRDS_NIGHT("swampland.birds.night"),
    //  Caves
    CAVES_DROPLETS("environmental.caves.droplets"),
    CAVES_CRUMBLE("environmental.caves.crumble"),
    CAVE_CRICKETS("animals.insect.cave_crickets"),
    //  Ocean
    OCEAN("coastal.waves"),
    OCEAN_BIRD("coastal.gull"),
    // wind
    WIND("environmental.wind"),
    ELEVATION("environmental.wind.elevation"),
    // city
    BELL("cities.bell.one"),
    //Leaves
    LEAVES("environmental.leaves_rustle");

    private final String path;

    SoundsString(String envUrl) {
        this.path = envUrl;
    }

    public String getPath() {
        return path;
    }

}
