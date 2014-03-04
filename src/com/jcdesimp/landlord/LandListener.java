package com.jcdesimp.landlord;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.Entity;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * File created by jcdesimp on 3/4/14.
 */
public class LandListener implements Listener {

    JavaPlugin plugin;

    public LandListener(JavaPlugin p) {
        plugin = p;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void animalKill(EntityDamageByEntityEvent event){
        org.bukkit.entity.Entity victim = event.getEntity();
        org.bukkit.entity.Entity attacker = event.getDamager();

        System.out.println("Victim: "+victim.getType().toString());
        System.out.println("Attacker: " + attacker.getType().toString());
        if(attacker.getType().toString().equals("PLAYER")){
            Player p = (Player)attacker;
            System.out.println("Attacker Name:" + p.getName());

            if(p.getName().equalsIgnoreCase("jcdesimp")){

                event.setCancelled(true);
                return;
            }

        } else if(attacker.getType().toString().equalsIgnoreCase("Arrow")){
            Arrow a = (Arrow)attacker;
            Player p;
            if(a.getShooter() instanceof Player){
                p = (Player)a.getShooter();
                System.out.println("Shooter: " + p.getName());
                if(p.getName().equalsIgnoreCase("jcdesimp")){
                    event.setCancelled(true);
                    return;
                }
            }


        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void blockPlace(BlockPlaceEvent event){
        Player p = event.getPlayer();
        if(p.getName().equalsIgnoreCase("jcdesimp")){
            //p.sendMessage(ChatColor.RED + "You are not allowed to build on this land.");
            //event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void blockBreak(BlockBreakEvent event){
        Player p = event.getPlayer();
        if(p.getName().equalsIgnoreCase("jcdesimp")){
            //p.sendMessage(ChatColor.RED + "You are not allowed to break on this land.");
            //event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void storageOpen(PlayerInteractEvent event){
        String[] blockAccess = {"CHEST","TRAPPED_CHEST","BURNING_FURNACE","FURNACE","ANVIL","DROPPER","DISPENSER","HOPPER"};
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Arrays.asList(blockAccess).contains(event.getClickedBlock().getType().toString())){

            System.out.println("Block Clicked: " + event.getClickedBlock().getType().toString());
            event.setCancelled(true);


        }
    }


}
