package com.jcdesimp.landlord;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;
import java.util.List;

/**
 * File created by jcdesimp on 4/6/14.
 */
@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="ll_version")
public class DBVersion {


    @Id
    private int id;



    //Used to be the owners username
    @NotNull
    private String identifier;

    private String stringData;
    private int intData;

    public int getIntData() {
        return intData;
    }

    public void setIntData(int intData) {
        this.intData = intData;
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

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }
}
