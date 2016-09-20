package com.jcdesimp.landlord.persistantData;


import com.avaje.ebean.validation.NotNull;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayer;

/**
 * Friend object
 */

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "ll_friend")
public class Friend {

    @Id
    private int id;
    @NotNull
    private String playerName;

    public static Friend friendFromPlayer(Player p) {
        Friend fd = new Friend();
        fd.setPlayerName(p.getUniqueId().toString());
        return fd;
    }

    public static Friend friendFromOfflinePlayer(OfflinePlayer p) {
        Friend fd = new Friend();
        fd.setPlayerName(p.getUniqueId().toString());
        return fd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Friend && ((Friend) obj).getPlayerName().equalsIgnoreCase(playerName);
    }

    public String getName() {

        //mess ready
        String unknownFriend = "Unknown";

        /*
         * *************************************
         * mark for possible change    !!!!!!!!!
         * *************************************
         */
        OfflinePlayer op = getOfflinePlayer(UUID.fromString(playerName));
        if (!op.hasPlayedBefore() && !op.isOnline()) {
            return ChatColor.ITALIC + unknownFriend;
        }
        return op.getName();
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
