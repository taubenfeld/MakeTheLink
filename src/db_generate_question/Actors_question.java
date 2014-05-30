package db_generate_question;

import java.sql.*;
import java.io.IOException;

public class Actors_question {
	
	public static String[] generate_question(Connection conn) 
								throws ClassNotFoundException, SQLException, IOException{
		
		String[] q = new String[20];
		for(int i=0; i<20; i++)
			q[i]="";
		
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_cinema_actors where used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);
		
		int a=0,b=0,c=0,d=0;
		
		a = (int)(Math.random() * num_choices);
		
		do{
			b = (int)(Math.random() * num_choices);}
		while(b==a);
		
		do{
			c = (int)(Math.random() * num_choices);}
		while(c==a || c==b);
		
		do{
			d = (int)(Math.random() * num_choices);}
		while(d==a || d==b || d==c);
		
		rst = stmt.executeQuery(" select * from curr_cinema_actors where used=1 ");
		
		Integer id=0;
		Integer birth_year;
		
		int j=1;
		for(int i=0; i<num_choices; i++){
			rst.next();
			if(i==a){
				birth_year=rst.getInt(4);
				q[0]=rst.getString(2).replaceAll(" \\(actor\\)", "").replaceAll(" \\(actress\\)", "");
				q[4]="birth year: "+birth_year.toString();
				id=rst.getInt(1);
			}
			if(i==b || i==c || i==d){
				q[j]=rst.getString(2).replaceAll(" \\(actor\\)", "").replaceAll(" \\(actress\\)", "");
				j++;
			}
		}
		
		rst = stmt.executeQuery(
			" select distinct m.name, m.year_made from curr_cinema_movies m, curr_cinema_actor_movie am " +
			" where m.id=am.movie_id and am.actor_id="+id.toString()+" order by m.num_links desc ");
		
		int i;
		Integer year_made;
		
		for(i=5; i<20 && rst.next(); i++){
			year_made = rst.getInt(2);
			q[i] = "played in: " + 
					rst.getString(1).replaceAll(" \\(film\\)", "") + " ("+year_made.toString()+")";
		}
		
		rst.close();
		stmt.close();
		
		return q;
	}
}
