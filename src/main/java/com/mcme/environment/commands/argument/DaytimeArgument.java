package com.mcme.environment.commands.argument;

import com.mcmiddleearth.command.argument.HelpfulArgumentType;
import com.mcmiddleearth.pluginutil.NumericUtil;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class DaytimeArgument implements ArgumentType<Integer>, HelpfulArgumentType {

    private String tooltip = "Daytime you want to see the world at.";

    private final Map<String,Integer> daytimes = Map.of(
            "dawn", 23500,
            "sunrise", 0,
            "day", 1000,
            "noon", 6000,
            "sunset", 12000,
            "dusk", 12500,
            "night", 13000,
            "midnight", 18000);

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();
        String o = reader.getRemaining();
        if(o.contains(" ")) {
            o = reader.readStringUntil(' ');
        } else {
            reader.setCursor(reader.getTotalLength());
        }
        if(NumericUtil.isInt(o)) {
            int time = NumericUtil.getInt(o);
            while(time < 0) time += 24000;
            return time % 24000;
        }
        if(o.toLowerCase().endsWith("am") || o.toLowerCase().endsWith("pm")) {
            boolean am = o.toLowerCase().endsWith("am");
            int  hours = -1;
            int minutes = -1;
            String hoursMinutes = o.substring(0, o.length() - 2);
            if(NumericUtil.isInt(hoursMinutes)) {
                int temphours = NumericUtil.getInt(hoursMinutes);
                if(temphours > 0 && temphours <=12) {
                    hours = temphours;
                    minutes = 0;
                }
            } else {
                String[] split = hoursMinutes.split(":");
                if(split.length == 2 && NumericUtil.isInt(split[0]) && NumericUtil.isInt(split[1])) {
                    int temphours = NumericUtil.getInt(split[0]);
                    int tempminutes = NumericUtil.getInt(split[1]);
                    if(temphours > 0 && temphours <= 12)
                        hours = temphours;
                    if(tempminutes >= 0 && tempminutes <60)
                        minutes = tempminutes;
                }
            }
            if(hours > 0 && minutes >= 0) {
                if(hours < 12 && !am) {
                    hours += 12;
                } else if(hours == 12 && am) {
                    hours -= 12;
                }
                int ticks = (hours*1000+minutes*1000/60+18000)%24000;
                return ticks;
            }
        }
        String[] split = o.split(":");
        if(split.length == 2 && NumericUtil.isInt(split[0]) && NumericUtil.isInt(split[1])) {
            int hours = -1;
            int minutes = -1;
            int temphours = NumericUtil.getInt(split[0]);
            int tempminutes = NumericUtil.getInt(split[1]);
            if(temphours > 0 && temphours < 24)
                hours = temphours;
            if(tempminutes >= 0 && tempminutes < 60)
                minutes = tempminutes;
            if(hours > 0 && minutes >= 0) {
                return (hours*1000+minutes*1000/60+18000)%24000;
            }
        }
        String finalO = o;
        int result = daytimes.entrySet().stream().filter(daytime->daytime.getKey().equalsIgnoreCase(finalO))
                                           .map(Map.Entry::getValue).findAny().orElse(-1);
        if(result < 0) {
                reader.setCursor(start);
                throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage("Failed parsing of DaytimeArgument")),
                                           new LiteralMessage("Daytime may be one of: "+ String.join(", ", daytimes.keySet())));
        }
        return result;
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
