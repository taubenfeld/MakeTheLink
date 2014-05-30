package db_generate_question;

import java.sql.*;
import java.io.IOException;

import App.Question;

public class Movies_question {
	
	public static Question generate_question(Connection conn) throws ClassNotFoundException, SQLException, IOException{
		
		String[] q = new String[50];
		
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_cinema_movies where used=1 ");
		
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
		
		Question qst = new Question();
		
		qst.setAnswer(q[0]);
		
		a = (int)(Math.random() * 4);
		
		do{
			b = (int)(Math.random() * 4);}
		while(b==a);
		
		do{
			c = (int)(Math.random() * 4);}
		while(c==a || c==b);
		
		do{
			d = (int)(Math.random() * 4);}
		while(d==a || d==b || d==c);
		
		String[] answerOps = new String[4];
		
		answerOps[a]=q[0];
		answerOps[b]=q[1];
		answerOps[c]=q[2];
		answerOps[d]=q[3];
		
		qst.setAnswerOptions(answerOps);
		
		String[] hints = new String[i-4];
		
		for(j=0;j<i-4;j++){
			hints[j]=q[j+4];
		}
		
		qst.setHintsList(hints);
		
		rst.close();
		stmt.close();
		
		return qst;
	}
}
