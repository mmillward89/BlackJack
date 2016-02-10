package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class PlayerInformation {

    private int[] playerHand;
    private int playerValue, nextFreeHandSpace;
    private double playerMoney;
    private PlayerType type;

    PlayerInformation(PlayerType type) {
        playerValue = 0; nextFreeHandSpace = 0;
        playerMoney = 0.0;
        playerHand = new int[12];
        this.type = type;
    }

    int getPlayerValue() {
        return playerValue; //Represents the player's total hand value
    }

    PlayerType getPlayerType() {
        return type;
    }

    void addCard(int newCard) {
        playerHand[nextFreeHandSpace] = newCard;
        nextFreeHandSpace++;
        playerValue += newCard;
    }

    boolean playerHasAce() {
        for(int i=0; i< playerHand.length; i++) {
            if(playerHand[i] == 1) {
                return true; //1 represents an ace
            } else if (playerHand[i] == 0) {
                return false; //No more cards to examine
            }
        }
        return false; //If we get here player definitely doesn't have an ace
    }

    int[] getHand() {
        return playerHand;
    }

    void clearHand() {
        for(int i=0; i<playerHand.length; i++) {
            playerHand[i] = 0;
        }
        playerValue = 0;
        nextFreeHandSpace = 0;
    }

    void increaseAce() {
        boolean aceIncreased = false;
        int i =0;

        while(aceIncreased == false) {
            if(playerHand[i] == 1) {
                playerHand[i] += 10; playerValue += 10;
                aceIncreased = true;
            } else {
                i++;
            }
        }
    }

    void editMoney(double moneyToAdd) {
        playerMoney += moneyToAdd;
    }
}
