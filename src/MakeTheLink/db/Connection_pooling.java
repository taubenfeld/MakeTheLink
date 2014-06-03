package MakeTheLink.db;

import java.beans.PropertyVetoException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Connection_pooling {
	
	public static ComboPooledDataSource cpds;
	
	public static void create_pool(String username, String password) 
					throws PropertyVetoException 
			{
		
		cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.jdbc.Driver" );
		cpds.setJdbcUrl( 
			"jdbc:mysql://127.0.0.1:3306/DbMysql02?rewriteBatchedStatements=true&allowMultiQueries=true" );
		cpds.setUser(username);
		cpds.setPassword(password);
	}	
}
