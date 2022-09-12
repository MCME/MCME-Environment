package com.mcme.environment.commands.argument;

import com.mcmiddleearth.command.argument.HelpfulArgumentType;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class OnlinePlayerArgument implements ArgumentType<Player>, HelpfulArgumentType {

    private String tooltip = "Online player.";

    @Override
    public Player parse(StringReader reader) throws CommandSyntaxException {
        String o = reader.readUnquotedString();
        return Bukkit.getOnlinePlayers().stream().filter(player->player.getName().equalsIgnoreCase(o))
                                .findAny().orElseThrow(() ->
                new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage("Failed parsing of PlayerArgument")),
                                           new LiteralMessage("No online player found by name "+o)));
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.singleton("Eriol_Eandur");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        Bukkit.getOnlinePlayers().forEach(player -> builder.suggest(player.getName()));
        return builder.buildFuture();
    }

    @Override
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public String getTooltip() {
        return tooltip;
    }
}
