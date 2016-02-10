package com.example.mark.blackjack;

import java.util.Random;

/**
 * Created by Mark on 17/01/2016.
 */

class PlayerBehaviour {
    private Random r;
    private ValueChecker valueChecker;

    PlayerBehaviour() {
        r = new Random();
        valueChecker = new ValueChecker();
    }

    boolean shouldPlayerDrawCard(PlayerType type, int playerValue) {
        //Assume checks for aces, bust and blackjack are done by game manager

        if(playerValue < 10) {
            return true;  //Always hit if player can't go bust
        }

        switch(type) {
            case PLAYER:
                return false; //Shouldn't use this for player

            case SAFE:
                return shouldSafePlayerDrawCard(playerValue);

            case RISKY:
                return shouldRiskyPlayerDrawCard(playerValue);

            case DEALER:
                if(playerValue < 17 ) {
                    return true;
                }
                return false;
        }

        return false;
    }

    private boolean shouldRiskyPlayerDrawCard(int playerValue) {
        if(playerValue < 13 && (r.nextInt(8) < 6)) {
            return true; //75% hit rate on a low hand
        }

        if(playerValue < 17 && (r.nextInt(10) < 5)) {
            return true; //50% hit rate on a middling hand
        }

        return false;
    }

    private boolean shouldSafePlayerDrawCard(int playerValue) {
        if(playerValue < 13 && (r.nextInt(6) < 4)) {
            return true; //66% hit rate on a low hand
        }

        if(playerValue < 17 && (r.nextInt(6) < 2)) {
            return true; //33% hit rate on a middling hand
        }

        return false;
    }

    boolean shouldAceCountAsEleven(int handValue) {
        return handValue + 10 > 21;
    }

    double shouldPlayerBet(PlayerType type, ResultType lastResult, double min) {
        switch(type) {
            case PLAYER:
                return 0.0; //Shouldn't use this with player

            case SAFE:
                return shouldSafePlayerBet(min, didPlayerWinLast(lastResult));

            case RISKY:
                return shouldRiskyPlayerBet(min, didPlayerWinLast(lastResult));

            case DEALER:
                return 0.0; //Dealer doesn't bet
        }

        return min;//Shouldn't get here but just in case
    }

    private double shouldSafePlayerBet(double min, boolean playerWonPreviously) {
        int i = r.nextInt(10);

        if(playerWonPreviously) {

            if(i<4) {
                return min;
            } else if(i<7) {
                return min * 1.5;
            } else if(i<9) {
                return min * 2.5;
            } else if(i<10) {
                return min * 5.0;
            }

        } else {
            if(i<4) {
                return min;
            } else if(i<7) {
                return min * 1.5;
            } else if(i<9) {
                return min * 2.0;
            }
        }
        return min;
    }

    private double shouldRiskyPlayerBet(double min, boolean playerWonPreviously) {
        int i = r.nextInt(10);

        if(playerWonPreviously) {
            if(i<2) {
                return min;
            } else if(i<5) {
                return min * 2.0;
            } else if(i<8) {
                return min * 3.5;
            } else if(i<10) {
                return min * 6.0;
            }
        } else {
            if(i<3) {
                return min;
            } else if(i<5) {
                return min * 2.0;
            } else if(i<8) {
                return min * 2.5;
            } else if(i<10) {
                return min * 4.5;
            }
        }

        return min;
    }


    private boolean didPlayerWinLast(ResultType lastResult) {
        if(lastResult.getValue() > 1.0) {
            return true;
        }
        return false;
    }
}
