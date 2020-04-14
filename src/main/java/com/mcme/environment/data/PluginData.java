/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.data;

import com.mcme.environment.Environment;
import com.mcmiddleearth.pluginutil.message.MessageUtil;
import com.mcmiddleearth.pluginutil.region.CuboidRegion;
import com.mcmiddleearth.pluginutil.region.PrismoidRegion;
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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 *
 * @author Fraspace5
 */
public class PluginData {

    @Getter
    private static final MessageUtil messageUtil = new MessageUtil();

    static {
        messageUtil.setPluginName("Environment-MCME");
    }

    @Getter
    public static Map<String, RegionData> AllRegions = new HashMap<>();
    @Getter
    public static Map<UUID, Boolean> boolPlayers = new HashMap<>();

    public static void loadRegions() {
        AllRegions.clear();

        new BukkitRunnable() {

            @Override
            public void run() {

                try {
                    String statement = "SELECT * FROM " + Environment.getPluginInstance().database + ".environment_regions_data ;";

                    final ResultSet r = Environment.getPluginInstance().con.prepareStatement(statement).executeQuery();

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

                                Location loc = new Location(Bukkit.getWorld(UUID.fromString(location[0])), parseDouble(location[1]), parseDouble(location[2]), parseDouble(location[3]));

                                CuboidRegion rr = new CuboidRegion(loc, minCorner, maxCorner);

                                AllRegions.put(r.getString("name"), new RegionData(r.getString("name"), UUID.fromString(r.getString("idregion")), rr, r.getString("server"), r.getString("type"), r.getString("weather"), r.getBoolean("thunders"), r.getString("time")));

                            } else {

                                String[] xl = unserialize(r.getString("xlist"));
                                String[] zl = unserialize(r.getString("zlist"));
                                String[] location = unserialize(r.getString("location"));
                                Integer ymin = r.getInt("ymin");
                                Integer ymax = r.getInt("ymax");
                                List<Integer> xlist = StringtoListInt(unserialize(r.getString("xlist")));
                                List<Integer> zlist = StringtoListInt(unserialize(r.getString("zlist")));
                                Location loc = new Location(Bukkit.getWorld(UUID.fromString(location[0])), parseDouble(location[1]), parseDouble(location[2]), parseDouble(location[3]));

                                PrismoidRegion rr = new PrismoidRegion(loc, xlist, zlist, ymin, ymax);
                                AllRegions.put(r.getString("name"), new RegionData(r.getString("name"), UUID.fromString(r.getString("idregion")), rr, r.getString("server"), r.getString("type"), r.getString("weather"), r.getBoolean("thunders"), r.getString("time")));

                            }

                        } while (r.next());

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskAsynchronously(Environment.getPluginInstance());

    }

    public static String serialize(UUID uuid, Boolean bool) {
        return uuid + ";" + bool;
    }

    public static String[] unserialize(String line) {
        String[] dataArray = line.split(";");

        return dataArray;

    }

    public static UUID createId() {

        UUID uuid = UUID.randomUUID();

        return uuid;

    }

    public static List<Integer> StringtoListInt(String[] s) {

        List<Integer> list = new ArrayList();

        for (int i = 0; i < s.length; i++) {
            list.add(Integer.parseInt(s[i]));
        }
        return list;
    }
}
