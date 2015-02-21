package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by jcdesimp on 2/17/15.
 * LandlordCommand object that lets a user Claim land
 */
public class Claim implements LandlordCommand {


    private Landlord plugin;


    /**
     * Constructor for Claim command
     * @param plugin the main Landlord plugin
     */
    public Claim(Landlord plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when landlord claim command is executed
     * This command must be run by a player
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");   //mess
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");    //mess
                return true;
            }



            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();

            List<String> disabledWorlds = plugin.getConfig().getStringList("disabled-worlds");
            for (String s : disabledWorlds) {
                if (s.equalsIgnoreCase(currChunk.getWorld().getName())) {
                    player.sendMessage(ChatColor.RED+"You cannot claim in this world."); //mess
                    return true;
                }
            }

            // Check if worldguard is installed
            if(plugin.hasWorldGuard()){
                // if it is make sure that the attempted land claim isn't with a protected worldguard region.
                if(!plugin.getWgHandler().canClaim(player,currChunk)){
                    player.sendMessage(ChatColor.RED+"You cannot claim here."); //mess
                    return true;
                }
            }



            OwnedLand land = OwnedLand.landFromProperties(player, currChunk);
            OwnedLand dbLand = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());


            if(dbLand != null){
                //Check if they already own this land
                if (dbLand.ownerUUID().equals(player.getUniqueId())){
                    player.sendMessage(ChatColor.YELLOW + "You already own this land!");    //mess
                    return true;
                }
                player.sendMessage(ChatColor.YELLOW + "Someone else owns this land.");  //mess
                return true;

            }
            int orLimit = plugin.getConfig().getInt("limits.landLimit",10);
            int limit = plugin.getConfig().getInt("limits.landLimit",10);

            if(player.hasPermission("landlord.limit.extra5")){
                limit=orLimit+plugin.getConfig().getInt("limits.extra5",0);
            } else if(player.hasPermission("landlord.limit.extra4")){
                limit=orLimit+plugin.getConfig().getInt("limits.extra4",0);
            } else if(player.hasPermission("landlord.limit.extra3")){
                limit=orLimit+plugin.getConfig().getInt("limits.extra3",0);
            } else if(player.hasPermission("landlord.limit.extra2")){
                limit=orLimit+plugin.getConfig().getInt("limits.extra2",0);
            } else if(player.hasPermission("landlord.limit.extra")){
                limit=orLimit+plugin.getConfig().getInt("limits.extra",0);
            }

            if(limit >= 0 && !player.hasPermission("landlord.limit.override")){
                if(plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getUniqueId().toString()).findRowCount() >= limit){
                    player.sendMessage(ChatColor.RED+"You can only own " + limit + " chunks of land."); //mess
                    return true;
                }
            }

            //Money Handling
            if(plugin.hasVault()){
                if(plugin.getvHandler().hasEconomy()){
                    Double amt = plugin.getConfig().getDouble("economy.buyPrice", 100.0);
                    if(amt > 0){
                        int numFree = plugin.getConfig().getInt("economy.freeLand", 0);
                        if (numFree > 0 && plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getUniqueId().toString()).findRowCount() < numFree) {
                            //player.sendMessage(ChatColor.YELLOW+"You have been charged " + plugin.getvHandler().formatCash(amt) + " to purchase land.");
                        } else if(!plugin.getvHandler().chargeCash(player, amt)){
                            player.sendMessage(ChatColor.RED+"It costs " + plugin.getvHandler().formatCash(amt) + " to purchase land.");    //mess
                            return true;
                        } else {
                            player.sendMessage(ChatColor.YELLOW+"You have been charged " + plugin.getvHandler().formatCash(amt) + " to purchase land.");    //mess
                        }
                    }

                }
            }
            Landlord.getInstance().getDatabase().save(land);
            land.highlightLand(player, Effect.HAPPY_VILLAGER);
            sender.sendMessage(
                    ChatColor.GREEN + "Successfully claimed chunk (" + currChunk.getX() + ", " +
                            currChunk.getZ() + ") in world \'" + currChunk.getWorld().getName() + "\'." );  //mess

            if(plugin.getConfig().getBoolean("options.soundEffects",true)){
                player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE2,10,10);
            }


            plugin.getMapManager().updateAll();
            //sender.sendMessage(ChatColor.DARK_GREEN + "Land claim command executed!");
        }
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {

        //mess ready
        String usage = "/#{label} #{cmd}";                      // get the base usage string
        String desc = "Claim this chunk.";                      // get the description
        String priceWarning = "Costs #{pricetag} to claim.";    // get the price warning message


        String helpString = ""; // start building the help string

        helpString += Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

        if(plugin.hasVault()){
            if(plugin.getvHandler().hasEconomy() && plugin.getConfig().getDouble("economy.buyPrice", 100.0)>0){     //conf
                helpString += ChatColor.YELLOW+" "+ChatColor.ITALIC+ priceWarning
                        .replace(
                            "#{pricetag}",                  // insert the formatted price string
                            plugin.getvHandler().formatCash(plugin.getConfig().getDouble("economy.buyPrice", 100.0))        //conf
                        );
            }
        }


        // return the constructed and colorized help string
        return helpString;

    }

    @Override
    public String[] getTriggers() {
        return new String[]{"claim", "buy"};
    }
}
