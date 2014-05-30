package db_load_yago;

import java.sql.*;

import java.sql.Statement;

public class Populate_music_tables {

	public static void build(Connection conn) throws SQLException{

		Statement stmt = conn.createStatement();

		stmt.execute(

"	CREATE TABLE artists																							" +
"	(																												" +
"		name VARCHAR(300) NOT NULL,																					" +
"		birth_year int,																								" +
"		INDEX(name)																									" +
"	) ENGINE = MyISAM;																								" +

"	INSERT IGNORE INTO artists(name, birth_year)																	" +
"		SELECT type_musicians.c1, CAST(SUBSTRING(l_fact_birth_date.c2, 2,5) AS UNSIGNED INT)						" +
"		FROM type_musicians, l_fact_birth_date																		" +
"		WHERE l_fact_birth_date.c1=type_musicians.c1;																" +

"	INSERT IGNORE INTO artists(name, birth_year)																	" +
"		SELECT type_music_org.c1, CAST(SUBSTRING(l_fact_creation_date.c2, 2,5) AS UNSIGNED INT)						" +
"		FROM type_music_org, l_fact_creation_date																	" +
"		WHERE l_fact_creation_date.c1=type_music_org.c1;															" +

	//a temporary table that holds the musicians and the music they created
"	CREATE TABLE artists_creations																					" +
"	(																												" +
"		artist VARCHAR(165) NOT NULL,																				" +
"		creation VARCHAR(165) NOT NULL,																				" +
"		num_links int NOT NULL,																						" +
"		year_made int NOT NULL,																						" +
"		INDEX(creation)																								" +
"	) ENGINE = MyISAM;																								" +

"	INSERT IGNORE INTO artists_creations(artist, creation, num_links, year_made)											" +
"		SELECT fact_created.c1, fact_created.c2,	wiki_links.links_,													" +
"						CAST(SUBSTRING(l_fact_creation_date.c2, 2,5) AS UNSIGNED INT)								" +
"		FROM fact_created, wiki_links, 	l_fact_creation_date														" +
"		WHERE fact_created.c2 IN(SELECT c1 FROM type_music) and 													" +
"				fact_created.c1 IN(SELECT name FROM artists) and													" +
"				fact_created.c2=wiki_links.name_ and																" +
"				fact_created.c2=l_fact_creation_date.c1 ;															" +

	//remove redundant details from song names
"	UPDATE artists_creations																						" +
"	SET creation = CONCAT(SUBSTRING(creation, 1,LOCATE('_(', creation)-1), '>')										" +
"	WHERE creation LIKE '%song)%';																					" +
"	DELETE FROM artists_creations WHERE locate(substring(artist,2,length(artist)-2),creation) > 0;					" +

"	ALTER IGNORE TABLE artists_creations																			" +
"	ADD UNIQUE INDEX (artist, creation);																			" +

"	INSERT IGNORE INTO tmp_music_artists(name, num_links, birth_year) 												" +
"		SELECT x.artist, wiki_links.links_, artists.birth_year														" +
"		FROM wiki_links,  artists,																					" +
"							(select artists_creations.artist														" +
"							 from artists_creations																	" +
"							 group by artist) as x																	" +
"		WHERE x.artist = wiki_links.name_ and																		" +
"				x.artist = artists.name; 																			" +

"	DROP TABLE artists;																								" +

"	DELETE FROM artists_creations WHERE																				" +
"	artist NOT IN(SELECT name FROM tmp_music_artists);																" +

"	INSERT IGNORE INTO tmp_music_creations(name, num_links, year_made)												" +
"		SELECT ac.creation, ac.num_links, ac.year_made																	" +
"		FROM artists_creations ac																					" +
"		INNER JOIN tmp_music_artists a ON a.name=ac.artist;															" +

"	INSERT IGNORE INTO tmp_music_artist_creation(artist_id, creation_id)											" +
"	SELECT a.id, c.id																								" +
"	FROM tmp_music_artists a																						" +
"	INNER JOIN artists_creations ac ON a.name=ac.artist																" +
"	INNER JOIN tmp_music_creations c ON ac.creation=c.name;														" +

"	DROP TABLE artists_creations;																					");

		stmt.close();
	}
}

