package com.jcdesimp.landlord.landFlags;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.landManagement.Landflag;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * File created by jcdesimp on 4/11/14.
 */
/*
 *******************************************************
 * All flags need to extend the abstract Landflag class
 *******************************************************
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
    public Build(Landlord plugin) {
        super(plugin,
                plugin.getMessageConfig().getString("flags.build.displayName"),      //Display name (will be displayed to players)
                plugin.getMessageConfig().getString("flags.build.description"),
                new ItemStack(Material.COBBLESTONE),        //Itemstack (represented in manager)
                plugin.getMessageConfig().getString("flags.build.allowedTitle"),      //Text shown in manager for granted permission
                plugin.getMessageConfig().getString("flags.build.allowedText"),      //Description in manager for granted permission (ex: Friendly players <desc>)
                plugin.getMessageConfig().getString("flags.build.deniedTitle"),      //Text shown in manager for denied permission
                plugin.getMessageConfig().getString("flags.build.deniedText")       //Desciption in manager for denied permission (ex: Regular players <desc>)
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
     *
     * @param event that happened
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void blockPlace(BlockPlaceEvent event) {

        /*
         *******************************
         * Call getApplicableLand(location)
         * If it's null the action didn't happen on owned land
         * and could be ignored.
         *******************************
         */
        OwnedLand land = OwnedLand.getApplicableLand(event.getBlock().getLocation());
        if (land == null) {
            return;
        }

        //get the player associated with the event. (Might want to check for null if applicable to the event)
        Player p = event.getPlayer();

        /*
         *************************************
         * Finally check if the player has permission for your flag
         * with land.hasPermTo(player, this) ('this' representing this flag of course)
         * If they have permission to do the action you're checking for, you shouldn't
         * have to cancel the event.
         *************************************
         */
        if (!land.hasPermTo(p, this)) {
            p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.blockPlace"));
            event.setCancelled(true);
        }

    }

    /*
     *************************************
     * Of course u can register as many
     * event listeners as you need for your flag
     * to do it's job
     *************************************
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void blockBreak(BlockBreakEvent event) {
        OwnedLand land = OwnedLand.getApplicableLand(event.getBlock().getLocation());
        if (land == null) {
            return;
        }
        Player p = event.getPlayer();

        if (!land.hasPermTo(p, this)) {
            p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.blockBreak"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void liquidEmpty(PlayerBucketEmptyEvent event) {
        OwnedLand land = OwnedLand.getApplicableLand(event.getBlockClicked().getLocation());
        if (land == null) {
            return;
        }

        Player p = event.getPlayer();

        if (!land.hasPermTo(p, this)) {
            p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.bucketEmpty"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void liquidFill(PlayerBucketFillEvent event) {
        OwnedLand land = OwnedLand.getApplicableLand(event.getBlockClicked().getLocation());
        if (land == null) {
            return;
        }
        Player p = event.getPlayer();

        if (!land.hasPermTo(p, this)) {
            p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.bucketFill"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void paintingFrameBreak(HangingBreakByEntityEvent event) {
        org.bukkit.entity.Entity victim = event.getEntity();
        org.bukkit.entity.Entity remover = event.getRemover();
        OwnedLand land = OwnedLand.getApplicableLand(victim.getLocation());
        if (land == null) {
            return;
        }
        if (remover.getType().toString().equals("PLAYER")) {
            Player p = (Player) remover;
            if (!land.hasPermTo(p, this)) {
                p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.hangingBreak"));
                event.setCancelled(true);
            }
            //System.out.println("Attacker Name:" + p.getName());


        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void interactWithArmorStand(PlayerInteractAtEntityEvent event) {
        if (!event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
            return;
        }

        OwnedLand land = OwnedLand.getApplicableLand(event.getRightClicked().getLocation());
        if (land == null) {
            return;
        }

        if (!land.hasPermTo(event.getPlayer(), this)) {
            event.getPlayer().sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.useArmorStand"));
            event.setCancelled(true);
        }


    }


    @EventHandler(priority = EventPriority.HIGH)
    public void destroyArmorStand(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();
        //System.out.println("Victim: "+victim);
        if (!victim.getType().equals(EntityType.ARMOR_STAND)) {
            return;
        }

        OwnedLand land = OwnedLand.getApplicableLand(victim.getLocation());
        if (land == null) {
            return;
        }


        if (event.getDamager().getType().equals(EntityType.PLAYER)) {
            Player attacker = (Player) event.getDamager();
            //System.out.println(attacker.getName());
            if (!land.hasPermTo(attacker, this)) {
                attacker.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.breakArmorStandWithMelee"));
                event.setCancelled(true);
            }

        }
        if (event.getDamager().getType().equals(EntityType.ARROW)) {
            Arrow projectile = (Arrow) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                Player attacker = (Player) projectile.getShooter();
                if (!land.hasPermTo(attacker, this)) {
                    attacker.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.breakArmorStandWithArrow"));
                    event.setCancelled(true);
                }
            }

        }
        //System.out.println(event.getDamager().getType());

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void removeItemFromFrame(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();


        if (!victim.getType().equals(EntityType.ITEM_FRAME)) {
            return;
        }
        Player p;
        Entity attacker = event.getDamager();
        //System.out.println("Attacker: "+attacker.getType().toString());
        if (attacker.getType().toString().equals("PLAYER")) {
            p = (Player) attacker;

            OwnedLand land = OwnedLand.getApplicableLand(victim.getLocation());
            if (land == null) {
                return;
            }
            if (!land.hasPermTo(p, this)) {
                p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.itemFrameRemoveWithMelee"));
                event.setCancelled(true);
            }

        } else if (attacker.getType().toString().equals("ARROW")) {
            Arrow a = (Arrow) attacker;
            if (a.getShooter() instanceof Player) {
                p = (Player) a.getShooter();
            } else {
                return;
            }

            OwnedLand land = OwnedLand.getApplicableLand(victim.getLocation());
            if (land == null) {
                return;
            }
            if (!land.hasPermTo(p, this)) {
                p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.itemFrameRemoveWithArrow"));
                event.setCancelled(true);
            }
        }


    }

    @EventHandler(priority = EventPriority.HIGH)
    public void removeItemFromFrame(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player p = event.getPlayer();
        if (!entity.getType().equals(EntityType.ITEM_FRAME)) {
            return;
        }
        ItemFrame iFrame = (ItemFrame) entity;
        if (iFrame.getItem() != null && !iFrame.getItem().getType().equals(Material.AIR))
            return;
        OwnedLand land = OwnedLand.getApplicableLand(entity.getLocation());
        if (land == null) {
            return;
        }
        if (!land.hasPermTo(p, this)) {
            p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.itemFrameRemoveDirectly"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void rotateItemInFrame(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player p = event.getPlayer();
        if (!entity.getType().equals(EntityType.ITEM_FRAME)) {
            return;
        }
        ItemFrame iFrame = (ItemFrame) entity;
        if ((iFrame.getItem().equals(null)) || (iFrame.getItem().getType().equals(Material.AIR))) {
            return;
        }
        OwnedLand land = OwnedLand.getApplicableLand(entity.getLocation());
        if (land == null) {
            return;
        }
        if (!land.hasPermTo(p, this)) {
            p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.itemFrameRotate"));
            event.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void paintingFramePlace(HangingPlaceEvent event) {
        org.bukkit.entity.Entity placer = event.getPlayer();
        OwnedLand land = OwnedLand.getApplicableLand(event.getBlock().getLocation());
        if (land == null) {
            return;
        }

        if (placer.getType().toString().equals("PLAYER")) {
            Player p = (Player) placer;
            if (!land.hasPermTo(p, this)) {
                p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.paintingPlace"));
                event.setCancelled(true);
            }

        }

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void CropTrampleOrFireCharge(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }


        Player p = event.getPlayer();

        //trampling crops
        if (p != null && event.getAction().equals(Action.PHYSICAL) && event.getClickedBlock().getType().toString().equals("SOIL")) {
            OwnedLand land = OwnedLand.getApplicableLand(event.getClickedBlock().getLocation());
            if (land == null) {
                return;
            }
            if (land.hasPermTo(p, this)) {
                return;
            }
            p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.cropDestroy"));
            event.setCancelled(true);
            return;
        }

        //Using fire charge
        ItemStack item = event.getItem();
        if (p != null && item != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getItem().getType().equals(Material.FIREBALL)) {
            OwnedLand land = OwnedLand.getApplicableLand(event.getClickedBlock().getLocation());
            if (land == null) {
                return;
            }
            if (land.hasPermTo(p, this)) {
                return;
            }
            p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.useFireCharge"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void protectBlockStates(PlayerInteractEvent event) {
        Material[] blockedItems = {Material.NOTE_BLOCK, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON,
                Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.FLOWER_POT, Material.CAKE_BLOCK, Material.DAYLIGHT_DETECTOR 
, Material.DAYLIGHT_DETECTOR_INVERTED};
        if (event.getClickedBlock() == null) {
            return;
        }
        Player p = event.getPlayer();
        //System.out.println(event.getClickedBlock().getType());
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Arrays.asList(blockedItems).contains(event.getClickedBlock().getType())) {
            OwnedLand land = OwnedLand.getApplicableLand(event.getClickedBlock().getLocation());
            if (land == null) {
                return;
            }
            if (land.hasPermTo(p, this)) {
                return;
            }

            p.sendMessage(ChatColor.RED + getPlugin().getMessageConfig().getString("event.build.blockStateChange"));
            event.setCancelled(true);
        }

    }

}
