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
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
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

    public void save(File file) throws IOException {

        YamlConfiguration config = new YamlConfiguration();

        config.set("waterList", water);
        config.set("leavesList", leaves);

        config.save(file);
    }

    public void load(File file) throws IOException, FileNotFoundException, InvalidConfigurationException {
        YamlConfiguration config = new YamlConfiguration();

        config.load(file);

        water = (List<Location>) config.getList("waterList");
        leaves = (List<Location>) config.getList("leavesList");

    }
}
