package com.datarockets.mnchkn.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.datarockets.mnchkn.data.models.GameStep;
import com.datarockets.mnchkn.data.models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServiceImpl implements GameService {

    private static final String IS_GAME_STARTED = "is_game_started";


    MunchkinDatabaseHelper mDatabase;

    SharedPreferences mPreferences;

    SharedPreferences.Editor mPreferencesEditor;

    private Map<Player, List<GameStep>> mGameStepsMap;

    public GameServiceImpl(Context context) {
    }

    @Override
    public void insertStep(Player player) {
        GameStep gameStep = new GameStep();
        gameStep.setPlayerId(player.getId());
        gameStep.setPlayerLevel(player.getLevelScore());
        gameStep.setPlayerStrength(player.getStrengthScore());
        mDatabase.insertStep(gameStep);
    }

    @Override
    public void createPlayerIdGameStepsMap() {
        List<Player> playersList = mDatabase.getPlayers();
        List<GameStep> gameSteps = mDatabase.getGameSteps();

        Map<Player, List<GameStep>> playerGameStepsMap = new HashMap<>();
        for (Player player : playersList) {
            List<GameStep> playerSteps = new ArrayList<>();
            for (GameStep step : gameSteps) {
                if (step.getPlayerId() == player.getId()) {
                    playerSteps.add(step);
                }
            }
            playerGameStepsMap.put(player, playerSteps);
        }
        mGameStepsMap = playerGameStepsMap;
    }

    @Override
    public Map<Player, List<GameStep>> getScoresChartData() {
        return mGameStepsMap;
    }

//    @Override
//    public LineChartData createScoresChartData(int type, Map<Player, List<GameStep>> gameStepsMap) {
//        List<Line> playersLines = new ArrayList<>();
//        List<String> playerColors = new ArrayList<>();
//        LineChartData lineChartData;
//
//        for (Player player : gameStepsMap.keySet()) {
//            String color = player.getColor();
//            playerColors.add(color);
//        }
//
//        for (List<GameStep> gameSteps : gameStepsMap.values()) {
//            List<PointValue> pointValues = new ArrayList<>();
//            for (int i = 0; i < gameSteps.size(); i++) {
//                switch (type) {
//                    case 0:
//                        int playerLevel = gameSteps.get(i).getPlayerLevel();
//                        pointValues.add(new PointValue(i, playerLevel));
//                        break;
//                    case 1:
//                        int playerStrength = gameSteps.get(i).getPlayerStrength();
//                        pointValues.add(new PointValue(i, playerStrength));
//                        break;
//                    case 2:
//                        int playerTotal = gameSteps.get(i).getPlayerLevel() +
//                                gameSteps.get(i).getPlayerStrength();
//                        pointValues.add(new PointValue(i, playerTotal));
//                        break;
//                }
//            }
//            Line line = new Line();
//            line.setValues(pointValues);
//            line.setHasPoints(false);
//            line.setCubic(true);
//            playersLines.add(line);
//        }
//
//        for (int i = 0; i < playersLines.size(); i++) {
//            playersLines.get(i).setColor(Color.parseColor(playerColors.get(i)));
//        }
//
//        lineChartData = new LineChartData(playersLines);
//
//        return lineChartData;
//    }

    @Override
    public void clearSteps() {
        mDatabase.clearSteps();
    }

    @Override
    public void setGameStatus(boolean status) {
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putBoolean(IS_GAME_STARTED, status);
        mPreferencesEditor.apply();
    }

    public boolean isGameStarted() {
        return mPreferences.getBoolean(IS_GAME_STARTED, false);
    }

}
