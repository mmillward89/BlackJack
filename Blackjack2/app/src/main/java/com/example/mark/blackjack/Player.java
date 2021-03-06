package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class Player {
    private PlayerInformation information;
    private PlayerBehaviour behaviour;
    private ResultType lastResult;

    Player(PlayerType playerType) {
        information = new PlayerInformation(playerType);
        behaviour = new PlayerBehaviour();
        lastResult = ResultType.PUSH; //Initialize to 'push' as it's an equal return
    }

    PlayerType getPlayerType() {
        return information.getPlayerType();
    }

    void editMoney(double money) {
        information.editMoney(money);
    }
	
	void setLastBet(double bet) {
		information.setLastBet(bet);
	}
	
	double getLastBet() {
		return information.getLastBet();
	}

    void addCard(int card) {
        information.addCard(card);
    }

    void setLastResult(ResultType lastResult) {
        this.lastResult = lastResult;
    }

    ResultType getLastResult() {
        return lastResult;
    }

    double amountToBet(double min) {
        return behaviour.shouldPlayerBet(information.getPlayerType(), lastResult, min);
    }

    boolean shouldPlayerDrawCard(int value) {
        return behaviour.shouldPlayerDrawCard(information.getPlayerType(), value);
    }

    int[] getHand() {
        return information.getHand();
    }

    int getHandSize() {
		//getHand().length returns 12, need to know how many cards are in hand
		//rather than the size of array
        return information.getNextFreeHandSpace() - 1;
    }

    void clearHand() {
        information.clearHand();
    }

    int getHandValue() {
        return information.getPlayerValue();
    }

    void increaseAce() {
        information.increaseAce();
    }

    void setResultBeforeDealer() {
        information.setResultBeforeDealer();
    }


    boolean getResultBeforeDealer() {
        return information.getResultBeforeDealer();
    }

    boolean wouldAceIncreaseEndTurn() {
        if(shouldAceIncrease()) {
            int tempValue = information.getPlayerValue() + 10; //Check if increase would work before permanently increasing value
            return !shouldPlayerDrawCard(tempValue); //Should player draw card with increased ace value
        }
        return false; //Player doesn't have ace/shouldn't increase ace
    }

    private boolean shouldAceIncrease() {
        if(information.playerHasAce()) {
            return (information.getPlayerValue() + 10) > 21; //false if increase would cause bust
        }
        return false; //Player doesn't have ace
    }
}
