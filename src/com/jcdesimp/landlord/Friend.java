package com.jcdesimp.landlord;


import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;

/**
 * Friend object
 */

@Entity
@Table(name="ll_friend")
public class Friend extends Object {

    public static Friend friendFromName(String playerName) {
        Friend fd = new Friend();
        fd.setPlayerName(playerName);
        return fd;
    }



    @Id
    private int id;


    @NotNull
    private String playerName;



    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Friend) {
            //Landlord.getPlugin(Landlord.class).getLogger().warning("Friend: " + ((Friend) obj).getPlayerName());
            //Landlord.getPlugin(Landlord.class).getLogger().warning("TheFriend: " + playerName);
            return ((Friend) obj).getPlayerName().equalsIgnoreCase(playerName);
        } else {
            return false;
        }
    }


    /*
    public void setFriendOf(List<OwnedLand> friendOf) {
        this.friendOf = friendOf;
    }

    public List<OwnedLand> getFriendOf() {
        return friendOf;
    }
    */
}
