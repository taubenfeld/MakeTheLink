package db_load_yago;

import java.sql.*;
import java.io.IOException;

public class Load_yago_main {
	
	static String path = "/home/user7/Desktop/shared/yago2s_tsv/";
	static String username = "root";
	static String password = "prm1tsfc";
	
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
		
		clean_aux1(conn);
		clean_aux2(conn);
		
		db_manage_tables.Tables_main.destroy(conn, "curr");
		db_manage_tables.Tables_main.destroy(conn, "yago");
		
		db_manage_tables.Tables_main.destroy(conn, "tmp");
		db_manage_tables.Tables_main.create(conn, "tmp");
		
		Load_facts.load(conn, path);
		Load_literal_facts.load(conn, path);
		Load_types.load(conn, path);
		Load_wiki.load(conn, path);		
		
		Populate_music_tables.build(conn);
		Populate_cinema_tables.build(conn);
		Populate_places_tables.build(conn);
		Populate_sports_tables.build(conn);
		
		db_manage_tables.Tables_main.change_prefix(conn, "tmp", "curr");
		
		clean_aux1(conn);
		clean_aux2(conn);

		
		
		int[] categories = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		
		categories[1]=1;
		categories[0]=1;
		*/
		
		db_set_level.Movies_set_level.set_level(conn, 1);
		
		
		db_set_level.Places_set_level.set_level(conn, 1);
		
		db_set_level.Sports_set_level.set_level(conn, 5);
		
		db_set_level.Actors_set_level.set_level(conn, 5);
		
		db_set_level.Music_set_level.set_level(conn, 5);
		
		String[] q = db_generate_question.Movies_question.generate_question(conn);
		
		for(int i=0;i<20 && !q[i].equalsIgnoreCase("");i++){
			System.out.println(q[i]);
		}
		
		stmt.close();
		conn.close();
	}
	
	public static void clean_aux1(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		stmt.execute(	"	DROP TABLE IF EXISTS fact_acted_in;								" +
						"	DROP TABLE IF EXISTS fact_affiliated_to;							" +
						"	DROP TABLE IF EXISTS fact_created;								" +
						"	DROP TABLE IF EXISTS fact_has_capital;							" +
						"	DROP TABLE IF EXISTS fact_located_in;								" +
						"	DROP TABLE IF EXISTS fact_plays_for;								" +				
						
						"	DROP TABLE IF EXISTS films_action;								" +
						"	DROP TABLE IF EXISTS films_adventure;								" +
						"	DROP TABLE IF EXISTS films_animated;								" +
						"	DROP TABLE IF EXISTS films_comedy;								" +
						"	DROP TABLE IF EXISTS films_crime;									" +
						"	DROP TABLE IF EXISTS films_documentary;							" +
						"	DROP TABLE IF EXISTS films_drama;									" +
						"	DROP TABLE IF EXISTS films_fantasy;								" +
						"	DROP TABLE IF EXISTS films_horror;								" +
						"	DROP TABLE IF EXISTS films_musical;								" +
						"	DROP TABLE IF EXISTS films_mystery;								" +
						"	DROP TABLE IF EXISTS films_romance;								" +
						"	DROP TABLE IF EXISTS films_science_fiction;						" +
						"	DROP TABLE IF EXISTS films_sports;								" +
						"	DROP TABLE IF EXISTS films_teen;									" +
						"	DROP TABLE IF EXISTS films_television;							" +
						"	DROP TABLE IF EXISTS films_thriller;								" +				
						"	DROP TABLE IF EXISTS films_war;									" +
						"	DROP TABLE IF EXISTS films_western;								" +
						
						"	DROP TABLE IF EXISTS l_fact_area;									" +
						"	DROP TABLE IF EXISTS l_fact_birth_date;							" +
						"	DROP TABLE IF EXISTS l_fact_creation_date;						" +
						"	DROP TABLE IF EXISTS l_fact_gdp;									" +
						"	DROP TABLE IF EXISTS l_fact_population;							" +
						
						"	DROP TABLE IF EXISTS type_actors;									" +
						"	DROP TABLE IF EXISTS type_bbl_players;							" +
						"	DROP TABLE IF EXISTS type_geo;									" +
						"	DROP TABLE IF EXISTS type_music;									" +
						"	DROP TABLE IF EXISTS type_musicians;								" +
						"	DROP TABLE IF EXISTS type_music_org;								" +
						"	DROP TABLE IF EXISTS type_nba_clubs;								" +
						"	DROP TABLE IF EXISTS type_soccer_clubs_isr;						" +
						"	DROP TABLE IF EXISTS type_soccer_clubs_world;						" +
						
						"	DROP TABLE IF EXISTS wiki_links;									");	
		stmt.close();
	}
	
	public static void clean_aux2(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		stmt.execute(	"	DROP TABLE IF EXISTS actors_movies;										" +
						"	DROP TABLE IF EXISTS artists;											" +
						"	DROP TABLE IF EXISTS artists_creations;									" +
						"	DROP TABLE IF EXISTS transitive_1;										" +
						"	DROP TABLE IF EXISTS transitive_2;										" +
						"	DROP TABLE IF EXISTS transitive_3;										" +
						"	DROP TABLE IF EXISTS countries_locations;								" +
						"	DROP TABLE IF EXISTS players_teams;										");	
		stmt.close();
	}
}
