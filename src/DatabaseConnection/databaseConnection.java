package DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
	public static Question[] genrateQuestion(int numOfRounds, int[] subjects, Connection conn) 
			throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		int[] question_themes = new int[numOfRounds];
		for(int i=0; i<numOfRounds; i++){
			do{
				question_themes[i] = (int)(Math.random() * 7);}
			while(question_themes[i]==0);
		}
		
		Question[] qst = new Question[numOfRounds];
		
		for(int i=0; i<numOfRounds; i++){
			
			if(question_themes[i]==0){
				qst[i]=MakeTheLink.db.Generate_question.actors_question(conn);
			}
			
			if(question_themes[i]==1){
				qst[i]=MakeTheLink.db.Generate_question.movies_question(conn);
			}
			
			if(question_themes[i]==2){
				qst[i]=MakeTheLink.db.Generate_question.music_question(conn);
			}
			
			if(question_themes[i]==3){
				qst[i]=MakeTheLink.db.Generate_question.places_question(conn);
			}
			
			if(question_themes[i]==4){
				qst[i]=MakeTheLink.db.Generate_question.sports_question(conn, "nba");
			}
			
			if(question_themes[i]==5){
				qst[i]=MakeTheLink.db.Generate_question.sports_question(conn, "world_soccer");
			}
			
			if(question_themes[i]==6){
				qst[i]=MakeTheLink.db.Generate_question.sports_question(conn, "israeli_soccer");
			}
		}
		
		return null;
	}
}
