/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.commands;

import com.mcme.environment.SoundPacket.SoundType;
import com.mcme.environment.data.PluginData;
import com.mcme.environment.data.RegionData;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import com.mcmiddleearth.pluginutil.message.MessageType;
import com.mcmiddleearth.pluginutil.region.CuboidRegion;
import com.mcmiddleearth.pluginutil.region.Region;
import static java.lang.Long.parseLong;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 *
 * @author Fraspace5
 */
public class EnvironmentDetails extends EnvironmentCommand {

    public EnvironmentDetails(String... permissionNodes) {
        super(1, true, permissionNodes);
        setShortDescription(": Details about a region ");
        setUsageDescription(": Used to get all details about a region");
    }
//environment details nameRegion 
    //               0

    private String weather;
    private int weight;
    private boolean thunder;
    private String time;
    private String type;
    private SoundType soundAmb;

    @Override
    protected void execute(final CommandSender cs, final String... args) {

        if (PluginData.AllRegions.containsKey(args[0])) {
            Player pl = (Player) cs;
            RegionData redata = PluginData.AllRegions.get(args[0]);

            weight = redata.weight;
            thunder = redata.thunder;
            Region r = redata.region;
            time = redata.time;
            type = redata.type;
            weather = redata.weather;
            soundAmb = redata.soundAmbient;

            FancyMessage message = new FancyMessage(MessageType.INFO_NO_PREFIX, PluginData.getMessageUtil());

            message.addSimple(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Name of the region :" + ChatColor.BLUE + args[0] + "\n");

            if (weather != "default") {
                message.addSimple(ChatColor.GREEN + "Weather type: " + ChatColor.BLUE + weather.toUpperCase() + "\n");
            }

            if (thunder) {
                message.addTooltipped(ChatColor.GREEN.toString() + " with " + ChatColor.UNDERLINE.toString() + " THUNDERS" + "\n", ChatColor.BLUE.toString() + "Every 1 second 20% of thunderbolt ");

            }
            if (time != "default") {
                message.addSimple(ChatColor.GREEN + "Time set as: " + ChatColor.BLUE + parseTime(parseLong(time)) + " [" + time + " ticks]" + "\n");
            }
            if (soundAmb != SoundType.NONE) {
                message.addSimple(ChatColor.GREEN + "Sound heard: " + ChatColor.BLUE + soundAmb.name() + "\n");
            }

            message.addSimple(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Region data: \n");
            message.addSimple(ChatColor.GREEN + "Type: " + ChatColor.BLUE + type + "\n");
            message.addSimple(ChatColor.GREEN + "Weight: " + ChatColor.BLUE + weight + "\n");
            if (r instanceof CuboidRegion) {
                Vector min = ((CuboidRegion) r).getMinCorner();
                Vector max = ((CuboidRegion) r).getMaxCorner();
                message.addSimple(ChatColor.GREEN + "Min " + ChatColor.BLUE + min.getBlockX() + "," + min.getBlockY() + "," + min.getBlockZ() + "\n");
                message.addSimple(ChatColor.GREEN + "Max " + ChatColor.BLUE + max.getBlockX() + "," + max.getBlockY() + "," + max.getBlockZ() + "\n");
            }
            message.send(pl);

        } else {
            sendNo(cs);
        }

    }

    public static String parseTime(long time) {
        long gameTime = time;
        long hours = gameTime / 1000 + 6;
        long minutes = (gameTime % 1000) * 60 / 1000;
        String ampm = "AM";
        if (hours >= 12) {
            hours -= 12;
            ampm = "PM";
        }

        if (hours >= 12) {
            hours -= 12;
            ampm = "AM";
        }

        if (hours == 0) {
            hours = 12;
        }

        String mm = "0" + minutes;
        mm = mm.substring(mm.length() - 2, mm.length());

        return hours + ":" + mm + " " + ampm;
    }

    private void sendNo(CommandSender cs) {
        PluginData.getMessageUtil().sendErrorMessage(cs, "This region doesn't exists.Type before /environment create areaName");

    }

}
