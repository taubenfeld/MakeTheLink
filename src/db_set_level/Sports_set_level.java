package db_set_level;

import java.sql.*;
import java.io.IOException;

public class Sports_set_level {
	
	
	public static void set_level(Connection conn, int level)throws ClassNotFoundException, SQLException, IOException{
		set_league_level(conn, level, "nba");
		set_league_level(conn, level, "world_soccer");
		set_league_level(conn, level, "israeli_soccer");
	}
	
	
	public static void set_league_level(Connection conn, int level, String league) 
											throws ClassNotFoundException, SQLException, IOException{
		
		Statement stmt = conn.createStatement();

		ResultSet rst = stmt.executeQuery(
"			select count(*)	" +
"			from curr_" + league + "_players	");
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = num_rows*level/100;
		
		stmt.executeUpdate(" update curr_"+league+"_players set used=0; ");
		
		rst = stmt.executeQuery(
"		select min(n) from (select links_to_player n from curr_" + league+
				"_players order by links_to_player desc limit " + limit.toString()+ ") x; ");
		
		rst.next();
		Integer threshold = rst.getInt(1);
		
		stmt.executeUpdate(" update curr_" + league + 
				"_players set used=1 where links_to_player >= " + threshold.toString() + "; ");
		
		stmt.executeUpdate(" update curr_"+league+"_teams set used=0; ");
		
		stmt.executeUpdate(
				"update curr_"+league+"_teams set used=1 where name in" +
"				(select name from (select t.name from curr_"+league+"_teams t, " +
"							curr_"+league+"_player_team pt, curr_"+league+"_players p" +
"				where p.id=pt.player_id and pt.team_id=t.id and p.used=1" +
"				group by t.Name" +
"				having count(*)>5) x) ");
		
		rst.close();
		stmt.close();
	}
}
