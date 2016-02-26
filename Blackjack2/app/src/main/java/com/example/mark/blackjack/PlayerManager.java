package com.example.mark.blackjack;

/**
 * Created by Mark on 10/02/2016.
 */
class PlayerManager {
    private Player[] players;
    private Player currentPlayer, dealer;

    public PlayerManager(int numberOfPlayers) {
        players = new Player[numberOfPlayers]; //Can change this if adding more players

        //Could edit this to a for loop and initialize by value
        players[0] = new Player(PlayerType.PLAYER);
        players[1] = new Player(PlayerType.SAFE);
        players[2] = new Player(PlayerType.RISKY);
        dealer = new Player(PlayerType.DEALER);
    }
	
	void initializePlayerMoney(double money) {
		for(int i=0;i<players.length - 1;i++) {
            players[i].editMoney(money); //Start each player with 1000 cash, could make this user input
        }
	}

    int getNumberofPlayers() {
        return players.length;
    }
	
	Player[] getPlayers() {
		return players;
	}

    Player getPlayer(int playerNumber) {
        return players[playerNumber];
    }

    Player getDealer() {
        return dealer;
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    PlayerType getCurrentPlayerType() {
        return currentPlayer.getPlayerType();
    }

    int getCurrentPlayerValue() {
        return currentPlayer.getPlayerType().getValue();
    }

    void setCurrentPlayer(int playerNumber) {
        currentPlayer = players[playerNumber];
    }

    void nextCurrentPlayer(boolean increment) {
        if(increment) {
            setCurrentPlayer(getCurrentPlayerValue());
        } else {
            setCurrentPlayer(0);
        }
    }

    void clearHands() {
        for(Player player: players) {
            player.clearHand();
        }
        dealer.clearHand();
    }
}
