package com.fernandobarillas.mchorse;

import com.fernandobarillas.mchorse.helpers.MessageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class McHorseCommandExecutor implements CommandExecutor {

    private final static String PLUGIN_COMMAND_LONG_NAME = "horse";
    private final static String PLUGIN_COMMAND_SHORT_NAME = "h";
    private final Plugin mPlugin;
    private HashMap<String, Player> playerList;

    public McHorseCommandExecutor(Plugin plugin) {
        mPlugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // All plugin commands can only be sent by players
        if (!(sender instanceof Player)) {
            sender.sendMessage(Constants.PLUGIN_NAME + " commands can only be run by a player");
            return true;
        }

        // Now we know that the sender was a player
        Player player = (Player) sender;
        String commandName = command.getName();

        if (commandName.equalsIgnoreCase(PLUGIN_COMMAND_LONG_NAME) || commandName.equalsIgnoreCase(PLUGIN_COMMAND_SHORT_NAME)) {

            if (args.length < 1) {
                MessageHelper.pluginMessage(player, "Not enough arguments");
                return false;
            }

            if (args.length > 2) {
                MessageHelper.pluginMessage(player,"Too many arguments");
                return false;
            }

            // TODO: Add, remove, start, stop, score, players
            MessageHelper.pluginMessage(player, "Starting a game of horse");

            return true;
        }

        return false;
    }
}
