package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.LandlordCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

import static org.bukkit.util.NumberConversions.ceil;

/**
 * Created by jcdesimp on 2/19/15.
 * Command to display help information to the player
 */
public class Help implements LandlordCommand {

    private Landlord plugin;
    private ArrayList<LandlordCommand> registeredCommands;


    public Help(Landlord plugin, LandlordCommandExecutor commandHandler) {
        this.plugin = plugin;
        this.registeredCommands = new ArrayList<LandlordCommand>();
    }


    public void addCommand(LandlordCommand lc) {
        registeredCommands.add(lc);
    }



    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {

        //mess ready
        String badPageNum = "That is not a valid page number.";
        String helpHeader = "Landlord #{version} Created by #{author}";
        String aliases = "Aliases: #{aliases}";
        String nextPageString = "do #{label} #{cmd} #{pageNumber} for next page";

        String aliasList = "/landlord, /land, /ll";

        //check if page number is valid
        int pageNumber = 1;
        if (args.length > 1 && Arrays.asList(getTriggers()).contains(args[0]) ) {
            try{
                pageNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                // Is not a number!
                sender.sendMessage(ChatColor.RED+badPageNum);
                return true;
            }
        }


        // construct the header form the base strings
        String header = ChatColor.DARK_GREEN
                + "-|| " +
                helpHeader                                                                  // start out with the initial header
        .replace("#{version}", "v" + plugin.getDescription().getVersion())                  // fill in the version
                .replace("#{author}", ChatColor.BLUE + plugin.getDescription().getAuthors().get(0) + ChatColor.DARK_GREEN)   // fill in the author name
                + " ||--" +
        "\n   " + ChatColor.GRAY + aliases.replace("#{aliases}", aliasList);                                  // add the aliases line

        ArrayList<String> helpList = new ArrayList<String>();

        // Get each help string
        for(LandlordCommand lc : registeredCommands) {
            String currCmd = lc.getHelpText(sender);
            if (currCmd != null) {  // make sure the help string isn't null (can happen if conditions aren't right)
                helpList.add(currCmd.replace("#{label}",label));
            }
        }


        //Amount to be displayed per page
        final int numPerPage = 5;

        int numPages = ceil((double)helpList.size()/(double)numPerPage);
        if(pageNumber > numPages){
            sender.sendMessage(ChatColor.RED+badPageNum);
            return true;
        }
        String pMsg = header;
        if (pageNumber == numPages){
            for(int i = (numPerPage*pageNumber-numPerPage); i<helpList.size(); i++){
                pMsg+= '\n' + helpList.get(i);
            }
            pMsg+=ChatColor.DARK_GREEN+"\n------------------------------";
        } else {
            for(int i = (numPerPage*pageNumber-numPerPage); i<(numPerPage*pageNumber); i++){
                pMsg+= '\n' + helpList.get(i);
            }

            pMsg += "\n" + ChatColor.DARK_GREEN + "--- " + nextPageString
                    .replace("#{label}",ChatColor.YELLOW + "/" + label)
                    .replace("#{cmd}", getTriggers()[0])
                    .replace("#{pageNumber}", "" + (pageNumber + 1) + ChatColor.DARK_GREEN)
                    + " ---";
        }

        sender.sendMessage(pMsg);
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {

        //mess ready
        String usage = "/#{label} #{cmd} [page #]"; // get the base usage string
        String desc = "Show this help message.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());
    }

    @Override
    public String[] getTriggers() {
        return new String[]{"help", "?"};   //mess triggers
    }
}
