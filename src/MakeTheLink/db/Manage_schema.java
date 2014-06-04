package MakeTheLink.db;

import java.sql.*;
import java.io.IOException;


public class Manage_schema {
	
	public static void create(Connection conn, String prfx) throws ClassNotFoundException, SQLException, IOException{
		
		create_music(conn, prfx);
		create_cinema(conn, prfx);
		create_places(conn, prfx);
		create_sports(conn, prfx);
	}
	
	public static void destroy(Connection conn, String prfx) throws SQLException{
		Statement stmt = conn.createStatement();
		stmt.execute(	"	DROP TABLE IF EXISTS "+prfx+"_cinema_actor_movie;						" +
						"	DROP TABLE IF EXISTS "+prfx+"_cinema_movie_tag;						" +
						"	DROP TABLE IF EXISTS "+prfx+"_places_location_country;				" +
						"	DROP TABLE IF EXISTS "+prfx+"_israeli_soccer_player_team;				" +
						"	DROP TABLE IF EXISTS "+prfx+"_music_artist_creation;					" +
						"	DROP TABLE IF EXISTS "+prfx+"_nba_player_team;						" +
						"	DROP TABLE IF EXISTS "+prfx+"_world_soccer_player_team;				" +
						
						"	DROP TABLE IF EXISTS "+prfx+"_cinema_actors;							" +
						"	DROP TABLE IF EXISTS "+prfx+"_cinema_movies;							" +
						"	DROP TABLE IF EXISTS "+prfx+"_cinema_tags;							" +
						"	DROP TABLE IF EXISTS "+prfx+"_places_countries;						" +
						"	DROP TABLE IF EXISTS "+prfx+"_places_locations;						" +
						"	DROP TABLE IF EXISTS "+prfx+"_music_artists;							" +
						"	DROP TABLE IF EXISTS "+prfx+"_music_creations;						" +
						"	DROP TABLE IF EXISTS "+prfx+"_israeli_soccer_players;					" +
						"	DROP TABLE IF EXISTS "+prfx+"_israeli_soccer_teams;					" +
						"	DROP TABLE IF EXISTS "+prfx+"_nba_players;							" +
						"	DROP TABLE IF EXISTS "+prfx+"_nba_teams;								" +
						"	DROP TABLE IF EXISTS "+prfx+"_world_soccer_players;					" +
						"	DROP TABLE IF EXISTS "+prfx+"_world_soccer_teams;						");		
		
		stmt.close();
	}
	
	public static void change_prefix(Connection conn, String prev, String newPrfx) throws SQLException{
		Statement stmt = conn.createStatement();
		stmt.execute(	
				
	"	ALTER TABLE "+prev+"_cinema_actor_movie RENAME TO "+newPrfx+"_cinema_actor_movie;					" +
	"	ALTER TABLE "+prev+"_cinema_movie_tag RENAME TO "+newPrfx+"_cinema_movie_tag;						" +
	"	ALTER TABLE "+prev+"_places_location_country RENAME TO "+newPrfx+"_places_location_country;			" +
	"	ALTER TABLE "+prev+"_israeli_soccer_player_team RENAME TO "+newPrfx+"_israeli_soccer_player_team;	" +
	"	ALTER TABLE "+prev+"_music_artist_creation RENAME TO "+newPrfx+"_music_artist_creation;				" +
	"	ALTER TABLE "+prev+"_nba_player_team RENAME TO "+newPrfx+"_nba_player_team;							" +
	"	ALTER TABLE "+prev+"_world_soccer_player_team RENAME TO "+newPrfx+"_world_soccer_player_team;		" +
						
	"	ALTER TABLE "+prev+"_cinema_actors RENAME TO "+newPrfx+"_cinema_actors;								" +
	"	ALTER TABLE "+prev+"_cinema_movies RENAME TO "+newPrfx+"_cinema_movies;								" +
	"	ALTER TABLE "+prev+"_cinema_tags RENAME TO "+newPrfx+"_cinema_tags;									" +
	"	ALTER TABLE "+prev+"_places_countries RENAME TO "+newPrfx+"_places_countries;						" +
	"	ALTER TABLE "+prev+"_places_locations RENAME TO "+newPrfx+"_places_locations;						" +
	"	ALTER TABLE "+prev+"_music_artists RENAME TO "+newPrfx+"_music_artists;								" +
	"	ALTER TABLE "+prev+"_music_creations RENAME TO "+newPrfx+"_music_creations;							" +
	"	ALTER TABLE "+prev+"_israeli_soccer_players RENAME TO "+newPrfx+"_israeli_soccer_players;			" +
	"	ALTER TABLE "+prev+"_israeli_soccer_teams RENAME TO "+newPrfx+"_israeli_soccer_teams;				" +
	"	ALTER TABLE "+prev+"_nba_players RENAME TO "+newPrfx+"_nba_players;									" +
	"	ALTER TABLE "+prev+"_nba_teams RENAME TO "+newPrfx+"_nba_teams;										" +
	"	ALTER TABLE "+prev+"_world_soccer_players RENAME TO "+newPrfx+"_world_soccer_players;				" +
	"	ALTER TABLE "+prev+"_world_soccer_teams RENAME TO "+newPrfx+"_world_soccer_teams;					");		
		
		stmt.close();
	}
	
	public static void copy_tables(Connection conn, String oldPrfx, String newPrfx) throws SQLException{
		Statement stmt = conn.createStatement();
		stmt.execute(	
			
	"	CREATE TABLE "+newPrfx+"_cinema_actors LIKE "+oldPrfx+"_cinema_actors;								" +
	"	INSERT "+newPrfx+"_cinema_actors SELECT * FROM "+oldPrfx+"_cinema_actors;							" +	
	"	CREATE TABLE "+newPrfx+"_cinema_movies LIKE "+oldPrfx+"_cinema_movies;								" +
	"	INSERT "+newPrfx+"_cinema_movies SELECT * FROM "+oldPrfx+"_cinema_movies;							" +	
	"	CREATE TABLE "+newPrfx+"_cinema_tags LIKE "+oldPrfx+"_cinema_tags;									" +
	"	INSERT "+newPrfx+"_cinema_tags SELECT * FROM "+oldPrfx+"_cinema_tags;								" +	
	"	CREATE TABLE "+newPrfx+"_places_countries LIKE "+oldPrfx+"_places_countries;						" +
	"	INSERT "+newPrfx+"_places_countries SELECT * FROM "+oldPrfx+"_places_countries;						" +	
	"	CREATE TABLE "+newPrfx+"_places_locations LIKE "+oldPrfx+"_places_locations;						" +
	"	INSERT "+newPrfx+"_places_locations SELECT * FROM "+oldPrfx+"_places_locations;						" +
	"	CREATE TABLE "+newPrfx+"_music_artists LIKE "+oldPrfx+"_music_artists;								" +
	"	INSERT "+newPrfx+"_music_artists SELECT * FROM "+oldPrfx+"_music_artists;							" +
	"	CREATE TABLE "+newPrfx+"_music_creations LIKE "+oldPrfx+"_music_creations;							" +
	"	INSERT "+newPrfx+"_music_creations SELECT * FROM "+oldPrfx+"_music_creations;						" +
	"	CREATE TABLE "+newPrfx+"_israeli_soccer_players LIKE "+oldPrfx+"_israeli_soccer_players;			" +
	"	INSERT "+newPrfx+"_israeli_soccer_players SELECT * FROM "+oldPrfx+"_israeli_soccer_players;			" +
	"	CREATE TABLE "+newPrfx+"_israeli_soccer_teams LIKE "+oldPrfx+"_israeli_soccer_teams;				" +
	"	INSERT "+newPrfx+"_israeli_soccer_teams SELECT * FROM "+oldPrfx+"_israeli_soccer_teams;				" +
	"	CREATE TABLE "+newPrfx+"_nba_players LIKE "+oldPrfx+"_nba_players;									" +
	"	INSERT "+newPrfx+"_nba_players SELECT * FROM "+oldPrfx+"_nba_players;								" +
	"	CREATE TABLE "+newPrfx+"_nba_teams LIKE "+oldPrfx+"_nba_teams;										" +
	"	INSERT "+newPrfx+"_nba_teams SELECT * FROM "+oldPrfx+"_nba_teams;									" +
	"	CREATE TABLE "+newPrfx+"_world_soccer_players LIKE "+oldPrfx+"_world_soccer_players;				" +
	"	INSERT "+newPrfx+"_world_soccer_players SELECT * FROM "+oldPrfx+"_world_soccer_players;				" +
	"	CREATE TABLE "+newPrfx+"_world_soccer_teams LIKE "+oldPrfx+"_world_soccer_teams;					" +
	"	INSERT "+newPrfx+"_world_soccer_teams SELECT * FROM "+oldPrfx+"_world_soccer_teams;					" +
	
	"	CREATE TABLE "+newPrfx+"_cinema_actor_movie LIKE "+oldPrfx+"_cinema_actor_movie;					" +
	"	ALTER TABLE "+newPrfx+"_cinema_actor_movie ADD FOREIGN KEY(actor_id) 								" +
	"	REFERENCES "+newPrfx+"_cinema_actors (id) ON DELETE CASCADE ON UPDATE CASCADE;						" +
	"	ALTER TABLE "+newPrfx+"_cinema_actor_movie ADD FOREIGN KEY(movie_id) 								" +
	"	REFERENCES "+newPrfx+"_cinema_movies (id) ON DELETE CASCADE ON UPDATE CASCADE;						" +
	"	CREATE TABLE "+newPrfx+"_cinema_movie_tag LIKE "+oldPrfx+"_cinema_movie_tag;						" +
	"	ALTER TABLE "+newPrfx+"_cinema_movie_tag ADD FOREIGN KEY(tag_id) 									" +
	"	REFERENCES "+newPrfx+"_cinema_tags (id) ON DELETE CASCADE ON UPDATE CASCADE;						" +
	"	ALTER TABLE "+newPrfx+"_cinema_movie_tag ADD FOREIGN KEY(movie_id) 									" +
	"	REFERENCES "+newPrfx+"_cinema_movies (id) ON DELETE CASCADE ON UPDATE CASCADE;						" +
	"	CREATE TABLE "+newPrfx+"_places_location_country LIKE "+oldPrfx+"_places_location_country;			" +
	"	ALTER TABLE "+newPrfx+"_places_location_country ADD FOREIGN KEY(location_id) 						" +
	"	REFERENCES "+newPrfx+"_places_locations (id) ON DELETE CASCADE ON UPDATE CASCADE;					" +
	"	ALTER TABLE "+newPrfx+"_places_location_country ADD FOREIGN KEY(country_id) 						" +
	"	REFERENCES "+newPrfx+"_places_countries (id) ON DELETE CASCADE ON UPDATE CASCADE;					" +
	"	CREATE TABLE "+newPrfx+"_israeli_soccer_player_team LIKE "+oldPrfx+"_israeli_soccer_player_team;	" +
	"	ALTER TABLE "+newPrfx+"_israeli_soccer_player_team ADD FOREIGN KEY(player_id) 						" +
	"	REFERENCES "+newPrfx+"_israeli_soccer_players (id) ON DELETE CASCADE ON UPDATE CASCADE;				" +
	"	ALTER TABLE "+newPrfx+"_israeli_soccer_player_team ADD FOREIGN KEY(team_id) 						" +
	"	REFERENCES "+newPrfx+"_israeli_soccer_teams (id) ON DELETE CASCADE ON UPDATE CASCADE;				" +
	"	CREATE TABLE "+newPrfx+"_music_artist_creation LIKE "+oldPrfx+"_music_artist_creation;				" +
	"	ALTER TABLE "+newPrfx+"_music_artist_creation ADD FOREIGN KEY(artist_id) 							" +
	"	REFERENCES "+newPrfx+"_music_artists (id) ON DELETE CASCADE ON UPDATE CASCADE;						" +
	"	ALTER TABLE "+newPrfx+"_music_artist_creation ADD FOREIGN KEY(creation_id) 							" +
	"	REFERENCES "+newPrfx+"_music_creations (id) ON DELETE CASCADE ON UPDATE CASCADE;					" +
	"	CREATE TABLE "+newPrfx+"_nba_player_team LIKE "+oldPrfx+"_nba_player_team;							" +
	"	ALTER TABLE "+newPrfx+"_nba_player_team ADD FOREIGN KEY(player_id) 									" +
	"	REFERENCES "+newPrfx+"_nba_players (id) ON DELETE CASCADE ON UPDATE CASCADE;						" +
	"	ALTER TABLE "+newPrfx+"_nba_player_team ADD FOREIGN KEY(team_id) 									" +
	"	REFERENCES "+newPrfx+"_nba_teams (id) ON DELETE CASCADE ON UPDATE CASCADE;							" +
	"	CREATE TABLE "+newPrfx+"_world_soccer_player_team LIKE "+oldPrfx+"_world_soccer_player_team;		" +
	"	ALTER TABLE "+newPrfx+"_world_soccer_player_team ADD FOREIGN KEY(player_id) 						" +
	"	REFERENCES "+newPrfx+"_world_soccer_players (id) ON DELETE CASCADE ON UPDATE CASCADE;				" +
	"	ALTER TABLE "+newPrfx+"_world_soccer_player_team ADD FOREIGN KEY(team_id) 							" +
	"	REFERENCES "+newPrfx+"_world_soccer_teams (id) ON DELETE CASCADE ON UPDATE CASCADE;					" +
	
	"	INSERT "+newPrfx+"_cinema_actor_movie SELECT * FROM "+oldPrfx+"_cinema_actor_movie;					" +
	"	INSERT "+newPrfx+"_cinema_movie_tag SELECT * FROM "+oldPrfx+"_cinema_movie_tag;						" +
	"	INSERT "+newPrfx+"_places_location_country SELECT * FROM "+oldPrfx+"_places_location_country;		" +
	"	INSERT "+newPrfx+"_israeli_soccer_player_team SELECT * FROM "+oldPrfx+"_israeli_soccer_player_team;	" +
	"	INSERT "+newPrfx+"_music_artist_creation SELECT * FROM "+oldPrfx+"_music_artist_creation;			" +
	"	INSERT "+newPrfx+"_nba_player_team SELECT * FROM "+oldPrfx+"_nba_player_team;						" +
	"	INSERT "+newPrfx+"_world_soccer_player_team SELECT * FROM "+oldPrfx+"_world_soccer_player_team;		");		
		
		stmt.close();
	}
	
	public static void create_music(Connection conn, String prfx) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.execute(

"	CREATE TABLE "+prfx+"_music_artists															" +
"	(																							" +
"		id int PRIMARY KEY auto_increment NOT NULL,												" +
"		name VARCHAR(150) NOT NULL,																" +
"		num_links int DEFAULT 10000000,															" +
"		birth_year int,																			" +
"		used int DEFAULT 0,																		" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_music_creations														" +
"	(																							" +
"		id int PRIMARY KEY auto_increment NOT NULL,												" +
"		name VARCHAR(150) NOT NULL,																" +
"		num_links int NOT NULL,																	" +
"		year_made int NOT NULL,																	" +
"		used int DEFAULT 0,																		" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_music_artist_creation													" +
"	(																							" +
"		artist_id int NOT NULL,																	" +
"		creation_id int NOT NULL,																" +
"		PRIMARY KEY(artist_id, creation_id),													" +
"		INDEX(creation_id),																		" +
"		FOREIGN KEY (artist_id) REFERENCES "+prfx+"_music_artists(id)							" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE,																	" +
"		FOREIGN KEY (creation_id) REFERENCES "+prfx+"_music_creations(id)						" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE																	" +
"	) ENGINE = InnoDB;																			");

		stmt.close();
	}
	
	public static void create_cinema(Connection conn, String prfx) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.execute(

"	CREATE TABLE "+prfx+"_cinema_movies															" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		num_links int DEFAULT 10000000,															" +
"		year_made int,																			" +
"		actors_used int DEFAULT 0,																" +
"		movies_used int DEFAULT 0,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_cinema_actors															" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		num_links int DEFAULT 10000000,															" +
"		year_born int,																			" +
"		actors_used int DEFAULT 0,																" +
"		movies_used int DEFAULT 0,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_cinema_actor_movie													" +
"	(																							" +
"		actor_id int NOT NULL,																	" +
"		movie_id int NOT NULL,																	" +
"		INDEX(movie_id),																		" +
"		PRIMARY KEY(actor_id, movie_id),														" +
"		FOREIGN KEY (actor_id) REFERENCES "+prfx+"_cinema_actors(id)							" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE,																	" +
"		FOREIGN KEY (movie_id) REFERENCES "+prfx+"_cinema_movies(id)							" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE																	" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_cinema_tags															" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_cinema_movie_tag														" +
"	(																							" +
"		movie_id int NOT null,																	" +
"		tag_id int NOT null,																	" +
"		INDEX(tag_id),																			" +
"		PRIMARY KEY(movie_id, tag_id),															" +
"		FOREIGN KEY (movie_id) REFERENCES "+prfx+"_cinema_movies(id)							" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE,																	" +
"		FOREIGN KEY (tag_id) REFERENCES "+prfx+"_cinema_tags(id)								" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE																	" +
"	) ENGINE = InnoDB;																			");

		stmt.close();
	}
	
	public static void create_places(Connection conn, String prfx) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.execute(

"	CREATE TABLE "+prfx+"_places_countries (																" +
"	  id int PRIMARY KEY auto_increment, 														" +
"	  `Name` varchar(150) NOT NULL,																" +
"	  `Area (1000 km^2)` double DEFAULT NULL,													" +
"	  `GDP per capita (1000 $)` double DEFAULT NULL,											" +
"	  `Population (million)` double DEFAULT NULL,												" +
"	  `Capital` varchar(50) DEFAULT NULL,														" +
"	  `GDP (billion $)` double DEFAULT null,													" +
"	  used int DEFAULT 0,																		" +
"	  UNIQUE(`Name`)																			" +
"	) ENGINE=InnoDB;																			" +

"	CREATE TABLE "+prfx+"_places_locations (																" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		num_links int DEFAULT 10000000,															" +
"		population int,																			" +
"	    used int DEFAULT 0,																		" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_places_location_country														" +
"	(																							" +
"		location_id int NOT NULL,																" +
"		country_id int NOT NULL,																" +
"		PRIMARY KEY(location_id, country_id),													" +
"	  	INDEX(country_id),																		" +
"		FOREIGN KEY (location_id) REFERENCES "+prfx+"_places_locations(id)								" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE,													 				" +
"		FOREIGN KEY (country_id) REFERENCES "+prfx+"_places_countries(id)								" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE													 				" +
"	) ENGINE = InnoDB;																			");

		stmt.close();
	}
	
	public static void create_sports(Connection conn, String prfx) throws SQLException{
		Statement stmt = conn.createStatement();
		
		create_tables_league("nba", stmt, prfx);
		create_tables_league("israeli_soccer", stmt, prfx);
		create_tables_league("world_soccer", stmt, prfx);
		
		stmt.close();
	}
	
	public static void create_tables_league(String league, Statement stmt, String prfx) throws SQLException{
		
		stmt.execute(

"	CREATE TABLE "+prfx+"_"+league+"_teams																" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		links_to_team int DEFAULT 10000000,														" +
"		creation_year int,																		" +
"	 	used int DEFAULT 0,																		" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_"+league+"_players																" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		links_to_player int DEFAULT 10000000,													" +
"		birth_year int,																			" +
"	  	used int DEFAULT 0,																		" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_"+league+"_player_team															" +
"	(																							" +
"		team_id int NOT NULL,																	" +
"		player_id int NOT NULL,																	" +
"		INDEX(player_id),																		" +
"		PRIMARY KEY(team_id, player_id),														" +
"		FOREIGN KEY (team_id) REFERENCES "+prfx+"_"+league+"_teams(id)									" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE,																	" +
"		FOREIGN KEY (player_id) REFERENCES "+prfx+"_"+league+"_players(id)								" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE																	" +
"	) ENGINE = InnoDB;																			");

		
	}
}
