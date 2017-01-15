package com.datarockets.mnchkn.store;

import com.datarockets.mnchkn.data.models.Player;

import java.util.ArrayList;

public interface PlayerService {
    /**
     * An action to add a new player to the list
     *
     * @param name - the name of the player
     * @return Updated players list
     */
    Player addPlayer(String name);

    /**
     * An action to delete player
     *
     * @param position - position of the player in the list
     * @param id - id of the player in the database
     * @return Return position of the player the list which should be deleted
     */
    int deletePlayer(int position, long id);

    /**
     * An action to update player in the list
     *
     * @param player - updated player
     * @return Updated player item to list
     */
    Player updatePlayer(Player player);

    /**
     * An action to clear players stats
     *
     */
    void clearPlayersStats();

    ArrayList<Player> getPlayersList();
    /**
     * An action to get players by id, score, strength or total sum of scores
     *
     * @param orderValue - type of data to sort
     * @return players list
     */
    ArrayList<Player> getPlayersList(int orderValue);

}
