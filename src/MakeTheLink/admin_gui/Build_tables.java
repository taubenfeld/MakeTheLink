package MakeTheLink.admin_gui;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

import org.eclipse.swt.widgets.TabFolder;

public class Build_tables {
	
	
	public static void create_music_menu(Main_edit_data_gui menu) throws SQLException{
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
	public static void create_cinema_menu(Main_edit_data_gui menu) throws SQLException{
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
	
	public static void create_places_menu(Main_edit_data_gui menu) throws SQLException{
		TabItem places_item = new TabItem(menu.main_folder, SWT.NONE);
		places_item.setText("Places");
		
		TabFolder places_folder = new TabFolder(menu.main_folder, SWT.NONE);
		places_item.setControl(places_folder);
		
		TabItem countries_item = new TabItem(places_folder, SWT.NONE);
		countries_item.setText("Countries");
		
	    // Add an event listener to the places tab to create the countries table when first pressed		
		Helper_functions.add_tab_listener(menu.main_folder, menu.countries_loaded, 
				"Places", countries_item, new String[]{
				"id", "name", "area (1000 km^2)", "gdp per capita (1000 $)", "population (million)", "capital", 
				"gdp (billion $)"},
							" select * from curr_places_countries order by `GDP (billion $)` ", places_folder);
		
		TabItem locations_item = new TabItem(places_folder, SWT.NONE);
		locations_item.setText("Locations");
		
	    // Add an event listener to the locations tab to create the locations table when first pressed
		Helper_functions.add_tab_listener(places_folder, menu.locations_loaded, 
				"Locations", locations_item, new String[]{"id", "name", "rating", "population"},
				" select * from curr_places_locations order by num_links desc ", null);
	}
	public static void create_sports_menu(Main_edit_data_gui menu) throws SQLException{
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
