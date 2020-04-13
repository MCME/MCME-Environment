/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.mcme.environment.commands.EnvironmentCommandExecutor;
import com.mcme.environment.listeners.PlayerListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 *
 */
public class Environment extends JavaPlugin {

    static final Logger Logger = Bukkit.getLogger();

    @Getter
    public ConsoleCommandSender clogger = this.getServer().getConsoleSender();

    @Getter
    private static Environment pluginInstance;

    @Getter
    public static String nameserver;
    @Getter
    public static ProtocolManager manager;
    @Getter
    private WrappedDataWatcher thunderWatcher;

    @Override
    public void onEnable() {
        pluginInstance = this;
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults();
        try {
            openConnection();
        } catch (SQLException ex) {
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Environment" + ChatColor.DARK_GRAY + "] - " + ChatColor.RED + "Database error!");
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        getCommand("environment").setExecutor(new EnvironmentCommandExecutor());
        getCommand("environment").setTabCompleter(new EnvironmentCommandExecutor());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        manager = ProtocolLibrary.getProtocolManager();
        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");
        clogger.sendMessage(ChatColor.DARK_GREEN + "Environment Plugin v" + this.getDescription().getVersion() + " enabled!");
        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");
    }

    @Override
    public void onDisable() {
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
        clogger.sendMessage(ChatColor.DARK_GREEN + "Environment Plugin v" + this.getDescription().getVersion() + " disabled!");
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
    }

    public void openConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            return;
        }
        if (Environment.getPluginInstance().password.equalsIgnoreCase("default")) {
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Environment" + ChatColor.DARK_GRAY + "] - " + ChatColor.YELLOW + "Plugin INITIALIZED, change database information!");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {

            con = DriverManager.getConnection("jdbc:mysql://" + Environment.getPluginInstance().host + ":"
                    + Environment.getPluginInstance().port + "/"
                    + Environment.getPluginInstance().database + "?useSSL=false&allowPublicKeyRetrieval=true",
                    Environment.getPluginInstance().username,
                    Environment.getPluginInstance().password);
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Environment" + ChatColor.DARK_GRAY + "] - " + ChatColor.GREEN + "Database Found! ");

            new BukkitRunnable() {

                @Override
                public void run() {

                    String stat = "CREATE TABLE IF NOT EXISTS `" + database + "`.`environment_regions_data` (\n"
                            + "  `idregion` VARCHAR(45) NOT NULL,\n"
                            + "  `name` VARCHAR(45) NOT NULL,\n"
                            + "  `type` VARCHAR(45) NOT NULL,\n"
                            + "  `xlist` LONGTEXT NOT NULL,\n"
                            + "  `zlist` LONGTEXT NOT NULL,\n"
                            + "  `ymin` INT NOT NULL,\n"
                            + "  `ymax` INT NOT NULL,\n"
                            + "  `weather` VARCHAR(5) NOT NULL),\n"
                            + "  `thunders` BOOLEAN NOT NULL),\n"
                            + "  `time` LONGTEXT NOT NULL),\n"
                            + "  `server` VARCHAR(100) NOT NULL,\n"
                            + "  PRIMARY KEY (`idregion`));";
                    String stat2 = "CREATE TABLE IF NOT EXISTS `" + database + "`.`environment_players` (\n"
                            + "  `uuid` VARCHAR(45) NOT NULL,\n"
                            + "  `bool` BOOLEAN NOT NULL),\n"
                            + "  PRIMARY KEY (`uuid`));";
                    try {
                        con.createStatement().execute(stat);
                        con.createStatement().execute(stat2);
                    } catch (SQLException ex) {
                        Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.runTaskAsynchronously(Environment.getPluginInstance());
        }

    }

    @Getter
    public Connection con;
    @Getter
    String host = this.getConfig().getString("host");
    @Getter
    String port = this.getConfig().getString("port");
    @Getter
    public String database = this.getConfig().getString("database");
    @Getter
    String username = this.getConfig().getString("username");
    @Getter
    String password = this.getConfig().getString("password");

}
