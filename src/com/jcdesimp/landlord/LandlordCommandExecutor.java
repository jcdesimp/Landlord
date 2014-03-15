package com.jcdesimp.landlord;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.DarkBladee12.ParticleAPI.ParticleEffect;

import java.util.ArrayList;
import java.util.List;
import static org.bukkit.util.NumberConversions.ceil;

/**
 * Command Executor class for LandLord
 */
@SuppressWarnings("UnusedParameters")
public class LandlordCommandExecutor implements CommandExecutor {
    private Landlord plugin; //pointer to main class
    public LandlordCommandExecutor(Landlord plugin){
        this.plugin = plugin;

    }

    /**
     * Main command handler
     * @param sender who sent the command
     * @param label exact command (or alias) run
     * @param args given with command
     * @return boolean
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("landlord")){ // If the player typed /land then do the following...
            /*
             * ****************************************************************
             *   Use private methods below to define command implementation
             *   call those methods from within these cases
             * ****************************************************************
             */
            if(args.length == 0){

                //landlord
                return landlord(sender, label);

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
                return landlord(sender, label);
            }

        } //If this has happened the function will return true.
        // If this hasn't happened the value of false will be returned.
        return false;
    }





    /*
     * **********************************************************
     * private methods for handling each command functionality
     * **********************************************************
     */


    /**
     * Called when base command /landlord or aliases (/ll /land)
     * are executed with no parameters
     *
     * @param sender who executed the command
     * @return boolean
     */
    private boolean landlord(CommandSender sender, String label) {
        String helpMsg = "";
        helpMsg+=ChatColor.DARK_GREEN + "--|| Landlord v"+Landlord.getInstance().getDescription().getVersion() +
                " Created by " + ChatColor.BLUE+"Jcdesimp "+ChatColor.DARK_GREEN +"||--\n"+
                ChatColor.GRAY+"(Aliases: /landlord, /land, or /ll)\n"+
                ChatColor.DARK_AQUA+"/"+label + " help" + ChatColor.RESET + " - Show this help message.\n"+
                ChatColor.DARK_AQUA+"/"+label + " claim (or "+"/"+label +" buy) [x,z] [world]" + ChatColor.RESET + " - Claim this chunk.\n"+
                ChatColor.DARK_AQUA+"/"+label + " unclaim (or "+"/"+label +" sell)" + ChatColor.RESET + " - Unclaim this chunk.\n"+
                ChatColor.DARK_AQUA+"/"+label + " addfriend <player name>" + ChatColor.RESET + " - Add a friend to this land.\n"+
                ChatColor.DARK_AQUA+"/"+label + " remfriend <player name>" + ChatColor.RESET + " - Remove a friend from this land.\n"+
                ChatColor.DARK_AQUA+"/"+label + " manage" + ChatColor.RESET + " - Manage permissions for this land.\n"
        ;
        helpMsg+=ChatColor.DARK_AQUA+"/"+label + " map" + ChatColor.RESET + " - Toggle the land map.\n";
        helpMsg+=ChatColor.DARK_AQUA+"/"+label + " list" + ChatColor.RESET + " - List all your owned land.\n";
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
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
            }




            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();


            if(plugin.hasWorldGuard()){
                if(!plugin.getWgHandler().canClaim(player,currChunk)){
                    player.sendMessage(ChatColor.RED+"You cannot claim here.");
                    return true;
                }
            }



            OwnedLand land = OwnedLand.landFromProperties(player.getName(), currChunk);
            OwnedLand dbLand = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());


            if(dbLand != null){
                if (dbLand.getOwnerName().equalsIgnoreCase(player.getName())){
                    player.sendMessage(ChatColor.YELLOW + "You already own this land!");
                    return true;
                }
                player.sendMessage(ChatColor.YELLOW + "Someone else owns this land.");
                return true;

            }
            int limit = plugin.getConfig().getInt("limits.landLimit",10);
            if(limit >= 0){
                if(player.hasPermission("landlord.limit.extra")){
                    limit+=plugin.getConfig().getInt("limits.extra",0);
                }
                if(plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getName()).findRowCount() >= limit){
                    player.sendMessage(ChatColor.RED+"You have reached the land limit.");
                    return true;
                }
            }

            if(plugin.hasVault()){
                if(plugin.getvHandler().hasEconomy()){
                    Double amt = plugin.getConfig().getDouble("economy.buyPrice", 100.0);
                    if(amt > 0){
                        if(!plugin.getvHandler().chargeCash(player, amt)){
                            player.sendMessage(ChatColor.RED+"It costs " + plugin.getvHandler().formatCash(amt) + " to purchase land.");
                            return true;
                        } else {
                            player.sendMessage(ChatColor.YELLOW+"You have been charged " + plugin.getvHandler().formatCash(amt) + " to purchase land.");
                        }
                    }

                }
            }
            Landlord.getInstance().getDatabase().save(land);
            land.highlightLand(player, ParticleEffect.HAPPY_VILLAGER);
            sender.sendMessage(
                    ChatColor.GREEN + "Successfully claimed chunk (" + currChunk.getX() + ", " +
                            currChunk.getZ() + ") in world " + currChunk.getWorld().getName() + "." );

            if(plugin.getConfig().getBoolean("options.soundEffects",true)){
               player.playSound(player.getLocation(),Sound.FIREWORK_TWINKLE2,10,10);
            }


            plugin.getMapManager().updateAll();
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
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
            }
            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand dbLand = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if (dbLand == null || !dbLand.getOwnerName().equalsIgnoreCase(player.getName())){
                player.sendMessage(ChatColor.RED + "You do not own this land.");
                return true;
            }
            if(plugin.hasVault()){
                if(plugin.getvHandler().hasEconomy()){
                    Double amt = plugin.getConfig().getDouble("economy.sellPrice", 100.0);
                    if(amt > 0){
                        if(plugin.getvHandler().giveCash(player, amt)){
                            player.sendMessage(ChatColor.GREEN+"Land sold for " + plugin.getvHandler().formatCash(amt) + ".");
                            //return true;
                        }
                    }

                }
            } 
            plugin.getDatabase().delete(dbLand);
            dbLand.highlightLand(player, ParticleEffect.WITCH_MAGIC);

            sender.sendMessage(
                    ChatColor.YELLOW + "Successfully unclaimed chunk (" + currChunk.getX() + ", " +
                            currChunk.getZ() + ") in world " + currChunk.getWorld().getName() + "."
            );
            if(plugin.getConfig().getBoolean("options.soundEffects",true)){
                player.playSound(player.getLocation(),Sound.ENDERMAN_HIT,10,.5f);
            }
            plugin.getMapManager().updateAll();

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

        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            if (args.length < 2){
                sender.sendMessage(ChatColor.RED + "usage: /land addfriend <player>");
                return true;
            }
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
            }

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
            land.highlightLand(player, ParticleEffect.HEART, 2);
            plugin.getDatabase().save(land);
            if(plugin.getConfig().getBoolean("options.soundEffects",true)){
                player.playSound(player.getLocation(),Sound.ORB_PICKUP,10,.2f);
            }
            sender.sendMessage(ChatColor.GREEN + "Player " + args[1] +" is now a friend of this land.");
            plugin.getMapManager().updateAll();

        }
        return true;
    }


    /**
     * Removes a friend from an owned chunk
     * Called when landlord remfriend is executed
     * @param sender who executed the command
     * @param args given with command
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
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
            }

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
            land.highlightLand(player, ParticleEffect.ANGRY_VILLAGER, 2);
            plugin.getDatabase().save(land);
            if(plugin.getConfig().getBoolean("options.soundEffects",true)){
                player.playSound(player.getLocation(),Sound.ZOMBIE_INFECT,10,.5f);
            }
            player.sendMessage(ChatColor.GREEN + "Player " + args[1] + " is no longer a friend of this land.");

        }
        return true;

    }





    /**
     * Toggles the land map display
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    private boolean landlord_map(CommandSender sender, String[] args) {
        if(!plugin.getConfig().getBoolean("options.enableMap", true)){
            sender.sendMessage(ChatColor.YELLOW+"Land Map is disabled.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.map")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
            }
            plugin.getMapManager().toggleMap(player);

        }
        return true;

    }




    /**
     * Command for managing player land perms
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    private boolean landlord_manage(CommandSender sender, String[] args){
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
            }
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
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
            }

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
                String header = ChatColor.DARK_GREEN+"   | ( X, Z ) - World Name |     \n";
                ArrayList<String> landList = new ArrayList<String>();
                //OwnedLand curr = myLand.get(0);
                for (OwnedLand aMyLand : myLand) {
                    landList.add((ChatColor.GOLD + "     (" + aMyLand.getX() + ", " + aMyLand.getZ() + ") - "
                            + aMyLand.getWorldName()) + "\n")
                    ;
                }
                //Amount to be displayed per page
                final int numPerPage = 7;

                int numPages = ceil((double)landList.size()/(double)numPerPage);
                if(pageNumber > numPages){
                    player.sendMessage(ChatColor.RED+"That is not a valid page number.");
                    return true;
                }
                String pMsg = ChatColor.DARK_GREEN+"--- " +ChatColor.YELLOW+"Your Owned Land"+ChatColor.DARK_GREEN+" ---"+ChatColor.YELLOW+" Page "+pageNumber+ChatColor.DARK_GREEN+" ---\n"+header;
                if (pageNumber == numPages){
                    for(int i = (numPerPage*pageNumber-numPerPage); i<landList.size(); i++){
                        pMsg+=landList.get(i);
                    }
                    pMsg+=ChatColor.DARK_GREEN+"------------------------------";
                } else {
                    for(int i = (numPerPage*pageNumber-numPerPage); i<(numPerPage*pageNumber); i++){
                        pMsg+=landList.get(i);
                    }
                    pMsg+=ChatColor.DARK_GREEN+"--- do"+ChatColor.YELLOW+" /"+label+" list "+(pageNumber+1)+ChatColor.DARK_GREEN+" for next page ---";
                }

                player.sendMessage(pMsg);
            }

        }
        return  true;
    }

}
