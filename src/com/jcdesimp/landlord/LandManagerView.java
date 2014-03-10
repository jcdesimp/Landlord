package com.jcdesimp.landlord;

import com.DarkBladee12.ParticleAPI.ParticleEffect;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * File created by jcdesimp on 3/4/14.
 */
public class LandManagerView implements Listener {

    private Player player;
    private Inventory ui;
    private OwnedLand mLand;
    private String[][] perms;
    private boolean isOpen = false;

    public LandManagerView(Player player, OwnedLand mLand) {
        Landlord.getInstance().getServer().getPluginManager().registerEvents(this, Landlord.getInstance());


        this.player = player;
        this.mLand = mLand;
        this.perms = mLand.getLandPerms();
        String uiDisplayName = "Land Manager";
        this.ui = Bukkit.createInventory(null, 27, uiDisplayName);
        this.buildUI();
        this.setToggles();
        this.showUI();
    }

    private ItemStack makeButton(String displayName, String[] lore, Material material){
        return makeButton(displayName, lore, new ItemStack(material));


    }

    private ItemStack makeButton(String displayName, String[] lore, ItemStack stack){
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(lore));
        stack.setItemMeta(meta);
        return stack;
    }



    public void showUI(){
        player.openInventory(ui);
    }

    /*
    private void hideUI(){

    }
    */

    private void buildUI(){
        // Static Items Help and row/column markers
        ui.setItem(0,makeButton(ChatColor.GOLD+"Help", new String[]{ChatColor.RESET+"Click each wool block",
                ChatColor.RESET+"to toggle a permission for a group.",ChatColor.RESET+"Red wool means not allowed",
                ChatColor.RESET+"and green wool means allowed.",ChatColor.RESET+"Mouseover each item for more information."}, Material.ENCHANTED_BOOK));
        ui.setItem(1,makeButton(ChatColor.YELLOW+ "Build", new String[]{ChatColor.RESET+"Gives permission to place",ChatColor.RESET+"and break blocks."}, Material.COBBLESTONE));
        ui.setItem(2,makeButton(ChatColor.YELLOW+"Harm Animals", new String[]{ChatColor.RESET+"Gives permission to hurt or kill",
                ChatColor.RESET+"pigs, sheep, cows, mooshrooms,",ChatColor.RESET+"chickens, horses, dogs and cats."}, Material.LEATHER));
        ui.setItem(3, makeButton(ChatColor.YELLOW+"Open Containers", new String[]{ChatColor.RESET+"Gives permission to use trap chests,",
                ChatColor.RESET+"chests, furnaces, anvils, hoppers,", ChatColor.RESET+"droppers, and dispensers."}, Material.CHEST));
        ui.setItem(9,makeButton(ChatColor.YELLOW+"Everyone", new String[]{ChatColor.RESET+"Permissions in this row apply to",
                ChatColor.RESET+"people that aren't friends",ChatColor.RESET+"of this land."},
                new ItemStack(Material.SKULL_ITEM, 1, (short)2)));
        ui.setItem(18,makeButton(ChatColor.YELLOW+"Friends", new String[]{ChatColor.RESET+"Permissions in this row apply to", ChatColor.RESET+"friends of this land."},
                new ItemStack(Material.SKULL_ITEM, 1, (short)3)));

        //Generate dynamic items based on land perms


    }

    private void setToggles(){

        for(int g=0; g<perms.length; g++){
            //System.out.println(perms[g]);
            //System.out.println("Build Perms: "+perms[g][1]);
            switch (g){

                case 0:  //everyone
                    if(perms[g][1].equals("0")){
                        ui.setItem(10,makeButton(ChatColor.RED+"Denied Build", new String[]{ChatColor.WHITE+"Regular players cannot build.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)14)));
                    } else {
                        ui.setItem(10,makeButton(ChatColor.GREEN+"Allowed Build", new String[]{ChatColor.WHITE+"Regular players can build.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)5)));
                    }

                    if(perms[g][2].equals("0")){
                        ui.setItem(11,makeButton(ChatColor.RED+"Denied Animal Damage", new String[]{ChatColor.WHITE+"Regular players cannot harm animals.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)14)));
                    } else {
                        ui.setItem(11,makeButton(ChatColor.GREEN+"Allowed Animal Damage", new String[]{ChatColor.WHITE+"Regular players can harm animals.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)5)));
                    }

                    if(perms[g][3].equals("0")){
                        ui.setItem(12,makeButton(ChatColor.RED+"Denied Container Access", new String[]{ChatColor.WHITE+"Regular players cannot access containers.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)14)));
                    } else {
                        ui.setItem(12,makeButton(ChatColor.GREEN+"Allowed Container Access", new String[]{ChatColor.WHITE+"Regular players can access containers.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)5)));
                    }


                    break;
                case 1:  //friends
                    if(perms[g][1].equals("0")){
                        ui.setItem(19,makeButton(ChatColor.RED+"Denied Build", new String[]{ChatColor.WHITE+"Friends of this land cannot build.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)14)));
                    } else {
                        ui.setItem(19,makeButton(ChatColor.GREEN+"Allowed Build", new String[]{ChatColor.WHITE+"Friends of this land can build.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)5)));
                    }
                    if(perms[g][2].equals("0")){
                        ui.setItem(20,makeButton(ChatColor.RED+"Denied Animal Damage", new String[]{ChatColor.WHITE+"Friends of this land cannot harm animals.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)14)));
                    } else {
                        ui.setItem(20,makeButton(ChatColor.GREEN+"Allowed Animal Damage", new String[]{ChatColor.WHITE+"Friends of this land can cannot harm animals.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)5)));
                    }

                    if(perms[g][3].equals("0")){
                        ui.setItem(21,makeButton(ChatColor.RED+"Denied Container Access", new String[]{ChatColor.WHITE+"Friends of this land cannot", ChatColor.WHITE+"access containers.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)14)));
                    } else {
                        ui.setItem(21,makeButton(ChatColor.GREEN+"Allowed Container Access", new String[]{ChatColor.WHITE+"Friends of this land can",ChatColor.WHITE+"access containers.",ChatColor.YELLOW+"Click to toggle."},
                                new ItemStack(Material.WOOL, 1,(short)5)));
                    }
                    break;


            }
        }
    }
    /*
    private void forceClose(){
        player.closeInventory();
        InventoryCloseEvent.getHandlerList().unregister(this);
    }*/


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        HumanEntity p = event.getPlayer();
        //player.sendMessage(ChatColor.GREEN + "Closing! " + event.getEventName());
        if(event.getInventory().getTitle().contains("Land Manager") && p.getName().equalsIgnoreCase(player.getName())&& isOpen){


            mLand.setPermissions(mLand.permsToString(perms));
            Landlord.getInstance().getDatabase().save(mLand);
            player.sendMessage(ChatColor.GREEN + "Land permissions saved!");
            mLand.highlightLand(player, ParticleEffect.DRIP_LAVA);
            InventoryCloseEvent.getHandlerList().unregister(this);
        }
        isOpen = true;

    }

    @EventHandler
    public void clickButton(InventoryClickEvent event){
        if(event.getInventory().getTitle().contains("Land Manager") && event.getWhoClicked().getName().equalsIgnoreCase(player.getName())){
            //player.sendMessage(ChatColor.GREEN+"CLICK!");
            //System.out.println(event.getSlot() +"");
            event.setCancelled(true);
            switch (event.getSlot()){

                case 10:
                    if(perms[0][1].equals("1")){
                        perms[0][1]="0";
                    } else {
                        //System.out.println("Not allowed");
                        perms[0][1]="1";
                        //System.out.println("NewPerm: "+ perms[0][1]);
                    }
                    setToggles();
                    //showUI();
                    break;
                case 11:
                    if(perms[0][2].equals("1")){
                        perms[0][2]="0";
                    } else {
                        //System.out.println("Not allowed");
                        perms[0][2]="1";
                        //System.out.println("NewPerm: "+ perms[0][1]);
                    }
                    setToggles();
                    //showUI();
                    break;
                case 12:
                    if(perms[0][3].equals("1")){
                        perms[0][3]="0";
                    } else {
                        //System.out.println("Not allowed");
                        perms[0][3]="1";
                        //System.out.println("NewPerm: "+ perms[0][1]);
                    }
                    setToggles();
                    //showUI();
                    break;
                case 19:
                    if(perms[1][1].equals("1")){
                        perms[1][1]="0";
                    } else {
                        //System.out.println("Not allowed");
                        perms[1][1]="1";
                        //System.out.println("NewPerm: "+ perms[0][1]);
                    }
                    setToggles();
                    //showUI();
                    break;
                case 20:
                    if(perms[1][2].equals("1")){
                        perms[1][2]="0";
                    } else {
                        //System.out.println("Not allowed");
                        perms[1][2]="1";
                        //System.out.println("NewPerm: "+ perms[0][1]);
                    }
                    setToggles();
                    //showUI();
                    break;
                case 21:
                    if(perms[1][3].equals("1")){
                        perms[1][3]="0";
                    } else {
                        //System.out.println("Not allowed");
                        perms[1][3]="1";
                        //System.out.println("NewPerm: "+ perms[0][1]);
                    }
                    setToggles();
                    //showUI();
                    break;

            }
        }

    }






}
