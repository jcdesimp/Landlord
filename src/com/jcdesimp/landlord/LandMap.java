package com.jcdesimp.landlord;


import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Array;
import java.util.List;

/**
 * File created by jcdesimp on 3/1/14.
 */
public class LandMap {


    public static Scoreboard displayMap(Player p){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Team team = board.registerNewTeam("teamname");
        team.addPlayer(p);

        Objective objective = board.registerNewObjective("Land Map", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        //objective.setDisplayName("MAP");
        String[] mapData = buildMap(p);
        for(int i = 0; i<mapData.length; i++){

            OfflinePlayer ofp = Bukkit.getOfflinePlayer(mapData[i].substring(6,16));
            Score score = objective.getScore(ofp);
            score.setScore(mapData.length-i);


            Team t = board.registerNewTeam(i+"");
            t.setPrefix(mapData[i].substring(0,6));
            t.setSuffix(mapData[i].substring(16));
            t.addPlayer(ofp);
            t.setDisplayName(mapData[i]);


            //Score score = objective.getScore(Bukkit.getOfflinePlayer(i + ""));
            //score.setScore((mapData.length)-i);
        }
        //Score score = objective.getScore(Bukkit.getOfflinePlayer()); //Get a fake offline player
        //board.
        p.setScoreboard(board);

        return board;
    }


    public static String getPlayerDirection(Player playerSelf){
        String dir = "";
        float y = playerSelf.getLocation().getYaw();
        if( y < 0 ){y += 360;}
        y %= 360;
        int i = (int)((y+8) / 22.5);
        if(i == 0){dir = "south";}
        else if(i == 1){dir = "southwest";}
        else if(i == 2){dir = "southwest";}
        else if(i == 3){dir = "west";}
        else if(i == 4){dir = "west";}
        else if(i == 5){dir = "northwest";}
        else if(i == 6){dir = "northwest";}
        else if(i == 7){dir = "north";}
        else if(i == 8){dir = "north";}
        else if(i == 9){dir = "northeast";}
        else if(i == 10){dir = "northeast";}
        else if(i == 11){dir = "east";}
        else if(i == 12){dir = "east";}
        else if(i == 13){dir = "southeast";}
        else if(i == 14){dir = "southeast";}
        else if(i == 15){dir = "south";}
        else {dir = "south";}
        return dir;
    }

    public static String[][] getMapDir(String dir){

        String[][] mapDir = new String[][]{
                {"▒", "▒", "▓", "∞", "▓", "▒", "▒"},
                {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                {"▒", "▒", "▒", "∞", "▓", "▒", "▒"},
                {"▒", "▒", "▒", "█", "▒", "▒", "▒"},
                {"▒", "▒", "▒", "▓", "▓", "▒", "▒"},
                {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                {"▒", "▒", "▒", "▒", "▒", "▒", "▒"}
        };

        if(dir.equals("west")){
            mapDir = new String[][]{
                    {"▒", "▒", "▓", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▓", "▒", "▒"},
                    {"∞", "▒", "∞", "█", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▒", "▒", "▒"}
            };
        } else if(dir.equals("northwest")){
            mapDir = new String[][]{
                    {"∞", "▒", "▓", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                    {"▒", "▒", "∞", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "█", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▒", "▒", "▒"}
            };
        } else if(dir.equals("north")){
            mapDir = new String[][]{
                    {"▒", "▒", "▓", "∞", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "∞", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "█", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▒", "▒", "▒"}
            };
        } else if(dir.equals("northeast")){
            mapDir = new String[][]{
                    {"▒", "▒", "▓", "▓", "▓", "▒", "∞"},
                    {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "∞", "▒", "▒"},
                    {"▒", "▒", "▒", "█", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▒", "▒", "▒"}
            };
        } else if(dir.equals("east")){
            mapDir = new String[][]{
                    {"▒", "▒", "▓", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "█", "∞", "▒", "∞"},
                    {"▒", "▒", "▒", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▒", "▒", "▒"}
            };
        } else if(dir.equals("southeast")){
            mapDir = new String[][]{
                    {"▒", "▒", "▓", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "█", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "∞", "▒", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▒", "▒", "∞"}
            };
        } else if(dir.equals("south")){
            mapDir = new String[][]{
                    {"▒", "▒", "▓", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "█", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "∞", "▓", "▒", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "∞", "▒", "▒", "▒"}
            };
        } else if(dir.equals("southwest")){
            mapDir = new String[][]{
                    {"▒", "▒", "▓", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                    {"▒", "▒", "▒", "▒", "▓", "▒", "▒"},
                    {"▒", "▒", "▒", "█", "▒", "▒", "▒"},
                    {"▒", "▒", "∞", "▓", "▓", "▒", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"∞", "▒", "▒", "▒", "▒", "▒", "▒"}
            };
        }

        return mapDir;
    }

    public static String[] buildMap(Player p) {
        //String st ="▒▒▒▓▒▒▒\n▒▒▓▓▓▒▒\n▒▓▓▓▓▓▒\n▓▓▓█▓▓▓\n▓▓▒▒▒▓▓\n▓▒▒▒▒▒▓\n▒▒▒▒▒▒▒";

        int radius = 3;



        String[][] mapBoard = getMapDir(getPlayerDirection(p));
        /*String[][] mapBoard = new String[][]{
                {"▒", "▒", "▓", "▓", "▓", "▒", "▒"},
                {"▒", "▒", "▒", "▓", "▒", "▒", "▒"},
                {"▒", "▒", "▒", "▒", "▓", "▒", "▒"},
                {"▒", "▒", "▒", "█", "▒", "▒", "▒"},
                {"▒", "▒", "▒", "▓", "▓", "▒", "▒"},
                {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                {"▒", "▒", "▒", "▒", "▒", "▒", "▒"}
        };*/

        String[] mapRows = new String[mapBoard.length];

        Chunk pChunk = p.getLocation().getChunk();
        //OwnedLand.getLandFromDatabase(1,2,world)

        for(int z = 0; z < mapBoard.length; z++){
            String row = "";
            for(int x = 0; x < mapBoard[z].length; x++){
                OwnedLand ol = OwnedLand.getLandFromDatabase((pChunk.getX()-radius)+x,
                        (pChunk.getZ()-radius)+z,
                        pChunk.getWorld().getName());
                String currSpot = mapBoard[z][x];
                if(ol != null){
                    if(ol.getOwnerName().equals(p.getName())){
                        currSpot = ChatColor.GREEN + currSpot;
                    } else if(ol.isFriend(p.getName())){
                        currSpot = ChatColor.YELLOW + currSpot;
                    } else {
                        currSpot = ChatColor.RED + currSpot;
                    }
                } else {
                    currSpot = ChatColor.RESET + currSpot;
                }
                //System.out.println(currSpot);
                row += currSpot;

            }
            mapRows[z] = row;

        }
        //mapRows[0] = "";

        return mapRows;
    }

}
