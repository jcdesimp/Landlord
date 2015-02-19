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

import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getWorld;

/**
 * Created by jcdesimp on 2/17/15.
 * LandlordCommand object for players to unclaim land
 */
public class Unclaim implements LandlordCommand {

    private Landlord plugin;

    public Unclaim(Landlord plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when landlord unclaim command is executed
     * This command must be run by a player
     * @param sender who executed the command
     * @param args given with costp[mmand
     * @return boolean
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        //is sender a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");   //mess
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.own") && !player.hasPermission("landlord.admin.unclaim")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");                    //mess
                return true;
            }
            //sender.sendMessage(ChatColor.GOLD + "Current Location: " + player.getLocation().toString());
            Chunk currChunk = player.getLocation().getChunk();

            int x = currChunk.getX();
            int z = currChunk.getZ();
            String worldname = currChunk.getWorld().getName();

            List<String> disabledWorlds = plugin.getConfig().getStringList("disabled-worlds");  //conf
            for (String s : disabledWorlds) {
                if (s.equalsIgnoreCase(currChunk.getWorld().getName())) {
                    player.sendMessage(ChatColor.RED+"You cannot claim in this world.");            //mess
                    return true;
                }
            }


            if(args.length>1){
                try{
                    String[] coords = args[1].split(",");
                    //System.out.println("COORDS: "+coords);
                    x = Integer.parseInt(coords[0]);
                    z = Integer.parseInt(coords[1]);
                    currChunk = currChunk.getWorld().getChunkAt(x,z);
                    if(args.length>2){

                        if(plugin.getServer().getWorld(worldname) == null){
                            player.sendMessage(ChatColor.RED + "World \'"+worldname + "\' does not exist.");    //mess
                            return true;
                        }
                        currChunk = getWorld(worldname).getChunkAt(x, z);

                    }
                } catch (NumberFormatException e){
                    //e.printStackTrace();
                    player.sendMessage(ChatColor.RED+"usage: /"+label +" "+ args[0]+ " [x,z] [world]"); //mess
                    return true;

                } catch (ArrayIndexOutOfBoundsException e){
                    player.sendMessage(ChatColor.RED+"usage: /"+label +" "+ args[0]+" [x,z] [world]");  //mess
                    return true;
                }
            }
            OwnedLand dbLand = OwnedLand.getLandFromDatabase(x, z, worldname);


            if (dbLand == null || (!dbLand.ownerUUID().equals(player.getUniqueId()) && !player.hasPermission("landlord.admin.unclaim"))){
                player.sendMessage(ChatColor.RED + "You do not own this land.");    //mess
                return true;
            }
            if(plugin.hasVault()){
                if(plugin.getvHandler().hasEconomy()){
                    Double amt = plugin.getConfig().getDouble("economy.sellPrice", 100.0);  //conf
                    if(amt > 0){
                        int numFree = plugin.getConfig().getInt("economy.freeLand", 0);
                        if (numFree > 0 && plugin.getDatabase().find(OwnedLand.class).where().eq("ownerName",player.getUniqueId().toString()).findRowCount() <= numFree) {
                            //player.sendMessage(ChatColor.YELLOW+"You have been charged " + plugin.getvHandler().formatCash(amt) + " to purchase land.");
                        } else if(plugin.getvHandler().giveCash(player, amt)){
                            player.sendMessage(ChatColor.GREEN+"Land sold for " + plugin.getvHandler().formatCash(amt) + ".");  //mess
                            //return true;
                        }
                    }

                }
            }
            if(!player.getUniqueId().equals(dbLand.ownerUUID())){
                player.sendMessage(ChatColor.YELLOW+"Unclaimed " + getOfflinePlayer(dbLand.ownerUUID()).getName() + "'s land.");    //mess
            }
            plugin.getDatabase().delete(dbLand);
            dbLand.highlightLand(player, Effect.WITCH_MAGIC);

            sender.sendMessage(
                    ChatColor.YELLOW + "Successfully unclaimed chunk (" + currChunk.getX() + ", " +         //mess
                            currChunk.getZ() + ") in world \'" + currChunk.getWorld().getName() + "\'."
            );

            //Regen land if enabled
            if(plugin.getConfig().getBoolean("options.regenOnUnclaim",false)){
                currChunk.getWorld().regenerateChunk(currChunk.getX(),currChunk.getZ());
            }

            if(plugin.getConfig().getBoolean("options.soundEffects",true)){
                player.playSound(player.getLocation(), Sound.ENDERMAN_HIT,10,.5f);
            }
            plugin.getMapManager().updateAll();

        }
        return true;
    }

    @Override
    public String getHelpText() {

        String helptext = "unclaim [x,z] [world] (or "+"/#{label} sell)" + ChatColor.RESET + " - Unclaim this chunk.\n";    //mess #{*} parsing
        if (plugin.hasVault() && plugin.getvHandler().hasEconomy() && plugin.getConfig().getDouble("economy.sellPrice", 50.0) > 0) {    //conf
            if(plugin.getConfig().getBoolean("options.regenOnUnclaim",false)) {
                helptext+=ChatColor.RED+""+ChatColor.ITALIC +" Regenerates Chunk!";                         //mess
            }
            helptext += ChatColor.YELLOW + "" + ChatColor.ITALIC + " Get " + plugin.getvHandler().formatCash(plugin.getConfig().getDouble("economy.sellPrice", 50.0)) + " per unclaim.\n";
        } else if(plugin.getConfig().getBoolean("options.regenOnUnclaim",false)) {
            helptext+=ChatColor.RED+""+ChatColor.ITALIC +" Regenerates Chunk!\n";
        }

        return null;
    }

    @Override
    public String[] getTriggers() {
        return new String[]{"unclaim", "sell"};
    }
}
