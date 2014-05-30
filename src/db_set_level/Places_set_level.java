package db_set_level;

import java.sql.*;
import java.io.IOException;

public class Places_set_level {
	
	public static void set_level(Connection conn, int ratio) throws ClassNotFoundException, SQLException, IOException{
		
		Statement stmt = conn.createStatement();

		ResultSet rst = stmt.executeQuery(
"			select count(*)	" +
"			from curr_places_locations	");
		
		rst.next();
		int num_rows = rst.getInt(1);
		Integer limit = num_rows*ratio/100;
		
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
		
		rst.close();
		stmt.close();
	}
}
