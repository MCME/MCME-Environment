/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.me.fraspace5.environment;

import com.mcme.me.fraspace5.environment.Region.EnvRegion;
import com.mcme.me.fraspace5.environment.commands.RegionCommand;
import com.mcme.me.fraspace5.environment.event.RegionChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


/**
 *
 * @author Fraspace5
 */
public class Environment extends JavaPlugin implements Listener{
    
     @Getter
    private static Environment pluginInstance;
    private static Environment instance;
    private RunChecks runChecks;
    
    private List<String> regions = new ArrayList<>();
    
    HashMap<UUID, Boolean> bool = new HashMap<>();    
    
    HashMap<UUID, Boolean> other = new HashMap<>();
    
    private boolean registeredStuff = false;

    Logger Logger = Bukkit.getLogger();
    ConsoleCommandSender clogger = this.getServer().getConsoleSender();
    
    
    public Environment()
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
        getConfig().options().copyDefaults(true);
        saveConfig();
        RegionManager.loadRegions(this);
        if(!registeredStuff) {
            registerStuff();
        }
        runChecks = new RunChecks();
        runChecks.runTaskTimer(this, 0L, 20L);
        saveConfig();
       
        clogger.sendMessage(ChatColor.AQUA+ "---------------------------------------");
        clogger.sendMessage(ChatColor.DARK_GREEN+ "Environment Plugin 1.0 enabled");
        clogger.sendMessage(ChatColor.AQUA+ "---------------------------------------");
    }

    @Override
    public void onDisable()
    {
        RegionManager.stopChecks();
        clogger.sendMessage(ChatColor.RED+ "---------------------------------------");
        clogger.sendMessage(ChatColor.DARK_GREEN+ "Environment Plugin 1.0 Disabled");
        clogger.sendMessage(ChatColor.RED+ "---------------------------------------");
    }

    private void registerStuff()
    {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new RegionListener(), this);
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(this, this);
        getCommand("environment").setExecutor(this);
        registeredStuff=true;
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
    @EventHandler
    public void onChange(RegionChangeEvent e){
    
      
    final EnvRegion r = e.getRegion();
    final OfflinePlayer player = e.getPlayer();
    final long time = r.getTime();
    final boolean thundering = r.getThundering();
    long ti = player.getPlayer().getPlayerTime();
    UUID uuid = player.getPlayer().getUniqueId();
    final boolean check = bool.get(uuid);
    
    if (other.containsKey(uuid)){
    
    other.replace(uuid, true);
    
    
    }else{
    other.put(uuid, true);
    
    }
    
    new BukkitRunnable(){
    @Override
    public void run(){
    
    
    if (check == true){
    if (r.getWeather().equalsIgnoreCase("sun")){
    player.getPlayer().setPlayerWeather(WeatherType.CLEAR);
    player.getPlayer().setPlayerTime(time, false);
    
    
    if (thundering == true){
     new BukkitRunnable(){
        @Override
        public void run(){
        
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
       
        if (!player.isOnline()){
        cancel();
        
        }
        
        
        }}.runTaskTimer(Environment.getPlugin(), 60L, 400L);
     
    }
     else{
     
     
     }
 
    }
 
    else{
    player.getPlayer().setPlayerWeather(WeatherType.DOWNFALL);
    player.getPlayer().setPlayerTime(time, false);
    
    if (thundering == true){
     new BukkitRunnable(){
        @Override
        public void run(){
        
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
       
        if (!player.isOnline()){
        cancel();
        
        }
        
        
        }}.runTaskTimer(Environment.getPlugin(), 60L, 400L);
     
    }
     else{
     
     
     }
    
    }}else{
    
    
    return;
    
    }
    
    
    
    
    }
    
    
    
    
    }.runTaskTimer(this, 20L, 20L);
    
    
    
    
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e){

    Player pl = e.getPlayer();
    String r = RegionPlayer.getCurrent(pl);
    
   
    try {
    EnvRegion rf = RegionManager.getRegion(pl.getWorld().getName(), r);
    boolean thundering = rf.getThundering();

    final OfflinePlayer player = e.getPlayer();
    long time = rf.getTime();
    
    long ti = player.getPlayer().getPlayerTime();
    final UUID uuid = player.getPlayer().getUniqueId();
    boolean check = bool.get(uuid);
    
    if (other.containsKey(uuid)){
    
    other.replace(uuid, false);
    
    
    }else{
    other.put(uuid, false);
    
    }
    
    if (check == true){
    if (rf.getWeather().equalsIgnoreCase("sun")){
    player.getPlayer().setPlayerWeather(WeatherType.CLEAR);
    player.getPlayer().setPlayerTime(time, false);
    
    
    if (thundering == true){
     new BukkitRunnable(){
        @Override
        public void run(){
        
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
       
        if (!player.isOnline()){
        cancel();
        
        }
        
        if (other.get(uuid) == true){
        
            cancel();
        
        
        }
        
        }}.runTaskTimer(this, 60L, 400L);
     
    }
     else{
     
     
     }
 
    }
 
    else{
    player.getPlayer().setPlayerWeather(WeatherType.DOWNFALL);
    player.getPlayer().setPlayerTime(time, false);
    
    if (thundering == true){
     new BukkitRunnable(){
        @Override
        public void run(){
        
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
       
        if (other.get(uuid) == true){
        
            cancel();
        
        
        }
        
        
        if (!player.isOnline()){
        cancel();
        

        }
        
        
        }}.runTaskTimer(this, 60L, 400L);
     
    
     
     
    }
     else{
     
     
     }
    
    }}else{
    
    
    return;
    
    }
    } catch(Exception ex){
    pl.sendMessage("No region");
    return;
    }
    
    

    }
    
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] a)
    {
   
        
        // environment create NameRegion time weather true|false
        
        if (cs instanceof Player){
           Player player = (Player) cs;
            if (a.length == 0){
            
            player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"/env create | modify | delete | list"
            +"/env create NameRegion time weather true|false");
            return true;
            }
            
            if (a[0].equalsIgnoreCase("create")){
               if (player.hasPermission("environment.admin")){
               if (a.length == 5){
               System.out.println("5");
               if (WEUtil.worldEditExists() && WEUtil.hasSelection(player))
                {
                     System.out.println("6");
                    if(regions.contains(a[1])){
                    
                     player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Name already used!");
                     return true;
                    
                    }else {
                    System.out.println("7");
                    if (!a[3].equalsIgnoreCase("rain") && !a[3].equalsIgnoreCase("sun")){
                     System.out.println("8");
                    return true;
                    }
                        
                    if (a[3].equalsIgnoreCase("rain")){
                     
                    if (a[4].equalsIgnoreCase("true")){
                     
                    EnvRegion r = new EnvRegion();
                    r.setName(a[1]);
                    int tim = Integer.parseInt(a[2]);
                    r.setTime(tim);
                    r.setWeather("rain");
                    r.setThundering(true);
                    player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Setting current selection as region bounds...");
                    RegionUtil.attachRegionBuilder(player, r);
                    WEUtil.setWERegion(player);
                    }else{
                     System.out.println("11");
                        EnvRegion r = new EnvRegion();
                    r.setName(a[1]);
                    int tim = Integer.parseInt(a[2]);
                    r.setTime(tim);
                    r.setWeather("rain");
                    r.setThundering(false);
                    player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Setting current selection as region bounds...");
                    RegionUtil.attachRegionBuilder(player, r);
                    WEUtil.setWERegion(player);
                    
                    }
                   
                    
                    }else {
                    if (a[4].equalsIgnoreCase("true")){
                     EnvRegion r = new EnvRegion();
                    r.setName(a[1]);
                    int tim = Integer.parseInt(a[2]);
                    r.setTime(tim);
                    r.setWeather("sun");
                    r.setThundering(true);
                    player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Setting current selection as region bounds...");
                    RegionUtil.attachRegionBuilder(player, r);
                    WEUtil.setWERegion(player);
                    }else{
                     EnvRegion r = new EnvRegion();
                    r.setName(a[1]);
                    int tim = Integer.parseInt(a[2]);
                    r.setTime(tim);
                    r.setWeather("sun");
                    r.setThundering(false);
                    player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Setting current selection as region bounds...");
                    RegionUtil.attachRegionBuilder(player, r);
                    WEUtil.setWERegion(player);
                    
                    }
                    
                    
                    }    
                    
                  
                    }

                
                }else {
               player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"You have to make a selection first");
               
               }
                   
                   
                   
                   
               }else {
               player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Not enough argouments");
               }

               }else {
               
               player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"No permissions to use this command!");
               
               }
            
            
            
            
            
            }
            if (a[0].equalsIgnoreCase("on")){
            UUID uuid = player.getUniqueId();
            player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Fellowship environment active");
            if (bool.containsKey(uuid)){
            bool.remove(uuid, true);
            
            }else {
            
            bool.put(uuid, true);
            
            }
            
            
            
            return true;
            
            }if (a[0].equalsIgnoreCase("off")){
            UUID uuid = player.getUniqueId();
            player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Fellowship environment inactive");
            if (bool.containsKey(uuid)){
            bool.remove(uuid, false);
            
            }else {
            
            bool.put(uuid, false);
            
            }return true;
            } if (a[0].equalsIgnoreCase("delete") && player.hasPermission("environment.admin"))
        {
            File rf = FileUtil.getRegionToLoad(a[1]);
            if(rf!=null) {
                rf.delete();
                regions.remove(a[1]);
                player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Deleting region "+a[1]+" ...");
                Environment.reload();
                return true;
            } else {
                player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Region could not be found, searching for possible matches:");
                player.sendMessage(FileUtil.getMatches(a[1]));
                return true;
            }
        } if (a[0].equalsIgnoreCase("list") && player.hasPermission("environment.admin"))
        {
            player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"Environment regions: ");
            for(EnvRegion region:RegionManager.getRegions()) {
                player.sendMessage(ChatColor.GREEN.BOLD+"[Environment] :"+ChatColor.GREEN+"    - "+region.getWorldName()+"-"+region.getName());
            }
            return true;
        }
            

        
        }else{
        System.out.println("You can't use this command in console");
        
        
        }

    
    return false;
    }
    
    
    
    




}
