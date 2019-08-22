/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.me.fraspace5.environment;





import com.mcmiddleearth.pluginutil.NumericUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Fraspace5
 */
public class WorldConfig {

    @Getter
    private static final File worldConfigDir = new File(Environment.getPluginInstance().getDataFolder()
            + File.separator + "WorldConfig");

    @Getter
    private static final String cfgExtension = "yml";

    @Getter
    private static final String defaultWorldConfigName = "defaultWorldConfig";

    private static final File defaultConfigFile = new File(worldConfigDir + File.separator
            + defaultWorldConfigName + "." + cfgExtension);

    private static final String NO_PHYSICS_LIST = "noPhysicsList";

    private static final String INVENTORY_ACCESS = "inventoryAccess";

    private static final String NO_INTERACTION = "noInteraction";

    // 1.13 moved to NoPhysicsData private List<Integer> npList;
    private final String worldName;

    @Getter
    private YamlConfiguration worldConfig;

    private YamlConfiguration defaultConfig;

    private List<BlockData> noInteraction = new ArrayList<>();
    
    static {
        if (!worldConfigDir.exists()) {
            worldConfigDir.mkdirs();
        }
    }

    public WorldConfig(String worldName, YamlConfiguration defaultConfig) {
        this.defaultConfig = defaultConfig;
        this.worldName = worldName;
        if (!worldConfigDir.exists()) {
            worldConfigDir.mkdirs();
        }
        if (!defaultConfigFile.exists()) {
            this.defaultConfig = createDefaultConfig();
            saveDefaultConfig();
        }
        File configFile = getConfigFile();
        if (!configFile.exists()) {
            worldConfig = new YamlConfiguration();
            worldConfig.set("WorldConfiguration", "Values in this file will override values in defaultWorldConfig.yml file.");
            try {
                worldConfig.save(configFile);
            } catch (IOException ex) {
                Logger.getLogger(WorldConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            worldConfig = YamlConfiguration.loadConfiguration(configFile);
        }
        convertNoPhysicsList();
        loadNoInteraction();
        /*} else { 
            if(defaultConfigFile.exists()) {
                config = YamlConfiguration.loadConfiguration(defaultConfigFile);
            }
            else {
                config = new YamlConfiguration();
                for(Modules modul : Modules.values()) {
                    config.set(modul.getModuleKey(), true);
                }
                config.set(NO_PHYSICS_LIST, new ArrayList<Integer>());
                createInventoryAccess();
                createNoInteraction();
                try {
                    config.save(defaultConfigFile);
                } catch (IOException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            npList = config.getIntegerList(NO_PHYSICS_LIST);
            saveConfigFile();
        }
        if(config.getConfigurationSection(INVENTORY_ACCESS)==null) {
            createInventoryAccess();
            saveConfigFile();
        }
        if(config.getConfigurationSection(NO_INTERACTION)==null) {
            createNoInteraction();
            saveConfigFile();
        }*/
    }

    private File getConfigFile() {
        return new File(worldConfigDir + File.separator + worldName + "." + cfgExtension);
    }

    private YamlConfiguration createDefaultConfig() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("DefaultConfiguration", "Values in this file will be used for all worlds if not overriden in <worldName>.yml configuration file.");
        for (Modules modul : Modules.values()) {
            config.set(modul.getModuleKey(), true);
        }
        config.set(NO_PHYSICS_LIST, new ArrayList<>());
        createInventoryAccess(config);
        createNoInteraction(config);
        return config;
    }

    /*public final void saveConfigFile(){
        worldConfig.set(NO_PHYSICS_LIST, npList);
        File file = new File(worldConfigDir+"/"+worldName+"."+cfgExtension);
        try {
            worldConfig.save(file);
        } catch (IOException ex) {
            Logger.getLogger(WorldConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    private void saveDefaultConfig() {
        try {
            defaultConfig.save(defaultConfigFile);
        } catch (IOException ex) {
            Logger.getLogger(WorldConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveWorldConfig() {
        try {
            worldConfig.save(getConfigFile());
        } catch (IOException ex) {
            Logger.getLogger(WorldConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isModuleEnabled(Modules module, boolean defaultValue) {
        if (worldConfig.contains(module.getModuleKey())) {
            return worldConfig.getBoolean(module.getModuleKey());
        }
        if (defaultConfig.contains(module.getModuleKey())) {
            return defaultConfig.getBoolean(module.getModuleKey());
        } else {
            defaultConfig.set(module.getModuleKey(), defaultValue);
            saveDefaultConfig();
        }
        return defaultValue;
    }

    public void setModuleEnabled(Modules module, boolean enable) {
        worldConfig.set(module.getModuleKey(), enable);
        saveWorldConfig();
    }

    /* 1.13 removed 
    public String getNoPhysicsListAsString() {
        String result="";
       String[] npArray = npList.toArray(new Integer[0]);
       Arrays.sort(npArray);
        for(Integer i:npArray) {
            result = result + i + " ";
        }
        return result;
       return worldConfig
    }
     */
    private void convertNoPhysicsList() {
        List<String> npList = worldConfig.getStringList(NO_PHYSICS_LIST);
        List<String> convertedList = new ArrayList<>();
        boolean convert = false;
        for (String input : npList) {
            if (NumericUtil.isInt(input)) {
                /*Material mat = LegacyMaterialUtil.getMaterial(NumericUtil.getInt(input));
                if (mat != null) {
                    convertedList.add(mat.getKey().toString());
                    convert = true;
                }*/
                convert = true;
            }
        }
        if (convert) {
            convertedList.sort(null);
            worldConfig.set(NO_PHYSICS_LIST, convertedList);
            saveWorldConfig();
        }
    }

    public List<String> getNoPhysicsListAsStrings() {
        if (worldConfig.contains(NO_PHYSICS_LIST)) {
            return worldConfig.getStringList(NO_PHYSICS_LIST);
        } else if (defaultConfig.contains(NO_PHYSICS_LIST)) {
            return defaultConfig.getStringList(NO_PHYSICS_LIST);
        } else {
            //npList = new ArrayList<>();
            return new ArrayList<>();
        }
    }

    /*
    public void saveNoPhysicsList(List<String> npList) {
        if(worldConfig.contains(NO_PHYSICS_LIST)) {
            updateWorldNPConfig(npList);
        } else {
            defaultConfig.set(NO_PHYSICS_LIST, npList);
            uddateDefaultNPConfig(npList);
        }
    }*/
    public boolean addToNpList(String blockId) {
        List<String> npList = getWorldNoPhysicsList();
        if (npList.contains(blockId)) {
            return false;
        } else {
            npList.add(blockId);
            worldConfig.set(NO_PHYSICS_LIST, npList);
            saveWorldConfig();
            checkDeleteWorldNPConfig();
            return true;
        }
        /* 1.13 removed 
        boolean isNP = isNoPhysicsBlock(blockId);
        if(setDefault) {
            addToDefaultNPConfig(blockId);
        }
        if(!isNP) {
            npList.add(blockId);
            if(worldConfig.contains(NO_PHYSICS_LIST)) {
                updateWorldNPConfig();
            } else {
                if(!setDefault) {
                    createWorldNPConfig();
                }
            }
        }
        checkDeleteWorldNPConfig();
        return !isNP;
        /*if(!isNoPhysicsBlock(blockId)) {
            npList.add(blockId);
            saveNoPhyisicList();
            return true;
        }
        return false;*/
    }

    public boolean removeFromNpList(String blockId) {
        List<String> npList = getWorldNoPhysicsList();
        if (!npList.contains(blockId)) {
            return false;
        } else {
            npList.remove(blockId);
            worldConfig.set(NO_PHYSICS_LIST, npList);
            saveWorldConfig();
            checkDeleteWorldNPConfig();
            return true;
        }
        /* 1.13 removed
        boolean isNP = isNoPhysicsBlock(blockId);
        if(setDefault) {
            removeFromDefaultNPConfig(blockId);
        }
        if(isNP) {
            npList.remove(npList.indexOf(blockId));
            if(worldConfig.contains(NO_PHYSICS_LIST)) {
                updateWorldNPConfig();
            } else {
                if(!setDefault) {
                    createWorldNPConfig();
                }
            }
        }
        checkDeleteWorldNPConfig();
        return isNP;
        if(isNoPhysicsBlock(blockId)) {
            npList.remove(npList.indexOf(blockId));
            saveConfigFile();
            return true;
        }
        return false;*/
    }

    /* 1.13 removed 
    private void addToDefaultNPConfig(String blockData) {
        List<String> defaultList = defaultConfig.getStringList(NO_PHYSICS_LIST);
        if(!defaultList.contains(blockData)) {
            defaultList.add(blockData);
            defaultConfig.set(NO_PHYSICS_LIST, defaultList);
            saveDefaultConfig();
        }
    }
    
    private void removeFromDefaultNPConfig(String blockData) {
        List<String> defaultList = defaultConfig.getStringList(NO_PHYSICS_LIST);
        if(defaultList.contains(blockData)) {
            defaultList.remove(defaultList.indexOf(blockData));
            defaultConfig.set(NO_PHYSICS_LIST, defaultList);
            saveDefaultConfig();
        }
    }
    
    private void updateWorldNPConfig(List<String> npList) {
        worldConfig.set(NO_PHYSICS_LIST, npList);
        saveWorldConfig();
    }
    
    private void createWorldNPConfig(List<String> npList) {
        updateWorldNPConfig(npList);
    }*/
    private List<String> getWorldNoPhysicsList() {
        if (worldConfig.contains(NO_PHYSICS_LIST)) {
            return worldConfig.getStringList(NO_PHYSICS_LIST);
        } else {
            return new ArrayList<>();
        }
    }

    private void checkDeleteWorldNPConfig() {
        Set<String> worldNP = new HashSet<>();
        worldNP.addAll(worldConfig.getStringList(NO_PHYSICS_LIST));
        Set<String> defaultNP = new HashSet<>();
        defaultNP.addAll(defaultConfig.getStringList(NO_PHYSICS_LIST));
        for (String search : worldNP) {
            if (!defaultNP.contains(search)) {
                return;
            }
        }
        for (String search : defaultNP) {
            if (!worldNP.contains(search)) {
                return;
            }
        }
        worldConfig.set(NO_PHYSICS_LIST, null);
        saveWorldConfig();
    }

    public InventoryAccess getInventoryAccess(Inventory inventory) {
        boolean defaultValue = false;
        ConfigurationSection section = worldConfig.getConfigurationSection(INVENTORY_ACCESS);
        if (section == null) {
            defaultValue = true;
            section = defaultConfig.getConfigurationSection(INVENTORY_ACCESS);
            if (section == null) {
                return InventoryAccess.TRUE;
            }
        }
        if (inventory.getType().name().equals("SHULKER_BOX")) {
            InventoryAccess result = getShulkerAccess(section, inventory);
            if (result == null && !defaultValue) {
                defaultValue = true;
                section = defaultConfig.getConfigurationSection(INVENTORY_ACCESS);
                if (section == null) {
                    return InventoryAccess.TRUE;
                }
                result = getShulkerAccess(section, inventory);
            }
            return result == null ? InventoryAccess.TRUE : result;
        }
        /*    if(key == null) {
                section.set(inventory.getType().name(), "TRUE");
                saveDefaultConfig();
                return InventoryAccess.TRUE;
            //}
                ConfigurationSection shulkerConfig 
                        = section.getConfigurationSection(inventory.getType().name());
                if(shulkerConfig==null) {
                    return createNewInventoryConfigEntry(section,inventory.getType().name());
                }
                key = shulkerConfig.getString("ID"+((ShulkerBox)inventory.getHolder())
                                            .getRawData());
                if(key==null) {
                    return createNewInventoryConfigEntry(shulkerConfig,
                                "ID"+((ShulkerBox)inventory.getHolder()).getRawData());
                }
            }
            return InventoryAccess.valueOf(key);
        }*/
        String configValue = section.getString(inventory.getType().name());
        if (configValue == null && !defaultValue) {
            defaultValue = true;
            section = defaultConfig.getConfigurationSection(INVENTORY_ACCESS);
            if (section == null) {
                return InventoryAccess.TRUE;
            }
            configValue = section.getString(inventory.getType().name());
            if (configValue == null) {
                return createNewInventoryConfigEntry(section, inventory.getType().name());
            }
        }
        return InventoryAccess.valueOf(configValue);
    }

    private InventoryAccess getShulkerAccess(ConfigurationSection section, Inventory inventory) {
        String key = inventory.getType().name();
        String configValue = null;
        if (section.isConfigurationSection(key)) {
            ConfigurationSection shulkerConfig
                    = section.getConfigurationSection(key);
            configValue = shulkerConfig.getString("id" + ((ShulkerBox) inventory.getHolder())
                    .getType().getId());
//Logger.getGlobal().info("Config id: "+configValue);
            if (configValue == null) {
                configValue = shulkerConfig.getString("default");
//Logger.getGlobal().info("Config default: "+configValue);
            }
        } else {
            configValue = section.getString(key);
//Logger.getGlobal().info("Config string: "+configValue);
        }
        return configValue == null ? null : InventoryAccess.valueOf(configValue);
    }

    private InventoryAccess createNewInventoryConfigEntry(ConfigurationSection section, String key) {
        section.set(key, InventoryAccess.TRUE.name());
        saveDefaultConfig();
        return InventoryAccess.TRUE;
    }

    private void createInventoryAccess(ConfigurationSection config) {
        ConfigurationSection section = config.createSection(INVENTORY_ACCESS);
        section.set(InventoryType.ANVIL.name(), InventoryAccess.TRUE.name());
        section.set(InventoryType.BEACON.name(), InventoryAccess.EXCEPTION.name());
        section.set(InventoryType.BREWING.name(), InventoryAccess.BUILDER.name());
        section.set(InventoryType.CHEST.name(), InventoryAccess.TRUE.name());
        section.set(InventoryType.CRAFTING.name(), InventoryAccess.TRUE.name());
        section.set(InventoryType.CREATIVE.name(), InventoryAccess.TRUE.name());
        section.set(InventoryType.DISPENSER.name(), InventoryAccess.EXCEPTION.name());
        section.set(InventoryType.DROPPER.name(), InventoryAccess.EXCEPTION.name());
        section.set(InventoryType.ENCHANTING.name(), InventoryAccess.EXCEPTION.name());
        section.set(InventoryType.ENDER_CHEST.name(), InventoryAccess.TRUE.name());
        section.set(InventoryType.FURNACE.name(), InventoryAccess.BUILDER.name());
        section.set(InventoryType.HOPPER.name(), InventoryAccess.EXCEPTION.name());
        section.set(InventoryType.PLAYER.name(), InventoryAccess.TRUE.name());
        section.set(InventoryType.WORKBENCH.name(), InventoryAccess.TRUE.name());
        section.set(InventoryType.MERCHANT.name(), InventoryAccess.TRUE.name());
        ConfigurationSection shulker = section.createSection("SHULKER_BOX");
        shulker.set("default", InventoryAccess.TRUE.name());
    }

    
    
    private void loadNoInteraction() {
        List<String> data;
        if(worldConfig.contains(NO_INTERACTION)) {
            data = worldConfig.getStringList(NO_INTERACTION);
        } else {
            if(defaultConfig.contains(NO_INTERACTION)) {
                data = defaultConfig.getStringList(NO_INTERACTION);
            } else {
                createNoInteraction(defaultConfig);
                saveDefaultConfig();
                data = defaultConfig.getStringList(NO_INTERACTION);
            }
        }
        noInteraction.clear();
        for(String entry: data) {
            noInteraction.add(Bukkit.createBlockData(entry));
        }
    }

    private void createNoInteraction(ConfigurationSection config) {
        List<String> list = new ArrayList<>();
        list.add("minecraft:acacia_fence_gate");
        list.add("minecraft:birch_fence_gate[powered=true]");
        list.add("minecraft:jungle_fence_gate");
        list.add("minecraft:dark_oak_fence_gate");
        list.add("minecraft:spruce_fence_gate");
        list.add("minecraft:acacia_door[powered=false]");
        list.add("minecraft:jungle_door[powered=false]");
        list.add("minecraft:dark_oak_door[powered=false]");
        list.add("minecraft:spruce_door[powered=false]");
        list.add("minecraft:repeater");
        list.add("minecraft:comparator");
        config.set(NO_INTERACTION, list);
        
        /*section.set(Material.OAK_FENCE_GATE.name(), "8-15"); //1.13 renamed
        section.set(Material.SPRUCE_FENCE_GATE.name(), "0-15");
        section.set(Material.BIRCH_FENCE_GATE.name(), "8-15");
        section.set(Material.JUNGLE_FENCE_GATE.name(), "0-15");
        section.set(Material.ACACIA_FENCE_GATE.name(), "0-15");
        section.set(Material.DARK_OAK_FENCE_GATE.name(), "0-15");
        section.set(Material.ACACIA_FENCE_GATE.name(), "0-15");
        section.set(Material.OAK_DOOR.name(), "10-11"); //1.13 renamed
        section.set(Material.JUNGLE_DOOR.name(), "10-11");
        section.set(Material.SPRUCE_DOOR.name(), "10-11");
        section.set(Material.ACACIA_DOOR.name(), "10-11");
        section.set(Material.DARK_OAK_DOOR.name(), "10-11");
        section.set(Material.REPEATER.name(), "0-15"); //1.13 renamed
        //1.13 removed section.set(Material.DIODE_BLOCK_OFF.name(), "0-15");
        section.set(Material.COMPARATOR.name(), "0-15"); //1.13 renamed
        //1.13 removed section.set(Material.REDSTONE_COMPARATOR_OFF.name(), "0-15");*/
    }
}
