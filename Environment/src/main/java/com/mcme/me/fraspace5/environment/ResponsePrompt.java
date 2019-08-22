/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.me.fraspace5.environment;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

/**
 *
 * @author fraspace5
 */
public class ResponsePrompt extends MessagePrompt {
    
    private final String response;
    
    public ResponsePrompt(String response) {
        this.response = response;
    }

    @Override
    protected Prompt getNextPrompt(ConversationContext cc) {
        return new RegionEditPrompt();
    }

    @Override
    public String getPromptText(ConversationContext cc) {
        return PluginData.getMessageUtil().STRESSED+response;
    }
}
