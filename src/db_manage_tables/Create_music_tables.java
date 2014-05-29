package db_manage_tables;

import java.sql.*;

public class Create_music_tables {

	public static void create(Connection conn, String prfx) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.execute(

"	CREATE TABLE "+prfx+"_music_artists															" +
"	(																							" +
"		id int PRIMARY KEY auto_increment NOT NULL,												" +
"		name VARCHAR(150) NOT NULL,																" +
"		num_links int DEFAULT 10000000,															" +
"		birth_year int,																			" +
"		used int DEFAULT 0,																		" +
"		yago_data int DEFAULT 1,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_music_creations														" +
"	(																							" +
"		id int PRIMARY KEY auto_increment NOT NULL,												" +
"		name VARCHAR(150) NOT NULL,																" +
"		yago_data int DEFAULT 1,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_music_artist_creation													" +
"	(																							" +
"		artist_id int NOT NULL,																	" +
"		creation_id int NOT NULL,																" +
"		yago_data int DEFAULT 1,																" +
"		PRIMARY KEY(artist_id, creation_id),													" +
"		INDEX(creation_id),																		" +
"		FOREIGN KEY (artist_id) REFERENCES "+prfx+"_music_artists(id)							" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE,																	" +
"		FOREIGN KEY (creation_id) REFERENCES "+prfx+"_music_creations(id)						" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE																	" +
"	) ENGINE = InnoDB;																			");

		stmt.close();
	}
	
	
}
