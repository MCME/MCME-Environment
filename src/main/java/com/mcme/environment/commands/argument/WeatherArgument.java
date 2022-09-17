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
import org.bukkit.WeatherType;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class WeatherArgument implements ArgumentType<WeatherType>, HelpfulArgumentType {

    private String tooltip = "Weather you want to see in the world.";

    private final List<String> weathers = List.of("clear","downfall");

    @Override
    public WeatherType parse(StringReader reader) throws CommandSyntaxException {
        String o = reader.readUnquotedString();
        return weathers.stream().filter(weather->weather.equalsIgnoreCase(o))
                                .map(weather->WeatherType.valueOf(weather.toUpperCase())).findAny().orElseThrow(() ->
                new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage("Failed parsing of WeatherArgument")),
                                           new LiteralMessage("Weather must be one of: "+ String.join(", ", weathers))));
    }

    @Override
    public Collection<String> getExamples() {
        return weathers;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        for (String option :  weathers) {
            if (option.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                if(tooltip == null) {
                    builder.suggest(option);
                } else {
                    builder.suggest(option, new LiteralMessage(tooltip));
                }
            }
        }
        if(weathers.isEmpty() && tooltip != null) {
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
