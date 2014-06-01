package DatabaseConnection;

import java.util.Map;

import App.Question;


public class databaseConnection {
	
	int Actors = 1;
	
	/**
	 * @param CategoryList. list of the categories that was checked in the UI by user.
	 * creates views in the database for the current game.
	 * Note that each view will contain an auto increment index that will 
	 * to guarantee quick random select.
	 */
	public void createViews(Map<String, Integer> CategoryMap, int difficulty){
		
	}

	/**
	 * 
	 * @return
	 */
	public Question genQuestion(){
		
		
		
		return null;
		
	}

	/*
	 * returns all the question for the game
	 */
	public static Question[] genrateQuestion(int numOfRounds) {
		// TODO Auto-generated method stub
		return null;
	}
}
