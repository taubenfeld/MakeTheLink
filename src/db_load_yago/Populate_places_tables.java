package db_load_yago;

import java.sql.*;

import java.sql.Statement;

public class Populate_places_tables {

	public static void build(Connection conn) throws SQLException{

		Statement stmt = conn.createStatement();

		stmt.execute(

"	INSERT INTO tmp_places_countries(`Name`, `Area (1000 km^2)`, `GDP per capita (1000 $)`,						" + 
"							`Population (million)`, `Capital`, `GDP (billion $)`)									" + 
"		SELECT Name, TRUNCATE(Area/1000000000,2), TRUNCATE(max(GDP)*1000000/max(NumberOfPeople),2), 				" + 
"				TRUNCATE(max(NumberOfPeople)/1000000,2), Capital , max(GDP)											" + 
"		FROM (																										" + 
"			SELECT GDP_COUNTRIES.c1 AS Name, SUBSTRING(X.c2, 2,LOCATE('\"^', X.c2)-2) AS Area, 						" + 
"					A.c2 AS Capital ,																				" + 
"					CAST(SUBSTRING(C.c2, 2,LOCATE('\"^', C.c2)-2) as decimal) AS NumberOfPeople,					" + 
"					CAST(SUBSTRING(D.c2, 2,LOCATE('\"^', D.c2)-10) AS decimal)/10									" + 
"					 AS GDP																							" + 
"			FROM 																									" + 
"				(SELECT c1																							" + 
"				FROM l_fact_gdp ) AS GDP_COUNTRIES																	" + 

"			LEFT JOIN fact_has_capital AS A ON A.c1 =  GDP_COUNTRIES.c1												" + 

"			LEFT JOIN l_fact_population AS C ON C.c1 =  GDP_COUNTRIES.c1											" + 

"			LEFT JOIN l_fact_gdp AS D ON D.c1 =  GDP_COUNTRIES.c1													" + 
"				AND SUBSTRING(D.c2, 2,LOCATE('\"^', D.c2)-10)>0														" + 
"			LEFT JOIN l_fact_area AS X ON X.c1 =  GDP_COUNTRIES.c1													" + 

"			) AS T																									" + 
"		WHERE (CASE WHEN GDP is null then 1 else 0 end) = 0															" + 
"					AND																								" + 
"			 (CASE WHEN Capital is null then 1 else 0 end) = 0														" + 
"					AND																								" + 
"			 (CASE WHEN AREA is null then 1 else 0 end +															" + 
"			  CASE WHEN NumberOfPeople is null then 1 else 0 end) < 2												" + 
"					AND																								" + 
"				locate(substring(name,2,length(name)-2),capital)=0 													" + 
"		GROUP BY Name;																								" + 

"	DELETE FROM tmp_places_countries																				" + 
"	WHERE name='<Egovernment_in_the_UAE>' or name='<Brazil>' or name='<Chile>' 										" + 
"			or name='<United_States>' or name= '<El_Salvador>' or name like '%Ireland%';							" + 

	//all our locations must be geo types
"	CREATE TABLE located_in (																						" +
"		c1 varchar(150) NOT NULL,																					" +
"		c2 varchar(150) DEFAULT NULL,																				" +
"		INDEX (c1),																									" +
"		INDEX (c2)																									" +
"	) ENGINE=MyISAM;																								" +

"	INSERT INTO located_in(c1, c2)																					" +
"	SELECT l.c1, l.c2 FROM fact_located_in l INNER JOIN type_geo g ON l.c1=g.c1; 									" +

"	DROP TABLE fact_located_in;																						" +

"	ALTER TABLE located_in RENAME TO fact_located_in; 																" +
	
/*	//all our locations must be geo types
"	DELETE FROM fact_located_in																						" + 
"	WHERE c1 NOT IN(SELECT c1 FROM type_geo) or c2 NOT IN(SELECT c1 FROM type_geo);									" + 
*/
	//find all the locations transitively located in countries, 3 levels deep.
"	CREATE TABLE transitive_1 (																						" + 
"	  place varchar(150) NOT NULL,																					" + 
"	  is_in varchar(150) DEFAULT NULL,																				" + 
"	  INDEX (is_in),																								" + 
"	  UNIQUE(place, is_in)																							" + 
"	) ENGINE=MyISAM;																								" + 

"	INSERT IGNORE INTO transitive_1(place, is_in)																	" + 
"		SELECT fact_located_in.c1, fact_located_in.c2																" + 
"		FROM fact_located_in, (SELECT name FROM tmp_places_countries) AS countries									" + 
"		WHERE fact_located_in.c2 IN(countries.name);																" + 

"	CREATE TABLE transitive_2 (																						" + 
"	  place varchar(150) NOT NULL,																					" + 
"	  is_in varchar(150) NOT NULL,																					" + 
"	  INDEX (is_in),																								" + 
"	  UNIQUE(place, is_in)																							" + 
"	) ENGINE=MyISAM;																								" + 

"	INSERT IGNORE INTO transitive_2(place, is_in)																	" + 
"		SELECT fact_located_in.c1, fact_located_in.c2																" + 
"		FROM fact_located_in, (select place from transitive_1) AS t1												" + 
"		WHERE fact_located_in.c2 IN(t1.place);																		" + 

"	DELETE FROM transitive_2																						" + 
"	WHERE place IN(SELECT name FROM tmp_places_countries);															" + 

"	CREATE TABLE transitive_3 (																						" + 
"	  place varchar(150) NOT NULL,																					" + 
"	  is_in varchar(150) NOT NULL,																					" + 
"	  INDEX (is_in),																								" + 
"	  UNIQUE(place, is_in)																							" + 
"	) ENGINE=MyISAM;																								" + 
 
"	INSERT IGNORE INTO transitive_3(place, is_in)																	" + 
"		SELECT fact_located_in.c1, fact_located_in.c2																" + 
"		FROM fact_located_in, (select place from transitive_2) as t2												" + 
"		WHERE fact_located_in.c2 IN (t2.place);																		" + 

"	DELETE FROM transitive_3																						" + 
"	WHERE place IN(SELECT name FROM tmp_places_countries);															" + 

"	DELETE FROM transitive_3																						" + 
"	WHERE place IN(SELECT place FROM transitive_1);																	" + 

	//collect all the locations and the countries they're in in one table.
	//also all our locations must have population.
"	CREATE TABLE countries_locations (																				" + 
"	  location varchar(150) NOT NULL,																				" + 
"	  country varchar(150) NOT NULL,																				" + 
"	  UNIQUE(location, country)																					" + 
"	) ENGINE=MyISAM;																								" + 

"	INSERT IGNORE INTO countries_locations(location, country)														" + 
"		SELECT pop.c1, t1.is_in																						" + 
"		FROM l_fact_population pop																					" + 
"		INNER JOIN transitive_1 t1 ON pop.c1=t1.place;																" + 

"	INSERT IGNORE INTO countries_locations(location, country)														" + 
"		SELECT pop.c1, t1.is_in																						" + 
"		FROM l_fact_population pop																					" + 
"		INNER JOIN transitive_2 t2 ON pop.c1=t2.place																" + 
"		INNER JOIN transitive_1 t1 ON t2.is_in=t1.place;															" + 

"	INSERT IGNORE INTO countries_locations(location, country)														" + 
"		SELECT pop.c1, t1.is_in																						" + 
"		FROM l_fact_population pop																					" + 
"		INNER JOIN transitive_3 t3 ON pop.c1=t3.place																" + 
"		INNER JOIN transitive_2 t2 ON t3.is_in=t2.place																" + 
"		INNER JOIN transitive_1 t1 ON t2.is_in=t1.place;															" + 

"	DELETE FROM countries_locations																					" + 
"	WHERE locate(substring(location,2,length(location)-2),country)>0												" + 
"							OR locate(substring(country,2,length(country)-2),location)>0							" + 
"							OR	location like '%Ireland%';															" + 

"	INSERT IGNORE INTO tmp_places_locations(name, num_links, population)											" + 
"		SELECT cl.location, wl.links_, 																				" + 
"			CAST(SUBSTRING(pop.c2, 2,LOCATE('\"^', pop.c2)-2) AS UNSIGNED INT)										" + 
"		FROM countries_locations cl																					" + 
"		INNER JOIN wiki_links wl ON cl.location=wl.name_															" + 
"		INNER JOIN l_fact_population pop ON cl.location=pop.c1;														" + 

"	INSERT IGNORE INTO tmp_places_location_country(location_id, country_id)										" + 
"		SELECT l.id, c.id																							" + 
"		FROM tmp_places_countries c																				" + 
"		INNER JOIN countries_locations cl ON c.name=cl.country														" + 
"		INNER JOIN tmp_places_locations l ON l.name=cl.location;													" + 

"	DROP TABLE transitive_1;																						" + 
"	DROP TABLE transitive_2;																						" + 
"	DROP TABLE transitive_3;																						" + 
"	DROP TABLE countries_locations;																					" + 

"	DELETE FROM tmp_places_locations WHERE id NOT IN (SELECT location_id FROM tmp_places_location_country);		" + 

"	DELETE FROM tmp_places_countries WHERE id NOT IN (SELECT country_id FROM tmp_places_location_country);		");
		
		stmt.execute(
				
		"	UPDATE tmp_places_countries SET Name=SUBSTRING(REPLACE(Name, '_', ' '), 2, LENGTH(Name)-2);				" +
		"	UPDATE tmp_places_locations SET name=SUBSTRING(REPLACE(name, '_', ' '), 2, LENGTH(name)-2);				" +
		"	UPDATE tmp_places_countries SET Capital=SUBSTRING(REPLACE(Capital, '_', ' '), 2, LENGTH(Capital)-2);	");

		
		stmt.close();
	}
}