package db_load_yago;

import java.sql.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Load_literal_facts {
	
	static PreparedStatement[] pstmt = new PreparedStatement[5];
	static int[] cnt = {0,0,0,0,0};
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
				new FileReader(path+"yagoLiteralFacts.tsv"));
				
		String fields = " (c1 VARCHAR(300), c2 VARCHAR(300), INDEX(c1)) ENGINE = MyISAM; ";
		
		int i=0, j=0;
		
		int gdp=0, population=1, area=2, creation_date=3, birth_date=4;
		
		String[] l_fact = {"l_fact_gdp", "l_fact_population", "l_fact_area", "l_fact_creation_date", 
				"l_fact_birth_date"};
		
		for(i=0;i<5;i++){
			stmt.executeUpdate("CREATE TABLE "+l_fact[i]+fields);
			pstmt[i] = conn.prepareStatement(" INSERT INTO "+l_fact[i]+"(c1, c2)  VALUES (?,?)");
		}
		
		String line = null;
		
		while((line = reader.readLine()) != null){
			
			if(j%100000==0)
				System.out.println("l_facts: "+j);
			
			j++;

			data = line.split("\t");
			
			if ("<hasGDP>".equals(data[2]))
				insert(gdp);
			if ("<hasNumberOfPeople>".equals(data[2]))
				insert(population);
			if ("<hasArea>".equals(data[2]))
				insert(area);
			if ("<wasCreatedOnDate>".equals(data[2]))
				insert(creation_date);
			if ("<wasBornOnDate>".equals(data[2]))
				insert(birth_date);
		}
	
		for(i=0;i<5;i++){
			if(cnt[i]>0)
				pstmt[i].executeBatch();
			pstmt[i].close();
		}
		
		stmt.close();
		reader.close();
	}
}
