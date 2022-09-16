package com.mcme.environment.commands;

import com.google.common.base.Joiner;
import com.mcme.environment.Environment;
import com.mcme.environment.Util.UpdateTimePacketUtil;
import com.mcme.environment.commands.argument.DaytimeArgument;
import com.mcme.environment.commands.argument.OnlinePlayerArgument;
import com.mcme.environment.data.EnvironmentPlayer;
import com.mcme.environment.data.PluginData;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pluginutil.message.MessageUtil;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class PTimeCommand extends AbstractCommandHandler implements TabExecutor {

    public PTimeCommand(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
                .requires(sender -> (sender instanceof EnvironmentPlayer)
                        && ((EnvironmentPlayer) sender).getBukkitPlayer().hasPermission("env.ptime"))
                .executes(context -> {
                    getEnvironmentPlayer(context).sendMessage("ptime <daytime> | server | cycle (on|off) | <player> | copy (allow|deny) | warp <factor> | get");
                    return 0; })
                .then(HelpfulLiteralBuilder.literal("server")
                        .executes(this::resetPlayerTime))
                .then(HelpfulLiteralBuilder.literal("cycle")
                        .then(HelpfulLiteralBuilder.literal("on")
                                .executes(this::enableDaylightCycle))
                        .then(HelpfulLiteralBuilder.literal("off")
                                .executes(this::disableDaylightCycle)))
                .then(HelpfulRequiredArgumentBuilder.argument("ticks", integer())
                        .executes(context -> setTicks(context, context.getArgument("ticks", int.class))))
                .then(HelpfulRequiredArgumentBuilder.argument("daytime", new DaytimeArgument())
                        .executes(context -> setTicks(context, context.getArgument("daytime", int.class))))
                .then(HelpfulLiteralBuilder.literal("warp")
                        .then(HelpfulRequiredArgumentBuilder.argument("timeFactor", integer(1,100))
                                .executes(this::setWarpFactor)))
                .then(HelpfulLiteralBuilder.literal("get")
                        .requires(sender -> ((EnvironmentPlayer) sender).getBukkitPlayer().hasPermission("env.ptime.get"))
                        .then(HelpfulRequiredArgumentBuilder.argument("player", new OnlinePlayerArgument())
                                .executes(this::showPTime)))
                .then(HelpfulLiteralBuilder.literal("copy")
                        .then(HelpfulRequiredArgumentBuilder.argument("player", new OnlinePlayerArgument())
                                .executes(this::copyPlayerTime))
                        .then(HelpfulLiteralBuilder.literal("allow")
                                .executes(this::publishPlayerTime))
                        .then(HelpfulLiteralBuilder.literal("deny")
                                .executes(this::unpublishPlayerTime)));
        return helpfulLiteralBuilder;
    }

    private int showPTime(CommandContext<McmeCommandSender> context) {
        MessageUtil util = PluginData.getMessageUtils();
        EnvironmentPlayer other = PluginData.getOrCreateEnvironmentPlayer(context.getArgument("player",Player.class));
        util.sendInfoMessage(getBukkitPlayer(context),"Current time of "
                +util.STRESSED+other.getBukkitPlayer().getName()
                +util.INFO+".\nTime: "+util.STRESSED+formatTime(other.getBukkitPlayer().getPlayerTime())+"\n"
                +util.INFO+"Daytime cycle: "+util.STRESSED+(other.getBukkitPlayer().isPlayerTimeRelative()?"enabled":"disabled")+"\n"
                +util.INFO+"Time warp factor: "+util.STRESSED+other.getTimeWarp());
        return 0;
    }

    private int setWarpFactor(CommandContext<McmeCommandSender> context) {
        int factor = context.getArgument("timeFactor", int.class);
        getEnvironmentPlayer(context).setTimeWarp(factor);
        PluginData.getMessageUtils().sendInfoMessage(getBukkitPlayer(context),"Setting your time warp factor to "+factor+".");
        return 0;
    }

    private int publishPlayerTime(CommandContext<McmeCommandSender> context) {
        getEnvironmentPlayer(context).setPtimePublic(true);
        PluginData.getMessageUtils().sendInfoMessage(getBukkitPlayer(context),"Other players are now allowed to copy your time settings.");
        return 0;
    }

    private int unpublishPlayerTime(CommandContext<McmeCommandSender> context) {
        getEnvironmentPlayer(context).setPtimePublic(false);
        PluginData.getMessageUtils().sendInfoMessage(getBukkitPlayer(context),"Other players are now denied to copy your time settings.");
        return 0;
    }

    private int copyPlayerTime(CommandContext<McmeCommandSender> context) {
        EnvironmentPlayer other = PluginData.getOrCreateEnvironmentPlayer(context.getArgument("player",Player.class));
        if(other.isPtimePublic()) {
            getEnvironmentPlayer(context).setTimeWarp(other.getTimeWarp());
            getBukkitPlayer(context).setPlayerTime(other.getBukkitPlayer().getPlayerTimeOffset(),
                                                   other.getBukkitPlayer().isPlayerTimeRelative());
            PluginData.getMessageUtils().sendInfoMessage(getBukkitPlayer(context),"Copied time settings of "+other.getBukkitPlayer().getName());
        }
        PluginData.getMessageUtils().sendErrorMessage(getBukkitPlayer(context),other.getBukkitPlayer().getName()+" does not allow to copy his time settings.");
        return 0;
    }

    private int setTicks(CommandContext<McmeCommandSender> context, int time) {
        Player p = getBukkitPlayer(context);
        if(!p.isPlayerTimeRelative()) {
            p.setPlayerTime(time,false);
        } else {
            p.setPlayerTime(absoluteToRelativeTime(time,p.getWorld()), true);
        }
        PluginData.getMessageUtils().sendInfoMessage(getBukkitPlayer(context),"Setting your time to "+formatTime(p.getPlayerTime())+".");
        return 0;
    }

    private int resetPlayerTime(CommandContext<McmeCommandSender> context) {
        getBukkitPlayer(context).resetPlayerTime();
        getEnvironmentPlayer(context).setTimeWarp(1);
        PluginData.getMessageUtils().sendInfoMessage(getBukkitPlayer(context),"You are now following server time.");
        return 0;
    }

    private int enableDaylightCycle(CommandContext<McmeCommandSender> context){
        Player p = getBukkitPlayer(context);
//Logger.getGlobal().info("enable cycle");
        if(!p.isPlayerTimeRelative()) {
            long playerTime = p.getPlayerTime();
//Logger.getGlobal().info("old time: "+playerTime);
            long newTime = absoluteToRelativeTime(playerTime, p.getWorld());
//Logger.getGlobal().info("Server time: "+p.getWorld().getFullTime()+" new offset: "+newTime);
            p.setPlayerTime(newTime, true);
            UpdateTimePacketUtil.sendTime(p, p.getPlayerTime(),false);
//Logger.getGlobal().info("Check new time: "+p.getPlayerTime());
//Logger.getGlobal().info("Check new time offset: "+p.getPlayerTimeOffset());
            PluginData.getMessageUtils().sendInfoMessage(getBukkitPlayer(context),"Daylight cycle enabled.");
        }
        PluginData.getMessageUtils().sendErrorMessage(getBukkitPlayer(context),"You already have daylight cycle disabled.");
        return 0;
    }

    private int disableDaylightCycle(CommandContext<McmeCommandSender> context) {
        Player p = getBukkitPlayer(context);
//Logger.getGlobal().info("disable cycle");
        if(p.isPlayerTimeRelative()) {
            long playerTime = p.getPlayerTimeOffset();
//Logger.getGlobal().info("old offset: "+playerTime);
            long newTime = relativeToAbsoluteTime(playerTime, p.getWorld());
//Logger.getGlobal().info("Server time: "+p.getWorld().getTime()+" new time: "+newTime);
            p.setPlayerTime(newTime, false);
            UpdateTimePacketUtil.sendTime(p, p.getPlayerTime(), true);
            PluginData.getMessageUtils().sendInfoMessage(getBukkitPlayer(context),"Daylight cycle enabled.");
                    //p.setPlayerTime(newTime, false);
//Logger.getGlobal().info("Check new time: "+p.getPlayerTime());
//Logger.getGlobal().info("Check new time offset: "+p.getPlayerTimeOffset());
        }
        PluginData.getMessageUtils().sendErrorMessage(getBukkitPlayer(context),"You already have daylight cycle disabled.");
        return 0;
    }

    private long relativeToAbsoluteTime(long relative, World world) {
        long serverTime = world.getTime();
        return relative + serverTime;
    }

    private long absoluteToRelativeTime(long absolute, World world) {
        long serverTime = world.getFullTime();
        return absolute - serverTime;
    }

    private Player getBukkitPlayer(CommandContext<McmeCommandSender> context) {
        return getEnvironmentPlayer(context).getBukkitPlayer();
    }

    private EnvironmentPlayer getEnvironmentPlayer(CommandContext<McmeCommandSender> context) {
        return ((EnvironmentPlayer)(context.getSource()));
    }

    private String formatTime(long playerTime) {
        long day = playerTime/24000;
        long daytime = playerTime%24000;
        long hours = daytime/1000+6;
        long minutes = (daytime%1000)*60/1000;
        return "Day "+ day + " Time "+ hours+":"+(minutes<10?"0"+minutes:minutes);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You need to be logged in to use this command.");
        } else {
            McmeCommandSender wrappedSender = PluginData.getOrCreateEnvironmentPlayer((Player)sender);
            execute(wrappedSender, args);
        }
        return true;
    }

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
//Logger.getGlobal().info("tabComplete 1");
        if(!(sender instanceof Player)) {
            return Collections.emptyList();
        } else {
            TabCompleteRequest request = new SimpleTabCompleteRequest(PluginData.getOrCreateEnvironmentPlayer((Player)sender),
                    String.format("/%s %s", alias, Joiner.on(' ').join(args)));
            onTabComplete(request);
            //Logger.getGlobal().info("tabComplete 1");
            return request.getSuggestions();
        }
    }


}
