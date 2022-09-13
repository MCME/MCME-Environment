package com.mcme.environment.commands;

import com.google.common.base.Joiner;
import com.mcme.environment.commands.argument.OnlinePlayerArgument;
import com.mcme.environment.commands.argument.WeatherArgument;
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
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PWeatherCommand extends AbstractCommandHandler implements TabExecutor {

    public PWeatherCommand(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
                .requires(sender -> (sender instanceof EnvironmentPlayer)
                        && ((EnvironmentPlayer) sender).getBukkitPlayer().hasPermission("env.pweather"))
                .executes(context -> {
                    getEnvironmentPlayer(context).sendMessage("/pweather clear | downfall | server");//| <player> | copy (allow|deny)");
                    return 0; })
                .then(HelpfulRequiredArgumentBuilder.argument("weather", new WeatherArgument())
                        .executes(this::setWeather))
                .then(HelpfulLiteralBuilder.literal("server")
                        .executes(this::resetWeather))
                .then(HelpfulLiteralBuilder.literal("copy")
                        .then(HelpfulLiteralBuilder.literal("allow")
                                .executes(this::publishPlayerWeather))
                        .then(HelpfulLiteralBuilder.literal("deny")
                                .executes(this::unpublishPlayerWeather)))
                .then(HelpfulRequiredArgumentBuilder.argument("player", new OnlinePlayerArgument())
                        .executes(this::copyWeather));
        return helpfulLiteralBuilder;
    }

    private int unpublishPlayerWeather(CommandContext<McmeCommandSender> context) {
        getEnvironmentPlayer(context).setPweatherPublic(true);
        return 0;
    }

    private int publishPlayerWeather(CommandContext<McmeCommandSender> context) {
        getEnvironmentPlayer(context).setPweatherPublic(false);
        return 0;
    }

    private int copyWeather(CommandContext<McmeCommandSender> context) {
        EnvironmentPlayer other = PluginData.getOrCreateEnvironmentPlayer(context.getArgument("player",Player.class));
        if(other.isPweatherPublic()) {
            WeatherType otherWeather = other.getBukkitPlayer().getPlayerWeather();
            if(otherWeather!=null) {
                getBukkitPlayer(context).setPlayerWeather(otherWeather);
            } else {
                getBukkitPlayer(context).resetPlayerWeather();
            }
        }
        return 0;
    }

    private int setWeather(CommandContext<McmeCommandSender> context) {
        Player p = getBukkitPlayer(context);
        p.setPlayerWeather(context.getArgument("weather",WeatherType.class));
        return 0;
    }

    private int resetWeather(CommandContext<McmeCommandSender> context) {
        Player p = getBukkitPlayer(context);
        p.resetPlayerWeather();
        return 0;
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
