package com.jcdesimp.landlord.landFlags;

import com.jcdesimp.landlord.Landlord;
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

/**
 * File created by jcdesimp on 5/24/14.
 */

/*
 *******************************************************
 * All flags need to extend the abstract Landflag class
 *******************************************************
 */
public class PVP extends Landflag {

    public PVP(Landlord plugin) {
        //mess all these constructor values
        super(plugin,
                "PVP",                                          //Display name (will be displayed to players)
                "Gives permission to attack|" +
                        "players on this land|" ,
                new ItemStack(Material.IRON_SWORD),              //Itemstack (represented in and manager)
                "Allowed PvP",                                    //Text shown in manager for granted permission
                "can hurt other players.",                        //Description in manager for granted permission (ex: Friendly players <desc>)
                "Denied PvP",                                     //Text shown in manager for denied permission
                "cannot hurt other players."                      //Description in manager for denied permission (ex: Regular players <desc>)
        );
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void playerDamage(EntityDamageByEntityEvent event){
        //String[] safeAnimals = {"OCELOT","WOLF","HORSE","COW","PIG","MUSHROOM_COW","SHEEP","CHICKEN"};
        org.bukkit.entity.Entity victim = event.getEntity();
        if(!(victim instanceof Player)){
            return;
        }


        org.bukkit.entity.Entity attacker = event.getDamager();

        if(attacker.getType().toString().equals("PLAYER")){
            Player p = (Player)attacker;
            OwnedLand land = OwnedLand.getApplicableLand(victim.getLocation());
            if(land==null){
                return;
            }
            if(!land.hasPermTo(p, this)){

                p.sendMessage(ChatColor.RED+"You cannot harm other players on this land.");     //mess

                event.setCancelled(true);

            }

        } else if(attacker.getType().toString().equalsIgnoreCase("Arrow") || attacker.getType().toString().equalsIgnoreCase("SPLASH_POTION")){
            Projectile a = (Projectile)attacker;
            Player p;
            if(a.getShooter() instanceof Player){
                OwnedLand land = OwnedLand.getApplicableLand(victim.getLocation());
                p = (Player)a.getShooter();
                if(land==null){
                    return;
                }
                //System.out.println(a.getType());
                if(!land.hasPermTo(p, this)){
                    if(a.getType().toString().equals("ARROW")) {
                        p.sendMessage(ChatColor.RED + "You cannot harm other players on this land.");       //mess
                    }
                    a.remove();
                    event.setCancelled(true);
                }
            }


        }
    }



}
