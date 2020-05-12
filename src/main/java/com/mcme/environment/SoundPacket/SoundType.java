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
public enum SoundType {

    PLAIN(0),
    OCEAN(15),
    WIND(0),
    CAVE(0),
    SWAMPLAND(0),
    NONE(0),
    FOREST(0),
    BELL(100),
    LEAVES(15);
    private final Integer distance;

    SoundType(Integer env) {
        this.distance = env;
    }

    public Integer getDistanceTrigger() {
        return distance;
    }
}
