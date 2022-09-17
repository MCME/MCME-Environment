package com.mcme.environment.Util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.mcme.environment.Environment;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateTimePacketUtil {

    private static PacketListener updateTimeListener;

    public static void addUpdateTimePacketListener() {
        removeUpdateTimePacketListener();
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        updateTimeListener = new PacketAdapter(Environment.getPluginInstance(),
                ListenerPriority.NORMAL, PacketType.Play.Server.UPDATE_TIME) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                //long worldTimestamp = event.getPacket().getLongs().read(0);
                long daytime = event.getPacket().getLongs().read(1);
                //Logger.getGlobal().info("Player: "+player.getName()+" WorldTimestamp: "+worldTimestamp+" Daytime: "+daytime);
                if(!player.isPlayerTimeRelative() && daytime > 0) {
                    event.getPacket().getLongs().write(1,-daytime);
                }
            }
        };
        manager.addPacketListener(updateTimeListener);
    }

    public static void removeUpdateTimePacketListener() {
        if(updateTimeListener!=null) {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            manager.removePacketListener(updateTimeListener);
            updateTimeListener = null;
        }
    }

    public static void sendTime(Player player, long time, boolean stopped) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.UPDATE_TIME);
        packet.getLongs().write(0,player.getWorld().getFullTime());
        packet.getLongs().write(1,(stopped?-time:time));
        try {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            manager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            Logger.getLogger(Environment.class.getSimpleName()).log(Level.WARNING,"Error while sending Packet!",e);
        }
    }
}
