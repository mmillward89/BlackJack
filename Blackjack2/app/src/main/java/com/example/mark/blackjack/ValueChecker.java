package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class ValueChecker {

    int getHandValue(int[] hand) {
        int total = 0;

        for(int i=0; i<hand.length; i++) {
            if(hand[i] == 0) {
                i = hand.length - 1;
            } else {
                total += hand[i]; //add value to total
            }
        }

        return total;
    }

    boolean doesPlayerHave21(int[] hand) {
        int handValue = getHandValue(hand);
        if(!hasPlayerBust(hand)) {
            return handValue == 21;
        }
        return false;
    }

    int acesInPlayerHand(int[] hand) {
        int aces = 0;

        for(int i=0; i<hand.length; i++) {
            if(hand[i] == 0) {
                i = hand.length - 1;
            } else if(hand[i] == 1) {
                aces++;
            }
        }

        return aces;
    }

    boolean hasPlayerBust(int[] hand) {
        return getHandValue(hand) > 21;
    }
}

