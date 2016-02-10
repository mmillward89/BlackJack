package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */
enum ResultType {
    WIN(2.0), LOSS(0.0), BLACKJACK(2.5), PUSH(1.0), UNDECIDED(1.0);
    private double value;

    private ResultType(double value) {
        this.value = value;
    }

    double getValue() {
        return value;
    }
}
