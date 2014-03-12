package com.jcdesimp.landlord;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * File created by jcdesimp on 3/10/14.
 */
public class MapManager implements Listener {
    private HashMap<String, LandMap> mapList;

    public MapManager() {
        this.mapList = new HashMap<String, LandMap>();

    }


    private void addMap(LandMap m){
        mapList.put(m.getMapViewer().getName(), m);

    }

    public void toggleMap(Player p){
        if( mapList.containsKey(p.getName()) ){
            remMap(p.getName());
        } else {
            addMap(new LandMap(p));
        }
        //ystem.out.println(mapList.toString());

    }

    public void remMap(String pName){
        System.out.println("Before Rem: "+mapList.toString());
        if( mapList.containsKey(pName) ){
            LandMap curr = mapList.get(pName);
            curr.removeMap();
            mapList.remove(pName);
        }
        System.out.println("After Rem: "+mapList.toString());
    }

    public void removeAllMaps(){
        Iterator it = mapList.entrySet().iterator();
        for (String k : mapList.keySet()){
            mapList.get(k).removeMap();
        }
        mapList.clear();
    }

    public void updateAll(){
        Iterator it = mapList.entrySet().iterator();
        for (String k : mapList.keySet()){
            mapList.get(k).updateMap();
        }
    }


    @EventHandler
    public void playerLeave(PlayerQuitEvent event){
        if(mapList.containsKey(event.getPlayer().getName())){
            remMap(event.getPlayer().getName());
        }

    }
}
