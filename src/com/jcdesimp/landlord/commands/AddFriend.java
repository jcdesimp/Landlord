package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.Friend;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getOfflinePlayer;

/**
 * Created by jcdesimp on 2/18/15.
 * LandlordCommand to add a friend to a plot of land
 */
public class AddFriend implements LandlordCommand {

    private Landlord plugin;

    public AddFriend(Landlord plugin) {
        this.plugin = plugin;
    }


    /**
     * Adds a friend to an owned chunk
     * Called when landlord addfriend command is executed
     * This command must be run by a player
     * @param sender who executed the command
     * @param args given with command
     * @param label base command executed
     * @return boolean
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");       //mess
        } else {
            if (args.length < 2){
                sender.sendMessage(ChatColor.RED + "usage: /land addfriend <player>");      //mess
                return true;
            }
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");        //mess
                return true;
            }

            Chunk currChunk = player.getLocation().getChunk();

            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());

            //Does land exist, and if so does player own it
            if( land == null || (!land.ownerUUID().equals(player.getUniqueId()) && !player.hasPermission("landlord.admin.modifyfriends")) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");    //mess
                return true;
            }
            //
            OfflinePlayer possible = getOfflinePlayer(args[1]);
            if (!possible.hasPlayedBefore() && !possible.isOnline()) {
                player.sendMessage(ChatColor.RED+"That player is not recognized."); //mess
                return true;
            }
            Friend friend = Friend.friendFromOfflinePlayer(getOfflinePlayer(args[1]));
            /*
             * *************************************
             * mark for possible change    !!!!!!!!!
             * *************************************
             */

            if (! land.addFriend(friend)) {
                player.sendMessage(ChatColor.YELLOW + "Player " + args[1] + " is already a friend of this land.");  //mess
                return true;
            }
            if(plugin.getConfig().getBoolean("options.particleEffects",true)){      //conf
                land.highlightLand(player, Effect.HEART, 2);
            }

            plugin.getDatabase().save(land);
            if(plugin.getConfig().getBoolean("options.soundEffects",true)){     //conf
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,10,.2f);
            }
            sender.sendMessage(ChatColor.GREEN + "Player " + args[1] +" is now a friend of this land.");    //mess
            plugin.getMapManager().updateAll();

        }
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {
        //mess
        String usage = "/#{label} #{cmd} <player>"; // get the base usage string
        String desc = "Add friend to this land.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());
    }

    @Override
    public String[] getTriggers() {
        return new String[]{"friend", "addfriend"};
    }
}
