package com.jcdesimp.landlord.persistantData;

/**
 * File created by jcdesimp on 2/28/14.
 * This class represents a plot of owned land.
 */
//import com.jcdesimp.landlord.DarkBladee12.ParticleAPI.ParticleEffect;

import com.avaje.ebean.validation.NotNull;
import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.landManagement.Landflag;
import org.bukkit.*;
import org.bukkit.entity.Player;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayer;


@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "ll_land")
public class OwnedLand {


    @OneToMany(cascade = CascadeType.ALL)
    List<Friend> friends;
    @Id
    private int id;


    //Used to be the owners username
    @NotNull
    private String ownerName;


    @NotNull
    private int x;

    @NotNull
    private int z;

    @NotNull
    private String worldName;
    private String permissions;

    /**
     * Factory method that creates a new OwnedLand instance given an owner name and chunk
     *
     * @param owner The owner of the land
     * @param c     The chunk this land represents
     * @return OwnedLand
     */
    public static OwnedLand landFromProperties(Player owner, Chunk c) {
        OwnedLand lnd = new OwnedLand();
        //System.out.println(owner.getUniqueId());
        try {
            lnd.setProperties(owner.getUniqueId(), c);
        } catch (NullPointerException e) {
            lnd.setX(c.getX());
            lnd.setZ(c.getZ());
            lnd.setWorldName(c.getWorld().getName());
            lnd.setOwnerName("");
        }

        return lnd;

    }

    public static OwnedLand getApplicableLand(Location l) {
        Chunk c = l.getChunk();
        return getLandFromDatabase(c.getX(), c.getZ(), c.getWorld().getName());
    }

    /**
     * Helper method for boolean conversion
     *
     * @param s string to convert
     * @return true or false
     */
    public static boolean stringToBool(String s) {
        if (s.equals("1"))
            return true;
        if (s.equals("0"))
            return false;
        throw new IllegalArgumentException(s + " is not a bool. Only 1 and 0 are.");
    }

    /**
     * Gets land from the database
     *
     * @param x         coord of chunk
     * @param z         coord of chunk
     * @param worldName of chunk
     * @return OwnedLand instance
     */
    public static OwnedLand getLandFromDatabase(int x, int z, String worldName) {
        return Landlord.getInstance().getDatabase().find(OwnedLand.class)
                .where()
                .eq("x", x)
                .eq("z", z)
                .eq("worldName", worldName)
                .findUnique();
    }

    /**
     * Sets the properties of an OwnedLand instance
     *
     * @param pUUID name of player to be set owner
     * @param c     chunk to be represented
     */
    public void setProperties(UUID pUUID, Chunk c) {
        ownerName = pUUID.toString();
        setX(c.getX());
        setZ(c.getZ());
        setWorldName(c.getWorld().getName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public UUID ownerUUID() {
        if (ownerName.length() < 32) {
            return UUID.randomUUID();
        }
        //System.out.println(ownerName);
        //System.out.println(UUID.fromString(ownerName));
        return UUID.fromString(ownerName);
    }

    public UUID getOwnerUUID() {
        return UUID.fromString(ownerName);
    }

    public String getOwnerUsername() {

        //mess ready
        String unknownUser = "Unknown";

        /*
         * *************************************
         * mark for possible change    !!!!!!!!!
         * *************************************
         */
        if (!getOfflinePlayer(UUID.fromString(ownerName)).hasPlayedBefore() && !getOfflinePlayer(UUID.fromString(ownerName)).isOnline()) {
            return ChatColor.ITALIC + unknownUser;
        }
        return getOfflinePlayer(UUID.fromString(ownerName)).getName();
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getXBlock() {
        return getChunk().getBlock(0, 0, 0).getX();
    }

    public int getZBlock() {
        return getChunk().getBlock(0, 0, 0).getZ();
    }


    /*
     *********************
     * Permissions Stuff *
     *********************
     */

    /*public enum LandAction {
        BUILD, HARM_ANIMALS, OPEN_CONTAINERS
    }*/

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public Chunk getChunk() {
        World world = Bukkit.getServer().getWorld(worldName);
        return world.getChunkAt(x, z);
    }

    /*
    public void changePerm(int action, String group, int perm){
        String[][] np = getLandPerms();
        if(group.equals("friends")){
            np[1][action] = perm+"";
        } else {
            np[0][action] = perm+"";
        }
    }
    */

    /**
     * Get default permission string
     *
     * @return default permission string
     */
    private String[][] getDefaultPerms() {
        ArrayList<String> guestPerms = new ArrayList<String>();
        ArrayList<String> friendsPerms = new ArrayList<String>();
        guestPerms.add("1");
        friendsPerms.add("1");
        for (int i = 0; i < (Landlord.getInstance()).getFlagManager().numStoredPerms(); i++) {
            guestPerms.add("0");
            friendsPerms.add("1");
        }


        return new String[][]{guestPerms.toArray(new String[guestPerms.size()]), friendsPerms.toArray(new String[friendsPerms.size()])};
    }

    public String[][] getLandPerms() {
        String perms = getPermissions();

        if (perms == null) {
            //System.out.println("Is null...");
            return getDefaultPerms();
        }
        String[] permString = perms.split("\\|");
        //ArrayList<String[]> permArray = new ArrayList<String[]>();
        ArrayList<String> newPermString = new ArrayList<String>();
        for (String s : permString) {
            newPermString.add(Integer.toBinaryString(Integer.parseInt(s)));
        }

        String ePerms = newPermString.get(0);
        while (ePerms.length() < Landlord.getInstance().getFlagManager().numStoredPerms() + 1) {
            ePerms += "0";
        }
        newPermString.set(0, ePerms);
        String fPerms = newPermString.get(1);
        while (fPerms.length() < Landlord.getInstance().getFlagManager().numStoredPerms() + 1) {
            fPerms += "1";
        }
        newPermString.set(1, fPerms);

        String[][] permArray = new String[newPermString.size()][];
        for (int i = 0; i < newPermString.size(); i++) {
            //permArray.add(permString[i].split(""));
            permArray[i] = newPermString.get(i).split("(?!^)");
        }
        //return permArray.toArray(new String[permArray.size()]);
        return permArray;
    }

    public boolean hasPermTo(Player player, Landflag lf) {
        if (player.hasPermission("landlord.admin.bypass") || player.getUniqueId().equals(ownerUUID())) {
            return true;
        }
        String[][] perms = getLandPerms();
        int applicablePermSlot = (Landlord.getInstance()).getFlagManager()
                .getRegisteredFlags().get(lf.getClass().getSimpleName()).getPermSlot();
        if (UUID.fromString(getOwnerName()).equals(player.getUniqueId())) {
            return true;
        } else if (isFriend(player)) {
            String[] subPerms = perms[1];
            return stringToBool(subPerms[applicablePermSlot]);

        } else {
            String[] subPerms = perms[0];
            //System.out.println("Is guest");
            return stringToBool(subPerms[applicablePermSlot]);
        }
    }

    public boolean canEveryone(Landflag lf) {
        String[][] perms = getLandPerms();
        int applicablePermSlot = (Landlord.getInstance()).getFlagManager()
                .getRegisteredFlags().get(lf.getClass().getSimpleName()).getPermSlot();
        String[] subPerms = perms[0];
        //System.out.println("Is guest");
        return stringToBool(subPerms[applicablePermSlot]);
    }

    public String permsToString(String[][] perms) {
        String permString = "";
        for (int i = 0; i < perms.length; i++) {
            String currPerm = "";
            for (int ii = 0; ii < perms[i].length; ii++) {
                currPerm += perms[i][ii];

            }
            permString += Integer.toString(Integer.parseInt(currPerm, 2));
            if (i + 1 < perms.length) {
                permString += "|";
            }

        }
        //System.out.println(permString);
        return permString;

    }

    /**
     * Attempt to add a friend
     * Checks to make sure player is not already a friend of and instance
     *
     * @param f Friend to be added
     * @return boolean true if success false if already a friend
     */
    public boolean addFriend(Friend f) {
        if (!isFriend(f)) {
            friends.add(f);
            return true;
        }
        return false;
    }

    /**
     * Removes a friend
     *
     * @param f friend to remove
     * @return boolean
     */
    public boolean removeFriend(Friend f) {
        if (isFriend(f)) {
            Friend frd = Landlord.getInstance().getDatabase().find(Friend.class).where()
                    .eq("id", friends.get(friends.indexOf(f)).getId()).findUnique();
            Landlord.getInstance().getDatabase().delete(frd);
            return true;
        }
        return false;
    }

    /**
     * Returns whether or not a player is
     * a friend of this land
     *
     * @param f Friend to be checked
     * @return boolean is a friend or not
     */
    public boolean isFriend(Friend f) {
        return friends.contains(f);
    }

    public boolean isFriend(Player f) {
        return isFriend(Friend.friendFromPlayer(f));
    }

    public void delete() {
        Landlord.getInstance().getDatabase().delete(this);
    }


    /**
     * Highlights the border around the chunk with a particle effect.
     *
     * @param p player
     * @param e effect to play
     */
    public void highlightLand(Player p, Effect e) {
        highlightLand(p, e, 5);

    }

    public void highlightLand(Player p, Effect e, int amt) {
        if (!Landlord.getInstance().getConfig().getBoolean("options.particleEffects", true)) {
            return;
        }
        Chunk chunk = getChunk();
        ArrayList<Location> edgeBlocks = new ArrayList<Location>();
        for (int i = 0; i < 16; i++) {
            for (int ii = -1; ii <= 10; ii++) {

                edgeBlocks.add(chunk.getBlock(i, (int) (p.getLocation().getY()) + ii, 15).getLocation());
                edgeBlocks.add(chunk.getBlock(i, (int) (p.getLocation().getY()) + ii, 0).getLocation());
                edgeBlocks.add(chunk.getBlock(0, (int) (p.getLocation().getY()) + ii, i).getLocation());
                edgeBlocks.add(chunk.getBlock(15, (int) (p.getLocation().getY()) + ii, i).getLocation());
            }


        }
        //BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        for (Location edgeBlock : edgeBlocks) {
            edgeBlock.setZ(edgeBlock.getBlockZ() + .5);
            edgeBlock.setX(edgeBlock.getBlockX() + .5);

            p.spigot().playEffect(edgeBlock, e, 0, 0, 0.2f, 0.2f, 0.2f, 0.2f, amt, 9);

            //e.display(edgeBlock, 0.2f, 0.2f, 0.2f, 9.2f, amt, p);
            //p.playEffect(edgeBlocks.get(i), e, null);
        }

    }

}
