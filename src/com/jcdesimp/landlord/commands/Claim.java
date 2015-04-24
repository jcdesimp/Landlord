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

        // Message data mess ready
        String notPlayer = "This command can only be run by a player.";                             // When run by non-player
        String noPerms = "You do not have permission.";                                             // No permissions

        String cannotClaim = "You cannot claim in this world.";                                     // Claiming disabled in this world
        String alreadyOwn = "You already own this land!";                                           // When you already own this land
        String otherOwn = "Someone else owns this land.";                                           // Someone else owns this land
        String noClaimZone = "You cannot claim here.";                                              // You can't claim here! (Worldguard)
        String ownLimit = "You can only own #{limit} chunks of land.";                              // Chunk limit hit
        String claimPrice = "It costs #{cost} to purchase land.";                                   // Not enough funds
        String charged = "You have been charged #{cost} to purchase land.";                         // Charged for claim
        String success = "Successfully claimed chunk #{chunkCoords} in world \'#{worldName}\'.";    // Chunk claim successful

        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + notPlayer);
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own")){
                player.sendMessage(ChatColor.RED+ noPerms);
                return true;
            }



            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();

            List<String> disabledWorlds = plugin.getConfig().getStringList("disabled-worlds");
            for (String s : disabledWorlds) {
                if (s.equalsIgnoreCase(currChunk.getWorld().getName())) {
                    player.sendMessage(ChatColor.RED+ cannotClaim);
                    return true;
                }
            }

            // Check if worldguard is installed
            if(plugin.hasWorldGuard()){
                // if it is make sure that the attempted land claim isn't with a protected worldguard region.
                if(!plugin.getWgHandler().canClaim(player,currChunk)){
                    player.sendMessage(ChatColor.RED + noClaimZone);
                    return true;
                }
            }



            OwnedLand land = OwnedLand.landFromProperties(player, currChunk);
            OwnedLand dbLand = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());


            if(dbLand != null){
                //Check if they already own this land
                if (dbLand.ownerUUID().equals(player.getUniqueId())){
                    player.sendMessage(ChatColor.YELLOW + alreadyOwn);
                    return true;
                }
                player.sendMessage(ChatColor.YELLOW + otherOwn);
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
                    player.sendMessage(ChatColor.RED+ownLimit.replace("#{limit}", ""+limit));
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
                            player.sendMessage(ChatColor.RED+ claimPrice.replace("#{cost}", plugin.getvHandler().formatCash(amt)));
                            return true;
                        } else {
                            player.sendMessage(ChatColor.YELLOW+ charged.replace("#{cost}", plugin.getvHandler().formatCash(amt)));
                        }
                    }

                }
            }
            Landlord.getInstance().getDatabase().save(land);
            land.highlightLand(player, Effect.HAPPY_VILLAGER);
            sender.sendMessage(
                    ChatColor.GREEN + success
                            .replace("#{chunkCoords}", "(" + currChunk.getX() + ", " + currChunk.getZ() + ")")
                            .replace("#{worldName}", currChunk.getWorld().getName()));

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

        //Message data mess ready
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
        return new String[]{"claim", "buy"};    //mess triggers
    }
}
