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

public class Music {
	
	
	public static void create_cinema_menu(Test menu) throws SQLException{
		TabItem tbtmCinema = new TabItem(menu.main_folder, SWT.NONE);
		tbtmCinema.setText("Cinema");
		
		menu.cinema_folder = new TabFolder(menu.main_folder, SWT.NONE);
		tbtmCinema.setControl(menu.cinema_folder);
		
		TabItem tbtmActors = new TabItem(menu.cinema_folder, SWT.NONE);
		tbtmActors.setText("Actors");
		
		Table actors_table = Music.actors_table(menu.cinema_folder);
		
		tbtmActors.setControl(actors_table);
		
		menu.movies_item = new TabItem(menu.cinema_folder, SWT.NONE);
		menu.movies_item.setText("Movies");
		
	    // Add an event listener to the movies tab to create the movies table when first pressed
		Music.movies_tab_listener(menu);
		
		menu.categories_item = new TabItem(menu.cinema_folder, SWT.NONE);
		menu.categories_item.setText("Categories");
		
	    // Add an event listener to the categories tab to create the movies table when first pressed
		Music.categories_tab_listener(menu);
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
		

		
		movies_table.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				int index = movies_table.indexOf(item);
				String[] datum = movies_array[index];
				item.setText(new String[] {datum[0],
						datum[1], datum[2], datum[3],  });
			}

		});		

		// Add sort indicator and sort data when column selected
		Listener sortListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = movies_table.getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = movies_table.getSortDirection();
				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					movies_table.setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				final int index = currentColumn == id_column ? 0 : currentColumn == rating_column ? 2 : 
												currentColumn == release_year_column ? 3 : 1;
				final int direction = dir;
				Arrays.sort(movies_array, new Comparator<String[]>() {
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
				movies_table.setSortDirection(dir);
				movies_table.clearAll();
			}
		};
		
		
		id_column.addListener(SWT.Selection, sortListener);
		rating_column.addListener(SWT.Selection, sortListener);
		release_year_column.addListener(SWT.Selection, sortListener);
		name_column.addListener(SWT.Selection, sortListener);
		movies_table.setSortColumn(rating_column);
		movies_table.setSortDirection(SWT.DOWN);
		
		
		for(int i=0; i<movies_array.length; i++){
			TableItem tableItem = new TableItem(movies_table, SWT.NONE);
			tableItem.setText(new String[] {movies_array[i][0], movies_array[i][1], movies_array[i][2], movies_array[i][3]});
		}
		
		return movies_table;
	}
	
	public static void movies_tab_listener(final Test menu){
	    // Add an event listener to the movies tab to create the movies table when first pressed
	    menu.cinema_folder.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
	        if(!menu.movies_loaded && menu.cinema_folder.getSelection()[0].getText().compareTo("Movies")==0){
	        	try {
					menu.movies_item.setControl(movies_table(menu.cinema_folder));
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
		

		
		actors_table.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				int index = actors_table.indexOf(item);
				String[] datum = actors_array[index];
				item.setText(new String[] {datum[0],
						datum[1], datum[2], datum[3],  });
			}

		});		

		// Add sort indicator and sort data when column selected
		Listener sortListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = actors_table.getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = actors_table.getSortDirection();
				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					actors_table.setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				final int index = currentColumn == id_column ? 0 : currentColumn == rating_column ? 2 : 
												currentColumn == birth_year_column ? 3 : 1;
				final int direction = dir;
				Arrays.sort(actors_array, new Comparator<String[]>() {
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
				actors_table.setSortDirection(dir);
				actors_table.clearAll();
			}
		};
		
		
		id_column.addListener(SWT.Selection, sortListener);
		rating_column.addListener(SWT.Selection, sortListener);
		birth_year_column.addListener(SWT.Selection, sortListener);
		name_column.addListener(SWT.Selection, sortListener);
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
		
		
		categories_table.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				int index = categories_table.indexOf(item);
				String[] datum = categories_array[index];
				item.setText(new String[] {datum[0],
						datum[1]});
			}

		});		

		// Add sort indicator and sort data when column selected
		Listener sortListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = categories_table.getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = categories_table.getSortDirection();
				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					categories_table.setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				final int index = currentColumn == id_column ? 0 : 1;
				final int direction = dir;
				Arrays.sort(categories_array, new Comparator<String[]>() {
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
				categories_table.setSortDirection(dir);
				categories_table.clearAll();
			}
		};
		
		
		id_column.addListener(SWT.Selection, sortListener);
		name_column.addListener(SWT.Selection, sortListener);
		
		
		for(int i=0; i<categories_array.length; i++){
			TableItem tableItem = new TableItem(categories_table, SWT.NONE);
			tableItem.setText(new String[] {categories_array[i][0], categories_array[i][1]});
		}
		
		return categories_table;
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
	
}
