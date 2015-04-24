package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.Friend;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static org.bukkit.util.NumberConversions.ceil;

/**
 * Created by jcdesimp on 2/18/15.
 * List the friends of the current land
 */
public class Friends implements LandlordCommand {

    private Landlord plugin;

    public Friends(Landlord plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {

        //mess ready
        String notPlayer = "This command can only be run by a player.";
        String noPerms = "You do not have permission.";
        String notOwner = "You do not own this land.";
        String badPageNum = "That is not a valid page number.";
        String friendListHeader = "Friends of this Land";
        String noFriends = "This land has no friends.";
        String onlineString = "Online";
        String offlineString = "Offline";
        String invalidPage = "That is not a valid page number.";
        String nextPageString = "do #{label} #{cmd} #{pageNumber} for next page";


        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + notPlayer);
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+ noPerms);
                return true;
            }
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            if( land == null || ( !land.ownerUUID().equals(player.getUniqueId()) && !player.hasPermission("landlord.admin.friends") ) ){
                player.sendMessage(ChatColor.RED + notOwner);
                return true;
            }
//            if(!land.getOwnerName().equals(player.getUniqueId())){
//                //player.sendMessage(ChatColor.YELLOW+"Viewing friends of someone else's land.");
//            }
            if(plugin.getConfig().getBoolean("options.particleEffects",true)) {     //conf
                land.highlightLand(player, Effect.HEART, 3);
            }
            //check if page number is valid
            int pageNumber = 1;
            if (args.length > 1 && args[0].equals("friends")){
                try {
                    pageNumber = Integer.parseInt(args[1]);
                } catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+ badPageNum);
                    return true;
                }
            }

            //List<OwnedLand> myLand = plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getName()).findList();

            String header = ChatColor.DARK_GREEN + "----- " + friendListHeader + " -----\n";
            ArrayList<String> friendList = new ArrayList<String>();
            if(land.getFriends().isEmpty()){
                player.sendMessage(ChatColor.YELLOW+noFriends);
                return true;
            }
            for(Friend f: land.getFriends()){
                String fr = ChatColor.DARK_GREEN+" - "+ChatColor.GOLD+f.getName()+ChatColor.DARK_GREEN+" - ";
                /*
                 * *************************************
                 * mark for possible change    !!!!!!!!!
                 * *************************************
                 */
                if(Bukkit.getOfflinePlayer(f.getUUID()).isOnline()){
                    fr+= ChatColor.GREEN+""+ChatColor.ITALIC+ onlineString;
                } else {
                    fr+= ChatColor.RED+""+ChatColor.ITALIC+ offlineString;
                }

                fr+="\n";
                friendList.add(fr);
            }

            //Amount to be displayed per page
            final int numPerPage = 8;

            int numPages = ceil((double)friendList.size()/(double)numPerPage);
            if(pageNumber > numPages){
                sender.sendMessage(ChatColor.RED+invalidPage);
                return true;
            }
            String pMsg = header;
            if (pageNumber == numPages){
                for(int i = (numPerPage*pageNumber-numPerPage); i<friendList.size(); i++){
                    pMsg+=friendList.get(i);
                }
                pMsg+=ChatColor.DARK_GREEN+"------------------------------";
            } else {
                for(int i = (numPerPage*pageNumber-numPerPage); i<(numPerPage*pageNumber); i++){
                    pMsg+=friendList.get(i);
                }

                pMsg += "\n" + ChatColor.DARK_GREEN + "--- " + nextPageString
                        .replace("#{label}",ChatColor.YELLOW + "/" + label)
                        .replace("#{cmd}", args[0])
                        .replace("#{pageNumber}", "" + (pageNumber + 1) + ChatColor.DARK_GREEN)
                        + " ---";
            }
            player.sendMessage(pMsg);


        }
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {

        //mess ready
        String usage = "/#{label} #{cmd}"; // get the base usage string
        String desc = "List friends of this land.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

    }

    @Override
    public String[] getTriggers() {
        return new String[]{"friends"};     //mess triggers
    }
}
