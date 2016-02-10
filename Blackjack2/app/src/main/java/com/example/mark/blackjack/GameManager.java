package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class GameManager {

    private Player[] players;
    private Player currentPlayer, dealer;
    private int round, playersGone;
    private CardDealer cardDealer;
    private BetStorage theBank;
    private ValueChecker valueChecker;

    public GameManager() {
        //initialize objects
        players = new Player[4];
        cardDealer = new CardDealer();
        theBank = new BetStorage();
        valueChecker = new ValueChecker();
        round = 0;

        setPlayerInformation();
        startGame();
    }

    private void startGame() {
        playersGone = 0;
        dealCards(); //Ask for bet minimum then set bets through activity class
    }

    private void setStartingPlayer() {
        //Round is tracked at end of each game to move first player to go
        //around the table
        currentPlayer = players[round];
    }

    private void setPlayerInformation() {
        //Could edit this to a for loop and initialize by value
        players[0] = new Player(PlayerType.PLAYER);
        players[1] = new Player(PlayerType.SAFE);
        players[2] = new Player(PlayerType.RISKY);

        for(int i=0;i<players.length - 1;i++) {
            players[i].editMoney(1000.00); //Start each player with 1000 cash, could make this user input
        }

    }

    void setBetMinimum(int min) {
        theBank.setMinBetValue(min); //This comes from user input
    }

    void setBets(double playerBet) {
        //Call this after input from player bet has been taken
        for(int i=0; i<players.length; i++) {
            Player player = players[i];

            if(i==0) {
                theBank.addInitialBet(playerBet, PlayerType.PLAYER); //Bet with user value
            } else {
                theBank.addInitialBet(player.amountToBet(theBank.getMinBetValue()), player.getPlayerType());
            }

            player.editMoney(-playerBet);
        }
    }

    void dealCards() {
        setStartingPlayer(); //Determines next player to start game
        cardDealer.shuffleDeck();
        for(int i=0; i<players.length; i++) {
            players[i].addCard(cardDealer.drawCard());
            players[i].addCard(cardDealer.drawCard());
        }
        dealer.addCard(cardDealer.drawCard());
        dealer.addCard(cardDealer.drawCard());
    }

    void Hit() {
        currentPlayer.addCard(cardDealer.drawCard()); //Need to add checks for bust or 21 here or in activity
    }

    void Stay() {
        playersGone++; //Another player has completed their turn
        if(playersGone < players.length) {
            nextCurrentPlayer(); //Another player needs to go before the dealer
        } else {
            startAITurn(dealer);
        }

        if(currentPlayer.getPlayerType() != PlayerType.PLAYER) {
            startAITurn(currentPlayer); //Need to grey out controls unless player is current player
        }
    }

    private boolean incrementOrZero(int value) {
        return value++ < players.length;
    }

    private void nextCurrentPlayer() {
        int currentPlayervalue = currentPlayer.getPlayerType().getValue();

        //Determine if need to loop round to first player in array
        //Last player to go is not always last player in the array
        if(incrementOrZero(currentPlayervalue)) {
            currentPlayer = players[currentPlayervalue++];
        } else {
            currentPlayer = players[0];
        }
    }

    private void startAITurn(Player player) {
        if(player.getPlayerType() == PlayerType.PLAYER) {
            //Display error - throw exception?
        }

        boolean turnOver = false;

        while(turnOver == false) {
            if (player.wouldAceIncreaseEndTurn()) {
                player.increaseAce();
                turnOver = true;
            } else {
                if (player.shouldPlayerDrawCard(player.getHandValue())) {
                    player.addCard(cardDealer.drawCard());
                }
                if (hasPlayerBust(player)) {
                    player.setLastResult(ResultType.LOSS);
                    turnOver = true;
                } else {
                    if (doesPlayerHave21(player)) {
                        player.setLastResult(ResultType.BLACKJACK);
                        turnOver = true;
                    }
                }

            }

        }
        Stay();
    }

    void startScorer() {
        //No concern if a dealer wins or loses beyond the initial blackjack/bust confirmation
        //as the dealer's money doesn't change.
        for(int i=0; i<players.length; i++) {
            if(players[i].getLastResult() == ResultType.UNDECIDED) {
                players[i].setLastResult(Scorer.getResult(players[i], players[3])); //Player 4 is the dealer
            }
        }
    }

    void confirmWinnings() {
        for (int i=0; i<3; i++) {
            Player player = players[i];
            double winnings = theBank.calculateWinnings(player.getPlayerType(), player.getLastResult());
            player.editMoney(winnings); //returns nothing if lost, double if won, etc.
        }
    }

    void prepareNewGame() {
        clearHands(); //Empties player's hand and card value
        theBank.clearBets(); //Clears values bet to zero
    }

    private void clearHands() {
        for(Player player: players) {
            player.clearHand();
        }
    }

    boolean doesPlayerHave21(Player player) {
        return valueChecker.doesPlayerHave21(player.getHand());
    }

    boolean hasPlayerBust(Player player) {
        return valueChecker.hasPlayerBust(player.getHand());
    }

    int acesInPlayerHand(Player player) {
        return valueChecker.acesInPlayerHand(player.getHand());
    }

    int[] getPlayerHand(PlayerType playerType) {
        return players[playerType.getValue()].getHand();
    }

    void setLastResult(PlayerType playerType, ResultType result) {
        players[playerType.getValue()].setLastResult(result);
    }
}
