package MakeTheLink.db;

import java.sql.*;
import java.io.IOException;

public class Questions_set_level {
	
	public static int actors_set_level(Connection conn, int year, int ratio) 
						throws ClassNotFoundException, SQLException, IOException{
		
		Statement stmt = conn.createStatement();
		
		Integer year_born = year;
		
		ResultSet rst = stmt.executeQuery(
"			select count(distinct m.name)	" +
"			from curr_cinema_movies m ");
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = num_rows*ratio/100*(int)Math.sqrt(ratio)/10;
		
		stmt.executeUpdate(" update curr_cinema_movies set actors_used=0; ");
		
		stmt.executeUpdate(
"				update curr_cinema_movies set actors_used=1 where id in	" +
"				(select x.id from (select distinct m.id	" +
"				from curr_cinema_movies m 	" +
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");

		rst = stmt.executeQuery(
"			select count(distinct a.name)	" +
"			from curr_cinema_actors a	" +
"			inner join	" +
"					(select am.actor_id" +
"					from curr_cinema_actor_movie am " +
"					inner join curr_cinema_movies m on am.movie_id=m.id" +
"					where m.actors_used=1		" +
"					group by actor_id having count(*)>4) am	" +
"				on am.actor_id=a.id	" +

"			where year_born >= "+year_born.toString()+"	");
		
		rst.next();
		num_rows = rst.getInt(1);
		limit = num_rows*ratio/100*(int)Math.sqrt(ratio)/10;
		
		stmt.executeUpdate(" update curr_cinema_actors set actors_used=0; ");
		
		stmt.executeUpdate(
"				update curr_cinema_actors set actors_used=1 where id in	" +
"				(select x.id from (select distinct a.id	" +
"				from curr_cinema_actors a 	" +
"				inner join 	" +
"						(select am.actor_id" +
"						from curr_cinema_actor_movie am " +
"						inner join curr_cinema_movies m on am.movie_id=m.id" +
"						where m.actors_used=1		" +
"						group by actor_id having count(*)>4) am	" +
"					on am.actor_id=a.id	" +
"				where year_born >= "+year_born.toString()+
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");
			
		rst = stmt.executeQuery(" select count(*) from curr_cinema_actors where actors_used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);

		rst.close();
		stmt.close();
		
		return num_choices>=4?1:0;
	}
	
	public static int movies_set_level(Connection conn, int year, int ratio)
			throws ClassNotFoundException, SQLException, IOException{

		Statement stmt = conn.createStatement();
		
		Integer year_made = year;
		
		ResultSet rst = stmt.executeQuery(
"			select count(distinct a.name)	" +
"			from curr_cinema_actors a ");
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = num_rows*ratio/100*(int)Math.sqrt(ratio)/10;
		
		stmt.executeUpdate(" update curr_cinema_actors set movies_used=0; ");
		
		stmt.executeUpdate(
"				update curr_cinema_actors set movies_used=1 where id in	" +
"				(select x.id from (select distinct a.id	" +
"				from curr_cinema_actors a 	" +
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");
		
		rst = stmt.executeQuery(
"			select count(distinct m.name)	" +
"			from curr_cinema_movies m	" +
"			inner join curr_cinema_movie_tag mt on m.id=mt.movie_id	" +
"			inner join curr_cinema_tags t on t.id=mt.tag_id	" +
"			inner join	" +
"					(select am.movie_id " +
"					from curr_cinema_actor_movie am " +
"					inner join curr_cinema_actors a on am.actor_id=a.id	"+
"					where a.movies_used=1	" +
"					group by movie_id having count(*)>4) am	" +
"				on am.movie_id=m.id	" + 
"			where year_made >= "+year_made.toString()+"	");
		
		rst.next();
		num_rows = rst.getInt(1);
		limit = num_rows*ratio/100*(int)Math.sqrt(ratio)/10;
		
		stmt.executeUpdate(" update curr_cinema_movies set movies_used=0; ");
		
		stmt.executeUpdate(
"				update curr_cinema_movies set movies_used=1 where id in	" +
"				(select x.id from (select distinct m.id	" +
"				from curr_cinema_movies m 	" +
"				inner join curr_cinema_movie_tag mt on m.id=mt.movie_id	" +
"				inner join curr_cinema_tags t on t.id=mt.tag_id	" +
"				inner join 	" +
"						(select am.movie_id " +
"						from curr_cinema_actor_movie am " +
"						inner join curr_cinema_actors a on am.actor_id=a.id	"+
"						where a.movies_used=1	" +
"						group by movie_id having count(*)>4) am	" +
"					on am.movie_id=m.id	" +
"				where year_made >= "+year_made.toString()+
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");
	
		rst = stmt.executeQuery(" select count(*) from curr_cinema_movies where movies_used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);
		
		rst.close();
		stmt.close();
		
		return num_choices>=4?1:0;
	}
	
	public static int music_set_level(Connection conn, int year, int ratio)
			throws ClassNotFoundException, SQLException, IOException{

		Statement stmt = conn.createStatement();
		
		Integer year_born = year;
		
		
		
		ResultSet rst = stmt.executeQuery(
"			select count(distinct c.name)	" +
"			from curr_music_creations c ");
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = num_rows*ratio/100;
		
		stmt.executeUpdate(" update curr_music_creations set used=0; ");
		
		stmt.executeUpdate(
"				update curr_music_creations set used=1 where id in	" +
"				(select x.id from (select distinct c.id	" +
"				from curr_music_creations c 	" +
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");
	
		rst = stmt.executeQuery(
"			select count(distinct a.name)	" +
"			from curr_music_artists a	" +
"			inner join	" +
"				(select ac.artist_id " +
"				from curr_music_artist_creation ac " +
"				inner join curr_music_creations c on ac.creation_id=c.id	" +
"				where c.used=1	" +
"				group by artist_id having count(*)>4) ac	" +
"				on ac.artist_id=a.id	" +
"			where birth_year >= "+year_born.toString()+"	");
	
		rst.next();
		num_rows = rst.getInt(1);
		limit = num_rows*ratio/100*(int)Math.sqrt(ratio)/10;
		
		stmt.executeUpdate(" update curr_music_artists set used=0; ");
		
		stmt.executeUpdate(
"				update curr_music_artists set used=1 where id in	" +
"				(select x.id from (select distinct a.id	" +
"				from curr_music_artists a 	" +
"				inner join 	" +
"						(select ac.artist_id " +
"						from curr_music_artist_creation ac " +
"						inner join curr_music_creations c on ac.creation_id=c.id	" +
"						where c.used=1	" +
"						group by artist_id having count(*)>4) ac	" +
"					on ac.artist_id=a.id	" +
"				where birth_year >= "+year_born.toString()+
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");
	
		rst = stmt.executeQuery(" select count(*) from curr_music_artists where used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);
		
		rst.close();
		stmt.close();
		
		return num_choices>=4?1:0;
	}
	
	public static int places_set_level(Connection conn, int ratio) 
			throws ClassNotFoundException, SQLException, IOException{
		
		Statement stmt = conn.createStatement();
	
		ResultSet rst = stmt.executeQuery(
"			select count(*)	" +
"			from curr_places_locations	");
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = num_rows*ratio/100*ratio/100*ratio/100;
		
		stmt.executeUpdate(" update curr_places_locations set used=0; ");
		
		rst = stmt.executeQuery(
"		select min(n) from (select num_links n from curr_places_locations order by num_links desc limit "+
		limit.toString()+ ") x; ");
		
		rst.next();
		Integer threshold = rst.getInt(1);
		
		stmt.executeUpdate(" update curr_places_locations set used=1 where num_links >= "+threshold.toString()+"; ");
		
		stmt.executeUpdate(" update curr_places_countries set used=0; ");
		
		stmt.executeUpdate(
			"update curr_places_countries set used=1 where Name in" +
"				(select Name from (select c.Name from curr_places_countries c, " +
"							curr_places_location_country lc, curr_places_locations l" +
"				where l.id=lc.location_id and lc.country_id=c.id and l.used=1" +
"				group by c.Name" +
"				having count(*)>3) x) ");
	
		rst = stmt.executeQuery(" select count(*) from curr_places_countries where used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);
		
		rst.close();
		stmt.close();
		
		return num_choices>=4?1:0;
	}
	
	
	public static int[] sports_set_level(Connection conn, int year, int ratio)
			throws ClassNotFoundException, SQLException, IOException{
		
		int[] enough_entities = new int[3];
		
		enough_entities[0] = set_league_level(conn, year, ratio, "nba");
		enough_entities[1] = set_league_level(conn, year, ratio, "world_soccer");
		enough_entities[2] = set_league_level(conn, year, ratio, "israeli_soccer");
		
		return enough_entities;
	}
	
	
	public static int set_league_level(Connection conn, int year, int ratio, String league) 
											throws ClassNotFoundException, SQLException, IOException{
		
		Statement stmt = conn.createStatement();

		Integer year_born=year;
		
		ResultSet rst = stmt.executeQuery(
"			select count(*)	" +
"			from curr_" + league + "_players	" +
"			where birth_year >= "+year_born.toString());
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = league.compareTo("world_soccer")==0?
								num_rows*ratio/100*ratio/100*ratio/100:
									num_rows*ratio/100*(int)Math.sqrt(ratio)/10;
		
		stmt.executeUpdate(" update curr_"+league+"_players set used=0; ");
		
		rst = stmt.executeQuery(
"		select min(l) from (select links_to_player l " +
"							from curr_" + league+"_players " +
"							where birth_year >= " + year_born.toString() +
"							order by links_to_player desc " +
"							limit " + limit.toString()+ ") x; ");
		
		rst.next();
		Integer threshold = rst.getInt(1);
		
		stmt.executeUpdate(" update curr_" + league + "_players set used=1 " +
"								where links_to_player >= " + threshold.toString() + 
" 									and birth_year >= " + year_born.toString());
		
		stmt.executeUpdate(" update curr_"+league+"_teams set used=0; ");
		
		stmt.executeUpdate(
				"update curr_"+league+"_teams set used=1 where name in" +
"				(select name from (select t.name from curr_"+league+"_teams t, " +
"							curr_"+league+"_player_team pt, curr_"+league+"_players p" +
"				where p.id=pt.player_id and pt.team_id=t.id and p.used=1" +
"				group by t.Name" +
"				having count(*)>5) x) ");
		
		rst = stmt.executeQuery(" select count(*) from curr_"+league+"_teams where used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);
		
		rst.close();
		stmt.close();
		
		return num_choices>=4?1:0;
	}
}
