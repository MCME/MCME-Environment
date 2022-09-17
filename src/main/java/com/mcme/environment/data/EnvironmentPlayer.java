package com.mcme.environment.data;

import com.mcmiddleearth.command.McmeCommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnvironmentPlayer implements McmeCommandSender {

    private final Player bukkitPlayer;
    private boolean ptimePublic = false;
    private boolean pweatherPublic = false;
    private int timeWarp = 1;

    public EnvironmentPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    @Override
    public void sendMessage(BaseComponent[] baseComponents) {
        PluginData.getMessageUtils().sendInfoMessage(bukkitPlayer,
                Arrays.stream(baseComponents).map(component->component.toLegacyText()).collect(Collectors.joining()));
    }

    public void sendMessage(String message) {
        PluginData.getMessageUtils().sendInfoMessage(bukkitPlayer,message);
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public boolean isPtimePublic() {
        return ptimePublic;
    }

    public void setPtimePublic(boolean ptimePublic) {
        this.ptimePublic = ptimePublic;
    }

    public boolean isPweatherPublic() {
        return pweatherPublic;
    }

    public void setPweatherPublic(boolean pweatherPublic) {
        this.pweatherPublic = pweatherPublic;
    }

    public int getTimeWarp() {
        return timeWarp;
    }

    public void setTimeWarp(int timeWarp) {
        this.timeWarp = timeWarp;
    }
}
