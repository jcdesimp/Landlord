package com.jcdesimp.landlord.landFlags;

import com.jcdesimp.landlord.landManagement.Landflag;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * File created by jcdesimp on 4/16/14.
 */
public class OpenDoor extends Landflag {
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
    public OpenDoor() {
        //mess all these constructor values
        super(
                "Open Doors",                                       //Display name (will be displayed to players)
                "Gives permission to open/close doors.",            //Description (Lore of headerItem '|' will seperate lines of lore.)
                new ItemStack(Material.WOOD_DOOR),                  //Itemstack (represented in and manager)
                "Allowed to Open Doors",                            //Text shown in manager for granted permission
                "can open doors.",                                  //Description in manager for granted permission (ex: Friendly players <desc>)
                "Not Allowed to Open Doors",                        //Text shown in manager for denied permission
                "cannot open doors."                                //Desciption in manager for denied permission (ex: Regular players <desc>)
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
     * event listeners as you need for
     * your flag to do it's job
     *************************************
     */


    @EventHandler(priority = EventPriority.HIGH)
    public void protectBlockStates(PlayerInteractEvent event){
        Material[] blockedItems = {Material.WOOD_DOOR, Material.TRAP_DOOR, Material.WOODEN_DOOR, Material.FENCE_GATE,
                Material.ACACIA_DOOR, Material.ACACIA_FENCE_GATE,
                Material.DARK_OAK_DOOR, Material.DARK_OAK_FENCE_GATE,
                Material.BIRCH_DOOR, Material.BIRCH_FENCE_GATE,
                Material.JUNGLE_DOOR, Material.JUNGLE_FENCE_GATE,
                Material.SPRUCE_DOOR, Material.SPRUCE_FENCE_GATE,};

        if(event.getClickedBlock()==null){
            return;
        }
        Player p = event.getPlayer();
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Arrays.asList(blockedItems).contains(event.getClickedBlock().getType())){
            OwnedLand land = OwnedLand.getApplicableLand(event.getClickedBlock().getLocation());
            if (land == null) {
                return;
            }
            if(land.hasPermTo(p,this)){
                return;
            }

            p.sendMessage(ChatColor.RED+"You cannot open doors on this land."); //mess
            event.setCancelled(true);
        }

    }


}
