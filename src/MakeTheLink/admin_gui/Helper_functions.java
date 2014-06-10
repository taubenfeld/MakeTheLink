package MakeTheLink.admin_gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import MakeTheLink.db.Connection_pooling;
import org.eclipse.swt.widgets.TabFolder;


public class Helper_functions {
	
	//this function doesn't create the table immediately, but rather only when the user presses on its tab -
	//so that the main menu doesn't have to wait for all the tables to load.
	public static void add_tab_listener
	
	(final TabFolder primary_folder, final int[] load_indicator, 
			final String table_name, final TabItem tab_item, final String[] col_names, final String query,
			final TabFolder secondary_folder){
		
		
		
	    // Add an event listener to the movies tab to create the movies table when first pressed
		primary_folder.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
	    	 
	        if(load_indicator[0]!=1 && primary_folder.getSelection()[0].getText().compareTo(table_name)==0){
	        	
	        	try {
	        		tab_item.setControl(
							create_table(primary_folder,
									col_names, 
									query));
					
	        		load_indicator[0]=1;
				}
	        	catch (IllegalArgumentException e) {
	        		try {
	        			tab_item.setControl(
								create_table(secondary_folder,
										col_names, 
										query));
						
		        		load_indicator[0]=1;
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
	        		load_indicator[0]=1;
				}
	        	catch (SQLException e) {
					e.printStackTrace();
				}
	        }
	      }
	    });
	    
	}
	

	//create the table to be displayed
	public static Table create_table(TabFolder tabFolder, 
						String[] col_names, String query) 
			throws SQLException {
		
		int col_count = col_names.length;
		
		final Table table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		int size=0;
		
		ResultSet rst = stmt.executeQuery(query);
		
		if (rst.last()) {
			  size = rst.getRow();
			  rst.beforeFirst();
		}
		
		String[][] data = new String[size][col_count];
		String tmp;
		for(int i=0; rst.next(); i++){
			
			for(int j=0;j<col_count; j++){
				tmp = rst.getString(j+1);
				data[i][j]= tmp==null?"":tmp;
			}
		}
		
		conn.close();
		rst.close();
		stmt.close();
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		for(int i=0; i<col_count; i++){
			final TableColumn id_column = new TableColumn(table, SWT.CENTER);
			
			id_column.setWidth(100);
			id_column.setText(col_names[i]);
		}
		
		add_sort_column(table, data);
		
		
		for(int i=0; i<data.length; i++){
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(data[i]);
		}
		
		TableColumn[] clmns = table.getColumns();
		
		for(int i=0; i<clmns.length; i++){
			clmns[i].pack();
		}
		
		return table;
	}
	
	public static void refresh_table(Table table, String query)throws SQLException {
		
		
		int col_count = table.getColumnCount();
		
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		int size=0;
		
		ResultSet rst = stmt.executeQuery(query);
		
		if (rst.last()) {
			  size = rst.getRow();
			  rst.beforeFirst();
		}
		
		String[][] data = new String[size][col_count];
		String tmp;
		for(int i=0; rst.next(); i++){
			
			for(int j=0;j<col_count; j++){
				tmp = rst.getString(j+1);
				data[i][j]= tmp==null?"":tmp;
			}
		}
		conn.close();
		rst.close();
		stmt.close();
		
		for(int i=0; i<data.length; i++){
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(data[i]);
		}
		
		TableColumn[] clmns = table.getColumns();
		
		for(int i=0; i<clmns.length; i++){
			clmns[i].pack();
		}
		
		add_sort_column(table, data);
	}
	
	
	//add sorting function to the table's columns
	public static void add_sort_column(final Table tb, final String[][] data){
		
		final TableColumn[] clmns = tb.getColumns();
		
		while(tb.getListeners(SWT.SetData).length>0){
			tb.removeListener(SWT.SetData, tb.getListeners(SWT.SetData)[0]);
		}
		
		tb.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				int index = tb.indexOf(item);
				
				//System.out.println(data[index][1]);
				
				String[] datum = data[index];
				item.setText(datum);
			}

		});		

		// Add sort indicator and sort data when column selected
		Listener sortListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = tb.getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = tb.getSortDirection();
				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					tb.setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				
				int tmp=0;
				for(int i=0; i<clmns.length; i++){
					if(clmns[i]==currentColumn){
						tmp = i;
					}
				}
				
				final int index = tmp;
				
				final int direction = dir;
				Arrays.sort(data, new Comparator<String[]>() {
					@Override
					public int compare(String[] a, String[] b) {
						try{
							if (a[index].compareTo(b[index]) == 0) return 0;
							if (direction == SWT.UP) {
								return Double.parseDouble(a[index]) < Double.parseDouble(b[index]) ? -1 : 1;
							}
							return Double.parseDouble(a[index]) < Double.parseDouble(b[index]) ? 1 : -1;
						}
						catch (Exception e){
							if (a[index].compareTo(b[index]) == 0) return 0;
							if (direction == SWT.UP) {
								return a[index].compareTo(b[index])<0 ? -1 : 1;
							}
							return a[index].compareTo(b[index])<0 ? 1 : -1;
						}
					}
				});
				// update data displayed in table
				tb.setSortDirection(dir);
				tb.clearAll();
			}
		};
		
		for(int i=0; i<clmns.length; i++){
			while(clmns[i].getListeners(SWT.Selection).length>0){
				clmns[i].removeListener(SWT.Selection, clmns[i].getListeners(SWT.Selection)[0]);
			}
			
			clmns[i].addListener(SWT.Selection, sortListener);
		}
	}
	
	public static void filter_search(String search_var, int min_rating, int min_year, int min_population, 
			String table_name, Table tbl, int id){
		
		String match_links = " where ";

		if(table_name.compareTo("Actors")==0){
			
			if(id>0){
				match_links = ", curr_cinema_actor_movie am, curr_cinema_movies m " +
								" where am.actor_id = a.id and am.movie_id="+id+" and ";
			}
			
			String query = " select distinct a.id, a.name, a.num_links, a.year_born from curr_cinema_actors a" + 
					match_links +
					" a.name like '%"+search_var+"%' " +
					" and a.num_links >="+min_rating+
					" and a.year_born >="+min_year+" ";

			tbl.removeAll();
			
			try {
				Helper_functions.refresh_table(tbl, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(table_name.compareTo("Movies")==0){
			
			//positive id means we're getting the list of movies for some specific actor
			if(id>0){
				match_links = ", curr_cinema_actor_movie am, curr_cinema_actors a " +
								" where am.movie_id = m.id and am.actor_id="+id+" and ";
			}
			
			//negative id means we're getting the list of movies for some specific category
			if(id<0){
				match_links = ", curr_cinema_movie_tag mt, curr_cinema_tags t " +
						" where mt.movie_id = m.id and mt.tag_id="+(0-id)+" and ";
			}
			
			String query = " select distinct m.id, m.name, m.num_links, m.year_made from curr_cinema_movies m" + 
					match_links +
					" m.name like '%"+search_var+"%' " +
					" and m.num_links >="+min_rating+
					" and m.year_made >="+min_year+" ";

			tbl.removeAll();

			try {
				Helper_functions.refresh_table(tbl, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(table_name.compareTo("Categories")==0){
			
			String query = " select distinct t.id, t.name from curr_cinema_tags t" + 
					match_links +
					" t.name like '%"+search_var+"%' ";

			tbl.removeAll();
			
			try {
				Helper_functions.refresh_table(tbl, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(table_name.compareTo("Artists")==0){
			
			if(id>0){
				match_links = ", curr_music_artist_creation ac, curr_music_creations c " +
								" where ac.artist_id = a.id and ac.creation_id="+id+" and ";
			}
			
			String query = " select distinct a.id, a.name, a.num_links, a.birth_year from curr_music_artists a" + 
					match_links +
					" a.name like '%"+search_var+"%' " +
					" and a.num_links >="+min_rating+
					" and a.birth_year >="+min_year+" ";

			tbl.removeAll();
			
			try {
				Helper_functions.refresh_table(tbl, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(table_name.compareTo("Creations")==0){
			
			if(id>0){
				match_links = ", curr_music_artist_creation ac, curr_music_artists a " +
								" where ac.creation_id = c.id and ac.artist_id="+id+" and ";
			}
			
			String query = " select distinct c.id, c.name, c.num_links, c.year_made from curr_music_creations c" + 
					match_links +
					" c.name like '%"+search_var+"%' " +
					" and c.num_links >="+min_rating+
					" and c.year_made >="+min_year+" ";

			tbl.removeAll();
			
			try {
				Helper_functions.refresh_table(tbl, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(table_name.compareTo("Countries")==0){
			
			if(id>0){
				match_links = ", curr_places_location_country lc, curr_places_locations l " +
								" where lc.country_id = c.id and lc.location_id="+id+" and ";
			}
			
			String query = " select distinct c.id, c.`name`, c.`area (1000 km^2)`, c.`GDP per capita (1000 $)`, " +
								"c.`population (million)`, c.`capital`, c.`GDP (billion $)` " +
					" from curr_places_countries c " +
					match_links +
					" c.name like '%"+search_var+"%' " +
					" and (c.`population (million)` >="+(((double)min_population)/1000000)+" or c.`population (million)` is null) ";
			
			tbl.removeAll();

			
			
			try {
				Helper_functions.refresh_table(tbl, query);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(table_name.compareTo("Locations")==0){
			
			if(id>0){
				match_links = ", curr_places_location_country lc, curr_places_countries c " +
								" where lc.location_id = l.id and lc.country_id="+id+" and ";
			}
			
			String query = " select distinct l.id, l.name, l.num_links, l.population from curr_places_locations l" + 
					match_links +
					" l.name like '%"+search_var+"%' " +
					" and l.num_links >="+min_rating+
					" and l.population >="+min_population+" ";

			tbl.removeAll();
			
			try {
				Helper_functions.refresh_table(tbl, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(table_name.compareTo("NBA players")==0 || table_name.compareTo("Israeli soccer players")==0 ||
				table_name.compareTo("World soccer players")==0){
			
			String league = table_name.compareTo("NBA players")==0 ? "nba":
				table_name.compareTo("Israeli soccer players")==0?"israeli_soccer":"world_soccer";
			
			if(id>0){
				match_links = ", curr_"+league+"_player_team pt, curr_"+league+"_teams t " +
								" where pt.player_id = p.id and pt.team_id="+id+" and ";
			}
			
			String query = " select distinct p.id, p.name, p.links_to_player, "+
													"p.birth_year from curr_"+league+"_players p " + 
					match_links +
					" p.name like '%"+search_var+"%' " +
					" and p.links_to_player >="+min_rating+
					" and p.birth_year >="+min_year+" ";

			tbl.removeAll();
			
			try {
				Helper_functions.refresh_table(tbl, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		if(table_name.compareTo("NBA teams")==0 || table_name.compareTo("Israeli soccer teams")==0 ||
				table_name.compareTo("World soccer teams")==0){
			
			String league = table_name.compareTo("NBA teams")==0 ? "nba":
				table_name.compareTo("Israeli soccer teams")==0?"israeli_soccer":"world_soccer";
			
			if(id>0){
				match_links = ", curr_"+league+"_player_team pt, curr_"+league+"_players p " +
								" where pt.team_id = t.id and pt.player_id="+id+" and ";
			}
			
			String query = " select distinct t.id, t.name, t.links_to_team, t.creation_year from curr_"+league+"_teams t" + 
					match_links +
					" t.name like '%"+search_var+"%' " +
					" and t.links_to_team >="+min_rating+
					" and t.creation_year >="+min_year+" ";

			tbl.removeAll();
			
			try {
				Helper_functions.refresh_table(tbl, query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void un_link_items(String source_table_name, int id, int[] indexes) throws SQLException{
		PreparedStatement pstmt=null;
		Connection conn = Connection_pooling.cpds.getConnection();
		
		if(source_table_name.compareTo("Actors")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_cinema_actor_movie where actor_id = "+id+" and movie_id = ? ");
		if(source_table_name.compareTo("Movies")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_cinema_actor_movie where movie_id = "+id+" and actor_id = ? ");
		if(source_table_name.compareTo("Categories")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_cinema_movie_tag where tag_id = "+id+" and movie_id = ? ");
		if(source_table_name.compareTo("Artists")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_music_artist_creation where artist_id = "+id+" and creation_id = ? ");
		if(source_table_name.compareTo("Creations")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_music_artist_creation where creation_id = "+id+" and artist_id = ? ");
		if(source_table_name.compareTo("Countries")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_places_location_country where country_id = "+id+" and location_id = ? ");
		if(source_table_name.compareTo("Locations")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_places_location_country where location_id = "+id+" and country_id = ? ");
		if(source_table_name.compareTo("NBA players")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_nba_player_team where player_id = "+id+" and team_id = ? ");
		if(source_table_name.compareTo("NBA teams")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_nba_player_team where team_id = "+id+" and player_id = ? ");
		if(source_table_name.compareTo("Israeli soccer players")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_israeli_soccer_player_team where player_id = "+id+" and team_id = ? ");
		if(source_table_name.compareTo("Israeli soccer teams")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_israeli_soccer_player_team where team_id = "+id+" and player_id = ? ");
		if(source_table_name.compareTo("World soccer players")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_world_soccer_player_team where player_id = "+id+" and team_id = ? ");
		if(source_table_name.compareTo("World soccer teams")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_world_soccer_player_team where team_id = "+id+" and player_id = ? ");

		execute_stmt(pstmt, indexes);
		pstmt.close();
		conn.close();
	}
	public static void link_items(String source_table_name, int id, int[] indexes) throws SQLException{
		PreparedStatement pstmt=null;
		Connection conn = Connection_pooling.cpds.getConnection();
		
		if(source_table_name.compareTo("Actors")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_cinema_actor_movie (actor_id, movie_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Movies")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_cinema_actor_movie (actor_id, movie_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("Categories")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_cinema_movie_tag (category_id, movie_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Artists")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_music_artist_creation (artist_id, creation_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Creations")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_music_artist_creation (artist_id, creation_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("Countries")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_places_location_country (country_id, location_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Locations")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_places_location_country (country_id, location_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("NBA players")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_nba_player_team (player_id, team_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("NBA teams")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_nba_player_team (player_id, team_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("Israeli soccer players")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_israeli_soccer_player_team (player_id, team_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Israeli soccer teams")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_israeli_soccer_player_team (player_id, team_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("World soccer players")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_world_soccer_player_team (player_id, team_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("World soccer teams")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_world_soccer_player_team (player_id, team_id) VALUES (?,"+id+") ");

		execute_stmt(pstmt, indexes);
		pstmt.close();
		conn.close();
	}
	public static void execute_stmt(PreparedStatement pstmt, int[] indexes) throws SQLException{
		int cnt=0;
		for (int i=0; i<indexes.length; i++) {
		    
			pstmt.setInt(1, indexes[i]);
			pstmt.addBatch();
			cnt++;
			if(cnt>5000){
				pstmt.executeBatch();
				cnt=0;
			}
		}
		if(cnt>0)
			pstmt.executeBatch();
	}
	
	public static void save_attributes(String table_name, int id, Text[] attrs) throws SQLException{
		
		String name=attrs[0].getText();
		int rating;
		try{
			rating=Integer.parseInt(attrs[1].getText());
		}catch (Exception e){
			rating=0;
		}
		int year;
		try{
			year=Integer.parseInt(attrs[2].getText());
		}catch (Exception e){
			year=0;
		}
		String query="";
		
		if(table_name.compareTo("Actors")==0){
			query = " UPDATE IGNORE curr_cinema_actors "+
					" SET name='"+name+"', num_links="+rating+", year_born="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Movies")==0){
			query = " UPDATE IGNORE curr_cinema_movies "+
					" SET name='"+name+"', num_links="+rating+", year_made="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Categories")==0){
			query = " UPDATE IGNORE curr_cinema_tags "+
					" SET name='"+name+
					"' WHERE id="+id;
		}
		if(table_name.compareTo("Artists")==0){
			query = " UPDATE IGNORE curr_music_artists "+
					" SET name='"+name+"', num_links="+rating+", birth_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Creations")==0){
			query = " UPDATE IGNORE curr_music_creations "+
					" SET name='"+name+"', num_links="+rating+", year_made="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Locations")==0){
			query = " UPDATE IGNORE curr_places_locations "+
					" SET name='"+name+"', num_links="+rating+", population="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("NBA players")==0){
			query = " UPDATE IGNORE curr_nba_players "+
					" SET name='"+name+"', links_to_player="+rating+", birth_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("NBA teams")==0){
			query = " UPDATE IGNORE curr_nba_teams "+
					" SET name='"+name+"', links_to_team="+rating+", creation_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Israeli soccer players")==0){
			query = " UPDATE IGNORE curr_israeli_soccer_players "+
					" SET name='"+name+"', links_to_player="+rating+", birth_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Israeli soccer teams")==0){
			query = " UPDATE IGNORE curr_israeli_soccer_teams "+
					" SET name='"+name+"', links_to_team="+rating+", creation_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("World soccer players")==0){
			query = " UPDATE IGNORE curr_world_soccer_players "+
					" SET name='"+name+"', links_to_player="+rating+", birth_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("World soccer teams")==0){
			query = " UPDATE IGNORE curr_world_soccer_teams "+
					" SET name='"+name+"', links_to_team="+rating+", creation_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Countries")==0){
			double area;
			try{
				area = Double.parseDouble(attrs[1].getText());
			}catch(Exception e){
				area=0.0;
			}
			double GDP_pc;
			try{
				GDP_pc = Double.parseDouble(attrs[2].getText());
			}catch(Exception e){
				GDP_pc=0.0;
			}
			double population;
			try{
				population = Double.parseDouble(attrs[3].getText());
			}catch(Exception e){
				population=0.0;
			}
			String capital = attrs[4].getText();
			double GDP;
			try{
				GDP = Double.parseDouble(attrs[5].getText());
			}catch(Exception e){
				GDP=0.0;
			}
			query = " UPDATE IGNORE curr_places_countries "+
					" SET `name`='"+name+"', `area (1000 km^2)`="+area+", `GDP per capita (1000 $)`="+GDP_pc+
					", `population (million)`="+population+", `capital`='"+capital+"', `GDP (billion $)`="+GDP+
					" WHERE `id`="+id;
		}
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(query);
		
		stmt.close();
		conn.close();
	}
	public static void crop_rows(String table_name, int[] indexes) throws SQLException{
		
		PreparedStatement pstmt=null;
		Connection conn = Connection_pooling.cpds.getConnection();
		
		Statement stmt = conn.createStatement();
		ResultSet rst=null;
		String stmt_query="";
		String pstmt_query="";
		int[] all_indexes;
		int size=0;
		
		String query_var = "";
		
		HashSet<Integer> set = new HashSet<Integer>();
		for(int i=0; i<indexes.length; i++){
			set.add(indexes[i]);
		}
		
		if(table_name.compareTo("Actors")==0){
			query_var="cinema_actors";
		}
		if(table_name.compareTo("Movies")==0){
			query_var="cinema_movies";
		}
		if(table_name.compareTo("Categories")==0){
			query_var="cinema_tags";
		}
		if(table_name.compareTo("Artists")==0){
			query_var="music_artists";
		}
		if(table_name.compareTo("Creations")==0){
			query_var="music_creations";
		}
		if(table_name.compareTo("Countries")==0){
			query_var="places_countries";
		}
		if(table_name.compareTo("Locations")==0){
			query_var="places_locations";
		}
		if(table_name.compareTo("NBA players")==0){
			query_var="nba_players";
		}
		if(table_name.compareTo("NBA teams")==0){
			query_var="nba_teams";
		}
		if(table_name.compareTo("Israeli soccer players")==0){
			query_var="israeli_soccer_players";
		}
		if(table_name.compareTo("Israeli soccer teams")==0){
			query_var="israeli_soccer_teams";
		}
		if(table_name.compareTo("World soccer players")==0){
			query_var="world_soccer_players";
		}
		if(table_name.compareTo("World soccer teams")==0){
			query_var="world_soccer_teams";
		}
		
		stmt_query = " select id from curr_"+query_var+" ";
		pstmt_query = " delete from curr_"+query_var+" where id = ? ";
		pstmt = conn.prepareStatement(pstmt_query);
		
		rst=stmt.executeQuery(stmt_query);
		
		if (rst.last()) {
			  size = rst.getRow();
			  rst.beforeFirst();
		}
		
		all_indexes = new int[size];
		for(int i=0; rst.next(); i++){
			try{
				all_indexes[i]=rst.getInt(1);
			}catch (Exception e){
				all_indexes[i]=0;
			}
		}
		
		execute_crop_stmt(pstmt, all_indexes, set);
		stmt.close();
		pstmt.close();
		conn.close();
	}
	
	public static void execute_crop_stmt(PreparedStatement pstmt, int[] indexes, HashSet<Integer> set) throws SQLException{
		int cnt=0;
		for (int i=0; i<indexes.length; i++) {
		    if(!set.contains(indexes[i])){
				pstmt.setInt(1, indexes[i]);
				pstmt.addBatch();
				cnt++;
				if(cnt>5000){
					pstmt.executeBatch();
					cnt=0;
				}
		    }
		}
		if(cnt>0)
			pstmt.executeBatch();
	}
	
	public static void delete_rows(String table_name, int[] indexes) throws SQLException{
		
		PreparedStatement pstmt=null;
		Connection conn = Connection_pooling.cpds.getConnection();
		String pstmt_query="";
		
		String query_var = "";
		
		if(table_name.compareTo("Actors")==0){
			query_var="cinema_actors";
		}
		if(table_name.compareTo("Movies")==0){
			query_var="cinema_movies";
		}
		if(table_name.compareTo("Categories")==0){
			query_var="cinema_tags";
		}
		if(table_name.compareTo("Artists")==0){
			query_var="music_artists";
		}
		if(table_name.compareTo("Creations")==0){
			query_var="music_creations";
		}
		if(table_name.compareTo("Countries")==0){
			query_var="places_countries";
		}
		if(table_name.compareTo("Locations")==0){
			query_var="places_locations";
		}
		if(table_name.compareTo("NBA players")==0){
			query_var="nba_players";
		}
		if(table_name.compareTo("NBA teams")==0){
			query_var="nba_teams";
		}
		if(table_name.compareTo("Israeli soccer players")==0){
			query_var="israeli_soccer_players";
		}
		if(table_name.compareTo("Israeli soccer teams")==0){
			query_var="israeli_soccer_teams";
		}
		if(table_name.compareTo("World soccer players")==0){
			query_var="world_soccer_players";
		}
		if(table_name.compareTo("World soccer teams")==0){
			query_var="world_soccer_teams";
		}
		
		pstmt_query = " delete from curr_"+query_var+" where id = ? ";
		pstmt = conn.prepareStatement(pstmt_query);
		
		execute_delete_stmt(pstmt, indexes);
		pstmt.close();
		conn.close();
	}
	
	public static void execute_delete_stmt(PreparedStatement pstmt, int[] indexes) throws SQLException{
		int cnt=0;
		for (int i=0; i<indexes.length; i++) {

			pstmt.setInt(1, indexes[i]);
			pstmt.addBatch();
			cnt++;
			if(cnt>5000){
				pstmt.executeBatch();
				cnt=0;
		    }
		}
		if(cnt>0)
			pstmt.executeBatch();
	}
	
	public static void add_row(String table_name, String name) throws SQLException{
		
		String query="";
		
		if(table_name.compareTo("Actors")==0){
			query=" insert ignore into curr_cinema_actors (name, num_links, year_born) "+
						" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Movies")==0){
			query=" insert ignore into curr_cinema_movies (name, num_links, year_made) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Categories")==0){
			query=" insert ignore into curr_cinema_movies (name) "+
					" VALUES ('"+name+"') ";
		}
		if(table_name.compareTo("Artists")==0){
			query=" insert ignore into curr_music_artists (name, num_links, birth_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Creations")==0){
			query=" insert ignore into curr_music_creations (name, num_links, year_made) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Locations")==0){
			query=" insert ignore into curr_places_locations (name, num_links, population) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("NBA players")==0){
			query=" insert ignore into curr_nba_players (name, links_to_player, birth_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("NBA teams")==0){
			query=" insert ignore into curr_nba_teams (name, links_to_team, creation_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Israeli soccer players")==0){
			query=" insert ignore into curr_israeli_soccer_players (name, links_to_player, birth_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Israeli soccer teams")==0){
			query=" insert ignore into curr_israeli_soccer_teams (name, links_to_team, creation_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("World soccer players")==0){
			query=" insert ignore into curr_world_soccer_players (name, links_to_player, birth_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("World soccer teams")==0){
			query=" insert ignore into curr_world_soccer_teams (name, links_to_team, creation_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Countries")==0){
			query=" insert ignore into curr_places_countries " + 
					" (`name`, `area (1000 km^2)`, `GDP per capita (1000 $)`, `population (million)`, " +
					" `capital`, `GDP (billion $)` ) "+
					" VALUES ('"+name+"', 0.0, 0.0, 0.0, '', 0.0) ";
		}
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(query);
		
		stmt.close();
		conn.close();
		
	}
}