package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class BetStorage {
    private double max, min;
    private double[] valuesBet;

    BetStorage() {
        min = 0.0;
        valuesBet = new double[4];
    }

    void setMinBetValue(double min) {
        this.min = min;
        max = min * 20;
    }

    double getMinBetValue() {
        return min;
    }

    double getMaxBetValue() {
        return max;
    }

    double getPlayerBet(PlayerType player) {
        return valuesBet[player.getValue()];
    }

    void addInitialBet(double amountToAdd, PlayerType player) {
        //Store value based on player type's number for consistency
        valuesBet[player.getValue()] = amountToAdd;
    }

    double calculateWinnings(PlayerType player, ResultType result) {
        return getPlayerBet(player) * result.getValue(); //E.g. a win multiplies value bet by 2
    }

    void clearBets() {
        for(Double bet: valuesBet) {
            bet = 0.0;
        }
    }
}
