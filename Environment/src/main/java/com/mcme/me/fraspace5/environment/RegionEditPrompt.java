/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.me.fraspace5.environment;



import com.boydti.fawe.object.FawePlayer;
import com.mcme.me.fraspace5.environment.PluginData;
import com.mcme.me.fraspace5.environment.Region.EnvRegion;
import com.mcmiddleearth.pluginutil.NumericUtil;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import com.mcme.me.fraspace5.environment.RpManager;
import com.sk89q.worldedit.math.BlockVector2;

/**
 *
 * @author Fraspace5
 */
public class RegionEditPrompt extends StringPrompt implements ConversationAbandonedListener{

    
    @Override
    public String getPromptText(ConversationContext cc) {
        ChatColor ccStressed = PluginData.getMessageUtil().HIGHLIGHT_STRESSED;
        ChatColor ccHigh = PluginData.getMessageUtil().HIGHLIGHT;
        EnvRegion region = getRegion(cc);
        return "You are editing rp region: "+ccStressed+region.getName()+"\n"
             + ccHigh+"- weight: "+ccStressed+region.getWeight()+"\n"
             + ccHigh+"- rp: "+ccStressed+region.getRp()+"\n"
             + ccHigh+"- type: "+ccStressed+region.getRegion().getClass().getSimpleName();//region.getRegion() instanceof Polygonal2DRegion?"posible to edit":"not possible to edit");
    }

    @Override
    public Prompt acceptInput(ConversationContext cc, String input) {
        ChatColor ccStressed = PluginData.getMessageUtil().HIGHLIGHT_STRESSED;
        ChatColor ccHigh = PluginData.getMessageUtil().HIGHLIGHT;
        ChatColor ccError = PluginData.getMessageUtil().ERROR;
        EnvRegion editRegion = getRegion(cc);
        String[] words = input.split(" ");
        switch(words[0]) {
            case "weight":
                if(words.length<2) {
                    return new ResponsePrompt(ccError+"Missing argument.");
                }
                if(NumericUtil.isInt(words[1]) && NumericUtil.getInt(words[1])>0) {
                    
                    RpManager.saveRpRegion(getRegion(cc));
                    return new ResponsePrompt("Region weight set to: "+words[1]);
                } else {
                    return new ResponsePrompt("Weight must be a number greater 0.");
                }
            case "rp":
                if(words.length<2) {
                    return new ResponsePrompt("Missing argument.");
                }
                String rp = RpManager.matchRpName(words[1]);
                if(!rp.equalsIgnoreCase("")) {
                    getRegion(cc).setRp(rp);
                    RpManager.saveRpRegion(getRegion(cc));
                    return new ResponsePrompt("RP changed to: "+rp);
                } else {
                    return new ResponsePrompt("No RP found for name: "+words[1]);
                }
            case "info":
                Region weRegion = editRegion.getRegion();
                String info="Region info:\n";
                if(weRegion instanceof CuboidRegion) {
                    Vector min = weRegion.getMinimumPoint();
                    Vector max = weRegion.getMaximumPoint();
                    info= info+ccHigh+"Type: "+ccStressed+"cuboid\n" 
                         +ccHigh+"MinimumPoint: "+ccStressed+min.getBlockX()+","+min.getBlockY()+","+min.getBlockZ()+"\n"
                         +ccHigh+"MaximumPoint: "+ccStressed+max.getBlockX()+","+max.getBlockY()+","+max.getBlockZ()+"\n";
                } else if(weRegion instanceof Polygonal2DRegion) {
                    info= info+ccHigh+"Type: "+ccStressed+"polygonal\n"
                              +ccHigh+"Lower border: "+ccStressed+((Polygonal2DRegion)weRegion).getMinimumY()+"\n"
                              +ccHigh+"Upper border: "+ccStressed+((Polygonal2DRegion)weRegion).getMaximumY()+"\n"
                              +ccHigh+"Points: \n";
                    int i = 0;
                    for(BlockVector2 point: ((Polygonal2DRegion)weRegion).getPoints()) {
                        info = info + ccHigh+"- ["+i+"] "+ccStressed+point.getBlockX()+","+point.getBlockZ()+"\n";
                        i++;
                    }
                } else  {
                    info = info + ccHigh+"Type: "+ccStressed+weRegion.getClass().getSimpleName();
                }
                return new ResponsePrompt(info);
            case "set":
                Region newWeRegion = FawePlayer.wrap(getPlayer(cc)).getSelection();
                if(newWeRegion!=null) {
                    getRegion(cc).setRegion(newWeRegion.clone());
                    RpManager.saveRpRegion(getRegion(cc));
                    return new ResponsePrompt("Region borders changed to current selection.");
                } else {
                    return new ResponsePrompt(ccError+"Make a WE selection first");
                }
            case "setborder":
            case "setminy":
            case "setmaxy":
            case "addpoint":
            case "setpoint":
            case "removepoint":
                if(!(getRegion(cc).getRegion() instanceof Polygonal2DRegion)) {
                    return new ResponsePrompt(ccError+"You can edit polygonal regions only");
                }
                if(words[0].equals("setminy") 
                        || words[0].equals("setmaxy")) {
                    int y = getPlayer(cc).getLocation().getBlockY();
                    if(words.length>1 && NumericUtil.isInt(words[1])) {
                        y = NumericUtil.getInt(words[1]);
                    }
                    switch(words[0]) {
                        case "setmaxy":
                            ((Polygonal2DRegion)editRegion.getRegion()).setMaximumY(y);
                            break;
                        case "setminy":
                            ((Polygonal2DRegion)editRegion.getRegion()).setMinimumY(y);
                            break;
                        default:
                            return new ResponsePrompt(ccError+"Invalid subcommand.");
                    }
                    
                    RpManager.saveRpRegion(getRegion(cc));
                    return new ResponsePrompt("Region borders edited.");
                }
                if(words.length<2) {
                    return new ResponsePrompt(ccError+"Missing argument.");
                }
                if(!NumericUtil.isInt(words[1])) {
                    return new ResponsePrompt(ccError+"You need to specify an index.");
                }
                int x = getPlayer(cc).getLocation().getBlockX();
                int z = getPlayer(cc).getLocation().getBlockZ();
                if(words.length>3 && NumericUtil.isInt(words[2])
                                  && NumericUtil.isInt(words[3])) {
                    x = NumericUtil.getInt(words[2]);
                    z = NumericUtil.getInt(words[3]);
                }
                switch(words[0]) {
                    case "addpoint":
                        if(!insertPoint(editRegion,NumericUtil.getInt(words[1]),x,z)) {
                            return new ResponsePrompt(ccError+"Invalid index.");
                        }
                        break;
                    case "removepoint":
                        if(!removePoint(editRegion,NumericUtil.getInt(words[1]))) {
                            return new ResponsePrompt(ccError+"Invalid index.");
                        }
                        break;
                    case "setpoint":
                        if(!setPoint(editRegion,NumericUtil.getInt(words[1]),x,z)) {
                            return new ResponsePrompt(ccError+"Invalid index.");
                        }
                        break;
                    default: return new ResponsePrompt(ccError+"Invalid subcommand.");
                }
                
                RpManager.saveRpRegion(getRegion(cc));
                return new ResponsePrompt("Region borders edited.");
            case "name":
                if(words.length<2) {
                    return new ResponsePrompt(ccError+"Missing argument.");
                }
                EnvRegion region = RpManager.getRegion(words[1]);
                if(region!=null) {
                    return new ResponsePrompt(ccError+"A region with that name already exists.");
                }
                region = getRegion(cc);
                RpManager.removeRegion(region.getName());
                region.setName(words[1]);
                RpManager.saveRpRegion(region);
                RpManager.addRegion(region);
                return new ResponsePrompt("Region was renamed to: "+words[1]);
            case "quit":
                return Prompt.END_OF_CONVERSATION;
            default:
                return new ResponsePrompt(ccError+"Invalid command.");
        }
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent event) {
        if(event.gracefulExit()) {
            PluginData.getMessageUtil().sendInfoMessage((Player)event.getContext()
                                                             .getSessionData("player"), 
                    "You quit from region edit conversation.");
        } else {
            PluginData.getMessageUtil().sendInfoMessage((Player)event.getContext()
                                                             .getSessionData("player"), 
                    "Region edit conversation timed out.");
        }
    }
    
    private EnvRegion getRegion(ConversationContext cc) {
        return (EnvRegion) cc.getSessionData("region");
    }
    
    private Player getPlayer(ConversationContext cc) {
        return (Player) cc.getSessionData("player");
    }
    
    private boolean insertPoint(EnvRegion region, int index, int x, int z) {
        Polygonal2DRegion weRegion = (Polygonal2DRegion) region.getRegion();
        if(weRegion.size()<=index) {
            weRegion.addPoint(new BlockVector2D(x,z));
            return true;
        } else {
            index = Math.max(index, 0);
            List<BlockVector2D> points = new ArrayList<>();
            points.addAll(weRegion.getPoints());
            ListIterator<BlockVector2D> iterator = points.listIterator(index);
            iterator.add(new BlockVector2D(x,z));
            region.setRegion(new Polygonal2DRegion(weRegion.getWorld(),points,
                                   weRegion.getMinimumY(),weRegion.getMaximumY()));
            return true;
        }
    }

    private boolean removePoint(EnvRegion region, int index) {
        Polygonal2DRegion weRegion = (Polygonal2DRegion) region.getRegion();
        if(index >=0 && index < weRegion.size()) {
            List<BlockVector2D> points = new ArrayList<>();
            points.addAll(weRegion.getPoints());
            ListIterator<BlockVector2D> iterator = points.listIterator(index);
            iterator.next();
            iterator.remove();
            region.setRegion(new Polygonal2DRegion(weRegion.getWorld(),points,
                                   weRegion.getMinimumY(),weRegion.getMaximumY()));
            return true;
        }
        return false;
    }

    private boolean setPoint(EnvRegion region, int index, int x, int z) {
        Polygonal2DRegion weRegion = (Polygonal2DRegion) region.getRegion();
        if(index >=0 && index < weRegion.size()) {
            List<BlockVector2D> points = new ArrayList<>();
            points.addAll(weRegion.getPoints());
            ListIterator<BlockVector2D> iterator = points.listIterator(index);
            iterator.next();
            iterator.set(new BlockVector2D(x,z));
            region.setRegion(new Polygonal2DRegion(weRegion.getWorld(),points,
                                   weRegion.getMinimumY(),weRegion.getMaximumY()));
            return true;
        }
        return false;
    }
}
