package com.mcme.environment.commands;

import com.google.common.base.Joiner;
import com.mcme.environment.data.EnvironmentPlayer;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.word;

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
                    context.getSource().sendMessage("/pweather storm | clear | thunder | server | <player> | copy (allow|deny)");
                    return 0; })
                .then(HelpfulRequiredArgumentBuilder.argument("weather", word())
                        .executes(context -> {

                            return 0;}))
                .then(HelpfulLiteralBuilder.literal("server")
                        .executes(context -> {
                            Player p = ((EnvironmentPlayer)(context.getSource())).getBukkitPlayer();
                            p.resetPlayerWeather();
                            return 0;}))
                .then(HelpfulLiteralBuilder.literal("copy")
                        .then(HelpfulLiteralBuilder.literal("allow")
                                .executes(context -> {

                                    return 0;}))
                        .then(HelpfulLiteralBuilder.literal("deny")
                                .executes(context -> {

                                    return 0;})))
                .then(HelpfulRequiredArgumentBuilder.argument("player", word())
                        .executes(context -> {

                            return 0;}));
        return helpfulLiteralBuilder;
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
