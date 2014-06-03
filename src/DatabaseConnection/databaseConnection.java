package DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import App.Question;


public class databaseConnection {
	
	static String username = "username";
	static String password = "password";
	
	static int[] categories = {0,0,0,0,0,0,0};
	
	/**
	 * @param active_categories. boolean array that indicates the categories that 
	 * were selected in the UI by user.
	 * 
	 * @param year. array of minimum years from which to select entities in the
	 * different categories. specifically:
	 * 
	 * 		Actors - minimum birth year of the actors in the questions
	 * 		Movies - minimum release date of the movies in the questions
	 * 		Music - minimum birth year of the musicians/artists in the questions
	 * 		Places - no effect
	 * 		Sports - minimum birth year of the players in the questions
	 * 
	 *  @param difficulty. array of the difficulties for each category.
	 *  in each category, the top most famous entities are selected first.
	 *  the famousness of an entity is measured by the number of wikipedia links to it.
	 *  specifically:
	 *  
	 *  	Actors: specifies the percent of the top famous actors to be taken from the database.
	 *  	Movies: specifies the percent of the top famous movies to be taken from the database.
	 *  	Music: specifies the percent of the top famous musicians to be taken from the database.
	 *  	Places: specifies the percent of the top famous places to be taken from the database.
	 *  	Sports: specifies the percent of the top famous players to be taken from the database.
	 *  
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	
	/* active_categories key:
	 * 
	 * 			0-Actors 
	 * 			1-Movies 
	 * 			2-Music 
	 * 			3-Places 
	 * 			4-NBA 
	 * 			5-World_Soccer 
	 * 			6-Israeli_Soccer
	 * 
	 * year & difficulty key: 
	 * 
	 * 			0-Actors 
	 * 			1-Movies 
	 * 			2-Music 
	 * 			3-Places (year has no effect here)
	 * 			4-Sports
	 */
	public int setQuestionOps(int[] active_categories, int[] year, int[] difficulty) 
			throws ClassNotFoundException, SQLException, IOException{
		
		Connection conn = DriverManager.getConnection( 
				 "jdbc:mysql://127.0.0.1:3306/DbMysql02?rewriteBatchedStatements=true&allowMultiQueries=true", 
				 username, 
				 password); 
		
		for(int i=0; i<7; i++)
			categories[i]=0;
		
		if(0!=active_categories[0] && 
				0!=MakeTheLink.db.Questions_set_level.actors_set_level(conn, year[0], difficulty[0])){
			categories[0]=1;
		}
		
		if(0!=active_categories[1] && 
				0!=MakeTheLink.db.Questions_set_level.movies_set_level(conn, year[1], difficulty[1])){
			categories[0]=1;
		}
		
		if(0!=active_categories[2] && 
				0!=MakeTheLink.db.Questions_set_level.music_set_level(conn, year[2], difficulty[2])){
			categories[0]=1;
		}
		
		if(0!=active_categories[3] && 
				0!=MakeTheLink.db.Questions_set_level.places_set_level(conn, difficulty[3])){
			categories[0]=1;
		}
		
		
		int[] leagues_bool = {0,0,0};
		if (0!=active_categories[4] || 0!=active_categories[5] || 0!=active_categories[6])
			leagues_bool = MakeTheLink.db.Questions_set_level.sports_set_level(conn, year[4], difficulty[4]);
		
		if(0!=active_categories[4] && 0!=leagues_bool[0]){
			categories[4]=1;
		}
		
		if(0!=active_categories[5] && 0!=leagues_bool[1]){
			categories[5]=1;
		}
		
		if(0!=active_categories[6] && 0!=leagues_bool[2]){
			categories[6]=1;
		}
		
		int sum=0;
		
		for(int i=0; i<7; i++)
			sum+=categories[i];
		
		//return 0 if there isn't enough data to make questions in any of the chosen categories, else 1.
		return sum==0?0:1;
	}

	/*
	 * returns all the question for the game
	 */
	public static Question[] genrateQuestion(int numOfRounds) 
			throws ClassNotFoundException, SQLException, IOException {
		
		Connection conn = DriverManager.getConnection( 
				 "jdbc:mysql://127.0.0.1:3306/DbMysql02?rewriteBatchedStatements=true&allowMultiQueries=true", 
				 username, 
				 password); 
		
		int[] question_themes = new int[numOfRounds];
		for(int i=0; i<numOfRounds; i++){
			do{
				question_themes[i] = (int)(Math.random() * 7);}
			while(categories[question_themes[i]]==0);
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
