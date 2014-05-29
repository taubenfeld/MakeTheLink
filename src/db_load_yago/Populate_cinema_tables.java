package db_load_yago;

import java.sql.*;

import java.sql.Statement;

public class Populate_cinema_tables {

	public static void build(Connection conn) throws SQLException{
		
		Statement stmt = conn.createStatement();
		
		stmt.execute(
				
//there are a lot of musicians who also act in movies, remove them.
"	DELETE FROM type_actors																							" +
"	WHERE c1 IN (SELECT name FROM tmp_music_artists);																" +

//a temporary table that holds the actors and the movies they acted in.
"	CREATE TABLE actors_movies																						" +
"	(																												" +
"		actor VARCHAR(165) NOT NULL,																				" +
"		movie VARCHAR(165) NOT NULL,																				" +
"		INDEX(movie),																								" +
"		UNIQUE(actor, movie)																						" +
" 	) ENGINE = MyISAM;																								" +

" 	INSERT IGNORE INTO actors_movies(actor, movie)																	" +
"		SELECT c1, c2																								" +
"		FROM fact_acted_in																							" +
"		WHERE c1 IN(SELECT c1 FROM type_actors);																	" +

"	INSERT IGNORE INTO tmp_cinema_movies(name, num_links, year_made) 												" +
"		SELECT  x.movie, wiki_links.links_,																			" +
"				CAST(SUBSTRING(l_fact_creation_date.c2, 2,5) AS UNSIGNED INT)										" +
"		FROM wiki_links, l_fact_creation_date, (SELECT movie FROM actors_movies GROUP BY movie) AS x				" +
"		WHERE wiki_links.name_ = x.movie and																		" +
"				x.movie = l_fact_creation_date.c1;																	" +

"	INSERT IGNORE INTO tmp_cinema_actors(name, num_links, year_born) 												" +
"		SELECT x.actor, wiki_links.links_,																			" +
"					CAST(SUBSTRING(l_fact_birth_date.c2, 2,5) AS UNSIGNED INT)										" +
"		FROM wiki_links, l_fact_birth_date, (SELECT actor FROM actors_movies GROUP BY actor) AS x					" +
"		WHERE x.actor = wiki_links.name_ and																		" +
"				x.actor = l_fact_birth_date.c1; 																	" +

"	INSERT IGNORE INTO tmp_cinema_actor_movie(actor_id, movie_id)													" +
"	SELECT tmp_cinema_actors.id, tmp_cinema_movies.id																" +
"	FROM tmp_cinema_actors, tmp_cinema_movies, actors_movies														" +
"	WHERE	tmp_cinema_actors.name = actors_movies.actor AND														" +
"			actors_movies.movie = tmp_cinema_movies.name;															" +

"	DROP TABLE actors_movies;																						" +

	//delete entries of actors and movies that don't appear in the connecting table
"	DELETE FROM tmp_cinema_movies WHERE id NOT IN (SELECT movie_id FROM tmp_cinema_actor_movie);					" +

"	DELETE FROM tmp_cinema_actors WHERE id NOT IN (SELECT actor_id FROM tmp_cinema_actor_movie);					" +

"	INSERT INTO tmp_cinema_tags(id, name) values																	" +
"	(1,'action'),(2,'adventure'),(3,'animated'),(4,'comedy'),(5,'crime'),(6,'documentary'),							" +
"	(7,'drama'),(8,'fantasy'),(9,'horror'),(10,'musical'),(11,'mystery'),(12,'romance'),							" +
"	(13,'science_fiction'),(14,'sports'),(15,'teen'),(16,'television'),(17,'thriller'),								" +
"	(18,'war'),(19,'western');																						" );
		
		String[] type = {"action","adventure","animated","comedy","crime","documentary",
				"drama","fantasy","horror","musical","mystery","romance","science_fiction",
				"sports","teen","television","thriller","war","western"};
		int i=0;
		
		for(i=0;i<19;i++)
			stmt.executeUpdate(	"INSERT INTO tmp_cinema_movie_tag(movie_id, tag_id) "+
								"SELECT id, "+Integer.toString(i+1)+" "+
								"FROM tmp_cinema_movies as m "+
								"WHERE exists "+
								"(SELECT * FROM films_"+type[i]+" WHERE c1=m.name);");
		
		stmt.execute(
				
				"	UPDATE tmp_music_artists SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);		" +
				"	UPDATE tmp_music_creations SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);		");
		
		stmt.execute(
				
				"	UPDATE tmp_cinema_actors SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);		" +
				"	UPDATE tmp_cinema_movies SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);		");
	
		stmt.execute(
				
				"	delete from tmp_cinema_movies																	" +
				"	where id not in (select distinct movie_id from tmp_cinema_movie_tag);							");
		stmt.execute(
				
				"	delete from tmp_cinema_actors																	" +
				"	where id not in (select distinct actor_id from tmp_cinema_actor_movie);							");
		
		stmt.close();
	}
}
