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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 *
 */
public class Environment extends JavaPlugin implements PluginMessageListener {

    static final Logger Logger = Bukkit.getLogger();
    @Getter
    public ConsoleCommandSender clogger = this.getServer().getConsoleSender();
    @Getter
    private static Environment pluginInstance;
    @Getter
    @Setter
    public static String nameserver;
    @Getter
    public static ProtocolManager manager;
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
            ConnectionRunnable();
            Environment.setNameserver("default");

            new BukkitRunnable() {

                @Override
                public void run() {
                    PluginData.loadRegions();
                }

            }.runTaskLater(Environment.getPluginInstance(), 200L);
        }

    }

    @Override
    public void onDisable() {
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
        clogger.sendMessage(ChatColor.DARK_GREEN + "Environment Plugin v" + this.getDescription().getVersion() + " disabled!");
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
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
                            + "  `weather` VARCHAR(45),\n"
                            + "  `sound` VARCHAR(45),\n"
                            + "  `info_sound` LONGTEXT,\n"
                            + "  `thunders` BOOLEAN,\n"
                            + "  `location` LONGTEXT NOT NULL,\n"
                            + "  `weight` INT,\n"
                            + "  `time` LONGTEXT,\n"
                            + "  `server` VARCHAR(100) NOT NULL,\n"
                            + "  PRIMARY KEY (`idregion`));";
                    String stat2 = "CREATE TABLE IF NOT EXISTS `" + database + "`.`environment_players` (\n"
                            + "  `uuid` VARCHAR(45) NOT NULL,\n"
                            + "  `bool` BOOLEAN NOT NULL,\n"
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

    public void ConnectionRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    if (!con.isValid(2)) {

                        openConnection();

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskTimer(Environment.getPluginInstance(), 60L, 100L);

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

}
