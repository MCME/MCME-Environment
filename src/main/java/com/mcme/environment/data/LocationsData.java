/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
