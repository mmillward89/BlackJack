package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class GameManager {
    private PlayerManager playerManager;
    private Dealer dealer;
    private int round, playersGone, numberOfPlayers;
    private ValueChecker valueChecker;

    public GameManager() {
        //initialize objects
        playerManager = new PlayerManager();
        numberOfPlayers = playerManager.getNumberofPlayers();
        dealer = new Dealer();
        valueChecker = new ValueChecker();
        round = 0;
        startGame();
    }

    private void startGame() {
        playersGone = 0;
        dealCards(); //Ask for bet minimum then set bets through activity class
    }

    private void dealCards() {
        playerManager.setCurrentPlayer(round); //Determines next player to start game
        dealer.shuffleDeck(); //Shuffling prepares deck for next round
        for(int i=0; i<numberOfPlayers; i++) {
            dealer.dealHand(playerManager.getPlayer(i));
        }
        dealer.dealHand(playerManager.getDealer());
    }

    void setBetMinimum(int min) {
        dealer.setMinBetValue(min); //This comes from user input
    }

    void setBets(double playerBet) {
        //Call this after input from player bet has been taken
        for(int i=0; i<numberOfPlayers; i++) {
            Player player = playerManager.getPlayer(i);

            if(i==0) {
                dealer.addInitialBet(playerBet, PlayerType.PLAYER); //Bet with user value
            } else {
                //Gets the player's amount to bet by providing the min bet value and player type
                //then adds this to the bank
                dealer.addInitialBet(player.amountToBet(dealer.getMinBetValue()), player.getPlayerType());
            }

            player.editMoney(-playerBet);
        }
    }

    void Hit() {
        playerManager.getCurrentPlayer().addCard(dealer.drawCard()); //Need to add checks for bust or 21 here or in activity
    }

    void Stay() {
        playersGone++; //Another player has completed their turn
        if(playersGone < numberOfPlayers) {
            //Need to use I or Z with current player value, bit convoluted
            playerManager.nextCurrentPlayer(incrementOrZero(playerManager.getCurrentPlayerValue()));
            if(playerManager.getCurrentPlayerType() != PlayerType.PLAYER) {
                startAITurn(playerManager.getCurrentPlayer()); //Need to grey out controls unless player is current player
            }
        } else {
            startAITurn(playerManager.getDealer());
            startScorer();
        }
    }

    private boolean incrementOrZero(int value) {
        return value++ < numberOfPlayers;
    }

    private void startAITurn(Player player) {
        if(player.getPlayerType() == PlayerType.PLAYER) {
            //Display error - throw exception?
        }

        boolean turnOver = false; boolean resultConfirmed = false;

        while(turnOver == false) {
            if (player.wouldAceIncreaseEndTurn()) {
                player.increaseAce();
                turnOver = true;
            } else {
                if (player.shouldPlayerDrawCard(player.getHandValue())) {
                    player.addCard(dealer.drawCard());
                } else {
                    turnOver = true; //Player has drawn all cards needed
                }
                if (hasPlayerBust(player)) {
                    player.setLastResult(ResultType.LOSS);
                    resultConfirmed = true; turnOver = true;
                }
            }

            if (resultConfirmed == false && doesPlayerHave21(player)) {
                player.setLastResult(ResultType.BLACKJACK);
                resultConfirmed = true; turnOver = true;
            }

        }
        if(resultConfirmed == false) {
            player.setLastResult(ResultType.UNDECIDED);
        }

        Stay();
    }

    boolean doesPlayerHave21(Player player) {
        return valueChecker.doesPlayerHave21(player.getHand());
    }

    boolean hasPlayerBust(Player player) {
        return valueChecker.hasPlayerBust(player.getHand());
    }

    void startScorer() {
        //No concern if a dealer wins or loses beyond the initial blackjack/bust confirmation
        //as the dealer's money doesn't change.
        Player dealer = playerManager.getDealer();
        for(int i=0; i<numberOfPlayers; i++) {
            Player player= playerManager.getPlayer(i);
            if(player.getLastResult() == ResultType.UNDECIDED) {
                player.setLastResult(Scorer.getResult(player, dealer));
            }
        }

        confirmWinnings();
        //Display Results
    }

    void confirmWinnings() {
        for (int i=0; i<numberOfPlayers; i++) {
            Player player = playerManager.getPlayer(i);
            double winnings = dealer.calculateWinnings(player.getPlayerType(), player.getLastResult());
            player.editMoney(winnings); //returns nothing if lost, double if won, etc.
        }
        prepareNewGame();
    }

    void prepareNewGame() {
        playerManager.clearHands(); //Empties player's hand and card value
        dealer.clearBets(); //Clears values bet to zero
        round = incrementOrZero(round) ? round++ : 0;
        startGame();
    }
}
