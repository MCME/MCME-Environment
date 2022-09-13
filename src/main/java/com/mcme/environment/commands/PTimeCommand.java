package com.mcme.environment.commands;

import com.google.common.base.Joiner;
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
import com.mojang.brigadier.context.CommandContext;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

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
                    getEnvironmentPlayer(context).sendMessage("ptime <daytime> | server | cycle (on|off)");// | <player> | copy (allow|deny)");
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
                .then(HelpfulRequiredArgumentBuilder.argument("player", new OnlinePlayerArgument())
                        .executes(this::copyPlayerTime))
                .then(HelpfulLiteralBuilder.literal("copy")
                        .then(HelpfulLiteralBuilder.literal("allow")
                                .executes(this::publishPlayerTime))
                        .then(HelpfulLiteralBuilder.literal("deny")
                                .executes(this::unpublishPlayerTime)));
        return helpfulLiteralBuilder;
    }

    private int publishPlayerTime(CommandContext<McmeCommandSender> context) {
        getEnvironmentPlayer(context).setPtimePublic(true);
        return 0;
    }

    private int unpublishPlayerTime(CommandContext<McmeCommandSender> context) {
        getEnvironmentPlayer(context).setPtimePublic(false);
        return 0;
    }

    private int copyPlayerTime(CommandContext<McmeCommandSender> context) {
        EnvironmentPlayer other = PluginData.getOrCreateEnvironmentPlayer(context.getArgument("player",Player.class));
        if(other.isPtimePublic()) {
            getBukkitPlayer(context).setPlayerTime(other.getBukkitPlayer().getPlayerTimeOffset(),
                                                   other.getBukkitPlayer().isPlayerTimeRelative());
        }
        return 0;
    }

    private int setTicks(CommandContext<McmeCommandSender> context, int time) {
        Player p = getBukkitPlayer(context);
        if(!p.isPlayerTimeRelative()) {
            p.setPlayerTime(time,false);
        } else {
            p.setPlayerTime(absoluteToRelativeTime(time,p.getWorld()), true);
        }
        return 0;
    }

    private int resetPlayerTime(CommandContext<McmeCommandSender> context) {
        getBukkitPlayer(context).resetPlayerTime();
        return 0;
    }

    private int enableDaylightCycle(CommandContext<McmeCommandSender> context){
        Player p = getBukkitPlayer(context);
Logger.getGlobal().info("enable cycle");
        if(!p.isPlayerTimeRelative()) {
            long playerTime = p.getPlayerTime();
Logger.getGlobal().info("old time: "+playerTime);
            p.setPlayerTime(absoluteToRelativeTime(playerTime, p.getWorld()), true);
        }
        return 0;
    }

    private int disableDaylightCycle(CommandContext<McmeCommandSender> context) {
        Player p = getBukkitPlayer(context);
Logger.getGlobal().info("disable cycle");
        if(p.isPlayerTimeRelative()) {
            long playerTime = p.getPlayerTimeOffset();
Logger.getGlobal().info("old offset: "+playerTime);
            p.setPlayerTime(relativeToAbsoluteTime(playerTime, p.getWorld()), false);
        }
        return 0;
    }

    private long relativeToAbsoluteTime(long relative, World world) {
        long serverTime = world.getTime();
        return relative + serverTime;
    }

    private long absoluteToRelativeTime(long absolute, World world) {
        long serverTime = world.getTime();
        return absolute - serverTime;
    }

    private Player getBukkitPlayer(CommandContext<McmeCommandSender> context) {
        return getEnvironmentPlayer(context).getBukkitPlayer();
    }

    private EnvironmentPlayer getEnvironmentPlayer(CommandContext<McmeCommandSender> context) {
        return ((EnvironmentPlayer)(context.getSource()));
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
