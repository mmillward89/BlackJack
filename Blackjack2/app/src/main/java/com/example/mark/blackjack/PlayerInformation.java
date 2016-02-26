package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class PlayerInformation {

    private int[] playerHand;
    private int playerValue, nextFreeHandSpace;
    private double playerMoney, lastBet;
    private PlayerType type;
    private boolean resultBeforeDealer;

    PlayerInformation(PlayerType type) {
        playerValue = 0; nextFreeHandSpace = 0;
        playerMoney = 0.0; lastBet = 0.0;
        playerHand = new int[12]; //12 used as it's the maximum amount a hand can be without going bust (very unlikely though)
        this.type = type;
        resultBeforeDealer = false;
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
        return false; //Shouldn't get here
    }

    int[] getHand() {
        return playerHand;
    }
	
	void setLastBet(double bet) {
		lastBet = bet;
	}
	
	double getLastBet() {
		return lastBet;
	}

    int getNextFreeHandSpace() {
        return nextFreeHandSpace;
    }

    boolean getResultBeforeDealer() {
        return resultBeforeDealer;
    }

    void setResultBeforeDealer() {
        resultBeforeDealer = true;
    }

    void clearHand() {
        for(int i=0; i<playerHand.length; i++) {
            playerHand[i] = 0;
        }
        playerValue = 0; nextFreeHandSpace = 0;
        resultBeforeDealer = false;
    }

    void increaseAce() {
        boolean aceIncreased = false;
        int i = 0;

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
