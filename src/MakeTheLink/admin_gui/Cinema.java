package MakeTheLink.admin_gui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import MakeTheLink.db.Connection_pooling;
import org.eclipse.swt.widgets.TabFolder;

public class Cinema {
	
	
	public static void create_cinema_menu(Test menu) throws SQLException{
		TabItem tbtmCinema = new TabItem(menu.main_folder, SWT.NONE);
		tbtmCinema.setText("Cinema");
		
		menu.cinema_folder = new TabFolder(menu.main_folder, SWT.NONE);
		tbtmCinema.setControl(menu.cinema_folder);
		
		TabItem tbtmActors = new TabItem(menu.cinema_folder, SWT.NONE);
		tbtmActors.setText("Actors");
		
		Table actors_table = create_table(menu.cinema_folder, actors_array,
						new String[]{"id","name","rating","birth year"}, 
						" select * from curr_cinema_actors order by num_links desc");
		
		tbtmActors.setControl(actors_table);
		
		menu.movies_item = new TabItem(menu.cinema_folder, SWT.NONE);
		menu.movies_item.setText("Movies");
		
	    // Add an event listener to the movies tab to create the movies table when first pressed
		Cinema.movies_tab_listener(menu);
		
		menu.categories_item = new TabItem(menu.cinema_folder, SWT.NONE);
		menu.categories_item.setText("Categories");
		
	    // Add an event listener to the categories tab to create the movies table when first pressed
		Cinema.categories_tab_listener(menu);
	}
	
	
	static String[][] movies_array;

	public static Table movies_table(TabFolder tabFolder) throws SQLException {
		final Table movies_table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		int size;
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_cinema_movies ");
		rst.next();
		size=rst.getInt(1);
		movies_array = new String[size][4];
		rst = stmt.executeQuery(" select * from curr_cinema_movies order by num_links desc");
		
		for(int i=0; rst.next(); i++){
			for(int j=1;j<=4; j++){
				movies_array[i][j-1]=rst.getString(j);
			}
		}
		
		rst.close();
		stmt.close();
		
		movies_table.setLinesVisible(true);
		movies_table.setHeaderVisible(true);
		movies_table.setSelection(new int[] {2});
		movies_table.setSelection(3);
		
		final TableColumn id_column = new TableColumn(movies_table, SWT.CENTER);
		id_column.setWidth(100);
		id_column.setText("id");
		
		final TableColumn name_column = new TableColumn(movies_table, SWT.CENTER);
		name_column.setText("name");
		name_column.setWidth(123);
		
		final TableColumn rating_column = new TableColumn(movies_table, SWT.NONE);
		rating_column.setWidth(100);
		rating_column.setText("rating");
		
		final TableColumn release_year_column = new TableColumn(movies_table, SWT.NONE);
		release_year_column.setWidth(115);
		release_year_column.setText("release year");
		
		add_sort_column(movies_table, movies_array);
		
		movies_table.setSortColumn(rating_column);
		movies_table.setSortDirection(SWT.DOWN);
		
		
		for(int i=0; i<movies_array.length; i++){
			TableItem tableItem = new TableItem(movies_table, SWT.NONE);
			tableItem.setText(new String[] {movies_array[i][0], movies_array[i][1], movies_array[i][2], movies_array[i][3]});
		}
		
		return movies_table;
	}
	
	public static void movies_tab_listener
	(final Test menu, TabFolder tf, String[][] data, ){
	    // Add an event listener to the movies tab to create the movies table when first pressed
	    menu.cinema_folder.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
	        if(!menu.movies_loaded && menu.cinema_folder.getSelection()[0].getText().compareTo("Movies")==0){
	        	try {
					menu.movies_item.setControl(
							create_table(menu.cinema_folder, movies_array,
									new String[]{"id","name","rating","release year"}, 
									" select * from curr_cinema_movies order by num_links desc"));
					
					menu.movies_loaded=true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
	      }
	    });
	    
	}
	
	static String[][] actors_array;

	public static Table actors_table(TabFolder tabFolder) throws SQLException {
		final Table actors_table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		int size;
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_cinema_actors ");
		rst.next();
		size=rst.getInt(1);
		actors_array = new String[size][4];
		rst = stmt.executeQuery(" select * from curr_cinema_actors order by num_links desc");
		
		for(int i=0; rst.next(); i++){
			for(int j=1;j<=4; j++){
				actors_array[i][j-1]=rst.getString(j);
			}
		}
		
		rst.close();
		stmt.close();
		
		actors_table.setLinesVisible(true);
		actors_table.setHeaderVisible(true);
		actors_table.setSelection(new int[] {2});
		actors_table.setSelection(3);
		
		final TableColumn id_column = new TableColumn(actors_table, SWT.CENTER);
		id_column.setWidth(100);
		id_column.setText("id");
		
		final TableColumn name_column = new TableColumn(actors_table, SWT.CENTER);
		name_column.setText("name");
		name_column.setWidth(123);
		
		final TableColumn rating_column = new TableColumn(actors_table, SWT.NONE);
		rating_column.setWidth(100);
		rating_column.setText("rating");
		
		final TableColumn birth_year_column = new TableColumn(actors_table, SWT.NONE);
		birth_year_column.setWidth(115);
		birth_year_column.setText("birth year");
		
		add_sort_column(actors_table, actors_array);
		
		actors_table.setSortColumn(rating_column);
		actors_table.setSortDirection(SWT.DOWN);
		
		
		for(int i=0; i<actors_array.length; i++){
			TableItem tableItem = new TableItem(actors_table, SWT.NONE);
			tableItem.setText(new String[] {actors_array[i][0], actors_array[i][1], actors_array[i][2], actors_array[i][3]});
		}
		
		return actors_table;
	}

	static String[][] categories_array;

	public static Table categories_table(TabFolder tabFolder) throws SQLException {
		final Table categories_table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		int size;
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_cinema_tags ");
		rst.next();
		size=rst.getInt(1);
		categories_array = new String[size][2];
		rst = stmt.executeQuery(" select * from curr_cinema_tags");
		
		for(int i=0; rst.next(); i++){
			for(int j=1;j<=2; j++){
				categories_array[i][j-1]=rst.getString(j);
			}
		}
		
		rst.close();
		stmt.close();
		
		categories_table.setLinesVisible(true);
		categories_table.setHeaderVisible(true);
		categories_table.setSelection(new int[] {2});
		categories_table.setSelection(3);
		
		final TableColumn id_column = new TableColumn(categories_table, SWT.CENTER);
		id_column.setWidth(100);
		id_column.setText("id");
		
		final TableColumn name_column = new TableColumn(categories_table, SWT.CENTER);
		name_column.setText("name");
		name_column.setWidth(123);
	
		add_sort_column(categories_table, categories_array);
		
		for(int i=0; i<categories_array.length; i++){
			TableItem tableItem = new TableItem(categories_table, SWT.NONE);
			tableItem.setText(new String[] {categories_array[i][0], categories_array[i][1]});
		}
		
		return categories_table;
	}
	
	public static Table create_table(TabFolder tabFolder, String[][] data,
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
		
		data = new String[size][col_count];
		
		for(int i=0; rst.next(); i++){
			
			for(int j=0;j<col_count; j++){
				data[i][j]=rst.getString(j+1);
			}
		}
		
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
		
		return table;
	}
	
	public static void categories_tab_listener(final Test menu){
	    // Add an event listener to the movies tab to create the movies table when first pressed
	    menu.cinema_folder.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
	        if(!menu.categories_loaded && menu.cinema_folder.getSelection()[0].getText().compareTo("Categories")==0){
	        	try {
					menu.categories_item.setControl(categories_table(menu.cinema_folder));
					menu.categories_loaded=true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
	      }
	    });
	    
	}
	
	
	public static void add_sort_column(final Table tb, final String[][] data){
		
		final TableColumn[] clmns = tb.getColumns();
		
		tb.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				int index = tb.indexOf(item);
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
								return Integer.parseInt(a[index]) < Integer.parseInt(b[index]) ? -1 : 1;
							}
							return Integer.parseInt(a[index]) < Integer.parseInt(b[index]) ? 1 : -1;
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
			clmns[i].addListener(SWT.Selection, sortListener);
		}
	}
	
	
}
