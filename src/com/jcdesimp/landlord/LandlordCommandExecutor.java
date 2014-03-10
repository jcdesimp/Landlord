package com.jcdesimp.landlord;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.meta.MetaQueryStatistic;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import com.DarkBladee12.ParticleAPI.ParticleEffect;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.util.NumberConversions.ceil;

/**
 * Command Executor class for LandLord
 */
public class LandlordCommandExecutor implements CommandExecutor {
    private Landlord plugin; //pointer to main class

    public LandlordCommandExecutor(Landlord plugin){
        this.plugin = plugin;
    }

    /**
     * Main command handler
     * @param sender who sent the command
     * @param label  ???
     * @param args given with command
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("landlord")){ // If the player typed /land then do the following...
            /*
             * ****************************************************************
             *   Use private methods below to define command implementation
             *   call those methods form within these cases
             * ****************************************************************
             */
            if(args.length == 0){

                //landlord
                return landlord(sender, args, label);

            } else if(args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("buy")) {

                //landlord claim
                return landlord_claim(sender, args);

            } else if(args[0].equalsIgnoreCase("unclaim") || args[0].equalsIgnoreCase("sell")) {

                //landlord unclaim
                return landlord_unclaim(sender, args);
            } else if(args[0].equalsIgnoreCase("addfriend")) {

                //landlord addfriend
                return landlord_addfriend(sender, args);
            } else if(args[0].equalsIgnoreCase("remfriend")) {

                return landlord_remfriend(sender, args);
            } else if(args[0].equalsIgnoreCase("map")) {

                return landlord_map(sender, args);
            } else if(args[0].equalsIgnoreCase("manage")) {
                return landlord_manage(sender, args);

            } else if(args[0].equalsIgnoreCase("list")) {
                return landlord_list(sender, args, label);

            } else {
                return landlord(sender, args, label);
            }

        } //If this has happened the function will return true.
        // If this hasn't happened the value of false will be returned.
        return false;
    }





    /*
     * **********************************************************
     * private methods for handling each command's functionality
     * **********************************************************
     */


    /**
     * Called when base command /landlord or aliases (/ll /land)
     * are executed with no parameters
     * @param sender who executed the command
     * @param args given with the command
     * @return boolean
     */
    private boolean landlord(CommandSender sender, String[] args, String label) {
        String helpMsg = "";
        helpMsg+=ChatColor.DARK_GREEN + "--|| Landlord v"+Landlord.getInstance().getDescription().getVersion() +
                " Created by " + ChatColor.BLUE+"Jcdesimp "+ChatColor.DARK_GREEN +"||--\n"+
                ChatColor.GRAY+"(Aliases: /landlord, /land, or /ll)\n"+
                ChatColor.DARK_AQUA+"/"+label + " help" + ChatColor.RESET + " - Show this help message.\n"+
                ChatColor.DARK_AQUA+"/"+label + " claim (or "+"/"+label +" buy)" + ChatColor.RESET + " - Claim this chunk.\n"+
                ChatColor.DARK_AQUA+"/"+label + " unclaim (or "+"/"+label +" sell)" + ChatColor.RESET + " - Unclaim this chunk.\n"+
                ChatColor.DARK_AQUA+"/"+label + " addfriend <player name>" + ChatColor.RESET + " - Add a friend to this land.\n"+
                ChatColor.DARK_AQUA+"/"+label + " remfriend <player name>" + ChatColor.RESET + " - Remove a friend from this land.\n"+
                ChatColor.DARK_AQUA+"/"+label + " manage" + ChatColor.RESET + " - Manage permissions for this land.\n"
        ;
        helpMsg+=ChatColor.DARK_AQUA+"/"+label + " map" + ChatColor.RESET + " - Toggle the land map.\n";
        sender.sendMessage(helpMsg);
        return true;
    }

    /**
     * Called when landlord claim command is executed
     * This command must be run by a player
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    private boolean landlord_claim(CommandSender sender, String[] args) {


        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();

            OwnedLand land = OwnedLand.landFromProperties(player.getName(), currChunk);
            OwnedLand dbLand = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if(dbLand != null){
                if (dbLand.getOwnerName().equalsIgnoreCase(player.getName())){
                    player.sendMessage(ChatColor.YELLOW + "You already own this land!");
                    return true;
                }
                player.sendMessage(ChatColor.YELLOW + "Someone else owns this land.");
                return true;

            };
            Landlord.getInstance().getDatabase().save(land);
            land.higlightLand(player, ParticleEffect.HAPPY_VILLAGER);
            sender.sendMessage(
                ChatColor.GREEN + "Successfully claimed chunk (" + currChunk.getX() + ", " +
                currChunk.getZ() + ") in world " + currChunk.getWorld().getName() + "."
            );
            //sender.sendMessage(ChatColor.DARK_GREEN + "Land claim command executed!");
        }
        return true;
    }


    /**
     * Called when landlord unclaim command is executed
     * This command must be run by a player
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    private boolean landlord_unclaim(CommandSender sender, String[] args) {

        //is sender a plater
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand dbLand = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if (dbLand == null || !dbLand.getOwnerName().equalsIgnoreCase(player.getName())){
                player.sendMessage(ChatColor.RED + "You do not own this land.");
                return true;
            }
            dbLand.higlightLand(player, ParticleEffect.WITCH_MAGIC);
            plugin.getDatabase().delete(dbLand);

            sender.sendMessage(
                    ChatColor.YELLOW + "Successfully unclaimed chunk (" + currChunk.getX() + ", " +
                            currChunk.getZ() + ") in world " + currChunk.getWorld().getName() + "."
            );

        }
        return true;
    }

    /**
     * Adds a friend to an owned chunk
     * Called when landlord addfriend command is executed
     * This command must be run by a player
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    private boolean landlord_addfriend(CommandSender sender, String[] args) {

        //is sender a plater
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            if (args.length < 2){
                sender.sendMessage(ChatColor.RED + "usage: /land addfriend <player>");
                return true;
            }
            Player player = (Player) sender;

            Chunk currChunk = player.getLocation().getChunk();

            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());

            //Does land exist, and if so does player own it
            if( land == null || !land.getOwnerName().equalsIgnoreCase(player.getName()) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");
                return true;
            }
            //
            Friend friend = Friend.friendFromName(args[1]);
            if (! land.addFriend(friend)) {
                player.sendMessage(ChatColor.YELLOW + "Player " + args[1] + " is already a friend of this land.");
                return true;
            }
            land.higlightLand(player, ParticleEffect.HEART,2);
            plugin.getDatabase().save(land);
            sender.sendMessage(ChatColor.GREEN + "Player " + args[1] +" is now a friend of this land.");

        }
        return true;
    }


    /**
     * Removes a friend from an owned chunk
     * Called when landlord remfriend is executed
     * @param sender
     * @param args
     * @return boolean
     */
    private boolean landlord_remfriend(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            if (args.length < 2){
                sender.sendMessage(ChatColor.RED + "usage: /land remfriend <player>");
                return true;
            }
            Player player = (Player) sender;

            Chunk currChunk = player.getLocation().getChunk();
            Friend frd = Friend.friendFromName(args[1]);
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if( land == null || !land.getOwnerName().equalsIgnoreCase(player.getName()) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");
                return true;
            }
            if(!land.removeFriend(frd)){
                player.sendMessage(ChatColor.YELLOW + "Player " + args[1] + " is not a friend of this land.");
                return true;
            }
            land.higlightLand(player, ParticleEffect.ANGRY_VILLAGER,2);
            plugin.getDatabase().save(land);
            player.sendMessage(ChatColor.GREEN + "Player " + args[1] + " is no longer a friend of this land.");

        }
        return true;

    }


    /**
     *
     * @param sender
     * @param args
     * @return
     */
    private boolean landlord_map(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            final Player player = (Player) sender;

            /*String[] brd = LandMap.buildMap(player);
            for(int s = 0; s<brd.length; s++){
                player.sendMessage(brd[s]+"\n");
            }*/
            //player.sendMessage(ChatColor.YELLOW + "" + "░░░▒░░░\n░░▒▒▒░░\n░▒▒▒▒▒░\n▒▒▒█▒▒▒\n▒▒░░░▒▒\n▒░░░░░▒\n░░░░░░░");
            if(player.getScoreboard().getObjective("Land Map") != null){
                Bukkit.getServer().getScheduler().cancelAllTasks();
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                //player.setScoreboard();
                return true;
            }
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    LandMap.displayMap(player);
                }
            }, 0L, 5L);

            LandMap.displayMap(player);
            return  true;
        }
        return true;

    }

    private boolean landlord_manage(CommandSender sender, String[] args){
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if( land == null || !land.getOwnerName().equalsIgnoreCase(player.getName()) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");
                return true;
            }
            LandManagerView ui = new LandManagerView(player, land);
            ui.showUI();


        }
        return true;

    }

    /**
     * Display a list of all owned land to a player
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    private boolean landlord_list(CommandSender sender, String[] args, String label){

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;

            //check if page number is valid
            int pageNumber = 1;
            if (args.length > 1){
                try{
                    pageNumber = Integer.parseInt(args[1]);}
                catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+"That is not a valid page number.");
                    return true;
                }
            }

            List<OwnedLand> myLand = plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getName()).findList();
            if(myLand.size()==0){
                player.sendMessage(ChatColor.YELLOW+"You do not own any land!");
            } else {
                String header = ChatColor.DARK_GREEN+"-----( X, Z )--| World Name |-------\n";
                ArrayList<String> landList = new ArrayList<String>();
                //OwnedLand curr = myLand.get(0);
                for(int i = 0; i<myLand.size(); i++){
                    landList.add((ChatColor.GOLD + "     (" + myLand.get(i).getX() + ", " + myLand.get(i).getZ() + ") - "
                            + myLand.get(i).getWorldName()) +"\n")
                            ;
                }
                final int numPerPage = 6;
                int numPages = (int)ceil((double)landList.size()/(double)numPerPage);
                if(pageNumber > numPages){
                    player.sendMessage(ChatColor.RED+"That is not a valid page number.");
                    return true;
                }
                String pMsg = ChatColor.DARK_GREEN+"--- Your Owned Land ---"+" Page "+pageNumber+" ---\n"+header;
                if (pageNumber == numPages){
                    for(int i = (numPerPage*pageNumber-numPerPage); i<landList.size(); i++){
                        pMsg+=landList.get(i);
                    }
                    pMsg+=ChatColor.DARK_GREEN+"------------------------------";
                } else {
                    for(int i = (numPerPage*pageNumber-numPerPage); i<(numPerPage*pageNumber); i++){
                        pMsg+=landList.get(i);
                    }
                    pMsg+=ChatColor.DARK_GREEN+"--- do /"+label+" list "+(pageNumber+1)+" for next page ---";
                }

                player.sendMessage(pMsg);
            }

        }
        return  true;
    }

}
