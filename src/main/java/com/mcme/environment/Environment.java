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
package com.mcme.environment;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mcme.environment.commands.EnvironmentCommandExecutor;
import com.mcme.environment.data.PluginData;
import com.mcme.environment.listeners.PlayerListener;
import com.mcme.environment.runnable.RunnablePlayer;
import com.mcme.environment.runnable.SystemRunnable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Fraspace5
 *
 */
public class Environment extends JavaPlugin implements PluginMessageListener {

    static final Logger Logger = Bukkit.getLogger();

    @Getter
    private final ConsoleCommandSender clogger = this.getServer().getConsoleSender();
    @Getter
    private static Environment pluginInstance;
    @Getter
    private File envFolder;
    @Getter
    @Setter
    private static String nameserver;
    @Setter
    @Getter
    private ProtocolManager manager;
    @Getter
    private Connection connection;

    String host = this.getConfig().getString("host");
    String port = this.getConfig().getString("port");
    String database = this.getConfig().getString("database");
    String username = this.getConfig().getString("username");
    String password = this.getConfig().getString("password");

    @Getter
    @Setter
    private static boolean engine;
    @Getter
    private static PreparedStatement selectPlayer;
    @Getter
    private static PreparedStatement insertPlayerBool;
    @Getter
    private static PreparedStatement updatePlayerBool;
    @Getter
    private static PreparedStatement selectRegions;
    @Getter
    private static PreparedStatement selectLocations;
    @Getter
    private static PreparedStatement insertLocation;
    @Getter
    private static PreparedStatement removeLocation;
    @Getter
    private static PreparedStatement updateRegion;
    @Getter
    private static PreparedStatement createRegion;
    @Getter
    private static PreparedStatement editRegion;
    @Getter
    private static PreparedStatement setSound;
    @Getter
    private static PreparedStatement removeRegion;

    @Override
    public void onEnable() {
        pluginInstance = this;
        engine = true;
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults();
        try {
            openConnection();
        } catch (SQLException ex) {
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Environment" + ChatColor.DARK_GRAY + "] - " + ChatColor.RED + "Database error!");
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if (this.isEnabled()) {

            getCommand("environment").setExecutor(new EnvironmentCommandExecutor());
            getCommand("environment").setTabCompleter(new EnvironmentCommandExecutor());
            Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
            manager = ProtocolLibrary.getProtocolManager();
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
            clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");
            clogger.sendMessage(ChatColor.DARK_GREEN + "Environment Plugin v" + this.getDescription().getVersion() + " enabled!");
            clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");
            SystemRunnable.ConnectionRunnable();
            try {
                onInitiateFile();
            } catch (IOException ex) {
                Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
            }
            Environment.setNameserver("default");

            new BukkitRunnable() {

                @Override
                public void run() {
                    PluginData.loadRegions();
                    SystemRunnable.runnableLocations();
                    RunnablePlayer.runnableLocationsPlayers();
                    RunnablePlayer.runnableRegionsPlayers();
                    Bukkit.createBossBar(" ", BarColor.BLUE, BarStyle.SOLID, BarFlag.CREATE_FOG);
                }

            }.runTaskLater(Environment.getPluginInstance(), 200L);
            new BukkitRunnable() {

                @Override
                public void run() {
                    try {
                        PluginData.onLoad(envFolder);
                    } catch (IOException ex) {
                        Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidConfigurationException ex) {
                        Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }.runTaskLater(Environment.getPluginInstance(), 400L);
        }

    }

    @Override
    public void onDisable() {
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
        clogger.sendMessage(ChatColor.DARK_GREEN + "Environment Plugin v" + this.getDescription().getVersion() + " disabled!");
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
        PluginData.getAllRegions().keySet().forEach((str) -> {
            PluginData.getAllRegions().get(str).getTasks().values().forEach((s) -> {
                s.forEach((task) -> {
                    task.cancel();
                });
            });
        });

        try {
            PluginData.onSave(envFolder);
        } catch (IOException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("GetServer")) {
            String servern = in.readUTF();
            Environment.setNameserver(servern);

        }
    }

    /**
     *
     * @throws SQLException
     *
     */
    public void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        if (Environment.getPluginInstance().password.equalsIgnoreCase("default")) {
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Environment" + ChatColor.DARK_GRAY + "] - " + ChatColor.YELLOW + "Plugin INITIALIZED, change database information!");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            new BukkitRunnable() {

                @Override
                public void run() {
                    try {
                        connection = DriverManager.getConnection("jdbc:mysql://" + Environment.getPluginInstance().host + ":"
                                + Environment.pluginInstance.port + "/"
                                + Environment.getPluginInstance().database + "?useSSL=false&allowPublicKeyRetrieval=true",
                                Environment.getPluginInstance().username,
                                Environment.getPluginInstance().password);
                        clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Environment" + ChatColor.DARK_GRAY + "] - " + ChatColor.GREEN + "Database Found! ");

                        String stat = "CREATE TABLE IF NOT EXISTS `" + database + "`.`environment_regions_data` (\n"
                                + "  `idregion` VARCHAR(45) NOT NULL,\n"
                                + "  `name` VARCHAR(45) NOT NULL,\n"
                                + "  `type` VARCHAR(45) NOT NULL,\n"
                                + "  `xlist` LONGTEXT NOT NULL,\n"
                                + "  `zlist` LONGTEXT NOT NULL,\n"
                                + "  `ymin` INT NOT NULL,\n"
                                + "  `ymax` INT NOT NULL,\n"
                                + "  `weather` VARCHAR(45),\n"
                                + "  `sound` VARCHAR(45),\n"
                                + "  `thunders` BOOLEAN,\n"
                                + "  `location` LONGTEXT NOT NULL,\n"
                                + "  `weight` INT,\n"
                                + "  `time` LONGTEXT,\n"
                                + "  `server` VARCHAR(100) NOT NULL,\n"
                                + "  PRIMARY KEY (`idregion`));";
                        String stat2 = "CREATE TABLE IF NOT EXISTS `" + database + "`.`environment_locations_data` (\n"
                                + "  `name` VARCHAR(45) NOT NULL,\n"
                                + "  `idlocation` VARCHAR(45) NOT NULL,\n"
                                + "  `sound` VARCHAR(45),\n"
                                + "  `location` LONGTEXT,\n"
                                + "  `server` VARCHAR(100) NOT NULL,\n"
                                + "  PRIMARY KEY (`idlocation`));";
                        String stat3 = "CREATE TABLE IF NOT EXISTS `" + database + "`.`environment_players` (\n"
                                + "  `uuid` VARCHAR(45) NOT NULL,\n"
                                + "  `bool` BOOLEAN NOT NULL,\n"
                                + "  PRIMARY KEY (`uuid`));";

                        Statement statm1 = connection.prepareStatement(stat);
                        statm1.setQueryTimeout(10);
                        statm1.execute(stat);

                        Statement statm2 = connection.prepareStatement(stat2);
                        statm2.setQueryTimeout(10);
                        statm2.execute(stat2);

                        Statement statm3 = connection.prepareStatement(stat3);
                        statm3.setQueryTimeout(10);
                        statm3.execute(stat3);

                        prepareStatements();
                    } catch (SQLException ex) {
                        Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.runTaskAsynchronously(Environment.getPluginInstance());
        }

    }

    /**
     *
     * @param player The player that sends this message
     */
    public void sendNameServer(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("GetServer");

        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    private void onInitiateFile() throws IOException {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        envFolder = new File(Bukkit.getServer().getPluginManager().getPlugin("Environment").getDataFolder(), "locations");

        if (!envFolder.exists()) {

            envFolder.mkdir();

        }

    }

    private void prepareStatements() throws SQLException {

        String stat_select_player = "SELECT * FROM environment_players WHERE uuid = ? ;";
        String stat_insert_bool = "INSERT INTO environment_players (bool, uuid) VALUES (1, ? ) ; ";
        String stat_update_bool = "UPDATE environment_players SET bool = ? WHERE uuid = ? ;";

        String stat_select_locations = "SELECT * FROM environment_locations_data ;";
        String stat_insert_location = "INSERT INTO environment_locations_data (location, server, name, idlocation, sound) VALUES (?;?;?;?,?,?,?,?) ;";
        String stat_remove_location = "DELETE FROM environment_locations_data WHERE idlocation = ? ;";

        String stat_select_regions = "SELECT * FROM environment_regions_data ;";
        String stat_update_region = "UPDATE environment_regions_data SET type = ?, xlist = ?, zlist = ?, ymin = ?, ymax = ?, location = ?, server = ? WHERE idregion = ?;";
        String stat_edit_region = "UPDATE environment_regions_data SET thunders = ?, weather = ?, time = ? WHERE idregion = ? ;";
        String stat_create_region = "INSERT INTO environment_regions_data (idregion, name, type, xlist, zlist, ymin, ymax, location, server, weather, thunders, time, sound, weight) VALUES (?,?,?,?,?,?,?,?,?,'default','0','default',?,?) ;";

        String stat_set_sound = "UPDATE environment_regions_data SET sound = ?, info_sound = ? WHERE idregion = ? ;";

        String stat_remove_region = "DELETE FROM environment_regions_data WHERE idregion = '?' ;";

        selectPlayer = connection.prepareStatement(stat_select_player);
        insertPlayerBool = connection.prepareStatement(stat_insert_bool);
        updatePlayerBool = connection.prepareStatement(stat_update_bool);
        selectRegions = connection.prepareStatement(stat_select_regions);
        selectLocations = connection.prepareStatement(stat_select_locations);

        insertLocation = connection.prepareStatement(stat_insert_location);
        removeLocation = connection.prepareStatement(stat_remove_location);
        updateRegion = connection.prepareStatement(stat_update_region);
        editRegion = connection.prepareStatement(stat_edit_region);
        createRegion = connection.prepareStatement(stat_create_region);
        setSound = connection.prepareStatement(stat_set_sound);
        removeRegion = connection.prepareStatement(stat_remove_region);

        selectPlayer.setQueryTimeout(10);
        insertPlayerBool.setQueryTimeout(10);
        updatePlayerBool.setQueryTimeout(10);
        selectRegions.setQueryTimeout(10);
        selectLocations.setQueryTimeout(10);
        insertLocation.setQueryTimeout(10);
        removeLocation.setQueryTimeout(10);
        updateRegion.setQueryTimeout(10);
        editRegion.setQueryTimeout(10);
        createRegion.setQueryTimeout(10);
        setSound.setQueryTimeout(10);
        removeRegion.setQueryTimeout(10);
    }

}
