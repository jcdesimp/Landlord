package com.jcdesimp.landlord.pluginHooks;

import com.jcdesimp.landlord.Landlord;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * File created by jcdesimp on 3/15/14.
 */
public class VaultHandler {
    private Economy economy = null;

    public VaultHandler() {
        setupEconomy();
    }

    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = Landlord.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public boolean hasEconomy(){
        if(economy == null){
            return false;
        }
        return true;
    }

    private boolean canAfford(Player p, double amt){
        if(economy.getBalance(p) >= amt){
            return true;

        }
        return false;
    }

    public boolean chargeCash(Player p, double amt){
        if(canAfford(p,amt)){
            economy.withdrawPlayer(p, amt);
            return true;
        }
        return false;
    }

    public boolean giveCash(Player p, double amt){
        economy.depositPlayer(p,amt);
        return true;
    }

    public String formatCash(double amt){
        return economy.format(amt);
    }
}
