package MakeTheLink.db;

import java.io.IOException;
import java.sql.*;

public class Yago_to_schema {
	
	public static void copy(Connection conn) throws ClassNotFoundException, SQLException, IOException{
		
		System.out.println("copying final data to schema...");
		
		//copy user-entered data from curr to tmp
		copy_cinema(conn);
		copy_music(conn);
		copy_places(conn);
		copy_sports(conn);
	}
	
	public static void copy_cinema(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_cinema_actors " +
				" SELECT * FROM curr_cinema_actors " +
				" WHERE yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_cinema_movies " +
				" SELECT * FROM curr_cinema_movies " +
				" WHERE yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_cinema_tags " +
				" SELECT * FROM curr_cinema_tags " +
				" WHERE yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_cinema_actor_movie(actor_id, movie_id, yago_data) " +
				" SELECT ya.id, ym.id, 0 " +
				" FROM tmp_cinema_actors ya " +
				" INNER JOIN curr_cinema_actors ca ON ya.name=ca.name " +
				" INNER JOIN curr_cinema_actor_movie cam ON ca.id=cam.actor_id " +
				" INNER JOIN curr_cinema_movies cm ON cam.movie_id=cm.id " +
				" INNER JOIN tmp_cinema_movies ym ON cm.name=ym.name " +
				" WHERE cam.yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_cinema_movie_tag(movie_id, tag_id, yago_data) " +
				" SELECT ym.id, yt.id, 0 " +
				" FROM tmp_cinema_movies ym " +
				" INNER JOIN curr_cinema_movies cm ON ym.name=cm.name " +
				" INNER JOIN curr_cinema_movie_tag cmt ON cm.id=cmt.movie_id " +
				" INNER JOIN curr_cinema_tags ct ON cmt.tag_id=ct.id " +
				" INNER JOIN tmp_cinema_tags yt ON ct.name=yt.name " +
				" WHERE cmt.yago_data=0; ");
		
		stmt.close();
	}
	
	public static void copy_music(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_music_artists " +
				" SELECT * FROM curr_music_artists " +
				" WHERE yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_music_creations " +
				" SELECT * FROM curr_music_creations " +
				" WHERE yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_music_artist_creation(artist_id, creation_id, yago_data) " +
				" SELECT ya.id, yc.id, 0 " +
				" FROM tmp_music_artists ya " +
				" INNER JOIN curr_music_artists ca ON ya.name=ca.name " +
				" INNER JOIN curr_music_artist_creation cac ON ca.id=cac.artist_id " +
				" INNER JOIN curr_music_creations cc ON cac.creation_id=cc.id " +
				" INNER JOIN tmp_music_creations yc ON cc.name=yc.name " +
				" WHERE cac.yago_data=0; ");
		
		stmt.close();
	}
	
	public static void copy_places(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_places_locations " +
				" SELECT * FROM curr_places_locations " +
				" WHERE yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_places_countries " +
				" SELECT * FROM curr_places_countries " +
				" WHERE yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_places_location_country(location_id, country_id, yago_data) " +
				" SELECT yl.id, yc.id, 0 " +
				" FROM tmp_places_locations yl " +
				" INNER JOIN curr_places_locations cl ON yl.name=cl.name " +
				" INNER JOIN curr_places_location_country clc ON cl.id=clc.location_id " +
				" INNER JOIN curr_places_countries cc ON clc.country_id=cc.id " +
				" INNER JOIN tmp_places_countries yc ON cc.name=yc.name " +
				" WHERE clc.yago_data=0; ");
		
		stmt.close();
	}
	
	public static void copy_sports(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		
		copy_league(stmt, "nba");
		copy_league(stmt, "world_soccer");
		copy_league(stmt, "israeli_soccer");
		
		
		stmt.close();
		
	}
	
	public static void copy_league(Statement stmt, String league) throws SQLException{
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_"+league+"_players " +
				" SELECT * FROM curr_"+league+"_players " +
				" WHERE yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_"+league+"_teams " +
				" SELECT * FROM curr_"+league+"_teams " +
				" WHERE yago_data=0; ");
		
		stmt.executeUpdate(
				" INSERT IGNORE INTO tmp_"+league+"_player_team(team_id, player_id, yago_data) " +
				" SELECT yt.id, yp.id, 0 " +
				" FROM tmp_"+league+"_teams yt " +
				" INNER JOIN curr_"+league+"_teams ct ON yt.name=ct.name " +
				" INNER JOIN curr_"+league+"_player_team cpt ON ct.id=cpt.team_id " +
				" INNER JOIN curr_"+league+"_players cp ON cpt.player_id=cp.id " +
				" INNER JOIN tmp_"+league+"_players yp ON cp.name=yp.name " +
				" WHERE cpt.yago_data=0; ");
	}
}
