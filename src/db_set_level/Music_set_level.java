package db_set_level;

import java.sql.*;
import java.io.IOException;

public class Music_set_level {
	
	public static void set_level(Connection conn, int year, int ratio)
					throws ClassNotFoundException, SQLException, IOException{
		
		Statement stmt = conn.createStatement();
		
		Integer year_born = year;

		ResultSet rst = stmt.executeQuery(
"			select count(distinct a.name)	" +
"			from curr_music_artists a	" +
"			inner join	" +
"				(select ac.artist_id from curr_music_artist_creation ac group by artist_id having count(*)>4) ac	" +
"				on ac.artist_id=a.id	" +
"			where birth_year >= "+year_born.toString()+"	");
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = num_rows*ratio/100;
		
		stmt.executeUpdate(" update curr_music_artists set used=0; ");
		
		stmt.executeUpdate(
"				update curr_music_artists set used=1 where id in	" +
"				(select x.id from (select distinct a.id	" +
"				from curr_music_artists a 	" +
"				inner join 	" +
"					(select ac.artist_id from curr_music_artist_creation ac group by artist_id having count(*)>4) ac	" +
"					on ac.artist_id=a.id	" +
"				where birth_year >= "+year_born.toString()+
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");

		rst.close();
		stmt.close();
	}
}
