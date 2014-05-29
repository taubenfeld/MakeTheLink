package db_set_level;

import java.sql.*;
import java.io.IOException;

public class Movies_set_level {
	
	public static void set_level(Connection conn, int level) throws ClassNotFoundException, SQLException, IOException{
		
		Statement stmt = conn.createStatement();

		ResultSet rst = stmt.executeQuery(
"			select count(distinct m.name)	" +
"			from curr_cinema_movies m	" +
"			inner join curr_cinema_movie_tag mt on m.id=mt.movie_id	" +
"			inner join curr_cinema_tags t on t.id=mt.tag_id	" +
"			inner join	" +
"				(select am.movie_id from curr_cinema_actor_movie am group by movie_id having count(*)>3) am	" +
"				on am.movie_id=m.id	");
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = num_rows*level/100;
		
		stmt.executeUpdate(" update curr_cinema_movies set used=0; ");
		
		stmt.executeUpdate(
"				update curr_cinema_movies set used=1 where id in	" +
"				(select x.id from (select distinct m.id	" +
"				from curr_cinema_movies m 	" +
"				inner join curr_cinema_movie_tag mt on m.id=mt.movie_id	" +
"				inner join curr_cinema_tags t on t.id=mt.tag_id	" +
"				inner join 	" +
"					(select am.movie_id from curr_cinema_actor_movie am group by movie_id having count(*)>3) am	" +
"					on am.movie_id=m.id	" +
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");

		rst.close();
		stmt.close();
	}
}
