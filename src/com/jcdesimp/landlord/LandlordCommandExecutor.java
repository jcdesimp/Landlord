package com.jcdesimp.landlord;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                return landlord(sender, args);

            } else if(args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("buy")) {

                //landlord claim
                return landlord_claim(sender, args);

            } else if(args[0].equalsIgnoreCase("unclaim") || args[0].equalsIgnoreCase("sell")) {

                //landlord unclaim
                return landlord_unclaim(sender, args);
            }

        } //If this has happened the function will return true.
        // If this hasn't happened the value of false will be returned.
        return false;
    }





    /*
     * ****************************************************
     * private methods for handling each command's functionality
     * ****************************************************
     */


    /**
     * Called when base command /landlord or aliases (/ll /land)
     * are executed with no parameters
     * @param sender who executed the command
     * @param args given with the command
     * @return Boolean
     */
    private Boolean landlord(CommandSender sender, String[] args) {

        sender.sendMessage(ChatColor.DARK_GREEN + "Base Landlord command executed!");
        return true;
    }

    /**
     * Called when landlord claim command is executed
     * This command must be run by a player
     * @param sender who executed the command
     * @param args given with command
     * @return Boolean
     */
    private Boolean landlord_claim(CommandSender sender, String[] args) {


        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();
            sender.sendMessage(
                ChatColor.GREEN + "Successfully claimed chunk " + currChunk.getX() + ", " +
                currChunk.getZ() + " in world " + currChunk.getWorld().getName() + "."
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
     * @return Boolean
     */
    private Boolean landlord_unclaim(CommandSender sender, String[] args) {

        //is sender a plater
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();
            sender.sendMessage(
                    ChatColor.YELLOW + "Successfully unclaimed chunk " + currChunk.getX() + ", " +
                    currChunk.getZ() + " in world " + currChunk.getWorld().getName() + "."
            );
            //sender.sendMessage(ChatColor.DARK_GREEN + "Land claim command executed!");
        }
        return true;
    }

}
