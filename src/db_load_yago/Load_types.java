package db_load_yago;

import java.sql.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Load_types {
	
	static PreparedStatement[] pstmt = new PreparedStatement[28];
	static int[] cnt = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	static String[] data = null;
	
	public static void insert(int num) throws SQLException{
		pstmt[num].setString(1, data[1]);
		pstmt[num].addBatch();
		cnt[num]++;
		if(cnt[num]>5000){
			pstmt[num].executeBatch();
			cnt[num]=0;
		}
	}
	
	public static void load(Connection conn, String path) throws ClassNotFoundException, SQLException, IOException{
		Statement stmt = conn.createStatement();

		BufferedReader reader = new BufferedReader(
				new FileReader(path+"yagoTransitiveType.tsv"));
				
		String fields = " (c1 VARCHAR(300), UNIQUE(c1)) ENGINE = MyISAM; ";
		
		int i=0, j=0;
		
		int 	geo=0, soccer_clubs_isr=1, soccer_clubs_world=2, actors=3, music=4,
				musicians=5, music_org=6, nba_clubs=7, bbl_players=8,
				
				action=9, adventure=10, animated=11, comedy=12, crime=13, documentary=14, drama=15, 
				fantasy=16, horror=17, musical=18, mystery=19, romance=20, science_fiction=21,
				sports=22, teen=23, television=24, thriller=25, war=26, western=27;
		
		String[] type = {	"type_geo", "type_soccer_clubs_isr", "type_soccer_clubs_world", "type_actors",
							"type_music","type_musicians", "type_music_org", "type_nba_clubs", "type_bbl_players",
							
							"films_action","films_adventure","films_animated","films_comedy","films_crime",
							"films_documentary","films_drama","films_fantasy","films_horror",
							"films_musical","films_mystery","films_romance","films_science_fiction",
							"films_sports","films_teen","films_television","films_thriller","films_war",
							"films_western"};
		
		for(i=0;i<28;i++){
			stmt.executeUpdate("CREATE TABLE "+type[i]+fields);
			pstmt[i] = conn.prepareStatement(" INSERT IGNORE INTO "+type[i]+"(c1)  VALUES (?)");
		}
		
		String line = null;
		
		while((line = reader.readLine()) != null){
			
			if(j%1000000==0)
				System.out.println("types: "+j);
			
			j++;

			data = line.split("\t");
			
			if ("<yagoGeoEntity>".equals(data[3]))
				insert(geo);
			if ("<wikicategory_Football_clubs_in_Israel>".equals(data[3]))
				insert(soccer_clubs_isr);
			if ("<wikicategory_La_Liga_clubs>".equals(data[3]) ||
				"<wikicategory_Premier_League_clubs>".equals(data[3]) ||
				"<wikicategory_Serie_A_clubs>".equals(data[3]) ||
				"<wikicategory_G-14_clubs>".equals(data[3]))
				insert(soccer_clubs_world);
			if ("<wikicategory_New_Zealand_film_actors>".equals(data[3]) ||
				"<wikicategory_Australian_film_actors>".equals(data[3]) ||
				"<wikicategory_American_film_actors>".equals(data[3]) ||
				"<wikicategory_Canadian_film_actors>".equals(data[3]) ||
				"<wikicategory_Irish_film_actors>".equals(data[3]) ||
				"<wikicategory_Scottish_film_actors>".equals(data[3]) ||
				"<wikicategory_Welsh_film_actors>".equals(data[3]) ||
				"<wikicategory_English_film_actors>".equals(data[3]) ||
				"<wikicategory_British_film_actors>".equals(data[3]))
				insert(actors);
			if ("<wordnet_music_107020895>".equals(data[3]))
				insert(music);
			if ("<wordnet_musician_110339966>".equals(data[3]))
				insert(musicians);
			if ("<wordnet_musical_organization_108246613>".equals(data[3]))
				insert(music_org);
			if ("<wikicategory_National_Basketball_Association_teams>".equals(data[3]))
				insert(nba_clubs);
			if ("<wordnet_basketball_player_109842047>".equals(data[3]))
				insert(bbl_players);
			
			if (data[3].matches("(.*)_films>") && data[3].matches("<wikicategory(.*)")){
				
				if (data[3].matches("(.*)action_films>"))
					insert(action);
				if (data[3].matches("(.*)adventure_films>"))
					insert(adventure);
				if (data[3].matches("(.*)animated_films>"))
					insert(animated);	
				if (data[3].matches("(.*)comedy_films>"))
					insert(comedy);
				if (data[3].matches("(.*)crime_films>"))
					insert(crime);
				if (data[3].matches("(.*)documentary_films>"))
					insert(documentary);
				if (data[3].matches("(.*)drama_films>"))
					insert(drama);
				if (data[3].matches("(.*)fantasy_films>"))
					insert(fantasy);
				if (data[3].matches("(.*)horror_films>"))
					insert(horror);
				if (data[3].matches("(.*)musical_films>"))
					insert(musical);
				if (data[3].matches("(.*)mystery_films>"))
					insert(mystery);
				if (data[3].matches("(.*)romance_films>"))
					insert(romance);
				if (data[3].matches("(.*)science_fiction_films>"))
					insert(science_fiction);
				if (data[3].matches("(.*)sports_films>"))
					insert(sports);
				if (data[3].matches("(.*)teen_films>"))
					insert(teen);
				if (data[3].matches("(.*)television_films>"))
					insert(television);
				if (data[3].matches("(.*)thriller_films>"))
					insert(thriller);
				if (data[3].matches("(.*)war_films>"))
					insert(war);
				if (data[3].matches("(.*)Western_films>"))
					insert(western);
			}
		}
	
		for(i=0;i<28;i++){
			if(cnt[i]>0)
				pstmt[i].executeBatch();
			pstmt[i].close();
		}
		
		stmt.close();
		reader.close();
	}
}
