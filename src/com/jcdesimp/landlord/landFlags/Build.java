package com.jcdesimp.landlord.landFlags;

import com.jcdesimp.landlord.landManagement.Landflag;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * File created by jcdesimp on 4/11/14.
 * All flags need to extend the abstract Landflag class
 */
public class Build extends Landflag {
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
    public Build() {
        super(
                "Build",                                       //Display name
                "Gives permission to place|and break blocks",  //Description (Lore of headerItem '|' will seperate lines of lore.)
                new ItemStack(Material.COBBLESTONE),           //Itemstack (represented in and manager)
                "Allowed Build",                               //Text shown in manager for granted permission
                "can build.",                                  //Description in manager for granted permission (ex: Friendly players <desc>)
                "Denied Build",                                //Text shown in manager for denied permission
                "cannot build."                                //Desciption in manager for denied permission (ex: Regular players <desc>)
        );
    }



    /*
     ******************************************************************************
     * ALL event handlers for this flag NEED to be defined inside this class!!!!!
     ******************************************************************************
     */


    /**
     * Event handler for block placements
     * @param event that happened
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void blockPlace(BlockPlaceEvent event){

        //Call getApplicableLand()....
        OwnedLand land = OwnedLand.getApplicableLand(event.getBlock().getLocation());
        //Check if it returns null (aka not owned) and return if true.
        if(land == null){
            return;
        }

        //get the player associated with the event. (Might want to check for null if applicable to the event)
        Player p = event.getPlayer();

        //Finally check if the player has permission for your flag with land.hasPermTo(player, this) (this representing this flag of course)
        //If the player does NOT(!) have permission, you handle canceling of the event, player messages, etc.
        if(!land.hasPermTo(p, this)){
            p.sendMessage(ChatColor.RED + "You are not allowed to build on this land.");
            event.setCancelled(true);
        }
        //Of course u can register as many event listeners as you need

    }


}
