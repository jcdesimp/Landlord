package com.jcdesimp.landlord.landFlags;

import com.jcdesimp.landlord.landManagement.Landflag;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * File created by jcdesimp on 4/16/14.
 */
/*
 *******************************************************
 * All flags need to extend the abstract Landflag class
 *******************************************************
 */
public class HarmAnimals extends Landflag {
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
    public HarmAnimals() {
        super(
                "Harm Animals",                                       //Display name (will be displayed to players)
                "Gives permission to hurt or kill|" +
                        "pigs, sheep, cows, mooshrooms,|" +           //Description (Lore of headerItem '|' will seperate lines of lore.)
                        "chickens, horses, dogs, and cats.",
                new ItemStack(Material.LEATHER),                      //Itemstack (represented in and manager)
                "Allowed Animal Damage",                              //Text shown in manager for granted permission
                "can harm animals.",                                  //Description in manager for granted permission (ex: Friendly players <desc>)
                "Denied Animal Damage",                               //Text shown in manager for denied permission
                "cannot harm animals."                                //Desciption in manager for denied permission (ex: Regular players <desc>)
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
    public void animalKill(EntityDamageByEntityEvent event){
        String[] safeAnimals = {"OCELOT","WOLF","HORSE","COW","PIG","MUSHROOM_COW","SHEEP","CHICKEN"};
        org.bukkit.entity.Entity victim = event.getEntity();
        if(!Arrays.asList(safeAnimals).contains(victim.getType().toString())){
            return;
        }


        org.bukkit.entity.Entity attacker = event.getDamager();

        if(attacker.getType().toString().equals("PLAYER")){
            Player p = (Player)attacker;
            OwnedLand land = OwnedLand.getApplicableLand(victim.getLocation());
            if(!land.hasPermTo(p, this)){

                    p.sendMessage(ChatColor.RED+"You cannot harm animals on this land.");

                event.setCancelled(true);

            }

        } else if(attacker.getType().toString().equalsIgnoreCase("Arrow") || attacker.getType().toString().equalsIgnoreCase("SPLASH_POTION")){
            Projectile a = (Projectile)attacker;
            Player p;
            if(a.getShooter() instanceof Player){
                OwnedLand land = OwnedLand.getApplicableLand(victim.getLocation());
                p = (Player)a.getShooter();
                //System.out.println(a.getType());
                if(!land.hasPermTo(p, this)){
                    if(a.getType().toString().equals("ARROW")) {
                        p.sendMessage(ChatColor.RED + "You cannot harm animals on this land.");
                    }
                    a.remove();
                    event.setCancelled(true);
                }
            }


        }
    }





}
