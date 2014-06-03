package MakeTheLink.db;

import java.sql.*;
import java.io.IOException;

import App.Question;

public class DB_test {
	
	static String path = "/home/user7/Desktop/shared/yago2s_tsv/";
	static String username = "root";
	static String password = "1";
	
	public static void main(String [] args) throws ClassNotFoundException, SQLException, IOException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection( 
				 "jdbc:mysql://127.0.0.1:3306/DbMysql02?rewriteBatchedStatements=true&allowMultiQueries=true", 
				 username, 
				 password); 
		Statement stmt = conn.createStatement();
		/*
		//stmt.executeUpdate("CREATE SCHEMA DbMysql02;");
		//stmt.executeUpdate("USE DbMysql02;");
		
		Manage_schema.destroy(conn, "curr");
		Manage_schema.create(conn, "curr");
		
		Load_yago.load_yago(conn, path);
		*/
		/*
				//set the level of the modules:
		
		Questions_set_level.places_set_level(conn, 100);
		
		Questions_set_level.actors_set_level(conn, 1935, 7);
		
		Questions_set_level.music_set_level(conn, 1950, 5);
		
		Questions_set_level.sports_set_level(conn, 1970, 10);
		
		Questions_set_level.movies_set_level(conn, 1985, 10);
		*/
		
		
		//generate and display a sample question from the movies module:
		
		Question qst = Generate_question.sports_question(conn, "israeli_soccer");
		
		String[] opts = qst.getAnswerOptions();
		String answer = qst.getAnswer();
		String[] hints = qst.getHintsList();
		
		for(int i=0;i<4 ;i++){
			System.out.println("opt "+Integer.toString(i+1)+": "+opts[i]);
		}
		System.out.println(" ");
		
		System.out.println("answer: "+answer);
		
		System.out.println(" ");
		
		for(int i=0;i<hints.length && i<20;i++){
			System.out.println("hint "+Integer.toString(i+1)+": "+hints[i]);
		}

		stmt.close();
		conn.close();
	}	
}
