/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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
    public static String nameserver;
    @Getter
    public static ProtocolManager manager;

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
            nameserver = "default";
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
            nameserver = servern;

        }
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
                            + "  `weather` VARCHAR(5),\n"
                            + "  `thunders` BOOLEAN,\n"
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

    public void sendNameServer(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("GetServer");

        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

}
