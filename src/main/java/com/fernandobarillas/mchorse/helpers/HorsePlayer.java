package com.fernandobarillas.mchorse.helpers;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by fb on 3/26/15.
 */
public class HorsePlayer {

    private Player mPlayer;

    private int mScore;

    public HorsePlayer(Player player) {
        mPlayer = player;
        mScore = 0;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof HorsePlayer) {
            HorsePlayer otherHorsePlayer = (HorsePlayer) object;
            return this.getUniqueId().equals(otherHorsePlayer.getUniqueId());
        }

        return false;
    }

    public String getName() {
        return mPlayer.getName();
    }

    public int getScore() {
        return mScore;
    }

    public UUID getUniqueId() {
        return mPlayer.getUniqueId();
    }

    public void incrementScore() {
        mScore++;
    }

    public void resetScore() {
        mScore = 0;
    }

}
