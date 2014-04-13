package com.jcdesimp.landlord.landManagement;

import org.bukkit.event.Listener;

/**
 * File created by jcdesimp on 4/11/14.
 */
public abstract class Landflag implements Listener {
    //Data Fields
    private String uniqueName;
    private int permSlot;

    //Display Fields
    private String displayName;
    private String descrition;



    public String getUniqueName() {
        return uniqueName;
    }

    public int getPermSlot() {
        return permSlot;
    }

    public void setPermSlot(int permSlot) {
        this.permSlot = permSlot;
    }
}
