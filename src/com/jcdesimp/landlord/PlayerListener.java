package com.jcdesimp.landlord;

import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

/**
 * File created by jcdesimp on 4/30/14.
 * Alerts a player when they enter land.
 */
public class PlayerListener implements Listener {

    HashMap<String,String> landIn = new HashMap<String, String>();

    @EventHandler(priority = EventPriority.HIGH)
    public void alertPlayerLand(PlayerMoveEvent event) {
        Player player = event.getPlayer();




        if (player.getVehicle() != null) {
            return;
        }

        // Check if changed chunk
        if (event.getFrom().getChunk().getX() != event.getTo().getChunk().getX()
                || event.getFrom().getChunk().getZ() != event.getTo().getChunk().getZ()) {

            OwnedLand land = OwnedLand.getApplicableLand(event.getTo());


            //Leaving Land
            if(landIn.containsKey(player.getName())){
                if(land==null){
                    String prevName = landIn.get(player.getName());
                    if(prevName.equals(player.getName())){
                        player.sendMessage(ChatColor.YELLOW+"** Now leaving your land.");
                    } else {
                        player.sendMessage(ChatColor.YELLOW+"** Now leaving "+ prevName +"'s land.");
                    }

                } else {
                    String prevName = landIn.get(player.getName());
                    if(!prevName.equals(land.getOwnerUsername())){
                        if(prevName.equals(player.getName())){
                            player.sendMessage(ChatColor.YELLOW+"** Now leaving your land.");
                        } else {
                            player.sendMessage(ChatColor.YELLOW+"** Now leaving "+ prevName +"'s land.");
                        }
                    }
                }
            }



            //Entering Land
            if(land==null){
                landIn.remove(player.getName());
                return;
            }

            if(landIn.containsKey(player.getName())) {
                String prevName = landIn.get(player.getName());
                if (!prevName.equals(land.getOwnerUsername())) {
                    landIn.put(player.getName(), land.getOwnerUsername());

                    if (land.getOwnerUsername().equals(player.getName())) {
                        player.sendMessage(ChatColor.GREEN + "** Now entering your land.");
                    } else {
                        String ownerName = land.getOwnerUsername();
                        player.sendMessage(ChatColor.YELLOW + "** Now entering " + ownerName + "'s land.");

                    }
                }
            } else {
                landIn.put(player.getName(), land.getOwnerUsername());
                if (land.getOwnerUsername().equals(player.getName())) {
                    player.sendMessage(ChatColor.GREEN + "** Now entering your land.");
                } else {
                    String ownerName = land.getOwnerUsername();
                    player.sendMessage(ChatColor.YELLOW + "** Now entering " + ownerName + "'s land.");

                }
            }




        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeave(PlayerQuitEvent event){
        landIn.remove(event.getPlayer().getName());
    }


    public void clearPtrack() {
        landIn.clear();
    }
}
