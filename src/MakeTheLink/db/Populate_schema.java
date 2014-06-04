package MakeTheLink.db;

import java.sql.*;

import java.sql.Statement;

public class Populate_schema {

	public static void populate_music(Connection conn) throws SQLException{

		System.out.println("populating music...");
		
		Statement stmt = conn.createStatement();

		stmt.execute(

"	CREATE TABLE artists																							" +
"	(																												" +
"		name VARCHAR(300) NOT NULL,																					" +
"		birth_year int,																								" +
"		INDEX(name)																									" +
"	) ENGINE = MyISAM;																								" +

"	INSERT IGNORE INTO artists(name, birth_year)																	" +
"		SELECT type_musicians.c1, CAST(SUBSTRING(l_fact_birth_date.c2, 2,5) AS UNSIGNED INT)						" +
"		FROM type_musicians, l_fact_birth_date																		" +
"		WHERE l_fact_birth_date.c1=type_musicians.c1;																" +

"	INSERT IGNORE INTO artists(name, birth_year)																	" +
"		SELECT type_music_org.c1, CAST(SUBSTRING(l_fact_creation_date.c2, 2,5) AS UNSIGNED INT)						" +
"		FROM type_music_org, l_fact_creation_date																	" +
"		WHERE l_fact_creation_date.c1=type_music_org.c1;															" +

	//a temporary table that holds the musicians and the music they created
"	CREATE TABLE artists_creations																					" +
"	(																												" +
"		artist VARCHAR(165) NOT NULL,																				" +
"		creation VARCHAR(165) NOT NULL,																				" +
"		num_links int NOT NULL,																						" +
"		year_made int NOT NULL,																						" +
"		INDEX(creation)																								" +
"	) ENGINE = MyISAM;																								" +

"	INSERT IGNORE INTO artists_creations(artist, creation, num_links, year_made)											" +
"		SELECT fact_created.c1, fact_created.c2,	wiki_links.links_,													" +
"						CAST(SUBSTRING(l_fact_creation_date.c2, 2,5) AS UNSIGNED INT)								" +
"		FROM fact_created, wiki_links, 	l_fact_creation_date														" +
"		WHERE fact_created.c2 IN(SELECT c1 FROM type_music) and 													" +
"				fact_created.c1 IN(SELECT name FROM artists) and													" +
"				fact_created.c2=wiki_links.name_ and																" +
"				fact_created.c2=l_fact_creation_date.c1 ;															" +

	//remove redundant details from song names
"	UPDATE artists_creations																						" +
"	SET creation = CONCAT(SUBSTRING(creation, 1,LOCATE('_(', creation)-1), '>')										" +
"	WHERE creation LIKE '%song)%';																					" +
"	DELETE FROM artists_creations WHERE locate(substring(artist,2,length(artist)-2),creation) > 0;					" +

"	ALTER IGNORE TABLE artists_creations																			" +
"	ADD UNIQUE INDEX (artist, creation);																			" +

"	INSERT IGNORE INTO tmp_music_artists(name, num_links, birth_year) 												" +
"		SELECT x.artist, wiki_links.links_, artists.birth_year														" +
"		FROM wiki_links,  artists,																					" +
"							(select artists_creations.artist														" +
"							 from artists_creations																	" +
"							 group by artist) as x																	" +
"		WHERE x.artist = wiki_links.name_ and																		" +
"				x.artist = artists.name; 																			" +

"	DROP TABLE artists;																								" +

"	DELETE FROM artists_creations WHERE																				" +
"	artist NOT IN(SELECT name FROM tmp_music_artists);																" +

"	INSERT IGNORE INTO tmp_music_creations(name, num_links, year_made)												" +
"		SELECT ac.creation, ac.num_links, ac.year_made																	" +
"		FROM artists_creations ac																					" +
"		INNER JOIN tmp_music_artists a ON a.name=ac.artist;															" +

"	INSERT IGNORE INTO tmp_music_artist_creation(artist_id, creation_id)											" +
"	SELECT a.id, c.id																								" +
"	FROM tmp_music_artists a																						" +
"	INNER JOIN artists_creations ac ON a.name=ac.artist																" +
"	INNER JOIN tmp_music_creations c ON ac.creation=c.name;														" +

"	DROP TABLE artists_creations;																					");

		stmt.close();
	}
	
	public static void populate_cinema(Connection conn) throws SQLException{
		
		System.out.println("populating cinema...");
		
		Statement stmt = conn.createStatement();
		
		stmt.execute(
				
//there are a lot of musicians who also act in movies, remove them.
"	DELETE FROM type_actors																							" +
"	WHERE c1 IN (SELECT name FROM tmp_music_artists);																" +

//a temporary table that holds the actors and the movies they acted in.
"	CREATE TABLE actors_movies																						" +
"	(																												" +
"		actor VARCHAR(165) NOT NULL,																				" +
"		movie VARCHAR(165) NOT NULL,																				" +
"		INDEX(movie),																								" +
"		UNIQUE(actor, movie)																						" +
" 	) ENGINE = MyISAM;																								" +

" 	INSERT IGNORE INTO actors_movies(actor, movie)																	" +
"		SELECT c1, c2																								" +
"		FROM fact_acted_in																							" +
"		WHERE c1 IN(SELECT c1 FROM type_actors);																	" +

"	INSERT IGNORE INTO tmp_cinema_movies(name, num_links, year_made) 												" +
"		SELECT  x.movie, wiki_links.links_,																			" +
"				CAST(SUBSTRING(l_fact_creation_date.c2, 2,5) AS UNSIGNED INT)										" +
"		FROM wiki_links, l_fact_creation_date, (SELECT movie FROM actors_movies GROUP BY movie) AS x				" +
"		WHERE wiki_links.name_ = x.movie and																		" +
"				x.movie = l_fact_creation_date.c1;																	" +

"	INSERT IGNORE INTO tmp_cinema_actors(name, num_links, year_born) 												" +
"		SELECT x.actor, wiki_links.links_,																			" +
"					CAST(SUBSTRING(l_fact_birth_date.c2, 2,5) AS UNSIGNED INT)										" +
"		FROM wiki_links, l_fact_birth_date, (SELECT actor FROM actors_movies GROUP BY actor) AS x					" +
"		WHERE x.actor = wiki_links.name_ and																		" +
"				x.actor = l_fact_birth_date.c1; 																	" +

"	INSERT IGNORE INTO tmp_cinema_actor_movie(actor_id, movie_id)													" +
"	SELECT tmp_cinema_actors.id, tmp_cinema_movies.id																" +
"	FROM tmp_cinema_actors, tmp_cinema_movies, actors_movies														" +
"	WHERE	tmp_cinema_actors.name = actors_movies.actor AND														" +
"			actors_movies.movie = tmp_cinema_movies.name;															" +

"	DROP TABLE actors_movies;																						" +

	//delete entries of actors and movies that don't appear in the connecting table
"	DELETE FROM tmp_cinema_movies WHERE id NOT IN (SELECT movie_id FROM tmp_cinema_actor_movie);					" +

"	DELETE FROM tmp_cinema_actors WHERE id NOT IN (SELECT actor_id FROM tmp_cinema_actor_movie);					" +

"	INSERT INTO tmp_cinema_tags(id, name) values																	" +
"	(1,'action'),(2,'adventure'),(3,'animated'),(4,'comedy'),(5,'crime'),(6,'documentary'),							" +
"	(7,'drama'),(8,'fantasy'),(9,'horror'),(10,'musical'),(11,'mystery'),(12,'romance'),							" +
"	(13,'science_fiction'),(14,'sports'),(15,'teen'),(16,'television'),(17,'thriller'),								" +
"	(18,'war'),(19,'western');																						" );
		
		String[] type = {"action","adventure","animated","comedy","crime","documentary",
				"drama","fantasy","horror","musical","mystery","romance","science_fiction",
				"sports","teen","television","thriller","war","western"};
		int i=0;
		
		for(i=0;i<19;i++)
			stmt.executeUpdate(	"INSERT INTO tmp_cinema_movie_tag(movie_id, tag_id) "+
								"SELECT id, "+Integer.toString(i+1)+" "+
								"FROM tmp_cinema_movies as m "+
								"WHERE exists "+
								"(SELECT * FROM films_"+type[i]+" WHERE c1=m.name);");
		
		stmt.execute(
				
				"	UPDATE tmp_music_artists SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);		" +
				"	UPDATE tmp_music_creations SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);		");
		
		stmt.execute(
				
				"	UPDATE tmp_cinema_actors SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);		" +
				"	UPDATE tmp_cinema_movies SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);		");
	
		stmt.execute(
				
				"	delete from tmp_cinema_movies																	" +
				"	where id not in (select distinct movie_id from tmp_cinema_movie_tag);							");
		stmt.execute(
				
				"	delete from tmp_cinema_actors																	" +
				"	where id not in (select distinct actor_id from tmp_cinema_actor_movie);							");
		
		stmt.close();
	}
	
	public static void populate_places(Connection conn) throws SQLException{

		System.out.println("populating places...");
		
		Statement stmt = conn.createStatement();

		stmt.execute(

"	INSERT INTO tmp_places_countries(`Name`, `Area (1000 km^2)`, `GDP per capita (1000 $)`,						" + 
"							`Population (million)`, `Capital`, `GDP (billion $)`)									" + 
"		SELECT Name, TRUNCATE(Area/1000000000,2), TRUNCATE(max(GDP)*1000000/max(NumberOfPeople),2), 				" + 
"				TRUNCATE(max(NumberOfPeople)/1000000,2), Capital , max(GDP)											" + 
"		FROM (																										" + 
"			SELECT GDP_COUNTRIES.c1 AS Name, SUBSTRING(X.c2, 2,LOCATE('\"^', X.c2)-2) AS Area, 						" + 
"					A.c2 AS Capital ,																				" + 
"					CAST(SUBSTRING(C.c2, 2,LOCATE('\"^', C.c2)-2) as decimal) AS NumberOfPeople,					" + 
"					CAST(SUBSTRING(D.c2, 2,LOCATE('\"^', D.c2)-10) AS decimal)/10									" + 
"					 AS GDP																							" + 
"			FROM 																									" + 
"				(SELECT c1																							" + 
"				FROM l_fact_gdp ) AS GDP_COUNTRIES																	" + 

"			LEFT JOIN fact_has_capital AS A ON A.c1 =  GDP_COUNTRIES.c1												" + 

"			LEFT JOIN l_fact_population AS C ON C.c1 =  GDP_COUNTRIES.c1											" + 

"			LEFT JOIN l_fact_gdp AS D ON D.c1 =  GDP_COUNTRIES.c1													" + 
"				AND SUBSTRING(D.c2, 2,LOCATE('\"^', D.c2)-10)>0														" + 
"			LEFT JOIN l_fact_area AS X ON X.c1 =  GDP_COUNTRIES.c1													" + 

"			) AS T																									" + 
"		WHERE (CASE WHEN GDP is null then 1 else 0 end) = 0															" + 
"					AND																								" + 
"			 (CASE WHEN Capital is null then 1 else 0 end) = 0														" + 
"					AND																								" + 
"			 (CASE WHEN AREA is null then 1 else 0 end +															" + 
"			  CASE WHEN NumberOfPeople is null then 1 else 0 end) < 2												" + 
"					AND																								" + 
"				locate(substring(name,2,length(name)-2),capital)=0 													" + 
"		GROUP BY Name;																								" + 

"	DELETE FROM tmp_places_countries																				" + 
"	WHERE name='<Egovernment_in_the_UAE>' or name='<Brazil>' or name='<Chile>' 										" + 
"			or name='<United_States>' or name= '<El_Salvador>' or name like '%Ireland%'; 							" +

	//all our locations must be geo types
"	CREATE TABLE located_in (																						" +
"		c1 varchar(150) NOT NULL,																					" +
"		c2 varchar(150) DEFAULT NULL,																				" +
"		INDEX (c1),																									" +
"		INDEX (c2)																									" +
"	) ENGINE=MyISAM;																								" +

"	INSERT INTO located_in(c1, c2)																					" +
"	SELECT l.c1, l.c2 FROM fact_located_in l INNER JOIN type_geo g ON l.c1=g.c1; 									" +

"	DROP TABLE fact_located_in;																						" +

"	ALTER TABLE located_in RENAME TO fact_located_in; 																" +
	
/*	//all our locations must be geo types
"	DELETE FROM fact_located_in																						" + 
"	WHERE c1 NOT IN(SELECT c1 FROM type_geo) or c2 NOT IN(SELECT c1 FROM type_geo);									" + 
*/
	//find all the locations transitively located in countries, 3 levels deep.
"	CREATE TABLE transitive_1 (																						" + 
"	  place varchar(150) NOT NULL,																					" + 
"	  is_in varchar(150) DEFAULT NULL,																				" + 
"	  INDEX (is_in),																								" + 
"	  UNIQUE(place, is_in)																							" + 
"	) ENGINE=MyISAM;																								" + 

"	INSERT IGNORE INTO transitive_1(place, is_in)																	" + 
"		SELECT fact_located_in.c1, fact_located_in.c2																" + 
"		FROM fact_located_in, (SELECT name FROM tmp_places_countries) AS countries									" + 
"		WHERE fact_located_in.c2 IN(countries.name);																" + 

"	CREATE TABLE transitive_2 (																						" + 
"	  place varchar(150) NOT NULL,																					" + 
"	  is_in varchar(150) NOT NULL,																					" + 
"	  INDEX (is_in),																								" + 
"	  UNIQUE(place, is_in)																							" + 
"	) ENGINE=MyISAM;																								" + 

"	INSERT IGNORE INTO transitive_2(place, is_in)																	" + 
"		SELECT fact_located_in.c1, fact_located_in.c2																" + 
"		FROM fact_located_in, (select place from transitive_1) AS t1												" + 
"		WHERE fact_located_in.c2 IN(t1.place);																		" + 

"	DELETE FROM transitive_2																						" + 
"	WHERE place IN(SELECT name FROM tmp_places_countries);															" + 

"	CREATE TABLE transitive_3 (																						" + 
"	  place varchar(150) NOT NULL,																					" + 
"	  is_in varchar(150) NOT NULL,																					" + 
"	  INDEX (is_in),																								" + 
"	  UNIQUE(place, is_in)																							" + 
"	) ENGINE=MyISAM;																								" + 
 
"	INSERT IGNORE INTO transitive_3(place, is_in)																	" + 
"		SELECT fact_located_in.c1, fact_located_in.c2																" + 
"		FROM fact_located_in, (select place from transitive_2) as t2												" + 
"		WHERE fact_located_in.c2 IN (t2.place);																		" + 

"	DELETE FROM transitive_3																						" + 
"	WHERE place IN(SELECT name FROM tmp_places_countries);															" + 

"	DELETE FROM transitive_3																						" + 
"	WHERE place IN(SELECT place FROM transitive_1);																	" + 

	//collect all the locations and the countries they're in in one table.
	//also all our locations must have population.
"	CREATE TABLE countries_locations (																				" + 
"	  location varchar(150) NOT NULL,																				" + 
"	  country varchar(150) NOT NULL,																				" + 
"	  UNIQUE(location, country)																					" + 
"	) ENGINE=MyISAM;																								" + 

"	INSERT IGNORE INTO countries_locations(location, country)														" + 
"		SELECT pop.c1, t1.is_in																						" + 
"		FROM l_fact_population pop																					" + 
"		INNER JOIN transitive_1 t1 ON pop.c1=t1.place;																" + 

"	INSERT IGNORE INTO countries_locations(location, country)														" + 
"		SELECT pop.c1, t1.is_in																						" + 
"		FROM l_fact_population pop																					" + 
"		INNER JOIN transitive_2 t2 ON pop.c1=t2.place																" + 
"		INNER JOIN transitive_1 t1 ON t2.is_in=t1.place;															" + 

"	INSERT IGNORE INTO countries_locations(location, country)														" + 
"		SELECT pop.c1, t1.is_in																						" + 
"		FROM l_fact_population pop																					" + 
"		INNER JOIN transitive_3 t3 ON pop.c1=t3.place																" + 
"		INNER JOIN transitive_2 t2 ON t3.is_in=t2.place																" + 
"		INNER JOIN transitive_1 t1 ON t2.is_in=t1.place;															" + 

"	DELETE FROM countries_locations																					" + 
"	WHERE locate(substring(location,2,length(location)-2),country)>0												" + 
"							OR locate(substring(country,2,length(country)-2),location)>0							" + 
"							OR	location like '%Ireland%';															" + 

"	INSERT IGNORE INTO tmp_places_locations(name, num_links, population)											" + 
"		SELECT cl.location, wl.links_, 																				" + 
"			CAST(SUBSTRING(pop.c2, 2,LOCATE('\"^', pop.c2)-2) AS UNSIGNED INT)										" + 
"		FROM countries_locations cl																					" + 
"		INNER JOIN wiki_links wl ON cl.location=wl.name_															" + 
"		INNER JOIN l_fact_population pop ON cl.location=pop.c1;														" + 

"	INSERT IGNORE INTO tmp_places_location_country(location_id, country_id)										" + 
"		SELECT l.id, c.id																							" + 
"		FROM tmp_places_countries c																				" + 
"		INNER JOIN countries_locations cl ON c.name=cl.country														" + 
"		INNER JOIN tmp_places_locations l ON l.name=cl.location;													" + 

"	DROP TABLE transitive_1;																						" + 
"	DROP TABLE transitive_2;																						" + 
"	DROP TABLE transitive_3;																						" + 
"	DROP TABLE countries_locations;																					" + 

"	DELETE FROM tmp_places_locations WHERE id NOT IN (SELECT location_id FROM tmp_places_location_country);		" + 

"	DELETE FROM tmp_places_countries WHERE id NOT IN (SELECT country_id FROM tmp_places_location_country);		");
		
		stmt.execute(
				
		"	UPDATE tmp_places_countries SET Name=SUBSTRING(REPLACE(Name, '_', ' '), 2, LENGTH(Name)-2);				" +
		"	UPDATE tmp_places_locations SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);				" +
		"	UPDATE tmp_places_countries SET Capital=SUBSTRING(REPLACE(Capital, '_', ' '), 2, LENGTH(Capital)-2);	");

		
		stmt.close();
	}
	
	public static void populate_sports(Connection conn) throws SQLException{
		
		System.out.println("populating sports...");
		
		Statement stmt = conn.createStatement();
		
		stmt.execute(

"	CREATE TABLE players_teams																						" +
"	(																												" +
"		player VARCHAR(150) NOT NULL,																				" +
"		team VARCHAR(150) NOT NULL,																					" +
"		UNIQUE(player, team)																						" +
"	) ENGINE = MyISAM;																								" +

"	INSERT IGNORE INTO players_teams(player, team)																	" +
"		SELECT c1, c2																								" +
"		FROM fact_affiliated_to																						" +
"		WHERE 	c2 IN(SELECT c1 FROM type_nba_clubs) and															" +
"				c1 IN(SELECT c1 FROM type_bbl_players);																");
		
		populate_league("nba", stmt);
		
		stmt.execute(
		
"	CREATE TABLE players_teams																						" +
"	(																												" +
"		player VARCHAR(150) NOT NULL,																				" +
"		team VARCHAR(150) NOT NULL,																					" +
"		UNIQUE(player, team)																						" +
"	) ENGINE = MyISAM;																								" +

"	INSERT IGNORE INTO players_teams(player, team)																	" +
"		SELECT fact_plays_for.c1, fact_plays_for.c2																	" +
"		FROM fact_plays_for																							" +
"		WHERE 	fact_plays_for.c2 IN(SELECT c1 FROM type_soccer_clubs_world);										");
		
		populate_league("world_soccer", stmt);
		
		stmt.execute(
				
"	CREATE TABLE players_teams																						" +
"	(																												" +
"		player VARCHAR(150) NOT NULL,																				" +
"		team VARCHAR(150) NOT NULL,																					" +
"		UNIQUE(player, team)																						" +
"	) ENGINE = MyISAM;																								" +
	
"	INSERT IGNORE INTO players_teams(player, team)																	" +
"		SELECT fact_plays_for.c1, fact_plays_for.c2																	" +
"		FROM fact_plays_for, (	SELECT c2																			" +
"								FROM fact_plays_for, (SELECT c1 FROM type_soccer_clubs_isr) AS clubs				" +
"								WHERE 	c2 IN(clubs.c1)																" +
"								GROUP BY c2																			" +
"								HAVING count(*) > 30) AS good_teams													" +
"		WHERE fact_plays_for.c2 IN (good_teams.c2);");
		
		populate_league("israeli_soccer", stmt);
		
		stmt.close();
	}
	

	public static void populate_league(String league, Statement stmt) throws SQLException{
		
		stmt.execute(
			
"	INSERT IGNORE INTO tmp_"+league+"_teams(name, links_to_team, creation_year) 									" +
"		SELECT players_teams.team, wiki_links.links_, 																" +
"				CAST(SUBSTRING(l_fact_creation_date.c2, 2,4) AS UNSIGNED INT)										" +
"		FROM wiki_links, l_fact_creation_date, players_teams														" +
						
"		WHERE  wiki_links.name_ = players_teams.team and															" +
"				players_teams.team = l_fact_creation_date.c1;														" +

"	INSERT IGNORE INTO tmp_"+league+"_players(name, links_to_player, birth_year) 									" +
"		SELECT players_teams.player, wiki_links.links_,																" +
"					CAST(SUBSTRING(l_fact_birth_date.c2, 2,4) AS UNSIGNED INT)										" +
"		FROM wiki_links, l_fact_birth_date, players_teams															" +
	
"		WHERE players_teams.player = wiki_links.name_ and															" +
"				players_teams.player = l_fact_birth_date.c1; 														" +

"	INSERT IGNORE INTO tmp_"+league+"_player_team(team_id, player_id)												" +
"	SELECT tmp_"+league+"_teams.id, tmp_"+league+"_players.id													" +
"	FROM tmp_"+league+"_teams, tmp_"+league+"_players, players_teams											" +
"	WHERE	tmp_"+league+"_players.name = players_teams.player AND												" +
"			players_teams.team = tmp_"+league+"_teams.name;														" +
	
"	DROP TABLE players_teams;																						" +
	
"	DELETE FROM tmp_"+league+"_teams WHERE id NOT IN (SELECT team_id FROM tmp_"+league+"_player_team);			" +
"	DELETE FROM tmp_"+league+"_players WHERE id NOT IN (SELECT player_id FROM tmp_"+league+"_player_team);		");
		
		stmt.execute(
				
				"	UPDATE tmp_"+league+"_teams SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);		" +
				"	UPDATE tmp_"+league+"_players SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);	");
		
	}
	
	public static void clean_aux(Connection conn) throws SQLException{
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

