/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.me.fraspace5.environment;

import com.mcme.me.fraspace5.environment.PluginData;
import com.mcme.me.fraspace5.environment.Region.EnvRegion;
import com.mcme.me.fraspace5.environment.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author simonagottardi
 */

public class RpManager {
    
    private static final File playerFile = new File(Environment.getPluginInstance().getDataFolder(),"/playerRpData.dat");
    private static final File regionFolder = new File(Environment.getPluginInstance().getDataFolder(),"regions");
    
    @Getter
    private static Map<String, EnvRegion> regions = new HashMap<>();
    
    private static Map<UUID,RpPlayerData> playerRpData = new HashMap<>();
    
    @Getter
    private static RegionEditConversationFactory regionEditConversationFactory
            = new RegionEditConversationFactory(Environment.getPluginInstance());
    
    public static void init() {
        loadPlayerData();
        if(!regionFolder.exists()) {
            regionFolder.mkdir();
        }
        regions.clear();
        for(File file: regionFolder.listFiles((File dir, String name) -> name.endsWith(".reg"))) {
            try {
                YamlConfiguration config = new YamlConfiguration();
                config.load(file);
                final ConfigurationSection section = config.getConfigurationSection("rpRegion");
                new BukkitRunnable() {
                    int counter = 10;
                    @Override
                    public void run() {
                        EnvRegion region = (EnvRegion) EnvRegion.loadFromMap((Map<String,Object>)section.getValues(true));
                        if(region!=null) {
                            
                            addRegion(region);  
                            cancel();
                        } else {
                            counter--;
                            
                            if(counter<1) {
                                cancel();
                            }
                        }
                    }
                }.runTaskTimer(Environment.getPluginInstance(), 200, 20);
            } catch (IOException | InvalidConfigurationException ex) {
                Logger.getLogger(RpManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static EnvRegion getRegion(Location loc) {
        EnvRegion maxWeight = null;
        for(EnvRegion region: regions.values()) {
            
        }
        return maxWeight;
    }
    
    public static EnvRegion getRegion(String name) {
        return regions.get(name);
    }
    
   
    
    public static boolean removeRegion(String name) {
        EnvRegion region = regions.get(name);
        if(region!=null) {
            regions.remove(name);
            new File(regionFolder,name+".reg").delete();
            
            return true;
        }
        return false;
    }
    
    public static void addRegion(EnvRegion region) {
        regions.put(region.getName(), region);
        
    }
    
    public static RpPlayerData getPlayerData(Player player) {
        RpPlayerData data = playerRpData.get(player.getUniqueId());
        if(data == null) {
            data = new RpPlayerData();
            playerRpData.put(player.getUniqueId(), data);
        }
        return data;
    }
    
    /**
     * Gets the first configured URL of the given rp name.
     * @param rp name of the RP
     * @param player player to use rp setting from, may be null for default settings
     * @return first confiured URL in config.yml
     */

    
    public static String getResolutionKey(int px) {
        return px+"px";
    }
    
    public static void savePlayerData() {
        try {
            if(!playerFile.exists()) {
                playerFile.createNewFile();
            }
            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(playerFile))) {
                
                out.writeObject(playerRpData);
            }
        } catch (IOException ex) {
            Logger.getLogger(RpManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void loadPlayerData() {
        if(playerFile.exists()) {
            try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(playerFile))) {
                playerRpData = (Map<UUID,RpPlayerData>) in.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(RpManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void saveRpRegion(EnvRegion region) {
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.set("rpRegion", region.saveToMap());
            config.save(new File(regionFolder,region.getName()+".reg"));
        } catch (IOException ex) {
            Logger.getLogger(RpManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
