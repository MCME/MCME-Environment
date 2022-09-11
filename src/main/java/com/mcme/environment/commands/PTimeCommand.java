package com.mcme.environment.commands;

import com.google.common.base.Joiner;
import com.mcme.environment.data.EnvironmentPlayer;
import com.mcme.environment.data.PluginData;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

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
                    context.getSource().sendMessage("/ptime <daytime> | server | cycle (on|off) | <player> | copy (allow|deny)");
                    return 0; })
                .then(HelpfulLiteralBuilder.literal("server")
                        .executes(this::resetPlayerTime))
                .then(HelpfulLiteralBuilder.literal("cycle")
                        .then(HelpfulLiteralBuilder.literal("on")
                                .executes(this::enableDaylightCycle))
                        .then(HelpfulLiteralBuilder.literal("off")
                                .executes(this::disableDaylightCycle)))
                .then(HelpfulRequiredArgumentBuilder.argument("ticks", integer())
                        .executes(this::setTicks))
                .then(HelpfulRequiredArgumentBuilder.argument("daytime", word())
                        .executes(context -> {

                            return 0;}))
                .then(HelpfulRequiredArgumentBuilder.argument("player", word())
                        .executes(this::copyPlayerTime))
                .then(HelpfulLiteralBuilder.literal("copy")
                        .then(HelpfulLiteralBuilder.literal("allow")
                                .executes(context -> {

                                    return 0;}))
                        .then(HelpfulLiteralBuilder.literal("deny")
                                .executes(context -> {

                                    return 0;})));
        return helpfulLiteralBuilder;
    }

    private int copyPlayerTime(CommandContext<McmeCommandSender> context) {
        EnvironmentPlayer other = PluginData.getOrCreateEnvironmentPlayer(context.getArgument("player",String.class));
        if(other.isPtimePublic()) {
            getBukkitPlayer(context).setPlayerTime(other.getBukkitPlayer().getPlayerTimeOffset(),
                                                   other.getBukkitPlayer().isPlayerTimeRelative());
        }
        return 0;
    }

    private int setTicks(CommandContext<McmeCommandSender> context) {
        Player p = getBukkitPlayer(context);
        int time = context.getArgument("ticks", int.class);
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
        if(!p.isPlayerTimeRelative()) {
            long playerTime = p.getPlayerTime();
            p.setPlayerTime(absoluteToRelativeTime(playerTime, p.getWorld()), true);
        }
        return 0;
    }

    private int disableDaylightCycle(CommandContext<McmeCommandSender> context) {
        Player p = getBukkitPlayer(context);
        if(p.isPlayerTimeRelative()) {
            long playerTime = p.getPlayerTimeOffset();
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
            McmeCommandSender wrappedSender = new EnvironmentPlayer((Player) sender);
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
            TabCompleteRequest request = new SimpleTabCompleteRequest((EnvironmentPlayer) sender,
                    String.format("/%s %s", alias, Joiner.on(' ').join(args)));
            onTabComplete(request);
            //Logger.getGlobal().info("tabComplete 1");
            return request.getSuggestions();
        }
    }

}
