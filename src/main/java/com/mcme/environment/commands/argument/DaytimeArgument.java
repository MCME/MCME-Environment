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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DaytimeArgument implements ArgumentType<Integer>, HelpfulArgumentType {

    private String tooltip = "Daytime you want to see the world at.";

    private final Map<String,Integer> daytimes = Map.of(
            "Dawn", 23500,
            "Sunrise", 0,
            "Day", 1000,
            "Noon", 6000,
            "Sunset", 12000,
            "Dusk", 12500,
            "Night", 13000,
            "Midnight", 18000);

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        String o = reader.readUnquotedString();
        return daytimes.entrySet().stream().filter(daytime->daytime.getKey().equalsIgnoreCase(o))
                                           .map(Map.Entry::getValue).findAny().orElseThrow(() ->
                new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage("Failed parsing of DaytimeArgument")),
                                           new LiteralMessage("Daytime must be one of: "+ String.join(", ", daytimes.keySet()))));
    }

    @Override
    public Collection<String> getExamples() {
        return daytimes.keySet();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        for (String option :  daytimes.keySet()) {
            if (option.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                if(tooltip == null) {
                    builder.suggest(option);
                } else {
                    builder.suggest(option, new LiteralMessage(tooltip));
                }
            }
        }
        if(daytimes.keySet().isEmpty() && tooltip != null) {
            builder.suggest(tooltip);
        }
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
