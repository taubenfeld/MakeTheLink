package db_generate_question;

import java.sql.*;
import java.io.IOException;

public class Places_question {
	
	public static String[] generate_question(Connection conn) 
						throws ClassNotFoundException, SQLException, IOException{
		
		String[] q = new String[10000];
		for(int i=0; i<10000; i++)
			q[i]="";
		
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_places_countries where used=1 ");
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
		
		rst = stmt.executeQuery(" select * from curr_places_countries where used=1 ");
		
		Integer id=0;
		Double area = null;
		Double gdp_pc = null;
		Double population = null;
		String capital = null;
		Double gdp = null;
		
		
		int j=1, k=4;
		for(int i=0; i<num_choices; i++){
			rst.next();
			if(i==a){
				
				
				area = rst.getDouble(3);
				gdp_pc = rst.getDouble(4);
				population = rst.getDouble(5);
				capital = rst.getString(6);
				gdp = rst.getDouble(7);
				
				if(area!=0)
					q[k++] = "Area (1000 km^2): "+area.toString();
				
				if(gdp_pc!=0){
					q[k++] = "GDP per capita (1000$): "+gdp_pc.toString();
					q[k++] = "Population (Millions): "+population.toString();
				}
				q[k++] = "Capital: "+capital;
				q[k++] = "GDP (Billion $): "+gdp.toString();
				
				q[0]=rst.getString(2);
				
				id=rst.getInt(1);
			}
			if(i==b || i==c || i==d)
				q[j++]=rst.getString(2);
		}
		
		rst = stmt.executeQuery(
			" select distinct l.name from curr_places_locations l, curr_places_location_country lc " +
			" where l.id=lc.location_id and lc.country_id="+id.toString()+" order by l.num_links desc ");
		
		int i;
		
		for(i=k; i<10000 && rst.next(); i++){
			q[i] = "location: " + rst.getString(1);
		}
		
		rst.close();
		stmt.close();
		
		return q;
	}
}
