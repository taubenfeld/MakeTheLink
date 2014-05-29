package db_load_yago;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Load_wiki {
	
	public static void load(Connection conn, String path) throws ClassNotFoundException, SQLException, IOException{

		Statement stmt = conn.createStatement();

		BufferedReader reader = new BufferedReader(
				new FileReader(path+"yagoWikipediaInfo.tsv"));
		
		int j=0;
		
		stmt.executeUpdate("CREATE TABLE wiki_links(name_ VARCHAR(300), links_ int, UNIQUE(name_)) ENGINE = MyISAM; ");
		PreparedStatement pstmt = conn.prepareStatement(" INSERT IGNORE INTO wiki_links(name_, links_)  VALUES (?, ?)");
		
		String line = null;
		
		String[] data = null;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		while((line = reader.readLine()) != null){
			
			if(j%1000000==0)
				System.out.println("wiki1: "+j);
			j++;

			data = line.split("\t");
			
			Integer a;
			
			if ("<linksTo>".equals(data[2])){
				a = map.get(data[3]);
				if(a==null)
					map.put(data[3], 1);
				else
					map.put(data[3], map.get(data[3])+1);
			}
		}
		
		j=0;
		int cnt=0;
		
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    
			if(j%100000==0)
				System.out.println("wiki2: "+j);
			j++;
		    
			pstmt.setString(1, key);
			pstmt.setInt(2, value);
			pstmt.addBatch();
			cnt++;
			if(cnt>5000){
				pstmt.executeBatch();
				cnt=0;
			}
		    
		}

		if(cnt>0)
			pstmt.executeBatch();
		
		pstmt.close();
		stmt.close();
		reader.close();
	}
}
