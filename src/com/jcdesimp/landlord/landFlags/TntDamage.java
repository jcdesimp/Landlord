package com.jcdesimp.landlord.landFlags;

import com.jcdesimp.landlord.landManagement.Landflag;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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






}
