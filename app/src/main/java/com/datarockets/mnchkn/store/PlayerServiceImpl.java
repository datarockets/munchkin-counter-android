package com.datarockets.mnchkn.store;

import com.datarockets.mnchkn.models.Player;
import com.datarockets.mnchkn.utils.ColorUtil;

import java.util.ArrayList;

public class PlayerServiceImpl implements PlayerService {

    public static final String TAG = "PlayerServiceImpl";

    MunchkinDatabaseHelper mPlayerDatabase;

    @Override
    public Player addPlayer(String name) {
        Player player = new Player();
        player.setName(name);
        player.setLevelScore(1);
        player.setStrengthScore(1);
        player.setColor(ColorUtil.generatePlayerAvatarColor());
        long id = mPlayerDatabase.addPlayer(player);
        player.setId(id);
        return player;
    }

    @Override
    public int deletePlayer(int position, long id) {
        mPlayerDatabase.deletePlayer(id);
        return position;
    }

    @Override
    public Player updatePlayer(Player player) {
        return mPlayerDatabase.updatePlayer(player);
    }

    @Override
    public void clearPlayersStats() {
        mPlayerDatabase.clearPlayersStats();
    }

    @Override
    public ArrayList<Player> getPlayersList() {
        return mPlayerDatabase.getPlayers();
    }

    @Override
    public ArrayList<Player> getPlayersList(int orderValue) {
        return mPlayerDatabase.getPlayers(orderValue);
    }

}
