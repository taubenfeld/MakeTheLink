package db_load_yago;

import java.sql.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Load_facts {
	
	static PreparedStatement[] pstmt = new PreparedStatement[6];
	static int[] cnt = {0,0,0,0,0,0};
	static String[] data = null;
	
	public static void insert(int num) throws SQLException{
		pstmt[num].setString(1, data[1]);
		pstmt[num].setString(2, data[3]);
		pstmt[num].addBatch();
		cnt[num]++;
		if(cnt[num]>5000){
			pstmt[num].executeBatch();
			cnt[num]=0;
		}
	}
	
	public static void load(Connection conn, String path) throws ClassNotFoundException, SQLException, IOException{

		Statement stmt = conn.createStatement();

		BufferedReader reader = new BufferedReader(
				new FileReader(path+"yagoFacts.tsv"));
				
		String fields = " (c1 VARCHAR(300), c2 VARCHAR(300), INDEX(c1), INDEX(c2)) ENGINE = MyISAM; ";
		
		int i=0, j=0;
		
		int has_capital=0, plays_for=1, located_in=2, acted_in=3, created=4, affiliated_to=5;
		
		String[] fact = {"fact_has_capital", "fact_plays_for", "fact_located_in", "fact_acted_in", 
							"fact_created", "fact_affiliated_to"};
		
		for(i=0;i<6;i++){
			stmt.executeUpdate("CREATE TABLE "+fact[i]+fields);
			pstmt[i] = conn.prepareStatement(" INSERT INTO "+fact[i]+"(c1, c2)  VALUES (?,?)");
		}
		
		String line = null;
		
		while((line = reader.readLine()) != null){
			
			if(j%100000==0)
				System.out.println("facts: "+j);
				
			j++;

			data = line.split("\t");
			
			if ("<hasCapital>".equals(data[2]))
				insert(has_capital);
			if ("<playsFor>".equals(data[2]))
				insert(plays_for);
			if ("<isLocatedIn>".equals(data[2]))
				insert(located_in);
			if ("<actedIn>".equals(data[2]))
				insert(acted_in);
			if ("<created>".equals(data[2]))
				insert(created);
			if ("<isAffiliatedTo>".equals(data[2]))
				insert(affiliated_to);
		}
	
		for(i=0;i<6;i++){
			if(cnt[i]>0)
				pstmt[i].executeBatch();
			pstmt[i].close();
		}
		
		stmt.close();
		reader.close();
	}
}
