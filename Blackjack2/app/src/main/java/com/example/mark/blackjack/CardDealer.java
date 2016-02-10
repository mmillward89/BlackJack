package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class CardDealer {
    private Deck theDeck;

    CardDealer() {
        theDeck = new Deck();
    }

    void shuffleDeck() {
        theDeck.startNewGame(); //Deck needs to be prepared for new match
    }

    int drawCard() {
        return theDeck.drawNewCard();
    }

}
