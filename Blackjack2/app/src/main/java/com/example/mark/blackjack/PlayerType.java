package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

enum PlayerType {
    PLAYER(0), SAFE(1), RISKY(2), DEALER(3);
    private int value;

    private PlayerType(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
