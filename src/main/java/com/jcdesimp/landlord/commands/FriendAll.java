package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.Friend;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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

    public boolean execute(CommandSender sender, String[] args, String label) {
        FileConfiguration messages = plugin.getMessageConfig();
        final String notPlayerString = messages.getString("info.warnings.playerCommand");
        final String usageString = messages.getString("commands.friendAll.usage");
        final String noPermsString = messages.getString("info.warnings.noPerms");
        final String unknownPlayer = messages.getString("info.warnings.unknownPlayer");
        final String friendAddedString = messages.getString("commands.friendAll.alerts.success");
        final String noLandString = messages.getString("commands.friendAll.alerts.noLand");


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

            List<OwnedLand> pLand = plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName", player.getUniqueId()).findList();
            OfflinePlayer possible = getOfflinePlayer(args[1]);
            if (!possible.hasPlayedBefore() && !possible.isOnline()) {
                player.sendMessage(ChatColor.RED + unknownPlayer);
                return true;
            }

            if (pLand.size() > 0) {
				Friend f = Friend.friendFromOfflinePlayer(possible);
                for (OwnedLand l : pLand) {
                    l.addFriend(f);
                }

                plugin.getDatabase().save(pLand);


                player.sendMessage(ChatColor.GREEN + friendAddedString.replace("#{player}", args[1]));
                return true;
            } else {
                player.sendMessage(ChatColor.YELLOW + noLandString);
            }

        }
        return true;
    }

    public String getHelpText(CommandSender sender) {
        FileConfiguration messages = plugin.getMessageConfig();

        final String usage = messages.getString("commands.friendAll.usage"); // get the base usage string
        final String desc = messages.getString("commands.friendAll.description");   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

    }

    public String[] getTriggers() {
        final List<String> triggers = plugin.getMessageConfig().getStringList("commands.friendAll.triggers");
        return triggers.toArray(new String[triggers.size()]);
    }
}
