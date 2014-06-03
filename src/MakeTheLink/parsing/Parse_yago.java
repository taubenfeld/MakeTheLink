package MakeTheLink.parsing;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Parse_yago {
	
	public static void parse_facts(String path) throws ClassNotFoundException, SQLException, IOException{

		BufferedReader reader = new BufferedReader(
				new FileReader(path+"yagoFacts.tsv"));
		
		int j=0;
		
		int has_capital=0, plays_for=1, located_in=2, acted_in=3, created=4, affiliated_to=5;

		String line = null;
		String[] data = null;
		
		while((line = reader.readLine()) != null){
			
			if(j%100000==0)
				System.out.println("facts: "+j);
				
			j++;

			data = line.split("\t");
			
			if ("<hasCapital>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_facts(data, has_capital);
			if ("<playsFor>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_facts(data, plays_for);
			if ("<isLocatedIn>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_facts(data, located_in);
			if ("<actedIn>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_facts(data, acted_in);
			if ("<created>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_facts(data, created);
			if ("<isAffiliatedTo>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_facts(data, affiliated_to);
		}

		reader.close();
	}
	
	public static void parse_literal_facts(String path) throws ClassNotFoundException, SQLException, IOException{

		BufferedReader reader = new BufferedReader(
				new FileReader(path+"yagoLiteralFacts.tsv"));
		
		int j=0;
		
		int gdp=0, population=1, area=2, creation_date=3, birth_date=4;
		
		String line = null;
		String[] data = null;
		
		while((line = reader.readLine()) != null){
			
			if(j%100000==0)
				System.out.println("l_facts: "+j);
			
			j++;

			data = line.split("\t");
			
			if ("<hasGDP>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_literal_facts(data, gdp);
			if ("<hasNumberOfPeople>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_literal_facts(data, population);
			if ("<hasArea>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_literal_facts(data, area);
			if ("<wasCreatedOnDate>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_literal_facts(data, creation_date);
			if ("<wasBornOnDate>".equals(data[2]))
				MakeTheLink.db.Load_yago.insert_literal_facts(data, birth_date);
		}
		
		reader.close();
	}
	
	public static void parse_types(String path) throws ClassNotFoundException, SQLException, IOException{

		BufferedReader reader = new BufferedReader(
				new FileReader(path+"yagoTransitiveType.tsv"));
				
		int j=0;
		
		int 	geo=0, soccer_clubs_isr=1, soccer_clubs_world=2, actors=3, music=4,
				musicians=5, music_org=6, nba_clubs=7, bbl_players=8,
				
				action=9, adventure=10, animated=11, comedy=12, crime=13, documentary=14, drama=15, 
				fantasy=16, horror=17, musical=18, mystery=19, romance=20, science_fiction=21,
				sports=22, teen=23, television=24, thriller=25, war=26, western=27;
		
		String line = null;
		String[] data = null;
		
		while((line = reader.readLine()) != null){
			
			if(j%1000000==0)
				System.out.println("types: "+j);
			
			j++;

			data = line.split("\t");
			
			if ("<yagoGeoEntity>".equals(data[3]))
				MakeTheLink.db.Load_yago.insert_types(data, geo);
			if ("<wikicategory_Football_clubs_in_Israel>".equals(data[3]))
				MakeTheLink.db.Load_yago.insert_types(data, soccer_clubs_isr);
			if ("<wikicategory_La_Liga_clubs>".equals(data[3]) ||
				"<wikicategory_Premier_League_clubs>".equals(data[3]) ||
				"<wikicategory_Serie_A_clubs>".equals(data[3]) ||
				"<wikicategory_G-14_clubs>".equals(data[3]))
				MakeTheLink.db.Load_yago.insert_types(data, soccer_clubs_world);
			if ("<wikicategory_New_Zealand_film_actors>".equals(data[3]) ||
				"<wikicategory_Australian_film_actors>".equals(data[3]) ||
				"<wikicategory_American_film_actors>".equals(data[3]) ||
				"<wikicategory_Canadian_film_actors>".equals(data[3]) ||
				"<wikicategory_Irish_film_actors>".equals(data[3]) ||
				"<wikicategory_Scottish_film_actors>".equals(data[3]) ||
				"<wikicategory_Welsh_film_actors>".equals(data[3]) ||
				"<wikicategory_English_film_actors>".equals(data[3]) ||
				"<wikicategory_British_film_actors>".equals(data[3]))
				MakeTheLink.db.Load_yago.insert_types(data, actors);
			if ("<wordnet_music_107020895>".equals(data[3]))
				MakeTheLink.db.Load_yago.insert_types(data, music);
			if ("<wordnet_musician_110339966>".equals(data[3]))
				MakeTheLink.db.Load_yago.insert_types(data, musicians);
			if ("<wordnet_musical_organization_108246613>".equals(data[3]))
				MakeTheLink.db.Load_yago.insert_types(data, music_org);
			if ("<wikicategory_National_Basketball_Association_teams>".equals(data[3]))
				MakeTheLink.db.Load_yago.insert_types(data, nba_clubs);
			if ("<wordnet_basketball_player_109842047>".equals(data[3]))
				MakeTheLink.db.Load_yago.insert_types(data, bbl_players);
			
			if (data[3].matches("(.*)_films>") && data[3].matches("<wikicategory(.*)")){
				
				if (data[3].matches("(.*)action_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, action);
				if (data[3].matches("(.*)adventure_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, adventure);
				if (data[3].matches("(.*)animated_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, animated);	
				if (data[3].matches("(.*)comedy_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, comedy);
				if (data[3].matches("(.*)crime_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, crime);
				if (data[3].matches("(.*)documentary_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, documentary);
				if (data[3].matches("(.*)drama_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, drama);
				if (data[3].matches("(.*)fantasy_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, fantasy);
				if (data[3].matches("(.*)horror_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, horror);
				if (data[3].matches("(.*)musical_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, musical);
				if (data[3].matches("(.*)mystery_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, mystery);
				if (data[3].matches("(.*)romance_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, romance);
				if (data[3].matches("(.*)science_fiction_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, science_fiction);
				if (data[3].matches("(.*)sports_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, sports);
				if (data[3].matches("(.*)teen_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, teen);
				if (data[3].matches("(.*)television_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, television);
				if (data[3].matches("(.*)thriller_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, thriller);
				if (data[3].matches("(.*)war_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, war);
				if (data[3].matches("(.*)Western_films>"))
					MakeTheLink.db.Load_yago.insert_types(data, western);
			}
		}
		
		reader.close();
	}
	
	public static Map<String, Integer> parse_wiki(String path) 
								throws ClassNotFoundException, SQLException, IOException{

		BufferedReader reader = new BufferedReader(
				new FileReader(path+"yagoWikipediaInfo.tsv"));
		
		int j=0;

		String line = null;
		
		String[] data = null;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		while((line = reader.readLine()) != null){
			
			if(j%1000000==0)
				System.out.println("wiki1: "+j);
			j++;

			data = line.split("\t");
			
			Integer a;
			
			if ("<linksTo>".equals(data[2])){
				a = map.get(data[3]);
				if(a==null)
					map.put(data[3], 1);
				else
					map.put(data[3], map.get(data[3])+1);
			}
		}
		
		reader.close();
		
		return map;
	}
}
