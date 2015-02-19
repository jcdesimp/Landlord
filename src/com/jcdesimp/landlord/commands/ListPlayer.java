package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.util.NumberConversions.ceil;

/**
 * Created by jcdesimp on 2/18/15.
 * List the land of a specified player
 */
public class ListPlayer implements LandlordCommand {

    private Landlord plugin;

    public ListPlayer(Landlord plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender who sent the command
     * @param args  array of arguments given with the command
     * @param label the actual command/alias that was entered.
     * @return Boolean of success
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        String owner;

        //sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");
        if(args.length>1){
            owner = args[1];
        } else {
            sender.sendMessage(ChatColor.RED+"usage: /" + label + " listplayer <player> [page#]");  //mess
            return true;
        }

        //Player player = (Player) sender;
        if(!sender.hasPermission("landlord.admin.list")){
            sender.sendMessage(ChatColor.RED+"You do not have permission.");        //mess
            return true;
        }

        //check if page number is valid
        int pageNumber = 1;
        if (args.length > 2){
            try{
                pageNumber = Integer.parseInt(args[2]);}
            catch (NumberFormatException e){
                sender.sendMessage(ChatColor.RED+"That is not a valid page number.");       //mess
                return true;
            }
        }
        /*
         * *************************************
         * mark for possible change    !!!!!!!!!
         * *************************************
         */
        OfflinePlayer possible = getOfflinePlayer(args[1]);
        if (!possible.hasPlayedBefore() && !possible.isOnline()) {
            sender.sendMessage(ChatColor.YELLOW+ owner +" does not own any land!");     //mess
            return true;
        }
        List<OwnedLand> myLand = plugin.getDatabase().find(OwnedLand.class).where().ieq("ownerName", getOfflinePlayer(owner).getUniqueId().toString()).findList();
        if(myLand.size()==0){
            sender.sendMessage(ChatColor.YELLOW+ owner +" does not own any land!");
        } else {
            String header = ChatColor.DARK_GREEN+" | Coords - Chunk Coords - World Name |     \n";  //mess
            ArrayList<String> landList = new ArrayList<String>();
            //OwnedLand curr = myLand.get(0);
            for (OwnedLand aMyLand : myLand) {
                landList.add((ChatColor.GOLD + " ("+ aMyLand.getXBlock() +", "+ aMyLand.getZBlock() +") - (" + aMyLand.getX() + ", " + aMyLand.getZ() + ") - "
                        + aMyLand.getWorldName()) + "\n")       //mess
                ;
            }
            //Amount to be displayed per page
            final int numPerPage = 7;

            int numPages = ceil((double)landList.size()/(double)numPerPage);
            if(pageNumber > numPages){
                sender.sendMessage(ChatColor.RED+"That is not a valid page number.");   //mess
                return true;
            }
            String pMsg = ChatColor.DARK_GREEN+"--- " +ChatColor.YELLOW+ owner +"'s Owned Land"+ChatColor.DARK_GREEN+" ---"+ChatColor.YELLOW+" Page "+pageNumber+ChatColor.DARK_GREEN+" ---\n"+header;
            if (pageNumber == numPages){
                for(int i = (numPerPage*pageNumber-numPerPage); i<landList.size(); i++){        //mess
                    pMsg+=landList.get(i);
                }
                pMsg+=ChatColor.DARK_GREEN+"------------------------------";
            } else {
                for(int i = (numPerPage*pageNumber-numPerPage); i<(numPerPage*pageNumber); i++){            //mess
                    pMsg+=landList.get(i);
                }
                pMsg+=ChatColor.DARK_GREEN+"--- do"+ChatColor.YELLOW+" /"+label+" listplayer "+(pageNumber+1)+ChatColor.DARK_GREEN+" for next page ---";
            }

            sender.sendMessage(pMsg);
        }


        return  true;
    }

    @Override
    public String getHelpText() {
        //mess
        String usage = "/#{label} #{cmd} <player>"; // get the base usage string
        String desc = "List land owned by another player.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

    }

    @Override
    public String[] getTriggers() {
        return new String[]{"listplayer"};
    }
}
