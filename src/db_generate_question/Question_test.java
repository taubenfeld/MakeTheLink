package db_generate_question;

import java.sql.*;
import java.io.IOException;
import App.Question;

public class Question_test {
	
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
		
		//set the level of the modules:
		
		//db_set_level.Places_set_level.set_level(conn, 100);
		
		//db_set_level.Actors_set_level.set_level(conn, 1900, 100);
		
		//db_set_level.Music_set_level.set_level(conn, 1900, 100);
		
		//db_set_level.Actors_set_level.set_level(conn, 1900, 100);
		
		//db_set_level.Sports_set_level.set_level(conn, 1970, 1);
		
		
		//db_set_level.Movies_set_level.set_level(conn, 1985, 5);
		
		
		
		//generate and display a sample question from the movies module:
		
		Question qst = db_generate_question.Movies_question.generate_question(conn);
		
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
