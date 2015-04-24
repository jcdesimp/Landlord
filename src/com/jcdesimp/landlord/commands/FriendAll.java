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
 * Command for a player toi add a friend to all of their land at once.
 */
public class FriendAll implements LandlordCommand {

    private Landlord plugin;

    public FriendAll(Landlord plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        // Message Data mess ready
        String notPlayerString = "This command can only be run by a player.";
        String usageString = "usage: /#{label} friendall <player>";
        String noPermsString = "You do not have permission.";
        String unknownPlayer = "That player is not recognized.";
        String friendAddedString = "#{player} has been added as a friend to all of your land.";
        String noLandString = "You do not own any land!";


        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + notPlayerString);
        } else {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + usageString.replace("#{label}", label));
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("landlord.player.own")) {
                player.sendMessage(ChatColor.RED + noPermsString);
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
                    l.addFriend(Friend.friendFromOfflinePlayer(getOfflinePlayer(args[1])));
                }

                plugin.getDatabase().save(pLand);


                player.sendMessage(ChatColor.GREEN + friendAddedString.replace("#{player}", args[1]));
                return true;
            } else {
                player.sendMessage(ChatColor.YELLOW+noLandString);        //mess
            }

        }
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {

        //mess
        String usage = "/#{label} #{cmd} <player>"; // get the base usage string
        String desc = "Add friend to all your land.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

    }

    @Override
    public String[] getTriggers() {
        return new String[]{"friendall","addfriendall"};        //mess triggers
    }
}
