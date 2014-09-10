/*
 * ResourceRegions, a regions based texture-switcher
 * Copyright (c) 2014 dags <http://dags.me>
 *
 *   This program is free software: you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *   for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.dags.resourceregions;

import me.dags.resourceregions.commands.RPUrlCommand;
import me.dags.resourceregions.commands.RegionCommand;
import me.dags.resourceregions.listener.RegionListener;
import me.dags.resourceregions.region.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

/**
 * @author dags_ <dags@dags.me>
 */
public class ResourceRegions extends JavaPlugin
{

    private static ResourceRegions instance;
    private RunChecks runChecks;

    public ResourceRegions()
    {
        instance = this;
    }

    public static Plugin getPlugin()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        RegionManager.loadRegions(this);
        registerStuff();
        runChecks = new RunChecks();
        runChecks.runTaskTimer(this, 0L, 20L);
        saveConfig();
    }

    @Override
    public void onDisable()
    {
        RegionManager.stopChecks();
        RegionManager.clear();
    }

    private void registerStuff()
    {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new RegionListener(), this);
        getCommand("resourceregion").setExecutor(new RegionCommand());
        getCommand("rpurl").setExecutor(new RPUrlCommand());
    }

    public static void log(String msg)
    {
        instance.getLogger().log(Level.INFO, msg);
    }

    public static void reload()
    {
        instance.onDisable();
        instance.onEnable();
    }

    private class RunChecks extends BukkitRunnable
    {
        public void run()
        {
            if (RegionManager.isReady())
            {
                log("Starting region checks...");
                runChecks.cancel();
                RegionManager.runChecks();
            }
        }
    }

    public static void setPackUrl(String pack, String url)
    {
        getPlugin().getConfig().set(pack.toLowerCase(), url);
        getPlugin().saveConfig();
    }

    public static String getPack(String pack)
    {
        return getPlugin().getConfig().getString(pack.toLowerCase(), "");
    }

}
