package com.jcdesimp.landlord;

/**
 * File created by jcdesimp on 2/28/14.
 * This class represents a plot of owned land.
 */
import com.DarkBladee12.ParticleAPI.ParticleEffect;
import com.avaje.ebean.validation.NotNull;
import org.bukkit.*;

import javax.persistence.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="ll_land")
public class OwnedLand {


    /**
     * Factory method that creates a new OwnedLand instance given an owner name and chunk
     * @param owner The owner of the land
     * @param c The chunk this land represents
     * @return OwnedLand
     */
    public static OwnedLand landFromProperties(String owner, Chunk c){
        OwnedLand lnd = new OwnedLand();
        lnd.setProperties(owner, c);
        return lnd;

    }


    @Id
    private int id;


    @NotNull
    private String ownerName;



    @NotNull
    private int x;

    @NotNull
    private int z;

    @NotNull
    private String worldName;

    @OneToMany(cascade = CascadeType.ALL)
    List<Friend> friends;

    private String permissions;



    /**
     * Sets the properties of an OwnedLand instance
     * @param pName name of player to be set owner
     * @param c chunk to be represented
     */
    public void setProperties(String pName, Chunk c) {
        ownerName = pName;
        this.setX(c.getX());
        this.setZ(c.getZ());
        this.setWorldName(c.getWorld().getName());
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getZ() {
        return z;
    }

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
     *********************
     * Permissions Stuff *
     *********************
     */

    public enum LandAction {
        BUILD, HARM_ANIMALS, OPEN_CONTAINERS
    }

    /**
     * Get default permission string
     * @return default permission string
     */
    private String[][] getDefaultPerms() {
        return new String[][]{{"1","0","0","0","0","0","0","0","0","0"},{"1","1","1","1","1","1","0","0","0","0"}};
    }


    /**
     * Helper method for boolean conversion
     * @param s string to convert
     * @return true or false
     */
    public static boolean stringToBool(String s) {
        if (s.equals("1"))
            return true;
        if (s.equals("0"))
            return false;
        throw new IllegalArgumentException(s+" is not a bool. Only 1 and 0 are.");
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

    public String[][] getLandPerms(){
        String perms = getPermissions();

        if (perms==null){
            //System.out.println("Is null...");
            return getDefaultPerms();
        }
        String[] permString = perms.split("\\|");
        //ArrayList<String[]> permArray = new ArrayList<String[]>();
        String[][] permArray = new String[permString.length][];
        for(int i = 0; i<permString.length; i++){
            //permArray.add(permString[i].split(""));
            permArray[i]=permString[i].split("(?!^)");
        }
        //return permArray.toArray(new String[permArray.size()]);
        return permArray;
    }

    public boolean hasPermTo(String playerName, LandAction action){
        String[][] perms = getLandPerms();
        if(getOwnerName().equalsIgnoreCase(playerName)){
            return true;
        } else if (isFriend(playerName)) {
            String[] subPerms = perms[1];
            switch (action){
              case BUILD:
                  return stringToBool(subPerms[1]);
              case HARM_ANIMALS:
                  return stringToBool(subPerms[2]);
              case OPEN_CONTAINERS:
                  return stringToBool(subPerms[3]);
              default:
                  return false;
            }
        } else {
            String[] subPerms = perms[0];
            //System.out.println("Is guest");
            switch (action){

                case BUILD:
                    return stringToBool(subPerms[1]);
                case HARM_ANIMALS:
                    //System.out.println("check harm");
                    return stringToBool(subPerms[2]);
                case OPEN_CONTAINERS:
                    return stringToBool(subPerms[3]);
                default:
                    return false;

            }

        }
    }




    public String permsToString(String[][] perms) {
        String permString = "";
        for(int i = 0; i<perms.length; i++){
            for(int ii = 0; ii<perms[i].length; ii++){
                permString += perms[i][ii];

            }
            if(i+1<perms.length){
                permString += "|";
            }

        }
        //System.out.println(permString);
        return permString;

    }


    /**
     * Attempt to add a friend
     * Checks to make sure player is not already a friend of and instance
     * @param f Friend to be added
     * @return boolean true if success false if already a friend
     */
    public boolean addFriend(Friend f) {
        if(!isFriend(f)){
            friends.add(f);
            return true;
        }
        return false;
    }


    /**
     * Removes a friend
     * @param f friend to remove
     * @return boolean
     */
    public boolean removeFriend(Friend f) {
        if(isFriend(f)){
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
     * @param f Friend to be checked
     * @return boolean is a friend or not
     */
    public boolean isFriend(Friend f) {
        return friends.contains(f);
    }

    public boolean isFriend(String f) {
        return isFriend(Friend.friendFromName(f));
    }


    /**
     * Gets land from the database
     * @param x coord of chunk
     * @param z coord of chunk
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
     * Highlights the border around the chunk with a particle effect.
     * @param p player
     * @param e effect to play
     */
    public void highlightLand(Player p, ParticleEffect e){
        highlightLand(p, e, 5);

    }
    public void highlightLand(Player p, ParticleEffect e, int amt){
        Chunk chunk = getChunk();
        ArrayList<Location> edgeBlocks = new ArrayList<Location>();
        for(int i = 0; i<16; i++){
            for(int ii = -1; ii<=10; ii++){
                edgeBlocks.add(chunk.getBlock(i, (int) (p.getLocation().getY())+ii, 15).getLocation());
                edgeBlocks.add(chunk.getBlock(i, (int) (p.getLocation().getY())+ii, 0).getLocation());
                edgeBlocks.add(chunk.getBlock(0, (int) (p.getLocation().getY())+ii, i).getLocation());
                edgeBlocks.add(chunk.getBlock(15, (int) (p.getLocation().getY())+ii, i).getLocation());
            }


        }
        //BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        for (Location edgeBlock : edgeBlocks) {
            e.display(edgeBlock, 0.2f, 0.2f, 0.2f, 9.2f, amt, p);
            //p.playEffect(edgeBlocks.get(i), e, null);
        }

    }

}
