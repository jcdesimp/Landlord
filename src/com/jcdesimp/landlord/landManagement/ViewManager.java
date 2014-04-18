package com.jcdesimp.landlord.landManagement;

import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * File created by jcdesimp on 4/18/14.
 * Manages instantiated LandManagerViews
 */
public class ViewManager {

    HashMap<String, LandManagerView> activeViews;

    public ViewManager() {

        this.activeViews = new HashMap<String, LandManagerView>();
    }

    public void activateView(Player p, OwnedLand land){
        LandManagerView newView = new LandManagerView(p, land);
        newView.showUI();
        activeViews.put(p.getName(),newView) ;
    }

    public void deactivateView(Player p){
        if(activeViews.containsKey(p.getName())){
            activeViews.get(p.getName()).closeView();
            activeViews.remove(p.getName());
        }
    }

    public void deactivateView(String pName){
        if(activeViews.containsKey(pName)){
            activeViews.get(pName).closeView();
            activeViews.remove(pName);
        }
    }

    public void NoClosedeactivateView(Player p){
        if(activeViews.containsKey(p.getName())){
            activeViews.remove(p.getName());
        }
    }

    public void deactivateAll(){
        for(String s : activeViews.keySet()) {
            deactivateView(s);
        }
    }

}
