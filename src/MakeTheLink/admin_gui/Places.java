package MakeTheLink.admin_gui;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;

import org.eclipse.swt.widgets.TabFolder;

public class Places {
	
	
	public static void create_places_menu(Edit_data_gui menu) throws SQLException{
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
}
