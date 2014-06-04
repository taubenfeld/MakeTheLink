package MakeTheLink.db;

import java.io.IOException;
import java.sql.*;

public class Copy_yago_data {
	
	
	/* copies from the 'curr' schema to the 'tmp' schema only the user data that 
	 * doesn't conflict with the data in the 'tmp' schema (i.e. has the same name).
	 */
	public static void copy(Connection conn) throws ClassNotFoundException, SQLException, IOException{
		
		System.out.println("copying final data to schema...");
		
		//copy yago data from tmp to curr
		copy_cinema(conn);
		copy_music(conn);
		copy_places(conn);
		copy_sports(conn);
	}
	
	public static void copy_cinema(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(
				" REPLACE INTO curr_cinema_actors " +
				" SELECT * FROM tmp_cinema_actors ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_cinema_movies " +
				" SELECT * FROM tmp_cinema_movies ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_cinema_tags " +
				" SELECT * FROM tmp_cinema_tags ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_cinema_actor_movie(actor_id, movie_id) " +
				" SELECT ya.id, ym.id" +
				" FROM curr_cinema_actors ya " +
				" INNER JOIN tmp_cinema_actors ca ON ya.name=ca.name " +
				" INNER JOIN tmp_cinema_actor_movie cam ON ca.id=cam.actor_id " +
				" INNER JOIN tmp_cinema_movies cm ON cam.movie_id=cm.id " +
				" INNER JOIN curr_cinema_movies ym ON cm.name=ym.name ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_cinema_movie_tag(movie_id, tag_id) " +
				" SELECT ym.id, yt.id" +
				" FROM curr_cinema_movies ym " +
				" INNER JOIN tmp_cinema_movies cm ON ym.name=cm.name " +
				" INNER JOIN tmp_cinema_movie_tag cmt ON cm.id=cmt.movie_id " +
				" INNER JOIN tmp_cinema_tags ct ON cmt.tag_id=ct.id " +
				" INNER JOIN curr_cinema_tags yt ON ct.name=yt.name ");
		
		stmt.close();
	}
	
	public static void copy_music(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(
				" REPLACE INTO curr_music_artists " +
				" SELECT * FROM tmp_music_artists ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_music_creations " +
				" SELECT * FROM tmp_music_creations ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_music_artist_creation(artist_id, creation_id) " +
				" SELECT ya.id, yc.id" +
				" FROM curr_music_artists ya " +
				" INNER JOIN tmp_music_artists ca ON ya.name=ca.name " +
				" INNER JOIN tmp_music_artist_creation cac ON ca.id=cac.artist_id " +
				" INNER JOIN tmp_music_creations cc ON cac.creation_id=cc.id " +
				" INNER JOIN curr_music_creations yc ON cc.name=yc.name ");
		
		stmt.close();
	}
	
	public static void copy_places(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(
				" REPLACE INTO curr_places_locations " +
				" SELECT * FROM tmp_places_locations ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_places_countries " +
				" SELECT * FROM tmp_places_countries ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_places_location_country(location_id, country_id) " +
				" SELECT yl.id, yc.id" +
				" FROM curr_places_locations yl " +
				" INNER JOIN tmp_places_locations cl ON yl.name=cl.name " +
				" INNER JOIN tmp_places_location_country clc ON cl.id=clc.location_id " +
				" INNER JOIN tmp_places_countries cc ON clc.country_id=cc.id " +
				" INNER JOIN curr_places_countries yc ON cc.name=yc.name ");
		
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
				" REPLACE INTO curr_"+league+"_players " +
				" SELECT * FROM tmp_"+league+"_players ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_"+league+"_teams " +
				" SELECT * FROM tmp_"+league+"_teams ");
		
		stmt.executeUpdate(
				" REPLACE INTO curr_"+league+"_player_team(team_id, player_id) " +
				" SELECT yt.id, yp.id " +
				" FROM curr_"+league+"_teams yt " +
				" INNER JOIN tmp_"+league+"_teams ct ON yt.name=ct.name " +
				" INNER JOIN tmp_"+league+"_player_team cpt ON ct.id=cpt.team_id " +
				" INNER JOIN tmp_"+league+"_players cp ON cpt.player_id=cp.id " +
				" INNER JOIN curr_"+league+"_players yp ON cp.name=yp.name ");
	}
}
