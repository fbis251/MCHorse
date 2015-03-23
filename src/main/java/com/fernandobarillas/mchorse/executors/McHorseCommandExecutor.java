package com.fernandobarillas.mchorse.executors;

import com.fernandobarillas.mchorse.Constants;
import com.fernandobarillas.mchorse.helpers.MessageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class McHorseCommandExecutor implements CommandExecutor {

    private final static String PLUGIN_COMMAND_LONG_NAME = "horse";
    private final static String PLUGIN_COMMAND_SHORT_NAME = "h";
    private final Plugin mPlugin;

    public McHorseCommandExecutor(Plugin plugin) {
        mPlugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {

        // All plugin commands can only be sent by players
        if (!(sender instanceof Player)) {
            sender.sendMessage(Constants.PLUGIN_NAME + " commands can only be run by a player");
            return true;
        }

        // Now we know that the sender was a player
        Player player = (Player) sender;
        String commandName = command.getName();

        if (commandName.equalsIgnoreCase(PLUGIN_COMMAND_LONG_NAME) || commandName.equalsIgnoreCase(PLUGIN_COMMAND_SHORT_NAME)) {
            if (arguments.length < 1) {
                MessageHelper.pluginMessage(player, "Not enough arguments");
                return false;
            }

            if (arguments.length > 2) {
                MessageHelper.pluginMessage(player, "Too many arguments");
                return false;
            }

            String commandArgument, otherPlayerName;

            commandArgument = arguments[0];
            if (arguments.length == 2) {
                // TODO: Add permission for allowing players to add/remove other players
                otherPlayerName = arguments[1];
            }

            // Handle the plugin command arguments here to allow players to add, remove, etc. themselves from the queue
            // TODO: Add, remove, start, stop, score, players
            switch (commandArgument) {
                case "a":
                case "add":
                    MessageHelper.pluginMessage(player, "Adding yourself to the queue");
                    // TODO: Send an error message to the player if they're already in a queue
                    return true;
                case "r":
                case "remove":
                    MessageHelper.pluginMessage(player, "Removing yourself from the queue");
                    // TODO: Send an error message to the player if they're not in the queue
                    return true;
                default:
                    return false;
            }
        }

        return false;
    }
}
