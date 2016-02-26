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
        playerManager = new PlayerManager(3);
		playerManager.initializePlayerMoney(1000.00);
        numberOfPlayers = playerManager.getNumberofPlayers();
        dealer = new Dealer(); //Deals cards and manages bank
        valueChecker = new ValueChecker(); //Used to analyze player cards
        round = 0; //Used so first player changes after each round
    }

    Player[] dealCards() {
        playersGone = 0; 
		playerManager.setCurrentPlayer(round); //Determines next player to start game
        dealer.shuffleDeck(); //Shuffling prepares deck for next round (might need to fix to avoid reinitializing each time)
        return dealHands();
    }

    private Player[] dealHands() {
        Player[] players = playerManager.getPlayers(); //return players and dealer

        for(int i=0; i<numberOfPlayers; i++) {
            dealer.dealHand(players[i]);
        }
		//Seperate Player object represents dealer cards, Dealer class represents dealing cards and managing money
		//Switch to Dealer class exclusively?
        Player dealer1 = playerManager.getDealer();
        dealer.dealHand(dealer1);
        return players;
    }

    void setBetMinimum(double min) {
        dealer.setMinBetValue(min); //This comes from user input
    }

    Player[] setBets(double amountToBet) {
		Player[] players = playerManager.getPlayers();
        for(int i=0; i<numberOfPlayers; i++) {
			Player player = players[i];
            if(i!=0) {
				amountToBet = player.amountToBet(dealer.getMinBetValue()); //Get AI value to bet
			} 
            dealer.addInitialBet(amountToBet, player.getPlayerType());
			player.editMoney(-amountToBet); player.setLastBet(amountToBet);
        }
        return players;
    }
	
	Player[] getPlayers() {
		return playerManager.getPlayers();
	}

    Player getCurrentPlayer() {
        return playerManager.getCurrentPlayer();
    }
	
	Player getDealer() {
		return playerManager.getDealer();
	}

    int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    Player hit() {
        Player player = playerManager.getCurrentPlayer();
        player.addCard(dealer.drawCard());

		//setResultBeforeDealer() confirms a result was found before the dealer's turn
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
        return playersGone < numberOfPlayers; //pG increments after each player stays
    }

    private boolean incrementOrZero(int value) {
        return value++ < numberOfPlayers; //Confirms if all players have gone before dealer
    }

    void startAITurn(Player player) {
		//Method Summary: 
		//a) Starts a loop that checks if increasing an ace to 11 would end the turn positively (17-21 most likely) 
		//b) hits if the player method determines it should, otherwise ending the loop 
		//c) ends the loop if the hit causes bust/blackjack
		//d) checks if an ace was increased and if that lead to blackjack
		//e) sets the player result to undecided if it has yet to be confirmed (to be compared with dealer)
		
        if(player.getPlayerType() == PlayerType.PLAYER) {
            //Display error - throw exception?
        }

        boolean turnOver = false; boolean resultConfirmed = false; boolean aceIncreased = false;

        while(turnOver == false) {
            if (player.wouldAceIncreaseEndTurn()) {
                //Checks if increasing ace to 11 places total card value in 'safe' zone (likely 17+)
                //This works on initial hand draw or after subsequent hits
                player.increaseAce(); turnOver = true; aceIncreased = true;            
			} else {
				if (player.shouldPlayerDrawCard(player.getHandValue())) {
					hit(); //Draws card and confirms if bust/blackjack
                    if(player.getResultBeforeDealer()) {
                        resultConfirmed = true; turnOver = true; //Player has bust/blackjack
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
        return valueChecker.doesPlayerHave21(player);
    }

    boolean hasPlayerBust(Player player) {
        return valueChecker.hasPlayerBust(player);
    }

    Player[] getScores() {
		Player[] players = playerManager.getPlayers();
        Player dealer = playerManager.getDealer();
        
		for(int i=0; i<numberOfPlayers; i++) {
            Player player= players[i];
            if(player.getLastResult() == ResultType.UNDECIDED) {
                player.setLastResult(Scorer.getResult(player, dealer)); 
            }
        }
		
		return players;
    }

    void confirmWinnings() {
		Player[] players = playerManager.getPlayers();
        for (int i=0; i<numberOfPlayers; i++) {
			Player player = players[i];
            double winnings = dealer.calculateWinnings(player.getPlayerType(), player.getLastResult());
            player.editMoney(winnings); //returns nothing if lost, double if won, etc.
        }
    }

    void prepareNewGame() {
        playerManager.clearHands(); //Resets player state, all cards and values removed
        dealer.clearBets(); //Clears values bet to zero
        round = incrementOrZero(round) ? round++ : 0;
    }
}
