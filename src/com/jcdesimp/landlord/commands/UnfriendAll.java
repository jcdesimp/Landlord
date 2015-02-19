package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.Friend;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static org.bukkit.Bukkit.getOfflinePlayer;

/**
 * Created by jcdesimp on 2/18/15.
 * Command for a user to remove a friend from all land at once.
 */
public class UnfriendAll implements LandlordCommand {

    private Landlord plugin;

    public UnfriendAll(Landlord plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        } else {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "usage: /land unfriendall <player>");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("landlord.player.own")) {
                player.sendMessage(ChatColor.RED + "You do not have permission.");
                return true;
            }

            List<OwnedLand> pLand = plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getUniqueId()).findList();

            OfflinePlayer possible = getOfflinePlayer(args[1]);
            if (!possible.hasPlayedBefore() && !possible.isOnline()) {
                player.sendMessage(ChatColor.RED + "That player is not recognized.");
                return true;
            }

            if (pLand.size() > 0){
                for(OwnedLand l : pLand){
                    l.removeFriend(Friend.friendFromOfflinePlayer(getOfflinePlayer(args[1])));
                }

                plugin.getDatabase().save(pLand);


                player.sendMessage(ChatColor.GREEN+args[1]+" has been removed as a friend from all of your land.");
                return true;
            } else {
                player.sendMessage(ChatColor.YELLOW+"You do not own any land!");
            }

        }
        return true;
    }

    @Override
    public String getHelpText() {
        //mess
        String usage = "/#{label} #{cmd} unfriend <player>"; // get the base usage string
        String desc = "Remove friend from all your land.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

    }

    @Override
    public String[] getTriggers() {
        return new String[]{"unfriendall","remfriendall"};
    }
}
