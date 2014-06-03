package MakeTheLink.db;

import java.sql.*;
import java.util.Map;
import java.io.IOException;

public class Load_yago {
	
	static int yago_data_ready=0;
	
	static String yago_loading_progress = "Yago data not ready for import";
	
	public static void import_yago_data(Connection conn)
			 throws ClassNotFoundException, SQLException, IOException{
		
		//copy yago data from tmp to curr
		Copy_yago_data.copy(conn);
		
		Manage_schema.destroy(conn, "tmp");
		
		yago_loading_progress = "Yago data not ready for import";
		yago_data_ready=0;
	}
	
	public static void prepare_yago_data(Connection conn, String path)
			 throws ClassNotFoundException, SQLException, IOException{
		
		clean_aux(conn);
		Populate_schema.clean_aux(conn);
		
		Manage_schema.destroy(conn, "tmp");
		Manage_schema.create(conn, "tmp");
		
		yago_loading_progress = "Loading facts (0% completed)";
		load_facts(conn, path);
		yago_loading_progress = "Loading literal facts (25% completed)";
		load_literal_facts(conn, path);
		yago_loading_progress = "Loading types (35% completed)";
		load_types(conn, path);
		yago_loading_progress = "Loading wikipedia info (55% completed)";
		load_wiki(conn, path);		
		yago_loading_progress = "Populating music (75% completed)";
		Populate_schema.populate_music(conn);
		yago_loading_progress = "Populating cinema (80% completed)";
		Populate_schema.populate_cinema(conn);
		yago_loading_progress = "Populating places (85% completed)";
		Populate_schema.populate_places(conn);
		yago_loading_progress = "Populating sports (95% completed)";
		Populate_schema.populate_sports(conn);
		
		clean_aux(conn);
		Populate_schema.clean_aux(conn);
		
		yago_loading_progress = "Yago data ready for import";
		
		yago_data_ready=1;
		
	}
	
	static PreparedStatement[] pstmt_facts = new PreparedStatement[6];
	static int[] cnt_facts = {0,0,0,0,0,0};
	
	public static void insert_facts(String[] data, int num) throws SQLException{
		pstmt_facts[num].setString(1, data[1]);
		pstmt_facts[num].setString(2, data[3]);
		pstmt_facts[num].addBatch();
		cnt_facts[num]++;
		if(cnt_facts[num]>5000){
			pstmt_facts[num].executeBatch();
			cnt_facts[num]=0;
		}
	}
	
	public static void load_facts(Connection conn, String path) 
			throws ClassNotFoundException, SQLException, IOException{

		Statement stmt = conn.createStatement();
				
		String fields = " (c1 VARCHAR(300), c2 VARCHAR(300), INDEX(c1), INDEX(c2)) ENGINE = MyISAM; ";
		
		int i=0;
		
		String[] fact = {"fact_has_capital", "fact_plays_for", "fact_located_in", "fact_acted_in", 
							"fact_created", "fact_affiliated_to"};
		
		for(i=0;i<6;i++){
			stmt.executeUpdate("CREATE TABLE "+fact[i]+fields);
			pstmt_facts[i] = conn.prepareStatement(" INSERT INTO "+fact[i]+"(c1, c2)  VALUES (?,?)");
		}
		
		MakeTheLink.parsing.Parse_yago.parse_facts(path);
	
		for(i=0;i<6;i++){
			if(cnt_facts[i]>0)
				pstmt_facts[i].executeBatch();
			pstmt_facts[i].close();
		}
		
		stmt.close();
	}
	
	static PreparedStatement[] pstmt_literal_facts = new PreparedStatement[5];
	static int[] cnt_literal_facts = {0,0,0,0,0};
	
	public static void insert_literal_facts(String[] data, int num) throws SQLException{
		pstmt_literal_facts[num].setString(1, data[1]);
		pstmt_literal_facts[num].setString(2, data[3]);
		pstmt_literal_facts[num].addBatch();
		cnt_literal_facts[num]++;
		if(cnt_literal_facts[num]>5000){
			pstmt_literal_facts[num].executeBatch();
			cnt_literal_facts[num]=0;
		}
	}
	
	public static void load_literal_facts(Connection conn, String path) throws ClassNotFoundException, SQLException, IOException{

		Statement stmt = conn.createStatement();
				
		String fields = " (c1 VARCHAR(300), c2 VARCHAR(300), INDEX(c1)) ENGINE = MyISAM; ";
		
		int i=0;
		
		String[] l_fact = {"l_fact_gdp", "l_fact_population", "l_fact_area", "l_fact_creation_date", 
				"l_fact_birth_date"};
		
		for(i=0;i<5;i++){
			stmt.executeUpdate("CREATE TABLE "+l_fact[i]+fields);
			pstmt_literal_facts[i] = conn.prepareStatement(" INSERT INTO "+l_fact[i]+"(c1, c2)  VALUES (?,?)");
		}
		
		MakeTheLink.parsing.Parse_yago.parse_literal_facts(path);
	
		for(i=0;i<5;i++){
			if(cnt_literal_facts[i]>0)
				pstmt_literal_facts[i].executeBatch();
			pstmt_literal_facts[i].close();
		}
		
		stmt.close();
	}
	
	static PreparedStatement[] pstmt_types = new PreparedStatement[28];
	static int[] cnt_types = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	
	public static void insert_types(String[] data, int num) throws SQLException{
		pstmt_types[num].setString(1, data[1]);
		pstmt_types[num].addBatch();
		cnt_types[num]++;
		if(cnt_types[num]>5000){
			pstmt_types[num].executeBatch();
			cnt_types[num]=0;
		}
	}
	
	public static void load_types(Connection conn, String path) throws ClassNotFoundException, SQLException, IOException{
		Statement stmt_types = conn.createStatement();
				
		String fields = " (c1 VARCHAR(300), UNIQUE(c1)) ENGINE = MyISAM; ";
		
		int i=0;
		
		String[] type = {	"type_geo", "type_soccer_clubs_isr", "type_soccer_clubs_world", "type_actors",
							"type_music","type_musicians", "type_music_org", "type_nba_clubs", "type_bbl_players",
							
							"films_action","films_adventure","films_animated","films_comedy","films_crime",
							"films_documentary","films_drama","films_fantasy","films_horror",
							"films_musical","films_mystery","films_romance","films_science_fiction",
							"films_sports","films_teen","films_television","films_thriller","films_war",
							"films_western"};
		
		for(i=0;i<28;i++){
			stmt_types.executeUpdate("CREATE TABLE "+type[i]+fields);
			pstmt_types[i] = conn.prepareStatement(" INSERT IGNORE INTO "+type[i]+"(c1)  VALUES (?)");
		}
		
		MakeTheLink.parsing.Parse_yago.parse_types(path);
	
		for(i=0;i<28;i++){
			if(cnt_types[i]>0)
				pstmt_types[i].executeBatch();
			pstmt_types[i].close();
		}
		
		stmt_types.close();
	}
	
	public static void load_wiki(Connection conn, String path) throws ClassNotFoundException, SQLException, IOException{

		Statement stmt = conn.createStatement();
		
		int j=0;
		
		stmt.executeUpdate("CREATE TABLE wiki_links(name_ VARCHAR(300), links_ int, UNIQUE(name_)) ENGINE = MyISAM; ");
		PreparedStatement pstmt = conn.prepareStatement(" INSERT IGNORE INTO wiki_links(name_, links_)  VALUES (?, ?)");
		
		Map<String, Integer> map = MakeTheLink.parsing.Parse_yago.parse_wiki(path);
		
		int cnt=0;
		
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    
			if(j%100000==0)
				System.out.println("wiki2: "+j);
			j++;
		    
			pstmt.setString(1, key);
			pstmt.setInt(2, value);
			pstmt.addBatch();
			cnt++;
			if(cnt>5000){
				pstmt.executeBatch();
				cnt=0;
			}
		    
		}

		if(cnt>0)
			pstmt.executeBatch();
		
		pstmt.close();
		stmt.close();
	}
	
	public static void clean_aux(Connection conn) throws SQLException{
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
}
