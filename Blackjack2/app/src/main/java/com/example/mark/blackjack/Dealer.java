package com.example.mark.blackjack;

/**
 * Created by Mark on 10/02/2016.
 */
class Dealer {
    private CardDealer cardDealer;
    private BetStorage theBank;

    public Dealer() {
        cardDealer = new CardDealer();
        theBank = new BetStorage();
    }

    void shuffleDeck() {
        cardDealer.shuffleDeck();
    }

    int drawCard() {
        return cardDealer.drawCard();
    }

    void dealHand(Player player) {
        player.addCard(cardDealer.drawCard());
        player.addCard(cardDealer.drawCard());
    }

    void setMinBetValue(double i) {
        theBank.setMinBetValue(i);
    }

    void addInitialBet(double amountToAdd, PlayerType playerType) {
        theBank.addInitialBet(amountToAdd, playerType);
    }

    void clearBets() {
        theBank.clearBets();
    }

    double calculateWinnings(PlayerType playerType, ResultType resultType) {
        return theBank.calculateWinnings(playerType, resultType);
    }

    double getMinBetValue() {
        return theBank.getMinBetValue();
    }
}
