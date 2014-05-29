package db_set_level;

import java.sql.*;
import java.io.IOException;

public class Movies_set_level {
	
	public static void set_level(Connection conn, int[] categories, int level) throws ClassNotFoundException, SQLException, IOException{
		
		Statement stmt = conn.createStatement();
		
		String[] category_comment = new String[19], category_uncomment = new String[19];
		
		for(int i=0;i<19;i++){
			if(categories[i]!=0)
			{
				category_comment[i]=category_uncomment[i]="";
			}
			else
			{
				category_comment[i]="/*";
				category_uncomment[i]="*/";
			}
		}

		ResultSet rst = stmt.executeQuery(
"			select count(distinct m.name)	" +
"			from curr_cinema_movies m	" +
"			inner join curr_cinema_movie_tag mt on m.id=mt.movie_id	" +
"			inner join curr_cinema_tags t on t.id=mt.tag_id	" +
"			inner join	" +
"				(select am.movie_id from curr_cinema_actor_movie am group by movie_id having count(*)>3) am	" +
"				on am.movie_id=m.id	" +
"			where	0=1 " +
				category_comment[0] + "	or	t.name='action'	" + category_uncomment[0] + "	" +
				category_comment[1] + "	or 	t.name='adventure'	" + category_uncomment[1] + "	" +
				category_comment[2] + "	or 	t.name='animated'	" + category_uncomment[2] + "	" +
				category_comment[3] + "	or 	t.name='comedy'	" + category_uncomment[3] + "	" +
				category_comment[4] + "	or 	t.name='crime'	" + category_uncomment[4] + "	" +
				category_comment[5] + "	or 	t.name='documentary'	" + category_uncomment[5] + "	" +
				category_comment[6] + "	or 	t.name='drama'	" + category_uncomment[6] + "	" +
				category_comment[7] + "	or 	t.name='fantasy'	" + category_uncomment[7] + "	" +
				category_comment[8] + "	or 	t.name='horror'	" + category_uncomment[8] + "	" + "	" +
				category_comment[9] + "	or 	t.name='musical'	" + category_uncomment[9] + "	" +
				category_comment[10]+ "	or 	t.name='mystery'	" + category_uncomment[10] + "	" +
				category_comment[11]+ "	or 	t.name='romance'	" + category_uncomment[11] + "	" +
				category_comment[12]+ "	or 	t.name='science_fiction'	" + category_uncomment[12] + "	" +
				category_comment[13]+ "	or 	t.name='sports'	" + category_uncomment[13] + "	" +
				category_comment[14]+ "	or 	t.name='teen'	" + category_uncomment[14] + "	" +
				category_comment[15]+ "	or 	t.name='television'	" + category_uncomment[15] + "	" +
				category_comment[16]+ "	or 	t.name='thriller'	" + category_uncomment[16] + "	" +
				category_comment[17]+ "	or 	t.name='war'	" + category_uncomment[17] +  "	" +
				category_comment[18]+ "	or 	t.name='western'	" + category_uncomment[18] + "	");
		
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
"				where  1=0 " +
				category_comment[0] + "	or	t.name='action'	" + category_uncomment[0] + "	" +
				category_comment[1] + "	or 	t.name='adventure'	" + category_uncomment[1] + "	" +
				category_comment[2] + "	or 	t.name='animated'	" + category_uncomment[2] + "	" +
				category_comment[3] + "	or 	t.name='comedy'	" + category_uncomment[3] + "	" +
				category_comment[4] + "	or 	t.name='crime'	" + category_uncomment[4] + "	" +
				category_comment[5] + "	or 	t.name='documentary'	" + category_uncomment[5] + "	" +
				category_comment[6] + "	or 	t.name='drama'	" + category_uncomment[6] + "	" +
				category_comment[7] + "	or 	t.name='fantasy'	" + category_uncomment[7] + "	" +
				category_comment[8] + "	or 	t.name='horror'	" + category_uncomment[8] + "	" +
				category_comment[9] + "	or 	t.name='musical'	" + category_uncomment[9] + "	" +
				category_comment[10]+ "	or 	t.name='mystery'	" + category_uncomment[10] + "	" +
				category_comment[11]+ "	or 	t.name='romance'	" + category_uncomment[11] + "	" +
				category_comment[12]+ "	or 	t.name='science_fiction'	" + category_uncomment[12] + "	" +
				category_comment[13]+ "	or 	t.name='sports'	" + category_uncomment[13] + "	" +
				category_comment[14]+ "	or 	t.name='teen'	" + category_uncomment[14] + "	" +
				category_comment[15]+ "	or 	t.name='television'	" + category_uncomment[15] + "	" +
				category_comment[16]+ "	or 	t.name='thriller'	" + category_uncomment[16] + "	" +
				category_comment[17]+ "	or 	t.name='war'	" + category_uncomment[17] +  "	" +
				category_comment[18]+ "	or 	t.name='western'	" + category_uncomment[18] + "	" +
"				order by num_links desc	" +
"				limit "+limit.toString()+") x);");

		rst.close();
		stmt.close();
	}
}
