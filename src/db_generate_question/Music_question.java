package db_generate_question;

import java.sql.*;
import java.io.IOException;

public class Music_question {
	
	public static String[] generate_question(Connection conn) 
						throws ClassNotFoundException, SQLException, IOException{
		
		String[] q = new String[100];
		for(int i=0; i<100; i++)
			q[i]="";
		
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_music_artists where used=1 ");
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
		
		rst = stmt.executeQuery(" select * from curr_music_artists where used=1 ");
		
		Integer id=0;
		Integer birth_year;
		
		int j=1;
		for(int i=0; i<num_choices; i++){
			rst.next();
			if(i==a){
				birth_year=rst.getInt(4);
				q[0]=rst.getString(2).replaceAll(" \\(entertainer\\)", "").replaceAll(" \\(musician\\)", "");
				q[4]="birth year: "+birth_year.toString();
				id=rst.getInt(1);
			}
			if(i==b || i==c || i==d){
				q[j]=rst.getString(2).replaceAll(" \\(entertainer\\)", "").replaceAll(" \\(musician\\)", "");
				j++;
			}
		}
		
		rst = stmt.executeQuery(
			" select distinct c.name from curr_music_creations c, curr_music_artist_creation ac " +
			" where c.id=ac.creation_id and ac.artist_id="+id.toString()+" order by c.num_links desc ");
		
		int i;
		
		for(i=5; i<100 && rst.next(); i++){
			q[i] = "created: " + rst.getString(1);
		}
		
		rst.close();
		stmt.close();
		
		return q;
	}
}
