package com.jcdesimp.landlord;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;

import java.util.List;

/**
 * File created by jcdesimp on 3/1/14.
 */
@SuppressWarnings("SpellCheckingInspection")
public class LandMap {

    Player mapViewer;
    //Scoreboard playerMap;
    int schedulerId;
    Chunk currChunk;
    List<OwnedLand> nearbyLand;
    String currDir;

    public LandMap(Player p) {
        this.mapViewer=p;
        this.currChunk = p.getLocation().getChunk();
        //System.out.println("CURR: "+currChunk);
        this.nearbyLand = Landlord.getInstance().getDatabase().find(OwnedLand.class)
                .where()
                .ge("x", this.currChunk.getX()-3).le("x", this.currChunk.getX()+3)
                .ge("z", this.currChunk.getZ()-3).le("z",this.currChunk.getZ()+3)
                .eq("worldName", this.currChunk.getWorld().getName())
                .findList();
        //displayMap(mapViewer);
        this.currDir = getPlayerDirection(mapViewer);
        displayMap(mapViewer);
        this.schedulerId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Landlord.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if(currDir!=getPlayerDirection(mapViewer) || currChunk!=mapViewer.getLocation().getChunk()){
                    displayMap(mapViewer);
                    currDir = getPlayerDirection(mapViewer);
                }

            }
        }, 0L, 7L);

        displayMap(this.mapViewer);


    }

    public Player getMapViewer() {
        return mapViewer;
    }

    public void removeMap(){
        mapViewer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Bukkit.getServer().getScheduler().cancelTask(schedulerId);

    }

    private Scoreboard displayMap(Player p){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Team team = board.registerNewTeam("teamname");
        team.addPlayer(p);

        Objective objective = board.registerNewObjective("Land Map", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        //objective.setDisplayName("MAP");
        String[] mapData = buildMap(p);
        for(int i = 0; i<mapData.length; i++){
            if(mapData[i].length()<21){
                for(int f = 0; f<(21-mapData[i].length()); f++){
                    mapData[i] += ChatColor.RESET;
                }
            }
            OfflinePlayer ofp = Bukkit.getOfflinePlayer(mapData[i].substring(5,17));
            Score score = objective.getScore(ofp);
            score.setScore(mapData.length - i);


            Team t = board.registerNewTeam(i+"");
            t.setPrefix(mapData[i].substring(0,5));
            t.setSuffix(mapData[i].substring(17));
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
        String dir;
        float y = playerSelf.getLocation().getYaw();
        if( y < 0 ){y += 360;}
        y %= 360;
        int i = (int)((y+8) / 22.5);
        if(i == 0){dir = "south";}
        else if(i == 1){dir = "south southwest";}
        else if(i == 2){dir = "southwest";}
        else if(i == 3){dir = "west southwest";}
        else if(i == 4){dir = "west";}
        else if(i == 5){dir = "west northwest";}
        else if(i == 6){dir = "northwest";}
        else if(i == 7){dir = "north northwest";}
        else if(i == 8){dir = "north";}
        else if(i == 9){dir = "north northeast";}
        else if(i == 10){dir = "northeast";}
        else if(i == 11){dir = "east northeast";}
        else if(i == 12){dir = "east";}
        else if(i == 13){dir = "east southeast";}
        else if(i == 14){dir = "southeast";}
        else if(i == 15){dir = "south southeast";}
        else {dir = "south";}
        return dir;
    }

    public static String[][] getMapDir(String dir){

        String[][] mapDir = new String[][]{
                {"▓", "▒", "▒", "∞", "▒", "▒", "▓"},
                {"▒", "▓", "▒", "∞", "▒", "▓", "▒"},
                {"▒", "▒", "▓", "∞", "▓", "▒", "▒"},
                {"▓", "▒", "▓", "█", "▒", "▓", "▒"},
                {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
        };

        if(dir.equals("west")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"∞", "∞", "∞", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("west northwest")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"∞", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "∞", "∞", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("northwest")){
            mapDir = new String[][]{
                    {"∞", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "∞", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "∞", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("north northwest")){
            mapDir = new String[][]{
                    {"▓", "∞", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "∞", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "∞", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("north")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "∞", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "∞", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "∞", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("north northeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "∞", "▓"},
                    {"▒", "▓", "▒", "▓", "∞", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "∞", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("northeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "∞"},
                    {"▒", "▓", "▒", "▓", "▒", "∞", "▒"},
                    {"▒", "▒", "▓", "▒", "∞", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("east northeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "∞"},
                    {"▒", "▒", "▓", "▒", "∞", "∞", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("east")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                /**/{"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "∞", "∞", "∞"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("east southeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "∞", "∞", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "∞"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("southeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "∞", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "∞", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "∞"}
            };
        } else if(dir.equals("south southeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "∞", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "∞", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "∞", "▒"}
            };
        } else if(dir.equals("south")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "∞", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "∞", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "∞", "▓", "▓", "▒"}
            };
        } else if(dir.equals("south southwest")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "∞", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "∞", "▓", "▓", "▒", "▓"},
                    {"▒", "∞", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("southwest")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "∞", "▒", "▒", "▓", "▓"},
                    {"▓", "∞", "▓", "▓", "▓", "▒", "▓"},
                    {"∞", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("west southwest")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "∞", "∞", "▒", "▒", "▓", "▓"},
                    {"∞", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        }

        return mapDir;
    }

    public void updateMap(){
        nearbyLand = Landlord.getInstance().getDatabase().find(OwnedLand.class)
                .where()
                .ge("x", currChunk.getX()-3).le("x", currChunk.getX()+3)
                .ge("z", currChunk.getZ()-3).le("z",currChunk.getZ()+3)
                .eq("worldName", currChunk.getWorld().getName())
                .findList();
        currChunk = mapViewer.getLocation().getChunk();
    }

    private String[] buildMap(Player p) {
        //String st ="▒▒▒▓▒▒▒\n▒▒▓▓▓▒▒\n▒▓▓▓▓▓▒\n▓▓▓█▓▓▓\n▓▓▒▒▒▓▓\n▓▒▒▒▒▒▓\n▒▒▒▒▒▒▒";

        int radius = 3;



        String[][] mapBoard = getMapDir(getPlayerDirection(p));

        String[] mapRows = new String[mapBoard.length + 3];

        //Chunk pChunk = p.getLocation().getChunk();
        //OwnedLand.getLandFromDatabase(1,2,world)
        //System.out.println(currChunk);
        if(!currChunk.equals(mapViewer.getLocation().getChunk())){
            updateMap();
        }
        //System.out.println("Viewer: "+mapViewer);
        //System.out.println("EndQuery: "+currChunk);
        //System.out.println("nearLand: "+nearbyLand);
        for(int z = 0; z < mapBoard.length; z++){
            String row = "";

            //if curr chunk
            for(int x = 0; x < mapBoard[z].length; x++){
                //System.out.println("Iteration: "+z +", "+x);
                List<OwnedLand> filteredList =
                        Landlord.getInstance().getDatabase().filter(OwnedLand.class)
                                .eq("x", currChunk.getX()-radius+x)
                                .eq("z", currChunk.getZ()-radius+z)
                                .eq("worldName", currChunk.getWorld().getName())
                                .filter(nearbyLand);
                //System.out.println("FilteredList: "+filteredList);
                /*
                OwnedLand ol = OwnedLand.getLandFromDatabase((pChunk.getX()-radius)+x,
                        (pChunk.getZ()-radius)+z,
                        pChunk.getWorld().getName());

                        */
                String currSpot = mapBoard[z][x];


                if(!filteredList.isEmpty()){
                    OwnedLand ol = filteredList.get(0);
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

            //if currchunk changed
            mapRows[z] = row;

        }
        mapRows[mapBoard.length] = ChatColor.GREEN + "█-Yours";
        mapRows[mapBoard.length+1] = ChatColor.YELLOW + "█-Friendly";
        mapRows[mapBoard.length+2] = ChatColor.RED + "█-Others'";
        //mapRows[0] = "";

        return mapRows;
    }

}
