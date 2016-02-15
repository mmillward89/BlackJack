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

    Player[] dealCards() {
        playerManager.setCurrentPlayer(round); //Determines next player to start game
        dealer.shuffleDeck(); //Shuffling prepares deck for next round
        return dealHands();
    }

    private Player[] dealHands() {
        Player[] players = new Player[numberOfPlayers + 1]; //return players and dealer

        for(int i=0; i<numberOfPlayers; i++) {
            players[i] = playerManager.getPlayer(i);
            dealer.dealHand(players[i]);
        }
        players[numberOfPlayers] = playerManager.getDealer();
        dealer.dealHand(players[numberOfPlayers]);
        return players;
    }

    void setBetMinimum(double min) {
        dealer.setMinBetValue(min); //This comes from user input
    }

    double[] setBets(double playerBet) {
        //Call this after input from player bet has been taken

        double[] bets = new double[numberOfPlayers]; //Returned to activity for display
        for(int i=0; i<numberOfPlayers; i++) {
            Player player = playerManager.getPlayer(i);

            if(i==0) {
                dealer.addInitialBet(playerBet, PlayerType.PLAYER); //Bet with user value
                bets[i] = playerBet;
            } else {
                //Gets the player's amount to bet by providing the min bet value and player type
                //then adds this to the bank
                double amountToBet = player.amountToBet(dealer.getMinBetValue());
                dealer.addInitialBet(amountToBet, player.getPlayerType());
                bets[i] = amountToBet;
            }

            player.editMoney(-playerBet);
        }
        return bets;
    }

    Player getCurrentPlayer() {
        return playerManager.getCurrentPlayer();
    }

    int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    Player hit() {
        Player player = playerManager.getCurrentPlayer();
        player.addCard(dealer.drawCard());

        if(hasPlayerBust(player)) {
            player.setLastResult(ResultType.LOSS);
            player.setResultBeforeDealer();
        }

        if(doesPlayerHave21(player)) {
            player.setLastResult(ResultType.BLACKJACK);
            player.setResultBeforeDealer();
        }

        return player;
    }

    void stay() {
		playersGone++;
		if(nextPlayer()) {
			playerManager.nextCurrentPlayer(incrementOrZero(playerManager.getCurrentPlayerValue()));
		}
    }

    boolean nextPlayer() {
        return playersGone < numberOfPlayers;
    }

    private boolean incrementOrZero(int value) {
        return value++ < numberOfPlayers;
    }

    void startAITurn(Player player) {
        if(player.getPlayerType() == PlayerType.PLAYER) {
            //Display error - throw exception?
        }

        boolean turnOver = false; boolean resultConfirmed = false; boolean aceIncreased = false;

        while(turnOver == false) {
            if (player.wouldAceIncreaseEndTurn()) {
                //No need to hit if increasing ace places hand value above stopping limit (17 most likely)
                //This works on initial hand draw or after subsequent hits
                player.increaseAce(); turnOver = true; aceIncreased = true;
            } else {
                if (player.shouldPlayerDrawCard(player.getHandValue())) {
                    hit(); //Draws card and confirms if bust/blackjack
                    if(player.getResultBeforeDealer()) {
                        resultConfirmed = true; turnOver = true;
                    }
                } else {
                    turnOver = true; //Player has drawn all cards needed
                }
            }
            if(aceIncreased && doesPlayerHave21(player)) {
                //Need to check if an increased ace leads to blackjack
                player.setLastResult(ResultType.BLACKJACK);
                resultConfirmed = true; turnOver = true;
                player.setResultBeforeDealer();
            }

        }

        if(resultConfirmed == false) {
            player.setLastResult(ResultType.UNDECIDED);
        }

        stay();
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
