package com.fernandobarillas.mchorse.helpers;

import com.fernandobarillas.mchorse.Constants;
import com.fernandobarillas.mchorse.McHorse;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * A class that handles scoring and turns for a game of Horse.
 *
 * Created by fb on 3/26/15.
 */
public class PlayerQueue {

    public static final int INVALID_SCORE = -1;

    private boolean sGameIsStarted = false;

    private int mMaxScore;

    private int mMinimumRequiredPlayers;

    private ArrayList<HorsePlayer> mQueuedPlayers;

    private int mCurrentPlayer;

    private McHorse mPlugin;

    public PlayerQueue(McHorse plugin) {
        mQueuedPlayers = new ArrayList<>();
        mCurrentPlayer = 0;
        mMinimumRequiredPlayers = 2; // Need at least this many players to play a game of horse
        mPlugin = plugin;
        mMaxScore = mPlugin.getHorseWord().length();
    }

    public void addPlayer(Player player) {
        // Don't add offline players
        if (!player.isOnline()) {
            return;
        }

        HorsePlayer horsePlayer = new HorsePlayer(player);
        if (!isInQueue(horsePlayer)) {
            mQueuedPlayers.add(horsePlayer);
        }
    }

    public int getPlayerCount() {
        return mQueuedPlayers.size();
    }

    public String getPlayerScoreString(Player player) {
        HorsePlayer horsePlayer = new HorsePlayer(player);

        if (isInQueue(horsePlayer)) {
            return getScoreString(getScore(player));
        }

        return "";
    }

    public ArrayList<HorsePlayer> getQueuedPlayers() {
        return mQueuedPlayers;
    }

    public int getScore(Player player) {
        int score = INVALID_SCORE;
        HorsePlayer horsePlayer = new HorsePlayer(player);

        if (isInQueue(horsePlayer)) {
            score = mQueuedPlayers.get(mQueuedPlayers.indexOf(horsePlayer)).getScore();
        }

        return score;
    }

    private String getScoreString(int score) {
        String horseWord = mPlugin.getHorseWord();

        if (score != INVALID_SCORE && score > 0) {
            if (score >= horseWord.length()) {
                // The player's score is higher than the length of the word, just return the word to avoid an index out
                // of bounds exception
                return horseWord;
            }

            return horseWord.substring(0, score);
        }

        return "";
    }

    public String getScoreStrings() {
        String result = "";
        String prefix = "";
        String suffix = "";

        for (HorsePlayer horsePlayer : mQueuedPlayers) {
            if (hasLost(horsePlayer)) {
                // Change the color in chat to signify that the player has lost
                prefix = Constants.LOST_PREFIX;
                suffix = Constants.LOST_SUFFIX;
            }
            result += prefix + horsePlayer.getName() + " (" + getScoreString(horsePlayer.getScore()) + ")" + suffix
                    + "\n";
        }

        return result;
    }

    // TODO: Return player name instead?
    public HorsePlayer getTurn() {
        // TODO: Make the minimum players to 2 here?
        if (mQueuedPlayers.size() < mMinimumRequiredPlayers) {
            return null;
        }

        if (mCurrentPlayer > mQueuedPlayers.size()) {
            mCurrentPlayer = 0;
        }

        return mQueuedPlayers.get(mCurrentPlayer);
    }

    public boolean hasLost(Player player) {
        return hasLost(new HorsePlayer(player));
    }

    public boolean hasLost(HorsePlayer horsePlayer) {
        if (isInQueue(horsePlayer)) {
            int playerScore = mQueuedPlayers.get(mQueuedPlayers.indexOf(horsePlayer)).getScore();
            return playerScore >= mMaxScore;
        }

        return true;
    }

    // TODO: Add decrement score in case of scoring errors, has to be done by hand
    public void incrementScore(Player player) {
        HorsePlayer horsePlayer = new HorsePlayer(player);

        if (isInQueue(horsePlayer)) {
            mQueuedPlayers.get(mQueuedPlayers.indexOf(horsePlayer)).incrementScore();
        }
    }

    public boolean isInQueue(HorsePlayer horsePlayer) {
        return mQueuedPlayers.contains(horsePlayer);
    }

    public boolean playersAreInQueue() {
        return mQueuedPlayers.size() > 0;
    }

    public void removePlayer(Player player) {
        HorsePlayer horsePlayer = new HorsePlayer(player);
        mQueuedPlayers.remove(horsePlayer);
    }

    public boolean startQueue() {
        if (mQueuedPlayers.size() > mMinimumRequiredPlayers) {
            sGameIsStarted = true;
        }

        return sGameIsStarted;
    }

    public void stopQueue() {
        sGameIsStarted = false;
    }

    @Override
    public String toString() {
        if (mQueuedPlayers.size() < 1) {
            return "There are no players in the queue";
        }

        String result = "";
        for (HorsePlayer player : mQueuedPlayers) {
            result += player.getName() + " (" + player.getScore() + ") ";
        }

        return "Queued players: " + result;
    }

    public boolean validateTurn(Player player) {
        if (!playersAreInQueue()) {
            return false;
        }

        HorsePlayer horsePlayer = new HorsePlayer(player);
        return getTurn().equals(horsePlayer) && !hasLost(player);
    }
}
