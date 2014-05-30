package db_set_level;

import java.sql.*;
import java.io.IOException;

public class Actors_set_level {
	
	public static void set_level(Connection conn, int year, int ratio) 
						throws ClassNotFoundException, SQLException, IOException{
		
		Statement stmt = conn.createStatement();
		
		Integer year_born = year;

		ResultSet rst = stmt.executeQuery(
"			select count(distinct a.name)	" +
"			from curr_cinema_actors a	" +
"			inner join	" +
"				(select am.actor_id from curr_cinema_actor_movie am group by actor_id having count(*)>3) am	" +
"				on am.actor_id=a.id	" +
"			where year_born >= "+year_born.toString()+"	");
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = num_rows*ratio/100;
		
		stmt.executeUpdate(" update curr_cinema_actors set used=0; ");
		
		stmt.executeUpdate(
"				update curr_cinema_actors set used=1 where id in	" +
"				(select x.id from (select distinct a.id	" +
"				from curr_cinema_actors a 	" +
"				inner join 	" +
"					(select am.actor_id from curr_cinema_actor_movie am group by actor_id having count(*)>3) am	" +
"					on am.actor_id=a.id	" +
"				where year_born >= "+year_born.toString()+
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");

		rst.close();
		stmt.close();
	}
}
