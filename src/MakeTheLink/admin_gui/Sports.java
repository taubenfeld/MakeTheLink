package MakeTheLink.admin_gui;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;

import org.eclipse.swt.widgets.TabFolder;

public class Sports {
	
	
	public static void create_sports_menu(Test menu) throws SQLException{
		TabItem sports_item = new TabItem(menu.main_folder, SWT.NONE);
		sports_item.setText("Sports");
		
		TabFolder sports_folder = new TabFolder(menu.main_folder, SWT.NONE);
		sports_item.setControl(sports_folder);
		
		TabItem nba_players_item = new TabItem(sports_folder, SWT.NONE);
		nba_players_item.setText("NBA players");
		
		// Add an event listener to the sports tab to create the nba_players table when first pressed
		Helper_functions.add_tab_listener(menu.main_folder, menu.nba_players_loaded, 
				"Sports", nba_players_item, new String[]{"id", "name", "rating", "birth year"},
							" select * from curr_nba_players order by links_to_player desc",
							sports_folder);
		
		TabItem nba_teams_item = new TabItem(sports_folder, SWT.NONE);
		nba_teams_item.setText("NBA teams");
		
	    // Add an event listener to the nba_teams tab to create the nba_teams table when first pressed
		Helper_functions.add_tab_listener(sports_folder, menu.nba_teams_loaded, 
				"NBA teams", nba_teams_item, new String[]{"id", "name", "rating", "creation year"},
				" select * from curr_nba_teams order by links_to_team desc", null);
		
		TabItem israeli_soccer_players_item = new TabItem(sports_folder, SWT.NONE);
		israeli_soccer_players_item.setText("Israeli soccer players");
		
		// Add an event listener to the israeli_soccer_players tab to create the israeli_soccer_players table when first pressed
		Helper_functions.add_tab_listener(sports_folder, menu.israeli_soccer_players_loaded, 
				"Israeli soccer players", israeli_soccer_players_item, new String[]{"id", "name", "rating", "birth year"},
							" select * from curr_israeli_soccer_players order by links_to_player desc",
							null);
		
		TabItem israeli_soccer_teams_item = new TabItem(sports_folder, SWT.NONE);
		israeli_soccer_teams_item.setText("Israeli soccer teams");
		
	    // Add an event listener to the israeli_soccer_teams tab to create the israeli_soccer_teams table when first pressed
		Helper_functions.add_tab_listener(sports_folder, menu.israeli_soccer_teams_loaded, 
				"Israeli soccer teams", israeli_soccer_teams_item, new String[]{"id", "name", "rating", "creation year"},
				" select * from curr_israeli_soccer_teams order by links_to_team desc", null);
		
		
		
		TabItem world_soccer_players_item = new TabItem(sports_folder, SWT.NONE);
		world_soccer_players_item.setText("World soccer players");
		
		// Add an event listener to the world_soccer_players tab to create the world_soccer_players table when first pressed
		Helper_functions.add_tab_listener(sports_folder, menu.world_soccer_players_loaded, 
				"World soccer players", world_soccer_players_item, new String[]{"id", "name", "rating", "birth year"},
							" select * from curr_world_soccer_players order by links_to_player desc",
							null);
		
		TabItem world_soccer_teams_item = new TabItem(sports_folder, SWT.NONE);
		world_soccer_teams_item.setText("World soccer teams");
		
	    // Add an event listener to the world_soccer_teams tab to create the world_soccer_teams table when first pressed
		Helper_functions.add_tab_listener(sports_folder, menu.world_soccer_teams_loaded, 
				"World soccer teams", world_soccer_teams_item, new String[]{"id", "name", "rating", "creation year"},
				" select * from curr_world_soccer_teams order by links_to_team desc", null);
	}
}
