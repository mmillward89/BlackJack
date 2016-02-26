package com.example.mark.blackjack;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GameManager gameManager;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        //Implement my stuff from here down
        gameManager = new GameManager();
		startGame();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

        }
    }
	
	void Quit() {
		//Implement, back to main menu/end activity
	}
	
	void startGame() {
		gameManager.prepareNewGame();
		dealCards();
		//Request bet here (setBetMinimum, setPlayerBet), give player ability to quit
		//Use getMaxBetValue to ensure player bet doesn't exceed max bet value
		gameLoop(); //After bets are set, game can start
		//Play again button, if yes restart method
	}
	
	void dealCards() {
        Player[] playerValues = gameManager.dealCards(); //Returns all players with cards dealt
        for(int i=0; i<playerValues.length; i++) {
            //Get each player's hand
			Player player = playerValues[i];
            int[] hand = player.getHand();
            
			//Display each card in their hand
			for(int j=0; j<player.getHandSize(); j++) {
                displayCard(player.getPlayerType(), hand[j]);
            }
        }
    }

    void setBetMinimum(double bet) {
        gameManager.setBetMinimum(bet);
    }

    void setPlayerBet(double bet) {
        Player[] playersWithBets = gameManager.setBets(bet); //Sets bets for all players
        for(Player player: playersWithBets) {
			animateMoney(player); //Edits displayed money values and animates bets
		}
    }

    void displayCard(PlayerType playerType, int cardValue) {
        //Use information to get card with appropriate value and send in direction of
        //appropriate player, triggering animation
    }
	
	void gameLoop() {
		while(gameManager.nextPlayer()) {
			nextTurn(); //Triggers player or AI turn
		}
		nextAITurn(gameManager.getDealer()); //Dealer has final turn
		getResults(); //Round has ended, all player values recorded, need to compare all undecided to dealer		
	}
	
	void nextTurn() {
		Player player = gameManager.getCurrentPlayer();
		if(player.getPlayerType() != PlayerType.Player) {				
			nextAITurn(player)		
		} else {
			//Make buttons available for player
		}	 
	}

    void nextAITurn(Player player){
        gameManager.startAITurn(player);
		animateAICards(player);
		if(player.getResultBeforeDealer() || player.getPlayerType() == PlayerType.DEALER) {
			//Show results if confirmed before dealer turn, or if it is the dealer,
			//as this signifies the round is ending
			animateResults(player);
		}
    }
	
	void getResults() {
		Player[] players = gameManager.getScores();
		for(Player player: players) {
			animateResults(player);
		}
		gameManager.confirmWinnings();
		Player[] players = gameManager.getPlayers();
		for(Player player: players) {
			animateMoney(player);
		}
	}

    void Hit() {
        Player player = gameManager.hit(); 
		int hand[] = player.getHand();
		displayCard(player.getPlayerType(), hand[player.getHandSize()]);
        
		if(player.getResultBeforeDealer()) {
            //Trigger animation
			nextTurn();
        }
    }

    void Stay() {
		Player player = gameManager.getCurrentPlayer();
		animateCards(player);
		if(player.getResultBeforeDealer()) {
			animateResults(player);
		}
        gameManager.stay();
        nextTurn();
    }
	
	void animateMoney(Player player) {
		//Animate total change showing player bet subtracted from total
	}
	
	void animateAICards(Player player) {
		//Cards are displayed one by one for player in Hit()
		if(player.getPlayerType() != PlayerType.PLAYER) {
			int[] hand = player.getHand();
			for(int i=2; i<player.getHandSize(); i++) {
				//i starts at 2 to avoid initial hand dealt
				displayCard(Player.getPlayerType(), hand[i])	
			}
		}
	}
	
	void animateResults(Player player) {

		switch(player.getLastResult()) {
			case ResultType.BLACKJACK:
				//Animate
			break;
			
			case ResultType.LOSS:
				//Animate
			break;
			
			case: ResultType.UNDECIDED:
				//Animate
			break;
			
			case: ResultType.PUSH:
				//Animate
			break;
			
		}
	}
	
	
}
