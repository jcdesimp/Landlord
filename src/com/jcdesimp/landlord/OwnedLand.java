package com.jcdesimp.landlord;

/**
 * Created by jcdesimp on 2/28/14.
 */

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name="ll_land")
public class OwnedLand {

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



    public Chunk getChunk() {
        World world = Bukkit.getServer().getWorld(worldName);
        return world.getChunkAt(x, z);
    }

    /**
     * Attempt to add a friend
     * Checks to make sure player is not already a friend of and instance
     * @param f
     * @return
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
     * @param f
     */
    public boolean removeFriend(Friend f) {
        if(isFriend(f)){
            //JavaPlugin.getPlugin(Landlord.class).getLogger().warning("Before: " + friends);
            //friends.get(friends.indexOf(f)).getId();
            JavaPlugin.getPlugin(Landlord.class).getLogger().warning("After: " + friends);
            Friend frd = JavaPlugin.getPlugin(Landlord.class).getDatabase().find(Friend.class).where()
                    .eq("id", friends.get(friends.indexOf(f)).getId()).findUnique();
            JavaPlugin.getPlugin(Landlord.class).getDatabase().delete(frd);
            return true;
        }
        return false;
    }

    /**
     * Returns whether or not a player is
     * a friend of this land
     * @param f
     * @return
     */
    public boolean isFriend(Friend f) {
        return friends.contains(f);
    }

    public static OwnedLand getLandFromDatabase(int x, int z, String worldName) {
        return JavaPlugin.getPlugin(Landlord.class).getDatabase().find(OwnedLand.class)
                .where()
                .eq("x", x)
                .eq("z", z)
                .eq("worldName", worldName)
                .findUnique();
    }

}
