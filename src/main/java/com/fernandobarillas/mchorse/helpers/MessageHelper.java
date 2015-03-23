package com.fernandobarillas.mchorse.helpers;

import com.fernandobarillas.mchorse.Constants;
import com.fernandobarillas.mchorse.McHorse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

/**
 * Created by FB on 3/20/2015.
 */
public class MessageHelper {

    public static void broadcastMessage(Plugin plugin, String message) {

        if (!(plugin instanceof McHorse)) {
            return;
        }

        McHorse mcHorsePlugin = (McHorse) plugin;
        HashMap<String, Player> playerHashMap = mcHorsePlugin.getOnlinePlayers();

        for (Player player : playerHashMap.values()) {
            pluginMessage(player, message);
        }
    }

    public static void pluginMessage(Player targetPlayer, String message) {
        targetPlayer.sendMessage(Constants.MESSAGE_PREFIX + message);
    }
}
