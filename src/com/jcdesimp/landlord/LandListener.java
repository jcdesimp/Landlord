package com.jcdesimp.landlord;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
/**
 * File created by jcdesimp on 3/4/14.
 */
public class LandListener implements Listener {

    JavaPlugin plugin;

    public LandListener(JavaPlugin p) {
        plugin = p;
    }


    /**
     * Event handler for animal damage
     * @param event being triggered
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void animalKill(EntityDamageByEntityEvent event){
        String[] safeAnimals = {"OCELOT","WOLF","HORSE","COW","PIG","MUSHROOM_COW","SHEEP","CHICKEN"};
        org.bukkit.entity.Entity victim = event.getEntity();
        boolean isItemFrame = false;
        //System.out.println("Victim:"+ victim.getType());
        if(victim.getType().toString().equalsIgnoreCase("ITEM_FRAME")){
            isItemFrame = true;
        } else if(!Arrays.asList(safeAnimals).contains(victim.getType().toString())){
            return;
        }

        Chunk loc = victim.getLocation().getChunk();
        OwnedLand land = OwnedLand.getLandFromDatabase(loc.getX(),loc.getZ(),loc.getWorld().getName());
        if(land == null){
            return;
        }

        org.bukkit.entity.Entity attacker = event.getDamager();

        //System.out.println("Victim: "+victim.getType().toString());
        //System.out.println("Attacker: " + attacker.getType().toString());
        if(attacker.getType().toString().equals("PLAYER")){
            Player p = (Player)attacker;
            if(p.hasPermission("landlord.admin.bypass")){
                return;
            }
            if(!land.hasPermTo(p.getName(), OwnedLand.LandAction.HARM_ANIMALS)){
                if(isItemFrame){
                    p.sendMessage(ChatColor.RED+"You cannot break that on this land.");
                } else {
                    p.sendMessage(ChatColor.RED+"You cannot harm animals on this land.");
                }

                event.setCancelled(true);

            }
            //System.out.println("Attacker Name:" + p.getName());


        } else if(attacker.getType().toString().equalsIgnoreCase("Arrow") || attacker.getType().toString().equalsIgnoreCase("SPLASH_POTION")){
            Projectile a = (Projectile)attacker;
            Player p;
            if(a.getShooter() instanceof Player){
                p = (Player)a.getShooter();
                if(p.hasPermission("landlord.admin.bypass")){
                    return;
                }
                if(!land.hasPermTo(p.getName(), OwnedLand.LandAction.HARM_ANIMALS)){
                    if(isItemFrame){
                        p.sendMessage(ChatColor.RED+"You cannot break that on this land.");

                    } else {
                        p.sendMessage(ChatColor.RED+"You cannot harm animals on this land.");

                    }
                    event.setCancelled(true);
                }
            }


        }
    }


    /**
     * Event handler for block placements
     * @param event that happened
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void blockPlace(BlockPlaceEvent event){
        Chunk loc = event.getBlock().getLocation().getChunk();
        OwnedLand land = OwnedLand.getLandFromDatabase(loc.getX(),loc.getZ(),loc.getWorld().getName());
        if(land == null){
            return;
        }
        Player p = event.getPlayer();
        if(p.hasPermission("landlord.admin.bypass")){
            return;
        }
        if(!land.hasPermTo(p.getName(), OwnedLand.LandAction.BUILD)){
            p.sendMessage(ChatColor.RED + "You are not allowed to build on this land.");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void blockBreak(BlockBreakEvent event){
        Chunk loc = event.getBlock().getLocation().getChunk();
        OwnedLand land = OwnedLand.getLandFromDatabase(loc.getX(),loc.getZ(),loc.getWorld().getName());
        if(land == null){
            return;
        }
        Player p = event.getPlayer();
        if(p.hasPermission("landlord.admin.bypass")){
            return;
        }

        if(!land.hasPermTo(p.getName(), OwnedLand.LandAction.BUILD)){
            p.sendMessage(ChatColor.RED + "You are not allowed to break on this land.");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void storageOpenCropTrample(PlayerInteractEvent event){
        //System.out.println(event.getAction().toString());
        //System.out.println(event.getClickedBlock().getType().toString());
        String[] blockAccess = {"CHEST","TRAPPED_CHEST","BURNING_FURNACE","FURNACE","ANVIL","DROPPER","DISPENSER","HOPPER","BREWING_STAND","SOIL"};
        //System.out.println(event.getClickedBlock().getType());

        if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.PHYSICAL))){
            return;
        }
        if(!Arrays.asList(blockAccess).contains(event.getClickedBlock().getType().toString())){
            return;
        }
        Chunk loc = event.getClickedBlock().getLocation().getChunk();
        OwnedLand land = OwnedLand.getLandFromDatabase(loc.getX(),loc.getZ(),loc.getWorld().getName());
        if(land == null){
            return;
        }
        Player p = event.getPlayer();
        if(p.hasPermission("landlord.admin.bypass")){
            return;
        }
        if(event.getAction().equals(Action.PHYSICAL) && !land.hasPermTo(p.getName(), OwnedLand.LandAction.BUILD)){
            p.sendMessage(ChatColor.RED + "You are not allowed to destroy crops on this land.");
            event.setCancelled(true);
            return;
        }
        if(!land.hasPermTo(p.getName(), OwnedLand.LandAction.OPEN_CONTAINERS)){
            p.sendMessage(ChatColor.RED + "You are not allowed to use containers on this land.");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void paintingFrameBreak(HangingBreakByEntityEvent event){
        org.bukkit.entity.Entity victim = event.getEntity();
        org.bukkit.entity.Entity remover = event.getRemover();
        //System.out.println("Victim: "+ victim.getType());
        //System.out.println("Remover: "+ remover);
        Chunk loc = victim.getLocation().getChunk();
        OwnedLand land = OwnedLand.getLandFromDatabase(loc.getX(),loc.getZ(),loc.getWorld().getName());
        if(land == null){
            return;
        }
        if(remover.getType().toString().equals("PLAYER")){
            Player p = (Player)remover;
            if(p.hasPermission("landlord.admin.bypass")){
                return;
            }
            if(!land.hasPermTo(p.getName(), OwnedLand.LandAction.BUILD)){
                p.sendMessage(ChatColor.RED+"You cannot break that on this land.");
                event.setCancelled(true);
            }
            //System.out.println("Attacker Name:" + p.getName());


        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void paintingFramePlace(HangingPlaceEvent event){
        org.bukkit.entity.Entity victim = event.getEntity();
        org.bukkit.entity.Entity remover = event.getPlayer();
        //System.out.println("Victim: "+ victim.getType());
        //System.out.println("Remover: "+ remover);
        Chunk loc = victim.getLocation().getChunk();
        OwnedLand land = OwnedLand.getLandFromDatabase(loc.getX(),loc.getZ(),loc.getWorld().getName());
        if(land == null){
            return;
        }
        if(remover.getType().toString().equals("PLAYER")){
            Player p = (Player)remover;
            if(p.hasPermission("landlord.admin.bypass")){
                return;
            }
            if(!land.hasPermTo(p.getName(), OwnedLand.LandAction.BUILD)){
                p.sendMessage(ChatColor.RED+"You cannot place that on this land.");
                event.setCancelled(true);
            }
            //System.out.println("Attacker Name:" + p.getName());


        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void liquidEmpty(PlayerBucketEmptyEvent event){
        Chunk loc = event.getBlockClicked().getLocation().getChunk();
        OwnedLand land = OwnedLand.getLandFromDatabase(loc.getX(),loc.getZ(),loc.getWorld().getName());
        if(land == null){
            return;
        }

        Player p=event.getPlayer();
        if(p.hasPermission("landlord.admin.bypass")){
            return;
        }
        if(!land.hasPermTo(p.getName(), OwnedLand.LandAction.BUILD)){
            p.sendMessage(ChatColor.RED+"You cannot place that on this land.");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void liquidFill(PlayerBucketFillEvent event){
        Chunk loc = event.getBlockClicked().getLocation().getChunk();
        OwnedLand land = OwnedLand.getLandFromDatabase(loc.getX(),loc.getZ(),loc.getWorld().getName());
        if(land == null){
            return;
        }
        Player p=event.getPlayer();
        if(p.hasPermission("landlord.admin.bypass")){
            return;
        }
        if(!land.hasPermTo(p.getName(), OwnedLand.LandAction.BUILD)){
            p.sendMessage(ChatColor.RED+"You cannot do that on this land.");
            event.setCancelled(true);
        }
    }

    /*
    @EventHandler
    public void playerJoin(PlayerLoginEvent e){
        System.out.println("PLAYER UUID: "+e.getPlayer().getUniqueId().toString().replace("-",""));
    }
    */


}
