package com.jcdesimp.landlord.landFlags;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.landManagement.FlagManager;
import com.jcdesimp.landlord.landManagement.Landflag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;

/**
 * File created by jcdesimp on 4/11/14.
 */
public class Build extends Landflag {
    public Build() {
        super("Build","Gives permission to place and break blocks",
                new ItemStack(Material.COBBLESTONE),"Allowed Build","can build.",
                "Denied Build","cannot build.");
        Landlord plugin = (Landlord)Landlord.getInstance();
        plugin.getFlagManager().registerFlag(this);
    }




}
