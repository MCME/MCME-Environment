/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.me.fraspace5.environment;

/**
 *
 * @author Fraspace5
 */

import com.mcme.me.fraspace5.environment.Region.EnvRegion;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;



public class RegionEditConversationFactory{
    
    private final ConversationFactory factory;
    
    public RegionEditConversationFactory(Plugin plugin){
        factory = new ConversationFactory(plugin)
                .withModality(false)
                .withPrefix(new ConversationPrefix(){
                    @Override
                    public String getPrefix(ConversationContext cc) {
                        return ChatColor.GOLD+"Environment ";
                    }
                })
                .withFirstPrompt(new RegionEditPrompt())
                .addConversationAbandonedListener(new RegionEditPrompt())
                .withTimeout(120)
                .withLocalEcho(true);
    }
    
    public void start(Player player, EnvRegion region) {
        Conversation conversation = factory.buildConversation(player);
        ConversationContext context = conversation.getContext();
        context.setSessionData("player", player);
        context.setSessionData("region", region);
        conversation.begin();
    }
   
}
