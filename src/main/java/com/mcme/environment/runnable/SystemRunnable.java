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
package com.mcme.environment.runnable;

import com.mcme.environment.Environment;
import com.mcme.environment.data.PluginData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class SystemRunnable {

    public static void runnableLocations() {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (!Environment.getNameserver().equalsIgnoreCase("default")) {
                    PluginData.loadLocations();
                }

            }

        }.runTaskTimer(Environment.getPluginInstance(), 50L, 200L);

    }

    public static void ConnectionRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    if (!Environment.getPluginInstance().getConnection().isValid(2)) {
                        Environment.getPluginInstance().getConnection().close();
                        Environment.getPluginInstance().openConnection();

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskTimerAsynchronously(Environment.getPluginInstance(), 60L, 800L);

    }
}
