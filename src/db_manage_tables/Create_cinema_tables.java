package db_manage_tables;

import java.sql.*;

public class Create_cinema_tables {

	public static void create(Connection conn, String prfx) throws SQLException{
		Statement stmt = conn.createStatement();
		
		stmt.execute(

"	CREATE TABLE "+prfx+"_cinema_movies															" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		num_links int DEFAULT 10000000,															" +
"		year_made int,																			" +
"		used int DEFAULT 0,																		" +
"		yago_data int DEFAULT 1,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_cinema_actors															" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		num_links int DEFAULT 10000000,															" +
"		year_born int,																			" +
"		used int DEFAULT 0,																		" +
"		yago_data int DEFAULT 1,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_cinema_actor_movie													" +
"	(																							" +
"		actor_id int NOT NULL,																	" +
"		movie_id int NOT NULL,																	" +
"		yago_data int DEFAULT 1,																" +
"		INDEX(movie_id),																		" +
"		PRIMARY KEY(actor_id, movie_id),														" +
"		FOREIGN KEY (actor_id) REFERENCES "+prfx+"_cinema_actors(id)							" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE,																	" +
"		FOREIGN KEY (movie_id) REFERENCES "+prfx+"_cinema_movies(id)							" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE																	" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_cinema_tags															" +
"	(																							" +
"		id int PRIMARY KEY auto_increment,														" +
"		name VARCHAR(150) NOT NULL,																" +
"		yago_data int DEFAULT 1,																" +
"		UNIQUE (name)																			" +
"	) ENGINE = InnoDB;																			" +

"	CREATE TABLE "+prfx+"_cinema_movie_tag														" +
"	(																							" +
"		movie_id int NOT null,																	" +
"		tag_id int NOT null,																	" +
"		yago_data int DEFAULT 1,																" +
"		INDEX(tag_id),																			" +
"		PRIMARY KEY(movie_id, tag_id),															" +
"		FOREIGN KEY (movie_id) REFERENCES "+prfx+"_cinema_movies(id)							" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE,																	" +
"		FOREIGN KEY (tag_id) REFERENCES "+prfx+"_cinema_tags(id)								" +
"			ON DELETE CASCADE																	" +
"			ON UPDATE CASCADE																	" +
"	) ENGINE = InnoDB;																			");

		stmt.close();
	}
	
	
}
