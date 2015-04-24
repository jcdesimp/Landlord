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

        //mess ready
        String notPlayer = "This command can only be run by a player.";
        String usageString = "usage: /#{label} #{command} <player>";
        String noPerms = "You do not have permission.";
        String unknownPlayer = "That player is not recognized.";
        String playerRemoved = "#{playername} has been removed as a friend from all of your land.";
        String noLand = "You do not own any land!";

        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + notPlayer);
        } else {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + usageString.replace("#label}", label).replace("#{command}", args[0]));
            }
            Player player = (Player) sender;
            if (!player.hasPermission("landlord.player.own")) {
                player.sendMessage(ChatColor.RED + noPerms);
                return true;
            }

            List<OwnedLand> pLand = plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getUniqueId()).findList();

            OfflinePlayer possible = getOfflinePlayer(args[1]);
            if (!possible.hasPlayedBefore() && !possible.isOnline()) {
                player.sendMessage(ChatColor.RED + unknownPlayer);
                return true;
            }

            if (pLand.size() > 0){
                for(OwnedLand l : pLand){
                    l.removeFriend(Friend.friendFromOfflinePlayer(getOfflinePlayer(args[1])));
                }
                
                plugin.getDatabase().save(pLand);

                player.sendMessage(ChatColor.GREEN+playerRemoved.replace("#{playername}", args[1]));
                return true;
            } else {
                player.sendMessage(ChatColor.YELLOW + noLand);
            }

        }
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {

        //mess ready
        String usage = "/#{label} #{cmd} <player>"; // get the base usage string
        String desc = "Remove friend from all your land.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

    }

    @Override
    public String[] getTriggers() {
        return new String[]{"unfriendall","remfriendall"};      //mess triggers
    }
}
