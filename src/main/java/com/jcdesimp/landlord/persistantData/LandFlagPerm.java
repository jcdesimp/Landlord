package com.jcdesimp.landlord.persistantData;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * File created by jcdesimp on 4/14/14.
 */
@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "ll_flagPerm")
public class LandFlagPerm {

    @Id
    private int id;

    @NotNull
    private String identifier;

    @NotNull
    private int permSlot;

    public static LandFlagPerm flagPermFromData(String ident, int pSlot) {
        LandFlagPerm lf = new LandFlagPerm();
        lf.setIdentifier(ident);
        lf.setPermSlot(pSlot);
        return lf;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getPermSlot() {
        return permSlot;
    }

    public void setPermSlot(int permSlot) {
        this.permSlot = permSlot;
    }
}
