package com.jcdesimp.landlord;

import com.jcdesimp.landlord.commands.*;
import com.jcdesimp.landlord.landManagement.LandManagerView;
import com.jcdesimp.landlord.persistantData.Friend;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getWorld;
import static org.bukkit.util.NumberConversions.ceil;

/**
 * Command Executor class for LandLord
 */
@SuppressWarnings("UnusedParameters")
public class LandlordCommandExecutor implements CommandExecutor {

    private Landlord plugin; //pointer to main class
    private HashMap<String, LandlordCommand> registeredCommands;
    private ArrayList<String> commandHelp;

    public LandlordCommandExecutor(Landlord plugin){
        this.plugin = plugin;
        this.registeredCommands = new HashMap<String, LandlordCommand>();
        this.commandHelp = new ArrayList<String>();


        // note order of registration will affect how they show up in the help menu
        this.register(new Claim(plugin));       // register the claim command
        this.register(new Unclaim(plugin));     // register the unclaim command
        this.register(new AddFriend(plugin));   // register the addfriend command
        this.register(new FriendAll(plugin));   // register the friendall command
        this.register(new UnfriendAll(plugin)); // register the unfriendall command
        this.register(new Unfriend(plugin));    // register the unfriend command
        this.register(new Friends(plugin));     // register the friends command
        this.register(new ShowMap(plugin));     // register the map command
        this.register(new Manage(plugin));      // register the manage command
        this.register(new LandList(plugin));    // register the list command
        this.register(new ListPlayer(plugin));  // register the listplayer command
        this.register(new ClearWorld(plugin));  // register the clearworld command
        this.register(new Reload(plugin));      // register the reload command
        this.register(new Info(plugin));        // register the info command

        //todo CommandRefactor - initially all commands should be .registered()

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

            // Check if the attempted command is registered
            if (args.length == 0 || !registeredCommands.containsKey(args[0].toLowerCase())) {
                // if there is no command, or it's not registered, show the help text as the command given is unknown
                return landlord_help(sender, args, label);
            } else {
                // if it is, execute it with the given args
                return registeredCommands.get(args[0].toLowerCase()).execute(sender, args, label);
            }


            /*
             * ****************************************************************
             *   Use private methods below to define command implementation
             *   call those methods from within these cases
             * ****************************************************************

            if(args.length == 0){

                //landlord
                return landlord_help(sender, args, label);

            } else if(args[0].equalsIgnoreCase("help")) {

                //landlord claim
                return landlord_help(sender, args, label);

            } else if(args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("buy")) {

                //landlord claim
                return landlord_claim(sender, args);

            } else if(args[0].equalsIgnoreCase("unclaim") || args[0].equalsIgnoreCase("sell")) {

                //landlord unclaim
                return landlord_unclaim(sender, args, label);
            } else if(args[0].equalsIgnoreCase("addfriend") || args[0].equalsIgnoreCase("friend")) {

                //landlord addfriend
                return landlord_addfriend(sender, args);
            } else if(args[0].equalsIgnoreCase("friendall")) {

                //landlord addfriend
                return landlord_friendall(sender, args);
            } else if(args[0].equalsIgnoreCase("unfriendall")) {

                //landlord addfriend
                return landlord_unfriendall(sender, args);
            } else if(args[0].equalsIgnoreCase("remfriend") || args[0].equalsIgnoreCase("unfriend")) {

                return landlord_remfriend(sender, args);
            } else if(args[0].equalsIgnoreCase("map")) {

                return landlord_map(sender, args);
                //sender.sendMessage(ChatColor.RED+"Land map is temporarily disabled!");
                //return true;
            } else if(args[0].equalsIgnoreCase("manage")) {
                return landlord_manage(sender, args);

            } else if(args[0].equalsIgnoreCase("list")) {
                return landlord_list(sender, args, label);

            } else if(args[0].equalsIgnoreCase("listplayer")) {
                return landlord_listplayer(sender, args, label);

            } else if(args[0].equalsIgnoreCase("clearworld")) {
                return landlord_clearWorld(sender, args, label);

            } else if(args[0].equalsIgnoreCase("reload")) {

                return landlord_reload(sender, args, label);
            } else if(args[0].equalsIgnoreCase("info")) {

                return landlord_info(sender, args, label);
            } else if(args[0].equalsIgnoreCase("friends")) {

                return landlord_friends(sender,args,label);
            } else {
                return landlord_help(sender, args, label);
            }*/

        } //If this has happened the function will return true.
        // If this hasn't happened the value of false will be returned.
        return false;
    }


    /**
     * Register a new command
     * @param cmd LandlordCommand to register
     * @return boolean of success - should fail if a requested label (name, alias) is not available
     */
    public boolean register(LandlordCommand cmd) {
        String[] commandTriggers = cmd.getTriggers();

        // if there are no aliases then fail, command would be impossible to trigger.
        if (commandTriggers.length <= 0) {

            return false;
        }

        // iterate the command aliases
        for (String trigger : commandTriggers) {

            // Check if the command is taken
            if(registeredCommands.containsKey(trigger.toLowerCase())) {
                System.out.println("Failed to register command with alias '" + trigger + "', already taken!");
                continue;   // Command name is taken already
            }

            // register an entry for this command trigger
            registeredCommands.put(trigger.toLowerCase(), cmd);
        }

        // add the commands help text to the command help list
        //commandHelp.add(cmd.getHelpText());

        return true;
    }






    /*
     * **********************************************************
     * private methods for handling each command functionality todo move into separate files
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

        //check if page number is valid
        int pageNumber = 1;
        if (args.length > 1 && args[0].equals("help")) {
            try{
                pageNumber = Integer.parseInt(args[1]);}
            catch (NumberFormatException e){
                // Is not a number!
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
        if (plugin.hasVault() && plugin.getvHandler().hasEconomy() && plugin.getConfig().getDouble("economy.sellPrice", 50.0) > 0) {
            if(plugin.getConfig().getBoolean("options.regenOnUnclaim",false)) {
                unclaim+=ChatColor.RED+""+ChatColor.ITALIC +" Regenerates Chunk!";
            }
            unclaim += ChatColor.YELLOW + "" + ChatColor.ITALIC + " Get " + plugin.getvHandler().formatCash(plugin.getConfig().getDouble("economy.sellPrice", 50.0)) + " per unclaim.\n";
        } else if(plugin.getConfig().getBoolean("options.regenOnUnclaim",false)) {
            unclaim+=ChatColor.RED+""+ChatColor.ITALIC +" Regenerates Chunk!\n";
        }
        helpList.add(unclaim);

        helpList.add(ChatColor.DARK_AQUA+"/"+label + " addfriend <player>" + ChatColor.RESET + " - Add friend to this land.\n");
        helpList.add(ChatColor.DARK_AQUA+"/"+label + " unfriend <player>" + ChatColor.RESET + " - Remove friend from this land.\n");
        helpList.add(ChatColor.DARK_AQUA+"/"+label + " friendall <player>" + ChatColor.RESET + " - Add friend to all your land.\n");
        helpList.add(ChatColor.DARK_AQUA+"/"+label + " unfriendall <player>" + ChatColor.RESET + " - Remove friend from all your land.\n");
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
            String clearHelp = ChatColor.DARK_AQUA+"/"+label + " clearworld <world> [player]" + ChatColor.RESET + " - Delete all land owned by a player in a world." +
                    " Delete all land of a world from console.\n";
            if(plugin.getConfig().getBoolean("options.regenOnUnclaim",false)){
                clearHelp += ChatColor.YELLOW+""+ChatColor.ITALIC+" Does not regenerate chunks.\n";
            }
            helpList.add(clearHelp);

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






}
