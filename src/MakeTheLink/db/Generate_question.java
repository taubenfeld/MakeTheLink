package MakeTheLink.db;

import java.sql.*;
import java.io.IOException;
import App.Question;

public class Generate_question {
	
	public static Question actors_question(Connection conn) 
								throws ClassNotFoundException, SQLException, IOException{
		
		String[] q = new String[200];
		
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_cinema_actors where used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);
		
		int[] rnd = randomize(num_choices);
		int a=rnd[0],b=rnd[1],c=rnd[2],d=rnd[3];
		
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
		
		for(i=5; i<200 && rst.next(); i++){
			year_made = rst.getInt(2);
			q[i] = "played in: " + 
					rst.getString(1).replaceAll(" \\(film\\)", "") + " ("+year_made.toString()+")";
		}
		
		rst.close();
		stmt.close();
		
		return generate_question(q, i);
	}
	
	public static Question movies_question(Connection conn) throws ClassNotFoundException, SQLException, IOException{
		
		String[] q = new String[50];
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_cinema_movies where used=1 ");
		
		rst.next();
		
		int num_choices=rst.getInt(1);
		
		int[] rnd = randomize(num_choices);
		int a=rnd[0],b=rnd[1],c=rnd[2],d=rnd[3];
		
		rst = stmt.executeQuery(" select * from curr_cinema_movies where used=1 ");
		
		Integer id=0;
		Integer release_year;
		
		int j=1;
		for(int i=0; i<num_choices; i++){
			rst.next();
			if(i==a){
				release_year=rst.getInt(4);
				q[0]=rst.getString(2).replaceAll(" \\(film\\)", "");
				q[4]="release year: "+release_year.toString();
				id=rst.getInt(1);
			}
			if(i==b || i==c || i==d){
				q[j]=rst.getString(2).replaceAll(" \\(film\\)", "");
				j++;
			}
		}
		
		int i;
		
		rst = stmt.executeQuery(
				" select distinct t.name from curr_cinema_tags t, curr_cinema_movie_tag mt " +
				" where t.id=mt.tag_id and mt.movie_id="+id.toString()+" ");
		
		for(i=5; i<30 && rst.next(); i++){
			q[i] = "category: " + rst.getString(1);
		}
		
		rst = stmt.executeQuery(
			" select distinct a.name from curr_cinema_actors a, curr_cinema_actor_movie am " +
			" where a.id=am.actor_id and am.movie_id="+id.toString()+" order by a.num_links desc ");
		
		for(; i<50 && rst.next(); i++){
			q[i] = "cast member: " + 
					rst.getString(1).replaceAll(" \\(actor\\)", "").replaceAll(" \\(actress\\)", "");
		}
		
		rst.close();
		stmt.close();
		
		return generate_question(q, i);
	}
	
	public static Question music_question(Connection conn) 
			throws ClassNotFoundException, SQLException, IOException{
	
		String[] q = new String[1000];
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_music_artists where used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);
		
		int[] rnd = randomize(num_choices);
		int a=rnd[0],b=rnd[1],c=rnd[2],d=rnd[3];
		
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
		
		for(i=5; i<1000 && rst.next(); i++){
			q[i] = "created: " + rst.getString(1);
		}
		
		rst.close();
		stmt.close();
		
		return generate_question(q, i);
	}
	
	public static Question places_question(Connection conn) 
			throws ClassNotFoundException, SQLException, IOException{

		String[] q = new String[10000];
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_places_countries where used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);
		
		int[] rnd = randomize(num_choices);
		int a=rnd[0],b=rnd[1],c=rnd[2],d=rnd[3];
		
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
		
		return generate_question(q, i);
	}
	
	public static Question sports_question(Connection conn, String league) 
			throws ClassNotFoundException, SQLException, IOException{
		
		return laegue_question(conn, league);
	}
	
	public static Question laegue_question(Connection conn, String league) 
			throws ClassNotFoundException, SQLException, IOException{

		String[] q = new String[1000];
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_"+league+"_teams where used=1 ");
		rst.next();
		int num_choices=rst.getInt(1);
		
		int[] rnd = randomize(num_choices);
		int a=rnd[0],b=rnd[1],c=rnd[2],d=rnd[3];
		
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
			q[i] = "player: " + rst.getString(1).replaceAll(" \\(footballer\\)", "");
		}
		
		rst.close();
		stmt.close();
		
		return generate_question(q, i);
	}
	
	public static Question generate_question(String[] q, int i){
		
		Question qst = new Question();
		int j=0;
		qst.setAnswer(q[0]);
		int[] rnd = randomize(4);
		int a=rnd[0],b=rnd[1],c=rnd[2],d=rnd[3];
		String[] answerOps = new String[4];
		answerOps[a]=q[0];
		answerOps[b]=q[1];
		answerOps[c]=q[2];
		answerOps[d]=q[3];
		qst.setAnswerOptions(answerOps);
		/*
		String[] hints = new String[i-4];
		for(j=0;j<i-4;j++){
			hints[j]=q[j+4];
		}
		*/
		String[] hints = new String[6];
		for(j=0;j<6;j++){
			hints[j]=q[j+4];
		}
		qst.setHintsList(hints);
		return qst;
	}
	
	public static int[] randomize(int num_choices){
		
		int[] rnd = new int[4];
		
		rnd[0] = (int)(Math.random() * num_choices);
		
		do{
			rnd[1] = (int)(Math.random() * num_choices);}
		while(rnd[1]==rnd[0]);
		
		do{
			rnd[2] = (int)(Math.random() * num_choices);}
		while(rnd[2]==rnd[0] || rnd[2]==rnd[1]);
		
		do{
			rnd[3] = (int)(Math.random() * num_choices);}
		while(rnd[3]==rnd[0] || rnd[3]==rnd[1] || rnd[3]==rnd[2]);
		
		return rnd;
	}
}
