package com.example.mark.blackjack;

/**
 * Created by Mark on 17/01/2016.
 */

class Scorer {

    public static final ResultType getResult(Player player, Player dealer) {
        ResultType playerResult = player.getLastResult();
        ResultType dealerResult = dealer.getLastResult();

        if(dealerResult == ResultType.BLACKJACK) {
            playerResult = ResultType.LOSS; //If the player had blackjack or bust they wouldn't be here
        } else if (dealerResult == ResultType.LOSS) {
            playerResult = ResultType.WIN;
        } else if(dealerResult == ResultType.UNDECIDED){
            int result = player.getHandValue() - dealer.getHandValue();

            if(result>0) {
                playerResult = ResultType.WIN;	//Result is simply who has more now
            } else if (result<0) {
                playerResult = ResultType.LOSS;
            } else {
                playerResult = ResultType.PUSH;
            }
        }
        return playerResult;
    }

}
