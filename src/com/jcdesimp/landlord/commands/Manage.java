package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by jcdesimp on 2/18/15.
 * Command to bring up the land management interface.
 */
public class Manage implements LandlordCommand {

    private Landlord plugin;

    public Manage(Landlord plugin) {
        this.plugin = plugin;
    }

    /**
     * Command for managing player land perms
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");   //mess
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");                    //mess
                return true;
            }
            if(plugin.getFlagManager().getRegisteredFlags().size() <= 0){
                player.sendMessage(ChatColor.RED+"There is nothing to manage!");                    //mess
                return true;
            }
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if( land == null || ( !land.ownerUUID().equals(player.getUniqueId()) && !player.hasPermission("landlord.admin.manage") ) ){
                player.sendMessage(ChatColor.RED + "You do not own this land.");        //mess
                return true;
            }
            if(!land.ownerUUID().equals(player.getUniqueId())){
                player.sendMessage(ChatColor.YELLOW+"Managing someone else's land.");      //mess
            }
            plugin.getManageViewManager().activateView(player, land);


        }
        return true;
    }

    @Override
    public String getHelpText() {
        //mess
        String usage = "/#{label} #{cmd}"; // get the base usage string
        String desc = "Manage permissions for this land.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

    }

    @Override
    public String[] getTriggers() {
        return new String[]{"manage"};
    }
}
