package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class ValueChecker {

    boolean doesPlayerHave21(Player player) {
        if(!hasPlayerBust(player)) {
            return player.getHandValue() == 21;
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
        return player.getHandValue() > 21;
    }
}

