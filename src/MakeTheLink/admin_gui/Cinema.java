package MakeTheLink.admin_gui;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

import org.eclipse.swt.widgets.TabFolder;

public class Cinema {
	
	
	public static void create_cinema_menu(Test menu) throws SQLException{
		TabItem cinema_item = new TabItem(menu.main_folder, SWT.NONE);
		cinema_item.setText("Cinema");
		
		TabFolder cinema_folder = new TabFolder(menu.main_folder, SWT.NONE);
		cinema_item.setControl(cinema_folder);
		
		TabItem actors_item = new TabItem(cinema_folder, SWT.NONE);
		actors_item.setText("Actors");
		
		
		//create the actors table when the window opens - it is the first table the user sees.
		Table actors_table = Helper_functions.create_table(cinema_folder, 
						new String[]{"id","name","rating","birth year"}, 
						" select * from curr_cinema_actors order by num_links desc");
		
		actors_item.setControl(actors_table);
		
		TabItem movies_item = new TabItem(cinema_folder, SWT.NONE);
		movies_item.setText("Movies");
		
	    // Add an event listener to the movies tab to create the movies table when first pressed		
		Helper_functions.add_tab_listener(cinema_folder, menu.movies_loaded, 
							"Movies", movies_item, new String[]{"id", "name", "rating", "release year"},
							" select * from curr_cinema_movies order by num_links desc", null);
		
		TabItem categories_item = new TabItem(cinema_folder, SWT.NONE);
		categories_item.setText("Categories");
		
	    // Add an event listener to the categories tab to create the movies table when first pressed
		Helper_functions.add_tab_listener(cinema_folder, menu.categories_loaded, 
				"Categories", categories_item, new String[]{"id", "name"},
				" select * from curr_cinema_tags ", null);
	}
}
