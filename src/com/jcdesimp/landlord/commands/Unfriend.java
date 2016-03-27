package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.Friend;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getOfflinePlayer;

/**
 * Created by jcdesimp on 2/18/15.
 * Removed a friend form an owned chunk
 */
public class Unfriend implements LandlordCommand {

    private Landlord plugin;

    public Unfriend(Landlord plugin) {
        this.plugin = plugin;
    }


    /**
     * Removes a friend from an owned chunk
     * Called when landlord remfriend is executed
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");   //mess
        } else {
            if (args.length < 2){
                sender.sendMessage(ChatColor.RED + "usage: /land unfriend <player>");   //mess
                return true;
            }
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");    //mess
                return true;
            }

            Chunk currChunk = player.getLocation().getChunk();
            /*
             * *************************************
             * mark for possible change    !!!!!!!!!
             * *************************************
             */
            Friend frd = Friend.friendFromOfflinePlayer(getOfflinePlayer(args[1]));
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if( land == null || (!land.ownerUUID().equals(player.getUniqueId()) && !player.hasPermission("landlord.admin.modifyfriends")) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");    //mess
                return true;
            }
            if(!land.removeFriend(frd)){
                player.sendMessage(ChatColor.YELLOW + "Player " + args[1] + " is not a friend of this land.");  //mess
                return true;
            }
            if(plugin.getConfig().getBoolean("options.particleEffects",true)) { //conf
                land.highlightLand(player, Effect.VILLAGER_THUNDERCLOUD, 2);
            }
            plugin.getDatabase().save(land);
            if(plugin.getConfig().getBoolean("options.soundEffects",true)){ //conf
                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT,10,.5f);
            }
            player.sendMessage(ChatColor.GREEN + "Player " + args[1] + " is no longer a friend of this land."); //mess

        }
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {
        //mess
        String usage = "/#{label} #{cmd} <player>"; // get the base usage string
        String desc = "Remove friend from this land.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());
    }

    @Override
    public String[] getTriggers() {
        return new String[]{"unfriend","remfriend"};
    }
}
