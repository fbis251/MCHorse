package com.fernandobarillas.mchorse;

import com.fernandobarillas.mchorse.listeners.McHorsePlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class McHorse extends JavaPlugin {

    private HashMap<String, Player> mPlayerMap;
    private McHorsePlayerListener mPlayerListener = new McHorsePlayerListener(this);

    public HashMap<String, Player> getOnlinePlayers() {
        mPlayerMap = new HashMap<String, Player>();

        for (Player player : getServer().getOnlinePlayers()) {
            mPlayerMap.put(player.getName(), player);
        }

        return mPlayerMap;
    }

    @Override
    public void onDisable() {
        mPlayerMap = null;
    }

    @Override
    public void onEnable() {
        getOnlinePlayers();
        getLogger().info("Online Players:");
        for (Player player : mPlayerMap.values()) {
            getLogger().info(player.getName());
        }

        getCommand("horse").setExecutor(new McHorseCommandExecutor(this));
        getCommand("h").setExecutor(new McHorseCommandExecutor(this));

        // Register event handlers
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(mPlayerListener, this);
    }
}
