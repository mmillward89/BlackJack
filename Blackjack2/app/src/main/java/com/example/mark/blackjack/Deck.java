package com.example.mark.blackjack;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mark on 17/01/2016.
 */
class Deck {

    private int[] hearts, diamonds, spades, clubs;
    private ArrayList<int[]> cards;

    Deck() {
        startNewGame();
    }

    void startNewGame() {
        //Seperate method used to ensure there are no zeroes (cards considered in play)
        //when starting new game. Might need to reconfigure if initializing each time
        //is bad idea
        diamonds = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};
        spades = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};
        clubs = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};

        cards = new ArrayList<int[]>();
        cards.add(hearts);
        cards.add(diamonds);
        cards.add(spades);
        cards.add(clubs);
    }

    int drawNewCard() {
        //pick a random suit
        Random r = new Random();
        int randomSuit = r.nextInt(4);
        int[] randomSuitAvailableCards = cards.get(randomSuit);

        //draw a random card from the deck
        int cardNumber = r.nextInt(13);
        int card = randomSuitAvailableCards[cardNumber];

        //keep redrawing while card is zero (already drawn)
        while(card == 0) {
            cardNumber = r.nextInt(13);
            card = randomSuitAvailableCards[cardNumber];
        }

        //Replace card with zero so it can't be chosen again
        randomSuitAvailableCards[cardNumber] = 0;

        return card;
    }
}

