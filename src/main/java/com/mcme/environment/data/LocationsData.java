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
package com.mcme.environment.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Fraspace5
 */
public class LocationsData {

    public List<Location> water;

    public List<Location> leaves;

    public LocationsData() {

        water = new ArrayList<>();
        leaves = new ArrayList<>();

    }

    public LocationsData(List<Location> s, List<Location> l) {

        water = s;

        leaves = l;

    }

    public void save(File file) throws IOException {

        YamlConfiguration config = new YamlConfiguration();
        List<String> locationsWater = new ArrayList<>();
        List<String> locationsLeaves = new ArrayList<>();

        for (Location w : water) {
            String s = w.getX() + ";" + w.getY() + ";" + w.getZ();
            locationsWater.add(s);
        }
        for (Location w : leaves) {
            String s = w.getX() + ";" + w.getY() + ";" + w.getZ();
            locationsLeaves.add(s);
        }

        config.set("waterList", locationsWater);
        config.set("leavesList", locationsLeaves);

        config.save(file);
    }

    public void load(File file, World world) throws IOException, FileNotFoundException, InvalidConfigurationException {
        YamlConfiguration config = new YamlConfiguration();

        config.load(file);

        List<String> waterList = (List<String>) config.getList("waterList");
        List<String> leavesList = (List<String>) config.getList("leavesList");

        for (String s : waterList) {

            String[] line = PluginData.unserialize(s);

            Location l = new Location(world, parseDouble(line[0]), parseDouble(line[1]), parseDouble(line[2]));
            water.add(l);
        }
        for (String s : leavesList) {

            String[] line = PluginData.unserialize(s);

            Location l = new Location(world, parseDouble(line[0]), parseDouble(line[1]), parseDouble(line[2]));
            leaves.add(l);
        }

    }
}
