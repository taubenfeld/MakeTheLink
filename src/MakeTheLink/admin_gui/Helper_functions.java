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
	
	//add sorting function to the table's columns
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
			clmns[i].addListener(SWT.Selection, sortListener);
		}
	}
	
	
}
