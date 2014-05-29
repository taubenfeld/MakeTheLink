package db_manage_tables;

import java.sql.*;

public class Create_places_tables {

	public static void create(Connection conn, String prfx) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.execute(

"	CREATE TABLE "+prfx+"_places_countries (																" +
"	  id int PRIMARY KEY auto_increment, 														" +
"	  `Name` varchar(150) NOT NULL,																" +
"	  `Area (1000 km^2)` double DEFAULT NULL,													" +
"	  `GDP per capita (1000 $)` double DEFAULT NULL,											" +
"	  `Population (million)` double DEFAULT NULL,												" +
"	  `Capital` varchar(50) DEFAULT NULL,														" +
"	  `GDP (billion $)` double DEFAULT null,													" +
"	  used int DEFAULT 0,																		" +
"	  yago_data int DEFAULT 1,																	" +
"	  UNIQUE(`Name`)																			" +
"	) ENGINE=InnoDB;																			" +

"	CREATE TABLE "+prfx+"_places_locations (																" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		num_links int DEFAULT 10000000,															" +
"		population int,																			" +
"	    used int DEFAULT 0,																		" +
"	    yago_data int DEFAULT 1,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_places_location_country														" +
"	(																							" +
"		location_id int NOT NULL,																" +
"		country_id int NOT NULL,																" +
"		yago_data int DEFAULT 1,																" +
"		PRIMARY KEY(location_id, country_id),													" +
"	  	INDEX(country_id),																		" +
"		FOREIGN KEY (location_id) REFERENCES "+prfx+"_places_locations(id)								" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE,													 				" +
"		FOREIGN KEY (country_id) REFERENCES "+prfx+"_places_countries(id)								" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE													 				" +
"	) ENGINE = InnoDB;																			");

		stmt.close();
	}
	
	
}
