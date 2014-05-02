package com.jcdesimp.landlord.landFlags;

import com.jcdesimp.landlord.landManagement.Landflag;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * File created by jcdesimp on 4/16/14.
 */
public class TntDamage extends Landflag {
 /*
  **********************
  * IMPORTANT!!!! Landlord will take care of registering
  * the listeners, all you need to do is register the
  * class with landlord's flagManager!!!
  **********************
  */

    /**
     * Constructor needs to be defined and properly call super()
     */
    public TntDamage() {
        super(
                "TNT Damage",                         //Display name (will be displayed to players)
                "Gives permission to cause block|" +
                        "damage with tnt.",           //Description (Lore of headerItem '|' will seperate lines of lore.)
                new ItemStack(Material.TNT),          //Itemstack (represented in and manager)
                "Allowed TNT Damage",                 //Text shown in manager for granted permission
                "can damage with TNT.",               //Description in manager for granted permission (ex: Friendly players <desc>)
                "Denied TNT Damage",                  //Text shown in manager for denied permission
                "cannot damage with TNT."             //Desciption in manager for denied permission (ex: Regular players <desc>)
        );
    }



    /*
     ******************************************************************************
     * ALL event handlers for this flag NEED to be defined inside this class!!!!!
     * REMEMBER! Do not register this class with bukkit, register with Landlord's
     * flag manager and landlord will register the event handlers.
     ******************************************************************************
     */


    /**
     * Event handler for block placements
     * @param event that happened
     */


    /*
     *************************************
     * Of course u can register as many
     * event listeners as you need for your flag
     * to fo it's job
     *************************************
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void tntExplode( EntityExplodeEvent event ){
        if(event.getEntityType()==null){
            return;
        }
        if(!(event.getEntityType().equals(EntityType.PRIMED_TNT))){
            return;
        }

        TNTPrimed tnt = (TNTPrimed)event.getEntity();
        OwnedLand land = OwnedLand.getApplicableLand(event.getLocation());
        if (land != null) {
            if(tnt.getSource() != null && tnt.getSource().getType().equals(EntityType.PLAYER)){
                Player p = (Player)tnt.getSource();
                if(!land.hasPermTo(p,this)){
                    p.sendMessage(ChatColor.RED+"You cannot detonate TNT on this land.");
                    event.setCancelled(true);
                    return;
                }

            } else if(!land.canEveryone(this)){
                event.setCancelled(true);
                return;
            }
        }
        List<Block> destroyed = event.blockList();
        Iterator<Block> it = destroyed.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            OwnedLand lnd = OwnedLand.getApplicableLand(block.getLocation());
            if (lnd != null && !lnd.canEveryone(this) ) {
                if (!(tnt.getSource() instanceof Player) || !lnd.hasPermTo((Player) tnt.getSource(), this)) {
                    it.remove();
                }

            }

        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void tntMinecartExplode( EntityExplodeEvent event ){
        if(event.getEntityType()==null){
            return;
        }
        if(!event.getEntityType().equals(EntityType.MINECART_TNT)){
            return;
        }

        OwnedLand land = OwnedLand.getApplicableLand(event.getLocation());
        if (land != null) {
            if(!land.canEveryone(this)){
                event.setCancelled(true);
                return;
            }

        }

        List<Block> destroyed = event.blockList();
        Iterator<Block> it = destroyed.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            OwnedLand lnd = OwnedLand.getApplicableLand(block.getLocation());
            if (lnd != null && !lnd.canEveryone(this)) {
                it.remove();
            }

        }


    }

    @EventHandler(priority = EventPriority.HIGH)
    public void igniteTnt(PlayerInteractEvent event){

        if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
            return;
        }
        //System.out.println(event.getAction().toString());
        //System.out.println(event.getClickedBlock().getType().toString());
        //System.out.println(event.getItem().getType());
        if((event.getClickedBlock().getType().equals(Material.TNT)) && event.getItem() != null &&event.getItem().getType().equals(Material.FLINT_AND_STEEL)) {

            OwnedLand land = OwnedLand.getApplicableLand(event.getClickedBlock().getLocation());
            if (land == null) {
                return;
            }
            Player p = event.getPlayer();
            if (!land.hasPermTo(p, this)) {
                p.sendMessage(ChatColor.RED + "You are not allowed to ignite tnt on this land.");
                event.setCancelled(true);
            }
        }
    }





}
