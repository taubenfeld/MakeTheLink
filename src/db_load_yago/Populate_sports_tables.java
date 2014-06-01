package db_load_yago;

import java.sql.*;

import java.sql.Statement;

public class Populate_sports_tables {
	
	public static void build(Connection conn) throws SQLException{
		
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
		
		build_rest("nba", stmt);
		
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
		
		build_rest("world_soccer", stmt);
		
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
		
		build_rest("israeli_soccer", stmt);
		
		stmt.close();
	}
	

	public static void build_rest(String league, Statement stmt) throws SQLException{
		
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
}
