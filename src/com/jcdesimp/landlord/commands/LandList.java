package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.util.NumberConversions.ceil;

/**
 * Created by jcdesimp on 2/18/15.
 * Command to get a list of the players land
 */
public class LandList implements LandlordCommand {

    private Landlord plugin;


    public LandList(Landlord plugin) {
        this.plugin = plugin;
    }

    /**
     * Display a list of all owned land to a player
     *
     * @param sender who executed the command
     * @param args   given with command
     * @return boolean
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {

        //mess ready
        String notPlayer = "This command can only be run by a player.";
        String noPerms = "You do not have permission.";
        String badPageNum = "That is not a valid page number.";
        String noLand = "You do not own any land!";

        String outputHeader = "Coords - Chunk Coords - World Name";
        String ownedLandString =  "Your Owned Land";
        String pageNum = "Page #{pageNum}";
        String nextPageString = "do #{label} #{cmd} #{pageNumber} for next page";


        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + notPlayer);
        } else {
            Player player = (Player) sender;
            if (!player.hasPermission("landlord.player.own")) {
                player.sendMessage(ChatColor.RED + noPerms);
                return true;
            }

            //check if page number is valid
            int pageNumber = 1;
            if (args.length > 1) {
                try {
                    pageNumber = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + badPageNum);
                    return true;
                }
            }

            List<OwnedLand> myLand = plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName", player.getUniqueId().toString()).findList();
            if (myLand.size() == 0) {
                player.sendMessage(ChatColor.YELLOW + noLand);
            } else {
                String header = ChatColor.DARK_GREEN + " | " + outputHeader + " |\n";
                ArrayList<String> landList = new ArrayList<String>();
                //OwnedLand curr = myLand.get(0);
                for (OwnedLand aMyLand : myLand) {
                    landList.add((ChatColor.GOLD + " (" + aMyLand.getXBlock() + ", " + aMyLand.getZBlock() + ") - (" + aMyLand.getX() + ", " + aMyLand.getZ() + ") - "
                            + aMyLand.getWorldName()) + "\n")
                    ;
                }
                //Amount to be displayed per page
                final int numPerPage = 7;

                int numPages = ceil((double) landList.size() / (double) numPerPage);
                if (pageNumber > numPages) {
                    player.sendMessage(ChatColor.RED + badPageNum);
                    return true;
                }

                //String pMsg = ChatColor.DARK_GREEN + "--- " + ChatColor.YELLOW + ownedLandString + ChatColor.DARK_GREEN + " ---" + ChatColor.YELLOW + " Page " + pageNumber + ChatColor.DARK_GREEN + " ---\n" + header;
                String pMsg = (ChatColor.DARK_GREEN + "--- " + ChatColor.YELLOW + ownedLandString + ChatColor.DARK_GREEN + " ---" + ChatColor.YELLOW + " ")
                        .replace("#{pageNum}", pageNum)
                        + ChatColor.DARK_GREEN + " ---\n" + header;


                if (pageNumber == numPages) {
                    for (int i = (numPerPage * pageNumber - numPerPage); i < landList.size(); i++) {
                        pMsg += landList.get(i);
                    }
                    pMsg += ChatColor.DARK_GREEN + "------------------------------";
                } else {
                    for (int i = (numPerPage * pageNumber - numPerPage); i < (numPerPage * pageNumber); i++) {
                        pMsg += landList.get(i);
                    }
                    //pMsg += ChatColor.DARK_GREEN + "--- do" + ChatColor.YELLOW + " /" + label + " list " + (pageNumber + 1) + ChatColor.DARK_GREEN + " for next page ---";

                    pMsg += ChatColor.DARK_GREEN + "--- " + ChatColor.YELLOW + nextPageString
                            .replace("#{label}", "/"+label)
                            .replace("#{cmd}", args[0])
                            .replace("#{pageNumber}", "" + pageNumber + 1)
                            + " ---";
                }

                player.sendMessage(pMsg);
            }

        }
        return true;
    }


    @Override
    public String getHelpText(CommandSender sender) {
        //mess ready
        String usage = "/#{label} #{cmd}"; // get the base usage string
        String desc = "List all your own land.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

    }

    @Override
    public String[] getTriggers() {
        return new String[]{"list"};    //mess triggers
    }
}
