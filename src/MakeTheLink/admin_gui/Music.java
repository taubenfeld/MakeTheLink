package MakeTheLink.admin_gui;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;

import org.eclipse.swt.widgets.TabFolder;

public class Music {
	
	
	public static void create_music_menu(Test menu) throws SQLException{
		TabItem music_item = new TabItem(menu.main_folder, SWT.NONE);
		music_item.setText("Music");
		
		TabFolder music_folder = new TabFolder(menu.main_folder, SWT.NONE);
		music_item.setControl(music_folder);
		
		TabItem artists_item = new TabItem(music_folder, SWT.NONE);
		artists_item.setText("Artists");
		
		// Add an event listener to the music tab to create the artists table when first pressed		
		Helper_functions.add_tab_listener(menu.main_folder, menu.artists_loaded, 
				"Music", artists_item, new String[]{"id", "name", "rating", "birth year"},
							" select * from curr_music_artists order by num_links desc",
							music_folder);
		
		TabItem creations_item = new TabItem(music_folder, SWT.NONE);
		creations_item.setText("Creations");
		
	    // Add an event listener to the creations tab to create the creations table when first pressed
		Helper_functions.add_tab_listener(music_folder, menu.creations_loaded, 
				"Creations", creations_item, new String[]{"id", "name", "rating", "release year"},
				" select * from curr_music_creations order by num_links desc", null);
	}
}
