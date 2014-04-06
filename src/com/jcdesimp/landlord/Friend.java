package com.jcdesimp.landlord;


import com.avaje.ebean.validation.NotNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.persistence.*;
import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayer;

/**
 * Friend object
 */

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="ll_friend")
public class Friend {

    public static Friend friendFromPlayer(Player p) {
        Friend fd = new Friend();
        fd.setPlayerName(p.getUniqueId().toString());
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
        return obj instanceof Friend && ((Friend) obj).getPlayerName().equalsIgnoreCase(playerName);
    }

    public String getName() {
        /*
         * *************************************
         * mark for possible change    !!!!!!!!!
         * *************************************
         */
        if (!getOfflinePlayer(UUID.fromString(playerName)).hasPlayedBefore()) {
            return ChatColor.ITALIC+"Unknown";
        }
        return getOfflinePlayer(UUID.fromString(playerName)).getName();
    }

    public UUID getUUID() {
        return UUID.fromString(playerName);
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
