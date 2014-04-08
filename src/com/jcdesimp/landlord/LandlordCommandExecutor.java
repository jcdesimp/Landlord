package com.jcdesimp.landlord;

import com.jcdesimp.landlord.landManagement.LandManagerView;
import com.jcdesimp.landlord.persistantData.Friend;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.jcdesimp.landlord.DarkBladee12.ParticleAPI.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getPlayer;
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
                return landlord(sender, args, label);

            } else if(args[0].equalsIgnoreCase("help")) {

                //landlord claim
                return landlord_help(sender, args, label);

            } else if(args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("buy")) {

                //landlord claim
                return landlord_claim(sender, args);

            } else if(args[0].equalsIgnoreCase("unclaim") || args[0].equalsIgnoreCase("sell")) {

                //landlord unclaim
                return landlord_unclaim(sender, args, label);
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

            } else if(args[0].equalsIgnoreCase("listplayer")) {
                return landlord_listplayer(sender, args, label);

            } else if(args[0].equalsIgnoreCase("clearworld")) {
                return landlord_clearWorld(sender, args, label);

            } else if(args[0].equalsIgnoreCase("reload")) {

                return landlord_reload(sender,args,label);
            } else if(args[0].equalsIgnoreCase("info")) {

                return landlord_info(sender,args,label);
            } else if(args[0].equalsIgnoreCase("friends")) {

                return landlord_friends(sender,args,label);
            } else {
                return landlord(sender, args, label);
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
    private boolean landlord(CommandSender sender, String[] args, String label) {
        sender.sendMessage(ChatColor.DARK_GREEN + "--|| Landlord v"+Landlord.getInstance().getDescription().getVersion() +
                " Created by " + ChatColor.BLUE+"Jcdesimp "+ChatColor.DARK_GREEN +"||--\n"+
                //ChatColor.GRAY+"(Aliases: /landlord, /land, or /ll)\n"+
                ChatColor.DARK_GREEN+"Type " +ChatColor.YELLOW+"/"+label+" help "+ChatColor.DARK_GREEN +"for a list of commands");
        return true;
    }

    private boolean landlord_help(CommandSender sender, String[] args, String label) {
        /*String helpMsg = "";
        helpMsg+=ChatColor.DARK_GREEN + "--|| Landlord v"+Landlord.getInstance().getDescription().getVersion() +
                " Created by " + ChatColor.BLUE+"Jcdesimp "+ChatColor.DARK_GREEN +"||--\n"+
                ChatColor.GRAY+"(Aliases: /landlord, /land, or /ll)\n"+
                ChatColor.DARK_AQUA+"/"+label + " help" + ChatColor.RESET + " - Show this help message.\n"+
                ChatColor.DARK_AQUA+"/"+label + " claim (or "+"/"+label +" buy)" + ChatColor.RESET + " - Claim this chunk.\n"+
                ChatColor.DARK_AQUA+"/"+label + " unclaim [x,z] [world] (or "+"/"+label +" sell)" + ChatColor.RESET + " - Unclaim this chunk.\n"+
                ChatColor.DARK_AQUA+"/"+label + " addfriend <player name>" + ChatColor.RESET + " - Add a friend to this land.\n"+
                ChatColor.DARK_AQUA+"/"+label + " remfriend <player name>" + ChatColor.RESET + " - Remove a friend from this land.\n"+
                ChatColor.DARK_AQUA+"/"+label + " manage" + ChatColor.RESET + " - Manage permissions for this land.\n"
        ;
        helpMsg+=ChatColor.DARK_AQUA+"/"+label + " map" + ChatColor.RESET + " - Toggle the land map.\n";
        helpMsg+=ChatColor.DARK_AQUA+"/"+label + " list" + ChatColor.RESET + " - List all your owned land.\n";
        sender.sendMessage(helpMsg);
        return true;*/


        //check if page number is valid
        int pageNumber = 1;
        if (args.length > 1 && args[0].equals("help")){
            try{
                pageNumber = Integer.parseInt(args[1]);}
            catch (NumberFormatException e){
                sender.sendMessage(ChatColor.RED+"That is not a valid page number.");
                return true;
            }
        }

        //List<OwnedLand> myLand = plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getName()).findList();

        String header = ChatColor.DARK_GREEN + "--|| Landlord v"+Landlord.getInstance().getDescription().getVersion() +
                " Created by " + ChatColor.BLUE+"Jcdesimp "+ChatColor.DARK_GREEN +"||--\n"+
                ChatColor.GRAY+"(Aliases: /landlord, /land, or /ll)\n";

        ArrayList<String> helpList = new ArrayList<String>();


        helpList.add(ChatColor.DARK_AQUA+"/"+label + " help [page #]" + ChatColor.RESET + " - Show this help message.\n");
        String claim = ChatColor.DARK_AQUA+"/"+label + " claim (or "+"/"+label +" buy)" + ChatColor.RESET + " - Claim this chunk.\n";
        if(plugin.hasVault()){
            if(plugin.getvHandler().hasEconomy() && plugin.getConfig().getDouble("economy.buyPrice", 100.0)>0){
                claim += ChatColor.YELLOW+""+ChatColor.ITALIC+" Costs "+plugin.getvHandler().formatCash(plugin.getConfig().getDouble("economy.buyPrice", 100.0))+" to claim.\n";
            }
        }
        helpList.add(claim);
        String unclaim = ChatColor.DARK_AQUA+"/"+label + " unclaim [x,z] [world] (or "+"/"+label +" sell)" + ChatColor.RESET + " - Unclaim this chunk.\n";
        if(plugin.hasVault()){
            if(plugin.getvHandler().hasEconomy() && plugin.getConfig().getDouble("economy.sellPrice", 50.0)>0){
                unclaim += ChatColor.YELLOW+""+ChatColor.ITALIC+" Get "+plugin.getvHandler().formatCash(plugin.getConfig().getDouble("economy.sellPrice", 50.0))+" per unclaim.\n";
            }
        }
        helpList.add(unclaim);

        helpList.add(ChatColor.DARK_AQUA+"/"+label + " addfriend <player name>" + ChatColor.RESET + " - Add friend to this land.\n");
        helpList.add(ChatColor.DARK_AQUA+"/"+label + " remfriend <player name>" + ChatColor.RESET + " - Remove friend from this land.\n");
        helpList.add(ChatColor.DARK_AQUA+"/"+label + " friends" + ChatColor.RESET + " - List friends of this land.\n");
        helpList.add(ChatColor.DARK_AQUA+"/"+label + " manage" + ChatColor.RESET + " - Manage permissions for this land.\n");
        helpList.add(ChatColor.DARK_AQUA+"/"+label + " list" + ChatColor.RESET + " - List all your owned land.\n");
        if(sender.hasPermission("landlord.player.map") && plugin.getConfig().getBoolean("options.enableMap",true)){
            helpList.add(ChatColor.DARK_AQUA+"/"+label + " map" + ChatColor.RESET + " - Toggle the land map.\n");
        }
        if(sender.hasPermission("landlord.player.info") && plugin.getConfig().getBoolean("options.enableMap",true)){
            helpList.add(ChatColor.DARK_AQUA+"/"+label + " info" + ChatColor.RESET + " - View info about this chunk.\n");
        }
        if(sender.hasPermission("landlord.admin.list")){
            helpList.add(ChatColor.DARK_AQUA+"/"+label + " listplayer <player>" + ChatColor.RESET + " - List land owned by another player.\n");
        }
        if(sender.hasPermission("landlord.admin.clearworld")){
            helpList.add(ChatColor.DARK_AQUA+"/"+label + " clearworld <world> [player]" + ChatColor.RESET + " - Delete all land owned by a player in a world." +
                    " Delete all land of a world from console.\n");
        }
        if(sender.hasPermission("landlord.admin.reload")){
            helpList.add(ChatColor.DARK_AQUA+"/"+label + " reload" + ChatColor.RESET + " - Reloads the Landlord config file.\n");
        }
        //OwnedLand curr = myLand.get(0);

        //Amount to be displayed per page
        final int numPerPage = 5;

        int numPages = ceil((double)helpList.size()/(double)numPerPage);
        if(pageNumber > numPages){
            sender.sendMessage(ChatColor.RED+"That is not a valid page number.");
            return true;
        }
        String pMsg = header;
        if (pageNumber == numPages){
            for(int i = (numPerPage*pageNumber-numPerPage); i<helpList.size(); i++){
                pMsg+=helpList.get(i);
            }
            pMsg+=ChatColor.DARK_GREEN+"------------------------------";
        } else {
            for(int i = (numPerPage*pageNumber-numPerPage); i<(numPerPage*pageNumber); i++){
                pMsg+=helpList.get(i);
            }
            pMsg+=ChatColor.DARK_GREEN+"--- do"+ChatColor.YELLOW+" /"+label+" help "+(pageNumber+1)+ChatColor.DARK_GREEN+" for next page ---";
        }

        sender.sendMessage(pMsg);
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
                return true;
            }




            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();


            if(plugin.hasWorldGuard()){
                if(!plugin.getWgHandler().canClaim(player,currChunk)){
                    player.sendMessage(ChatColor.RED+"You cannot claim here.");
                    return true;
                }
            }



            OwnedLand land = OwnedLand.landFromProperties(player, currChunk);
            OwnedLand dbLand = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());


            if(dbLand != null){
                //Check if they already own this land
                if (dbLand.ownerUUID().equals(player.getUniqueId())){
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
                if(plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getUniqueId().toString()).findRowCount() >= limit){
                    player.sendMessage(ChatColor.RED+"You can only own " + limit + " chunks of land.");
                    return true;
                }
            }                       //

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
                            currChunk.getZ() + ") in world \'" + currChunk.getWorld().getName() + "\'." );

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
    private boolean landlord_unclaim(CommandSender sender, String[] args, String label) {

        //is sender a plater
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own") && !player.hasPermission("landlord.admin.unclaim")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
                return true;
            }
            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();

            int x = currChunk.getX();
            int z = currChunk.getZ();
            String worldname = currChunk.getWorld().getName();

            if(args.length>1){
                try{
                    String[] coords = args[1].split(",");
                    //System.out.println("COORDS: "+coords);
                    x = Integer.parseInt(coords[0]);
                    z = Integer.parseInt(coords[1]);
                    if(args.length>2){
                        worldname = args[2];
                        if(plugin.getServer().getWorld(worldname) == null){
                            player.sendMessage(ChatColor.RED + "World \'"+worldname + "\' does not exist.");
                            return true;
                        }
                    }
                } catch (NumberFormatException e){
                    //e.printStackTrace();
                    player.sendMessage(ChatColor.RED+"usage: /"+label +" "+ args[0]+ " [x,z] [world]");
                    return true;

                } catch (ArrayIndexOutOfBoundsException e){
                    player.sendMessage(ChatColor.RED+"usage: /"+label +" "+ args[0]+" [x,z] [world]");
                    return true;
                }
            }
            OwnedLand dbLand = OwnedLand.getLandFromDatabase(x, z, worldname);


            if (dbLand == null || (!dbLand.ownerUUID().equals(player.getUniqueId()) && !player.hasPermission("landlord.admin.unclaim"))){
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
            if(!player.getUniqueId().equals(dbLand.ownerUUID())){
                player.sendMessage(ChatColor.YELLOW+"Unclaimed " + getOfflinePlayer(dbLand.ownerUUID()) + "'s land.");
            }
            plugin.getDatabase().delete(dbLand);
            dbLand.highlightLand(player, ParticleEffect.WITCH_MAGIC);

            sender.sendMessage(
                    ChatColor.YELLOW + "Successfully unclaimed chunk (" + currChunk.getX() + ", " +
                            currChunk.getZ() + ") in world \'" + currChunk.getWorld().getName() + "\'."
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
                return true;
            }

            Chunk currChunk = player.getLocation().getChunk();

            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());

            //Does land exist, and if so does player own it
            if( land == null || !land.ownerUUID().equals(player.getUniqueId()) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");
                return true;
            }
            //
            if (!getOfflinePlayer(args[1]).hasPlayedBefore()){
                player.sendMessage(ChatColor.RED+"That player is not recognized.");
                return true;
            }
            Friend friend = Friend.friendFromPlayer(getOfflinePlayer(args[1]).getPlayer());
            /*
             * *************************************
             * mark for possible change    !!!!!!!!!
             * *************************************
             */

            if (! land.addFriend(friend)) {
                player.sendMessage(ChatColor.YELLOW + "Player " + args[1] + " is already a friend of this land.");
                return true;
            }
            if(plugin.getConfig().getBoolean("options.particleEffects",true)){
                land.highlightLand(player, ParticleEffect.HEART, 2);
            }

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
                return true;
            }

            Chunk currChunk = player.getLocation().getChunk();
            /*
             * *************************************
             * mark for possible change    !!!!!!!!!
             * *************************************
             */
            Friend frd = Friend.friendFromPlayer(getOfflinePlayer(args[1]).getPlayer());
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if( land == null || !land.ownerUUID().equals(player.getUniqueId()) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");
                return true;
            }
            if(!land.removeFriend(frd)){
                player.sendMessage(ChatColor.YELLOW + "Player " + args[1] + " is not a friend of this land.");
                return true;
            }
            if(plugin.getConfig().getBoolean("options.particleEffects",true)) {
                land.highlightLand(player, ParticleEffect.ANGRY_VILLAGER, 2);
            }
                plugin.getDatabase().save(land);
            if(plugin.getConfig().getBoolean("options.soundEffects",true)){
                player.playSound(player.getLocation(),Sound.ZOMBIE_INFECT,10,.5f);
            }
            player.sendMessage(ChatColor.GREEN + "Player " + args[1] + " is no longer a friend of this land.");

        }
        return true;

    }


    private boolean landlord_friends(CommandSender sender, String[] args, String label) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
                return true;
            }
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if( land == null || ( !land.ownerUUID().equals(player.getUniqueId()) && !player.hasPermission("landlord.admin.friends") ) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");
                return true;
            }
            if(!land.getOwnerName().equals(player.getUniqueId())){
                //player.sendMessage(ChatColor.YELLOW+"Viewing friends of someone else's land.");
            }
            if(plugin.getConfig().getBoolean("options.particleEffects",true)) {
                land.highlightLand(player, ParticleEffect.HEART, 3);
            }
            //check if page number is valid
            int pageNumber = 1;
            if (args.length > 1 && args[0].equals("friends")){
                try{
                    pageNumber = Integer.parseInt(args[1]);}
                catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+"That is not a valid page number.");
                    return true;
                }
            }

            //List<OwnedLand> myLand = plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getName()).findList();

            String header = ChatColor.DARK_GREEN + "----- Friends of this Land -----\n";

            ArrayList<String> friendList = new ArrayList<String>();
            if(land.getFriends().isEmpty()){
                player.sendMessage(ChatColor.YELLOW+"This land has no friends.");
                return true;
            }
            for(Friend f: land.getFriends()){
                String fr = ChatColor.DARK_GREEN+" - "+ChatColor.GOLD+f.getName()+ChatColor.DARK_GREEN+" - ";
                /*
                 * *************************************
                 * mark for possible change    !!!!!!!!!
                 * *************************************
                 */
                if(Bukkit.getOfflinePlayer(f.getUUID()).isOnline()){
                    fr+= ChatColor.GREEN+""+ChatColor.ITALIC+" Online";
                } else {
                    fr+= ChatColor.RED+""+ChatColor.ITALIC+" Offline";
                }

                fr+="\n";
                friendList.add(fr);
            }

            //Amount to be displayed per page
            final int numPerPage = 8;

            int numPages = ceil((double)friendList.size()/(double)numPerPage);
            if(pageNumber > numPages){
                sender.sendMessage(ChatColor.RED+"That is not a valid page number.");
                return true;
            }
            String pMsg = header;
            if (pageNumber == numPages){
                for(int i = (numPerPage*pageNumber-numPerPage); i<friendList.size(); i++){
                    pMsg+=friendList.get(i);
                }
                pMsg+=ChatColor.DARK_GREEN+"------------------------------";
            } else {
                for(int i = (numPerPage*pageNumber-numPerPage); i<(numPerPage*pageNumber); i++){
                    pMsg+=friendList.get(i);
                }
                pMsg+=ChatColor.DARK_GREEN+"--- do"+ChatColor.YELLOW+" /"+label+" friends "+(pageNumber+1)+ChatColor.DARK_GREEN+" for next page ---";
            }
           player.sendMessage(pMsg);


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
                return true;
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
                return true;
            }
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if( land == null || ( !land.ownerUUID().equals(player.getUniqueId()) && !player.hasPermission("landlord.admin.manage") ) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");
                return true;
            }
            if(!land.ownerUUID().equals(player.getUniqueId())){
                player.sendMessage(ChatColor.YELLOW+"Managing someone else's land.");
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
                return true;
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

            List<OwnedLand> myLand = plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getUniqueId().toString()).findList();
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

    private boolean landlord_listplayer(CommandSender sender, String[] args, String label){
        String owner;

        //sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        if(args.length>1){
            owner = args[1];
        } else {
            sender.sendMessage(ChatColor.RED+"usage: /" + label + " listplayer <player> [page#]");
            return true;
        }

        //Player player = (Player) sender;
        if(!sender.hasPermission("landlord.admin.list")){
            sender.sendMessage(ChatColor.RED+"You do not have permission.");
            return true;
        }

        //check if page number is valid
        int pageNumber = 1;
        if (args.length > 2){
            try{
                pageNumber = Integer.parseInt(args[2]);}
            catch (NumberFormatException e){
                sender.sendMessage(ChatColor.RED+"That is not a valid page number.");
                return true;
            }
        }
        /*
         * *************************************
         * mark for possible change    !!!!!!!!!
         * *************************************
         */
        if( !getOfflinePlayer(owner).hasPlayedBefore()){
            sender.sendMessage(ChatColor.YELLOW+ owner +" does not own any land!");
            return true;
        }
        List<OwnedLand> myLand = plugin.getDatabase().find(OwnedLand.class).where().ieq("ownerName", getOfflinePlayer(owner).getUniqueId().toString()).findList();
        if(myLand.size()==0){
            sender.sendMessage(ChatColor.YELLOW+ owner +" does not own any land!");
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
                sender.sendMessage(ChatColor.RED+"That is not a valid page number.");
                return true;
            }
            String pMsg = ChatColor.DARK_GREEN+"--- " +ChatColor.YELLOW+ owner +"'s Owned Land"+ChatColor.DARK_GREEN+" ---"+ChatColor.YELLOW+" Page "+pageNumber+ChatColor.DARK_GREEN+" ---\n"+header;
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

            sender.sendMessage(pMsg);
        }


        return  true;
    }

    private boolean landlord_clearWorld(CommandSender sender, String[] args, String label){
        if(!sender.hasPermission("landlord.admin.clearworld")){
            sender.sendMessage(ChatColor.RED+"You do not have permission.");
            return true;
        }
        if(args.length > 1){
            List<OwnedLand> land;
            if(args.length > 2){
                land = plugin.getDatabase().find(OwnedLand.class).where().ieq("ownerName",args[2]).eq("worldName",args[1]).findList();
            } else {
                if(sender instanceof Player){
                    sender.sendMessage(ChatColor.RED+"You can only delete entire worlds from the console.");
                    return true;
                }
                land = plugin.getDatabase().find(OwnedLand.class).where().eq("worldName",args[1]).findList();
            } 
            if(land.isEmpty()){
                sender.sendMessage(ChatColor.RED + "No land to remove.");
                return true;
            }

            plugin.getDatabase().delete(land);
            plugin.getMapManager().updateAll();
            sender.sendMessage(ChatColor.GREEN+"Land(s) deleted!");

        } else {
            sender.sendMessage(ChatColor.RED + "format: " + label + " clearworld <world> [<player>]");
        }
        return true;
    }


    /**
     * Reload landlord configuration file
     * @param sender who executed the command
     * @param args given with command
     * @param label exact command (or alias) run
     * @return boolean of success
     */
    private boolean landlord_reload(CommandSender sender, String[] args, String label){
        if(sender.hasPermission("landlord.admin.reload")){
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN+"Landlord config reloaded.");
            return true;
        }
        sender.sendMessage(ChatColor.RED+"You do not have permission.");
        return true;
    }


    private boolean landlord_info(CommandSender sender, String[] args, String label){
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.info")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");
                return true;
            }
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            String owner = ChatColor.GRAY + "" + ChatColor.ITALIC + "None";
            if( land != null ){

                /*
                 * *************************************
                 * mark for possible change    !!!!!!!!!
                 * *************************************
                 */
                owner = ChatColor.GOLD + land.getOwnerUsername();
            } else {
                land = OwnedLand.landFromProperties(null,currChunk);
            }

            if(plugin.getConfig().getBoolean("options.particleEffects")){
                land.highlightLand(player, ParticleEffect.DRIP_LAVA);
            }
            String msg = ChatColor.DARK_GREEN + "--- You are in chunk " + ChatColor.GOLD + "(" + currChunk.getX() + ", " + currChunk.getZ() + ") " +
                    ChatColor.DARK_GREEN + " in world \"" + ChatColor.GOLD + currChunk.getWorld().getName()  + ChatColor.DARK_GREEN + "\"\n"+ "----- Owned by: " +
                    owner;
            player.sendMessage(msg);

        }
        return true;

    }





}
