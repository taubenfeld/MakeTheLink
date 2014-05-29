package db_manage_tables;

import java.sql.*;

public class Create_sports_tables {

	public static void create(Connection conn, String prfx) throws SQLException{
		Statement stmt = conn.createStatement();
		
		create_tables("nba", stmt, prfx);
		create_tables("israeli_soccer", stmt, prfx);
		create_tables("world_soccer", stmt, prfx);
		
		stmt.close();
	}
	
	public static void create_tables(String league, Statement stmt, String prfx) throws SQLException{
		
		stmt.execute(

"	CREATE TABLE "+prfx+"_"+league+"_teams																" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		links_to_team int DEFAULT 10000000,														" +
"		creation_year int,																		" +
"	 	used int DEFAULT 0,																		" +
"	  	yago_data int DEFAULT 1,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_"+league+"_players																" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		links_to_player int DEFAULT 10000000,													" +
"		birth_year int,																			" +
"	  	used int DEFAULT 0,																		" +
"	  	yago_data int DEFAULT 1,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_"+league+"_player_team															" +
"	(																							" +
"		team_id int NOT NULL,																	" +
"		player_id int NOT NULL,																	" +
"	  	yago_data int DEFAULT 1,																" +
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
