package db_generate_question;

import java.sql.*;
import java.io.IOException;

public class Sports_question {

	public static String[] generate_question(Connection conn, String league) 
						throws ClassNotFoundException, SQLException, IOException{
		return generate(conn, league);
	}
	
	public static String[] generate(Connection conn, String league) 
						throws ClassNotFoundException, SQLException, IOException{
		
		String[] q = new String[1000];
		for(int i=0; i<1000; i++)
			q[i]="";
		
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_"+league+"_teams where used=1 ");
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
		
		rst = stmt.executeQuery(" select * from curr_"+league+"_teams where used=1 ");
		
		Integer id=0;
		Integer creation_year;
		
		int j=1;
		for(int i=0; i<num_choices; i++){
			rst.next();
			if(i==a){
				creation_year=rst.getInt(4);
				q[0]=rst.getString(2);
				q[4]="creation year: "+creation_year.toString();
				id=rst.getInt(1);
			}
			if(i==b || i==c || i==d){
				q[j]=rst.getString(2);
				j++;
			}
		}
		
		rst = stmt.executeQuery(
			" select distinct p.name from curr_"+league+"_players p, curr_"+league+"_player_team pt " +
			" where p.id=pt.player_id and pt.team_id="+id.toString()+" order by p.links_to_player desc ");
		
		int i;
		
		for(i=5; i<1000 && rst.next(); i++){
			q[i] = "player: " + rst.getString(1);
		}
		
		rst.close();
		stmt.close();
		
		return q;
	}
	
	
}
