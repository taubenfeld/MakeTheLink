package App;

import java.util.HashMap;
import java.util.Map;

import DatabaseConnection.databaseConnection;

public class Game {
	
	private int DifficultLevel; //represent a num of percentage
	private int NumOfRounds; // a number between 1 to max num
	
	/*
	 * the names of categories that the user selected.this category list also represent the name of the views
	 */
	private String[] CategoryList; 
	
	// players name and scores
	private Map <String, Integer> playerNameAndScore = new HashMap<>(); 
	
	private databaseConnection thisConnection;
	private Question thisQuestion;
	
	/**
	 * Constructs a new game. this constructor will be called by the UI class,
	 * when "start game" button is selected. the constructor will get game 
	 * properties that the user choose on the UI.
	 */
	public Game(int DifficultLevel, int NumOfRounds,
			String[] CategoryList, String[] PlayersName){
		
		this.DifficultLevel = DifficultLevel;
		this.NumOfRounds = NumOfRounds;
		this.CategoryList = CategoryList;
		for( String player:PlayersName ){
			this.playerNameAndScore.put(player, 0);
		}
		//creates views through DatabaseConnection class
		thisConnection = new databaseConnection();
		thisConnection.createViews(this.CategoryList, this.DifficultLevel);
		thisQuestion = new Question();
		
	}
	
	/**
	 * 
	 * @param playerName
	 * @param theAnswer
	 * @param clockTime
	 * 
	 * checks if the answer that was selected at UI, is the correct answer.
	 * if it is, starting a new round. and using updateScore func to updates
	 * the score for playerName.
	 * @return true if the answer is the correct answer. false otherwise.
	 */
	public boolean checkAnswerAndUpdate(String playerName,
			String theAnswer, int clockTime){
		boolean value = thisQuestion.checkAnswer(theAnswer);
		if (value) {
			updateScore(playerName, clockTime);

		}
		return value;
	}
	
	/**
	 * @param playerName
	 * @param timeLeft
	 * 
	 * Increase score for playerName by amount the compute by
	 * calcScoreByTime(int timeLeft) function
	 */
	public void updateScore(String playerName, int timeLeft){
		this.playerNameAndScore.put(playerName,
				playerNameAndScore.get(playerName)+calcScoreByTime(timeLeft));
	}
	
	/**
	 * @param timeLeft
	 * @return
	 * 
	 * calculate score by some arithmetic function that operates on input timeLeft
	 */
	public int calcScoreByTime(int timeLeft){
		return timeLeft;
	}
	


	
	
	


}
