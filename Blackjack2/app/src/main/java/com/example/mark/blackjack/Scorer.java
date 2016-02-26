package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class Scorer {

    public static final ResultType getResult(Player player, Player dealer) {
        ResultType playerResult = player.getLastResult();
        ResultType dealerResult = dealer.getLastResult();

		//If dealer has won or lost player has automatically done the opposite.
		//Player only comes here if their result is undecided, so they can't both have blackjack or bust here
        if(dealerResult == ResultType.BLACKJACK) {
            playerResult = ResultType.LOSS;
        } else if (dealerResult == ResultType.LOSS) {
            playerResult = ResultType.WIN;
        } else if(dealerResult == ResultType.UNDECIDED){
            int result = player.getHandValue() - dealer.getHandValue();

			//Result is simply who has more now
            if(result>0) {
                playerResult = ResultType.WIN;	
            } else if (result<0) {
                playerResult = ResultType.LOSS;
            } else {
                playerResult = ResultType.PUSH;
            }
        }
        return playerResult;
    }

}
