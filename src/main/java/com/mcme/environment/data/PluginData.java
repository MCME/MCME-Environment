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

import com.mcme.environment.Environment;
import com.mcme.environment.SoundPacket.SoundType;
import com.mcmiddleearth.pluginutil.message.MessageUtil;
import com.mcmiddleearth.pluginutil.region.CuboidRegion;
import com.mcmiddleearth.pluginutil.region.PrismoidRegion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 *
 * @author Fraspace5
 */
public class PluginData {

    @Getter
    private static final MessageUtil messageUtils = new MessageUtil();

    @Getter
    @Setter
    private static BossBar bossbar;

    static {
        messageUtils.setPluginName("Environment-MCME");
    }
    /**
     * String and regions data
     */
    @Getter
    private static final Map<String, RegionData> AllRegions = new HashMap<>();

    @Getter
    private static final Map<UUID, Boolean> boolPlayers = new HashMap<>();
    //IdRegion, Players
    @Getter
    private static final Map<String, LocatedSoundData> locSounds = new HashMap<>();

    /**
     * It reloads all regions from database
     */
    public static void loadRegions() {
        AllRegions.clear();

        new BukkitRunnable() {

            @Override
            public void run() {

                try {

                    final ResultSet r = Environment.getSelectRegions().executeQuery();

                    if (r.first()) {
                        do {

                            if (r.getString("type").equalsIgnoreCase("cuboid")) {

                                String[] xlist = unserialize(r.getString("xlist"));
                                String[] zlist = unserialize(r.getString("zlist"));
                                String[] location = unserialize(r.getString("location"));

                                Integer ymin = r.getInt("ymin");
                                Integer ymax = r.getInt("ymax");
                                Vector minCorner = new Vector(parseInt(xlist[0]),
                                        ymin,
                                        parseInt(zlist[0]));
                                Vector maxCorner = new Vector(parseInt(xlist[1]),
                                        ymax,
                                        parseInt(zlist[1]));

                                Location loc = new Location(Bukkit.getWorld(location[0]), parseDouble(location[1]), parseDouble(location[2]), parseDouble(location[3]));
                                CuboidRegion rr = new CuboidRegion(loc, minCorner, maxCorner);

                                AllRegions.put(r.getString("name"), new RegionData(r.getString("name"), UUID.fromString(r.getString("idregion")), rr, r.getString("server"), r.getString("type"), r.getString("weather"), r.getBoolean("thunders"), r.getString("time"), r.getInt("weight"), SoundType.valueOf(r.getString("sound"))));

                            } else {

                                String[] location = unserialize(r.getString("location"));

                                Integer ymin = r.getInt("ymin");
                                Integer ymax = r.getInt("ymax");
                                List<Integer> xlist = StringtoListInt(unserialize(r.getString("xlist")));
                                List<Integer> zlist = StringtoListInt(unserialize(r.getString("zlist")));
                                Location loc = new Location(Bukkit.getWorld(location[0]), parseDouble(location[1]), parseDouble(location[2]), parseDouble(location[3]));

                                PrismoidRegion rr = new PrismoidRegion(loc, xlist, zlist, ymin, ymax);
                                AllRegions.put(r.getString("name"), new RegionData(r.getString("name"), UUID.fromString(r.getString("idregion")), rr, r.getString("server"), r.getString("type"), r.getString("weather"), r.getBoolean("thunders"), r.getString("time"), r.getInt("weight"), SoundType.valueOf(r.getString("sound"))));

                            }

                        } while (r.next());

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskAsynchronously(Environment.getPluginInstance());

    }

    public static void loadLocations() {
        locSounds.clear();

        new BukkitRunnable() {

            @Override
            public void run() {

                try {

                    final ResultSet r = Environment.getSelectLocations().executeQuery();

                    if (r.first()) {
                        do {
                            String[] location = unserialize(r.getString("location"));

                            Location loc;

                            if (Environment.getNameserver().equalsIgnoreCase(r.getString("server"))) {
                                loc = new Location(Bukkit.getWorld(location[0]), parseDouble(location[1]), parseDouble(location[2]), parseDouble(location[3]));

                                locSounds.put(r.getString("name"), new LocatedSoundData(loc, r.getString("name"), r.getString("server"), SoundType.valueOf(r.getString("sound")), UUID.fromString(r.getString("idlocation"))));

                            }

                        } while (r.next());

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskAsynchronously(Environment.getPluginInstance());

    }

    public static String[] unserialize(String line) {

        String[] dataArray = line.split(";");

        return dataArray;

    }

    public static UUID createId() {

        return UUID.randomUUID();

    }

    private static List<Integer> StringtoListInt(String[] s) {

        List<Integer> list = new ArrayList();

        for (String item : s) {
            list.add(Integer.parseInt(item));
        }

        return list;
    }

    public static String getNameFromUUID(UUID uuid) {
        String name = "";

        for (String s : PluginData.AllRegions.keySet()) {
            if (PluginData.AllRegions.get(s).getIdregion().equals(uuid)) {
                name = s;
            }
        }

        return name;
    }

    public static void onSave(File projectFolder) throws IOException {

        for (String regionName : AllRegions.keySet()) {
            if (!AllRegions.get(regionName).locData.getLeaves().isEmpty() || !AllRegions.get(regionName).locData.getWater().isEmpty()) {
                File regionFile = new File(projectFolder, regionName + ".yml");

                AllRegions.get(regionName).locData.save(regionFile);
            }
        }

        Environment.getPluginInstance().getClogger().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Environment" + ChatColor.DARK_GRAY + "] - " + ChatColor.BLUE + "Saving " + ChatColor.DARK_GRAY + AllRegions.size() + " regions...");

    }

    public static void onLoad(File projectFolder) throws IOException, FileNotFoundException, InvalidConfigurationException {

        for (File projectFile : projectFolder.listFiles()) {
            if (AllRegions.containsKey(projectFile.getName().substring(0, projectFile.getName().length() - 4))) {
                RegionData redata = AllRegions.get(projectFile.getName().substring(0, projectFile.getName().length() - 4));

                redata.locData.load(projectFile, redata.getRegion().getWorld());
            } else {
                projectFile.delete();
            }

        }

        Environment.getPluginInstance().getClogger().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Environment" + ChatColor.DARK_GRAY + "] - " + ChatColor.BLUE + "Loading regions: " + ChatColor.DARK_GRAY + projectFolder.listFiles().length + ChatColor.BLUE + " Found");

    }

}
