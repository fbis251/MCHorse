package com.fernandobarillas.mchorse.helpers;

import com.fernandobarillas.mchorse.Constants;
import com.fernandobarillas.mchorse.McHorse;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * A class to easily send messages to players currently playing a game of Horse
 *
 * Created by FB on 3/20/2015.
 */
public class MessageHelper {

    /**
     * Sends a message to all players currently playing a game of Horse
     *
     * @param plugin  The instance of the plugin to use to check for online players to send the message to.
     * @param message The message to send to the players.
     */
    public static void broadcastMessage(McHorse plugin, String message) {
        McHorse mcHorsePlugin = plugin;
        ArrayList<HorsePlayer> playerQueue = plugin.getPlayerQueue().getQueuedPlayers();

        for (HorsePlayer horsePlayer : playerQueue) {
            Player player = plugin.getServer().getPlayer(horsePlayer.getUniqueId());
            if (player != null && player.isOnline()) {
                pluginMessage(player, message);
            }
        }
    }

    /**
     * Sends a message to a single player playing a game of Horse
     *
     * @param targetPlayer The player to send the message to
     * @param message      The message to send to the player
     */
    public static void pluginMessage(Player targetPlayer, String message) {
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            return;
        }

        targetPlayer.sendMessage(Constants.MESSAGE_PREFIX + message);
    }
}
