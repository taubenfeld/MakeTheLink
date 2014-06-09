package MakeTheLink.admin_gui;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import MakeTheLink.db.Connection_pooling;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;

public class Main_edit_data_gui extends Shell {

	TabFolder main_folder = null;
	
	//these variables are used to indicate if the different tables were already loaded or not
	int[] movies_loaded = new int[]{0};
	int[] categories_loaded = new int[]{0};
	int[] artists_loaded = new int[]{0};
	int[] creations_loaded = new int[]{0};
	int[] countries_loaded = new int[]{0};
	int[] locations_loaded = new int[]{0};
	int[] nba_teams_loaded = new int[]{0};
	int[] nba_players_loaded = new int[]{0};
	int[] israeli_soccer_teams_loaded = new int[]{0};
	int[] israeli_soccer_players_loaded = new int[]{0};
	int[] world_soccer_teams_loaded = new int[]{0};
	int[] world_soccer_players_loaded = new int[]{0};
	private Text search_text;
	private Text new_row_text;
	Spinner sp_min_rating;
	Spinner sp_min_year;
	Spinner sp_min_population;
	
	boolean window_busy=false;
	static boolean menu_open=false;
	
	public static Map<String, String> category_links_map = new HashMap<String, String>();
	
	/**
	 * Launch the application.
	 * @param args
	 * @throws PropertyVetoException 
	 * @throws SQLException 
	 */
	public static void main(String args[]) throws PropertyVetoException, SQLException {
		
		if(!menu_open){
			menu_open=true;
			
			Connection_pooling.create_pool("root", "1");
			
			try {
				
				fill_map();
				Display display = Display.getDefault();
				Main_edit_data_gui shell = new Main_edit_data_gui(display);
				shell.open();
				shell.layout();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				menu_open=false;
			}
			menu_open=false;
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 * @throws SQLException 
	 */
	public Main_edit_data_gui(Display display) throws SQLException {
		super(display, SWT.SHELL_TRIM | SWT.BORDER);
		setLayout(new GridLayout(17, false));
		
		Button edit_row_button = new Button(this, SWT.NONE);
		edit_row_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		edit_row_button.setText("edit selected row");
		
		edit_row_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(!window_busy){
					window_busy=true;
					try{
						Edit_row_window.create(Main_edit_data_gui.this);
					}
					catch (Exception ex){
					}
					window_busy=false;
				}
			}
		});
		
		Button delete_button = new Button(this, SWT.NONE);
		delete_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		delete_button.setText("delete selected rows");
		delete_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){
					window_busy=true;
					try{	
						TabItem tab = ((TabFolder)(main_folder.getSelection()[0].getControl())).getSelection()[0];
						String table_name = tab.getText();
						TableItem[] rows = ((Table)tab.getControl()).getSelection();
						
						int[] indexes = new int[rows.length];
						for(int i=0; i<rows.length; i++){
							
							indexes[i]=Integer.parseInt(rows[i].getText(0));
						
						}

						//Helper_functions.delete_rows(table_name, indexes);
						
						
						String search_var = search_text.getText();						
						int min_rating = sp_min_rating.getSelection();
						int min_year = sp_min_year.getSelection();
						int min_pop = sp_min_population.getSelection();
						
						Helper_functions.filter_search(search_var, min_rating, min_year, min_pop, 
								table_name, (Table)tab.getControl(), 0);
					}
					catch (Exception ex){
					}
					window_busy=false;
				}
				
			}
		});
		
		Button crop_button = new Button(this, SWT.NONE);
		crop_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		crop_button.setText("crop selected rows");
		crop_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){
					window_busy=true;
					try{	
						TabItem tab = ((TabFolder)(main_folder.getSelection()[0].getControl())).getSelection()[0];
						String table_name = tab.getText();
						TableItem[] rows = ((Table)tab.getControl()).getSelection();
						
						int[] indexes = new int[rows.length];
						for(int i=0; i<rows.length; i++){
							
							indexes[i]=Integer.parseInt(rows[i].getText(0));
							
						}

						//Helper_functions.crop_rows(table_name, indexes);
						
						String search_var = search_text.getText();						
						int min_rating = sp_min_rating.getSelection();
						int min_year = sp_min_year.getSelection();
						int min_pop = sp_min_population.getSelection();
						
						Helper_functions.filter_search(search_var, min_rating, min_year, min_pop, 
								table_name, (Table)tab.getControl(), 0);
					}
					catch (Exception ex){
					}
					window_busy=false;
				}
				
			}
		});
		
		Button add_row_button = new Button(this, SWT.NONE);
		add_row_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		add_row_button.setText("add a row with the name:");
		add_row_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){
					window_busy=true;
					try{	
						TabItem tab = ((TabFolder)(main_folder.getSelection()[0].getControl())).getSelection()[0];
						String table_name = tab.getText();
						
						String row_name = new_row_text.getText();

						//Helper_functions.add_row(table_name, row_name);
						
						String search_var = search_text.getText();						
						int min_rating = sp_min_rating.getSelection();
						int min_year = sp_min_year.getSelection();
						int min_pop = sp_min_population.getSelection();
						
						Helper_functions.filter_search(search_var, min_rating, min_year, min_pop, 
								table_name, (Table)tab.getControl(), 0);
					}
					catch (Exception ex){
					}
					window_busy=false;
				}
				
			}
		});
		
		new_row_text = new Text(this, SWT.BORDER);
		new_row_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 8, 1));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		

		
		main_folder = new TabFolder(this, SWT.NONE);
		GridData gd_main_folder = new GridData(SWT.FILL, SWT.FILL, true, true, 12, 6);
		gd_main_folder.widthHint = 840;
		gd_main_folder.minimumWidth = 20;
		gd_main_folder.heightHint = 263;
		main_folder.setLayoutData(gd_main_folder);
		
		
		//create the menus
		Build_tables.create_cinema_menu(this);
		Build_tables.create_music_menu(this);
		Build_tables.create_places_menu(this);
		Build_tables.create_sports_menu(this);
		for(int i=0;i<30;i++)
			new Label(this, SWT.NONE);
		
		Label lblSearchParameter = new Label(this, SWT.NONE);
		lblSearchParameter.setText("search text:");
		
		Label lblMinimumRating = new Label(this, SWT.NONE);
		lblMinimumRating.setText("min. rating:");
		
		Label lbl_min_birth = new Label(this, SWT.NONE);
		lbl_min_birth.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		lbl_min_birth.setText("min. birth/release year:");
		
		Label lbl_min_population = new Label(this, SWT.NONE);
		lbl_min_population.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		lbl_min_population.setText("min. population (places):");
		new Label(this, SWT.NONE);
		
		
		Button search_button = new Button(this, SWT.NONE);
		GridData gd_search_button = new GridData(SWT.LEFT, SWT.FILL, false, false, 7, 2);
		gd_search_button.heightHint = 27;
		gd_search_button.widthHint = 181;
		search_button.setLayoutData(gd_search_button);
		search_button.setText("FILTER / SEARCH");
		search_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){
					window_busy=true;
					try{	
						String search_var = search_text.getText();
						int min_rating = sp_min_rating.getSelection();
						int min_year = sp_min_year.getSelection();
						int min_population = sp_min_population.getSelection();
						String table_name = 
						((TabFolder)(main_folder.getSelection()[0].getControl())).getSelection()[0].getText();
						Table tbl =
							(Table)(((TabFolder)(main_folder.getSelection()[0].getControl())).getSelection()[0].getControl());
						
						Helper_functions.filter_search(search_var, min_rating, min_year, min_population, table_name, tbl, 0);
						

					}
					catch (Exception ex){
					}
					window_busy=false;
				}
				
			}
		});
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		search_text = new Text(this, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 73;
		search_text.setLayoutData(gd_text);
		
		sp_min_rating = new Spinner(this, SWT.BORDER);
		sp_min_rating.setMaximum(2000000000);
		GridData gd_spinner = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_spinner.minimumWidth = 10;
		sp_min_rating.setLayoutData(gd_spinner);
		

		
		sp_min_year = new Spinner(this, SWT.BORDER);
		sp_min_year.setMaximum(2000000000);
		sp_min_year.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		
		sp_min_population = new Spinner(this, SWT.BORDER);
		sp_min_population.setMaximum(2000000000);
		sp_min_population.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(848, 491);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}	
	
	public static void fill_map(){
		category_links_map.put("Categories", "Movies");
		category_links_map.put("Actors", "Movies");
		category_links_map.put("Movies", "Actors");
		category_links_map.put("Artists", "Creations");
		category_links_map.put("Creations", "Artists");
		category_links_map.put("Countries", "Locations");
		category_links_map.put("Locations", "Countries");
		category_links_map.put("NBA players", "NBA teams");
		category_links_map.put("NBA teams", "NBA players");
		category_links_map.put("Israeli soccer players", "Israeli soccer teams");
		category_links_map.put("Israeli soccer teams", "Israeli soccer players");
		category_links_map.put("World soccer players", "World soccer teams");
		category_links_map.put("World soccer teams", "World soccer players");
	}
	
}
