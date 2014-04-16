package com.jcdesimp.landlord.landManagement;

import com.jcdesimp.landlord.DarkBladee12.ParticleAPI.ParticleEffect;
import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;

/**
 * File created by jcdesimp on 3/4/14.
 */
public class LandManagerView implements Listener {

    private Player player;
    private Inventory ui;
    private OwnedLand mLand;
    private String[][] perms;
    private ArrayList<ItemStack[]> permCols = new ArrayList<ItemStack[]>();
    private ArrayList<Integer> permSlots = new ArrayList<Integer>();
    private int pageNum = 0;
    private boolean isOpen = false;

    public LandManagerView(Player player, OwnedLand mLand) {
        Landlord.getInstance().getServer().getPluginManager().registerEvents(this, Landlord.getInstance());


        this.player = player;
        this.mLand = mLand;
        this.perms = mLand.getLandPerms();
        this.ui = Bukkit.createInventory(null, 36, "Land Manager");
        this.updateUIData();
        this.buildUI();
        this.setToggles();
        //this.showUI();
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

    private void updateUIData(){
        //Generate dynamic items based on land perms
        permSlots.clear();
        permCols.clear();
        for(Map.Entry<String, Landflag> entry : Landlord.getInstance().getFlagManager().getRegisteredFlags().entrySet()){
            Landflag l = entry.getValue();
            permSlots.add(new Integer(l.getPermSlot()));
            String[] loreData = l.getDescrition().split("\\|");
            String[] desc = colorLore(loreData);
            ItemStack header = makeButton(ChatColor.YELLOW+l.getDisplayName(),desc,l.getHeaderItem());
            ItemStack allState;
            //System.out.println("VALUE: "+mLand.getLandPerms()[0][l.getPermSlot()]);
            if(perms[0][l.getPermSlot()].equals("1")){
                desc = colorLore((
                        ("Regular players "+l.getAllowedText())+"|"+ChatColor.YELLOW+"Click to toggle.").split("\\|"));
                allState = makeButton(ChatColor.GREEN+l.getAllowedTitle(), desc, new ItemStack(Material.WOOL,1, (short)5));
            } else {
                desc = colorLore((("Regular players "+l.getDeniedText()+"|"+ChatColor.YELLOW+"Click to toggle.")).split("\\|"));
                allState = makeButton(ChatColor.RED+l.getDeniedTitle(), desc, new ItemStack(Material.WOOL,1, (short)14));
            }

            ItemStack friendState;
            if(perms[1][l.getPermSlot()].equals("1")){
                desc = colorLore((("Friends of this land "+l.getAllowedText())+"|"+ChatColor.YELLOW+"Click to toggle.").split("\\|"));
                friendState = makeButton(ChatColor.GREEN+l.getAllowedTitle(), desc, new ItemStack(Material.WOOL,1, (short)5));
            } else {
                desc = colorLore((("Friends of this land "+l.getDeniedText()+"|"+ChatColor.YELLOW+"Click to toggle.")).split("\\|"));
                friendState = makeButton(ChatColor.RED+l.getDeniedTitle(), desc, new ItemStack(Material.WOOL,1, (short)14));
            }


            permCols.add(new ItemStack[] {header,allState,friendState});
        }
    }

    private String[] colorLore(String[] loreData){
        String[] desc = new String[loreData.length];
        for(int s = 0; s<loreData.length; s++){
            desc[s] = ChatColor.RESET+loreData[s];
        }
        return desc;
    }

    private void buildUI(){
        // Static Items Help and row/column markers
        ui.setItem(0,makeButton(ChatColor.GOLD+"Help",
                new String[]{
                ChatColor.RESET+"Click each wool block",
                ChatColor.RESET+"to toggle a permission for a group.",
                ChatColor.RESET+"Red wool means not allowed",
                ChatColor.RESET+"and green wool means allowed.",
                ChatColor.RESET+"Mouseover each item for more information."
                }, Material.ENCHANTED_BOOK));

        /*
        ui.setItem(1,makeButton(ChatColor.YELLOW+ "Build", new String[]{ChatColor.RESET+"Gives permission to place",ChatColor.RESET+"and break blocks."}, Material.COBBLESTONE));
        ui.setItem(2,makeButton(ChatColor.YELLOW+"Harm Animals", new String[]{ChatColor.RESET+"Gives permission to hurt or kill",
                ChatColor.RESET+"pigs, sheep, cows, mooshrooms,",ChatColor.RESET+"chickens, horses, dogs and cats."}, Material.LEATHER));
        ui.setItem(3, makeButton(ChatColor.YELLOW+"Open Containers", new String[]{ChatColor.RESET+"Gives permission to use trap chests,",
                ChatColor.RESET+"chests, furnaces, anvils, hoppers,", ChatColor.RESET+"droppers, and dispensers."}, Material.CHEST));
        */
        ui.setItem(9,makeButton(ChatColor.YELLOW+"Everyone", new String[]{ChatColor.RESET+"Permissions in this row apply to",
                ChatColor.RESET+"people that aren't friends",ChatColor.RESET+"of this land."},
                new ItemStack(Material.SKULL_ITEM, 1, (short)2)));
        ui.setItem(18,makeButton(ChatColor.YELLOW+"Friends", new String[]{ChatColor.RESET+"Permissions in this row apply to", ChatColor.RESET+"friends of this land."},
                new ItemStack(Material.SKULL_ITEM, 1, (short)3)));





    }

    private void setToggles(){

        int numPages = (int)ceil(permCols.size()/8);
        int startIndex = pageNum*8;
        int endIndex;
        if(pageNum==numPages){
            endIndex = permCols.size();
        } else {
            endIndex = startIndex+8;
        }
        int slot = 1;
        for(int i=startIndex; i<endIndex; i++){
            ui.setItem(slot,permCols.get(i)[0]);
            ui.setItem(slot+9,permCols.get(i)[1]);
            ui.setItem(slot+18,permCols.get(i)[2]);
            slot++;
        }



    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        HumanEntity p = event.getPlayer();

        //player.sendMessage(ChatColor.GREEN + "Closing: " + event.getInventory().getTitle() + " of type "+ event.getInventory().getType());
        //player.sendMessage(ChatColor.GREEN + "Viewer:" +
                //" " + event.getViewers().toString());
        if(event.getInventory().getTitle().contains("Land Manager") && p.getName().equalsIgnoreCase(player.getName()) /*&& isOpen*/ ){


            mLand.setPermissions(mLand.permsToString(perms));
            Landlord.getInstance().getDatabase().save(mLand);
            player.sendMessage(ChatColor.GREEN + "Land permissions saved!");
            if(Landlord.getInstance().getConfig().getBoolean("options.soundEffects",true)){
                player.playSound(player.getLocation(),Sound.FIZZ,10,10);
            }
            mLand.highlightLand(player, ParticleEffect.DRIP_LAVA);
            //InventoryCloseEvent.getHandlerList().unregister(this);
            HandlerList.unregisterAll(this);
        }
        //isOpen = true;

    }

    @EventHandler
    public void clickButton(InventoryClickEvent event){
        if(event.getInventory().getTitle().contains("Land Manager") && event.getWhoClicked().getName().equalsIgnoreCase(player.getName())){
            //player.sendMessage(ChatColor.GREEN+"CLICK!");
            //System.out.println(event.getSlot() +"");
            event.setCancelled(true);
            //System.out.println("ClickedSlot: "+event.getRawSlot());
            int slot = event.getRawSlot();

            HashMap<String, Landflag>  pSlots = Landlord.getInstance().getFlagManager().getRegisteredFlags();

            //RowCount
            int row = slot/9;
            //System.out.println("ROW: "+row);

            //ColCount
            int col = slot%9;
            //System.out.println("COL: "+col);

            int numPages = (int)ceil(((double)permCols.size())/8.0);
            //System.out.println("PAGES: "+pageNum);
            int startIndex = pageNum*8;
            int endIndex;
            if(pageNum==numPages-1){
                endIndex = permCols.size();
            } else {
                endIndex = startIndex+8;
            }
            //System.out.println("EndIndex: "+endIndex);
            //System.out.println("StartIndex: "+startIndex);
            if((col<=(endIndex-startIndex) && col>0)){
                if(row == 1) {
                    perms[0][permSlots.get((col-1)+((pageNum)*8))] = bSwap(perms[0][permSlots.get((col-1)+((pageNum)*8))]);
                } else if (row == 2){
                    //System.out.println((col-1)+((pageNum)*8));
                    perms[1][permSlots.get((col-1)+((pageNum)*8))] = bSwap(perms[1][permSlots.get((col-1)+((pageNum)*8))]);
                    //System.out.println(perms[1][permSlots.get((col-1)+((pageNum)*8))]);
                }
                updateUIData();
                setToggles();
            }

        }

    }

    private String bSwap(String s){
        if(s.equals("0")){
            return "1";
        } else {
            return "0";
        }
    }






}
