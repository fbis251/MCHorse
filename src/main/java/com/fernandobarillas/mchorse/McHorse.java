package com.fernandobarillas.mchorse;

import com.fernandobarillas.mchorse.executors.McHorseCommandExecutor;
import com.fernandobarillas.mchorse.helpers.PlayerQueue;
import com.fernandobarillas.mchorse.listeners.McHorsePlayerListener;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class McHorse extends JavaPlugin {

    private PlayerQueue mPlayerQueue;

    private HashMap<String, Player> mPlayerMap;

    private McHorsePlayerListener mPlayerListener = new McHorsePlayerListener(this);

    private String mHorseWord;

    /**
     * Gets the word that the plugin is using to keep score. This is the word "Horse" by default.
     *
     * @return The word that the plugin is using to keep score.
     */
    public String getHorseWord() {
        return mHorseWord;
    }

    /**
     * Allows players to customize the word to use in a game of Horse, even though traditionally "Horse" is used.
     *
     * @param horseWord The new word to use for scoring. Must have a length of 1 or greater
     */
    public void setHorseWord(String horseWord) {
        if (horseWord.length() > 0) {
            mHorseWord = horseWord;
        }
    }

    /**
     * Gets the currently online players.
     */
    public HashMap<String, Player> getOnlinePlayers() {
        mPlayerMap = new HashMap<String, Player>();

        for (Player player : getServer().getOnlinePlayers()) {
            mPlayerMap.put(player.getName(), player);
        }

        return mPlayerMap;
    }

    /**
     * Gets the players currently added to the plugin's game queue. Players added to this queue
     */
    public PlayerQueue getPlayerQueue() {
        return mPlayerQueue;
    }

    @Override
    public void onDisable() {
        mPlayerMap = null;
        mPlayerQueue = null;
    }

    @Override
    public void onEnable() {
        mHorseWord = Constants.HORSE_WORD;
        mPlayerQueue = new PlayerQueue(this);

        getCommand("horse").setExecutor(new McHorseCommandExecutor(this));
        getCommand("h").setExecutor(new McHorseCommandExecutor(this));

        // Register event handlers
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(mPlayerListener, this);
    }
}
