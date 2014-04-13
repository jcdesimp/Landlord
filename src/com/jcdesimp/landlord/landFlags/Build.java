package com.jcdesimp.landlord.landFlags;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.landManagement.FlagManager;
import com.jcdesimp.landlord.landManagement.Landflag;
import org.bukkit.Bukkit;
import org.bukkit.Server;

/**
 * File created by jcdesimp on 4/11/14.
 */
public class Build extends Landflag {
    public Build() {
        //super("Build");









        Landlord plugin = (Landlord)Landlord.getInstance();
        plugin.getFlagManager().registerFlag(this);
    }




}
