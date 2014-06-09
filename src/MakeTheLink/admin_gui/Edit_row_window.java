package MakeTheLink.admin_gui;

import java.sql.SQLException;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Edit_row_window {
	
	int[] not_linked_loaded = new int[]{0};
	int[] linked_loaded = new int[]{0};

	protected Shell shell;
	protected Object result;
	private TabItem linked_itms_tab;
	private TabItem linkable_items_tab;
	private Button add_remove_links_btn;
	private Label lbl_search;
	private Text search_text;
	private Label lbl_rating;
	private Spinner sp_min_rating;
	private Label lbl_pop_year;
	private Spinner sp_min_pop_year;
	private Button search_button;

	Text[] attributes;//the attributes of the edited item are entered by the user in these fields
	
	Main_edit_data_gui parent_window;
	TabFolder tabs;
	Table source_table;//the table of the edited item
	Table linked_table;//the table of items that are link-able to the edited item
	String source_table_name;//the name of the table of the edited item
	int id;//the id of the edited item in its table
	TableItem[] selected;//the selected rows of the edited item's table - if there are any such rows then we'll edit the first of them.
	String[] attrs = null;//names of the edited item's attributes
	String[] attr_vals = null;//values of the edited item's attributes
	String[] linked_attrs;//names of the attributes of the items that are linked to the edited item
	String linked_query = null;//query to find what items are linked to the edited item
	String not_linked_query = null;//query to find all the link-able items to the edited item
	String label_min_parameter;//label of a search parameter
	String linked_category_name;//name of category of link-able items
	String search_var = "";//search string to search names
	int min_rating = 0;//value of a search parameter, obtained from user input
	int min_pop_year = 0;//value of a search parameter, obtained from user input
	boolean window_busy=false;
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void create(Main_edit_data_gui edg) {
		try {
			Edit_row_window window = new Edit_row_window();
			
			window.open(edg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void open(Main_edit_data_gui edg) throws SQLException {
		this.parent_window=edg;
		Display display = Display.getDefault();
		if(createContents()==1){
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
	}


	protected int createContents() throws SQLException {
		shell = new Shell();

		shell.setText("SWT Application");

		shell.setSize(900, 600);

		shell.setLayout(new GridLayout(11, false));
		
		Button save_and_close_button = new Button(shell, SWT.NONE);
		save_and_close_button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		save_and_close_button.setText("save attributes and close");
		save_and_close_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){
					window_busy=true;
					try{
						
						Helper_functions.save_attributes(source_table_name, id, attributes);
						
						Helper_functions.filter_search("",0,0,0,source_table_name,source_table,0);
						
						Edit_row_window.this.shell.close();

					}
					catch (Exception ex){
					}
					window_busy=false;
				}
				
			}
		});

		
		source_table_name = ((TabFolder)(parent_window.main_folder.getSelection()[0].getControl())).getSelection()[0].getText();
		source_table=
		(Table)(((TabFolder)(parent_window.main_folder.getSelection()[0].getControl())).getSelection()[0].getControl());
		selected = source_table.getSelection();
		try{
			id = Integer.parseInt(selected[0].getText(0));
		}catch(Exception e){
			return 0;
		}
		
		if(source_table_name.compareTo("Actors")==0)
			set_actor_data();
		if(source_table_name.compareTo("Movies")==0)
			set_movie_data();
		if(source_table_name.compareTo("Movies")==0)
			set_movie_data();
		if(source_table_name.compareTo("Categories")==0)
			set_category_data();
		if(source_table_name.compareTo("Artists")==0)
			set_artist_data();
		if(source_table_name.compareTo("Creations")==0)
			set_creation_data();
		if(source_table_name.compareTo("Countries")==0)
			set_country_data();
		if(source_table_name.compareTo("Locations")==0)
			set_location_data();
		if(source_table_name.compareTo("NBA players")==0)
			set_player_data("nba");
		if(source_table_name.compareTo("NBA teams")==0)
			set_team_data("nba");
		if(source_table_name.compareTo("Israeli soccer players")==0)
			set_player_data("israeli_soccer");
		if(source_table_name.compareTo("Israeli soccer teams")==0)
			set_team_data("israeli_soccer");
		if(source_table_name.compareTo("World soccer players")==0)
			set_player_data("world_soccer");
		if(source_table_name.compareTo("World soccer teams")==0)
			set_team_data("world_soccer");
		
		
		int j=0;
		for(; j<attrs.length; j++){
			Label lblNewLabel = new Label(shell, SWT.CENTER);
			lblNewLabel.setText(attrs[j]);
		}
		for(;j<10;j++){
			new Label(shell, SWT.NONE);
		}
		
		add_remove_links_btn = new Button(shell, SWT.NONE);
		add_remove_links_btn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		add_remove_links_btn.setText("add/remove links");
		add_remove_links_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){
					window_busy=true;
					try{
						TableItem[] rows = ((Table)(tabs.getSelection()[0].getControl())).getSelection();
						int[] indexes = new int[rows.length];
						for(int i=0; i<rows.length; i++){
							indexes[i]=Integer.parseInt(rows[i].getText(0));
						}
						//the tab of all the link-able items is selected - we should link the selected items to the current item
						if(tabs.getSelection()[0].getText().compareTo(linked_category_name +" LIST (TO BE ADDED)")==0){

							Helper_functions.link_items(source_table_name, id, indexes);
							
						}
						
						//the tab of currently linked items is selected - we should unlink the selected items from the current item
						if(tabs.getSelection()[0].getText().compareTo("LINKED " + linked_category_name +" (TO BE REMOVED)")==0){

							Helper_functions.un_link_items(source_table_name, id, indexes);
						}
						
						//refresh the tab of the linked items
						search_var = search_text.getText();						
						min_rating = sp_min_rating.getSelection();
						min_pop_year = sp_min_pop_year.getSelection();
						
						if(source_table_name.compareTo("Categories")==0 && id>0)
							id = 0 - id;
						
						Helper_functions.filter_search(search_var, min_rating, min_pop_year, min_pop_year, 
								Main_edit_data_gui.category_links_map.get(source_table_name), linked_table, id);

					}
					catch (Exception ex){
					}
					window_busy=false;
				}
				
			}
		});
		
		attributes = new Text[attr_vals.length];
		for(int i=0; i<attr_vals.length; i++){
			attributes[i] = new Text(shell, SWT.BORDER);
			attributes[i].setText(attr_vals[i]);
			attributes[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
								
		tabs = new TabFolder(shell, SWT.BORDER);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true, 11, 8);
		gd_tabFolder.heightHint = 170;
		tabs.setLayoutData(gd_tabFolder);
		
		linked_itms_tab = new TabItem(tabs, SWT.NONE);
		linked_itms_tab.setText("LINKED " + linked_category_name +" (TO BE REMOVED)");
		
		linked_table = Helper_functions.create_table(tabs, 
				linked_attrs,
				linked_query);
		linked_itms_tab.setControl(linked_table);
		
		linkable_items_tab = new TabItem(tabs, SWT.NONE);
		linkable_items_tab.setText(linked_category_name +" LIST (TO BE ADDED)");
		
		Helper_functions.add_tab_listener
		(		tabs, 
				not_linked_loaded, 
				linked_category_name +" LIST (TO BE ADDED)", 
				linkable_items_tab, 
				linked_attrs, 
				not_linked_query,
				null);
		
		lbl_search = new Label(shell, SWT.NONE);
		lbl_search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lbl_search.setText("Search text:");
		
		lbl_rating = new Label(shell, SWT.NONE);
		lbl_rating.setText("min. rating:");
		
		lbl_pop_year = new Label(shell, SWT.NONE);
		lbl_pop_year.setText(label_min_parameter);
		new Label(shell, SWT.NONE);
		
		search_button = new Button(shell, SWT.NONE);
		search_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 7, 2));
		search_button.setText("FILTER/SEARCH");
		search_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){
					window_busy=true;
					try{						
						int tmp_id=id;
						if(tabs.getSelection()[0].getText().compareTo(linked_category_name +" LIST (TO BE ADDED)")==0){

							tmp_id=0;
							
						}
						//refresh the tab of the linked items
						search_var = search_text.getText();						
						min_rating = sp_min_rating.getSelection();
						min_pop_year = sp_min_pop_year.getSelection();
						
						if(source_table_name.compareTo("Categories")==0 && id>0)
							id = 0 - id;
						
						Table tbl = (Table)(tabs.getSelection()[0].getControl());
						
						Helper_functions.filter_search(search_var, min_rating, min_pop_year, min_pop_year, 
								Main_edit_data_gui.category_links_map.get(source_table_name), tbl, tmp_id);

					}
					catch (Exception ex){
					}
					window_busy=false;
				}
				
			}
		});
		
		search_text = new Text(shell, SWT.BORDER);
		search_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		sp_min_rating = new Spinner(shell, SWT.BORDER);
		sp_min_rating.setMaximum(2000000000);
		sp_min_rating.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		sp_min_pop_year = new Spinner(shell, SWT.BORDER);
		sp_min_pop_year.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		sp_min_pop_year.setMaximum(2000000000);
		new Label(shell, SWT.NONE);
		
		return 1;
	}
	
	public void set_actor_data(){
		
		attrs = new String[]{"actor name:", "rating:", "birth year:"};
		attr_vals = new String[]{selected[0].getText(1), selected[0].getText(2), selected[0].getText(3)};
		
		linked_query =	" select distinct m.id, m.name, m.num_links, m.year_made " +
											" from curr_cinema_movies m, " +
											" curr_cinema_actor_movie am" +
											" where am.movie_id=m.id and am.actor_id="+id+
											" order by num_links desc ";
		
		
		not_linked_query = " select distinct m.id, m.name, m.num_links, m.year_made " +
				" from curr_cinema_movies m ";
		
		linked_attrs = new String[]{"id", "movie name", "rating", "release year"};
		label_min_parameter="min. release year:";
		linked_category_name = "MOVIES";
	}
	public void set_movie_data(){

		attrs = new String[]{"movie name:", "rating:", "release year:"};
		attr_vals = new String[]{selected[0].getText(1), selected[0].getText(2), selected[0].getText(3)};
		
		linked_query =	" select distinct a.id, a.name, a.num_links, a.year_born " +
											" from curr_cinema_actors a, " +
											" curr_cinema_actor_movie am" +
											" where am.actor_id=a.id and am.movie_id="+id+
											" order by num_links desc ";
		
		
		not_linked_query = " select distinct a.id, a.name, a.num_links, a.year_born " +
				" from curr_cinema_actors a ";
		
		linked_attrs = new String[]{"id", "actor name", "rating", "birth year"};
		label_min_parameter="min. birth year:";
		linked_category_name = "ACTORS";
	}
	public void set_category_data(){

		attrs = new String[]{"category name:"};
		attr_vals = new String[]{selected[0].getText(1)};
		
		linked_query =	" select distinct m.id, m.name, m.num_links, m.year_made " +
				" from curr_cinema_movies m, " +
				" curr_cinema_movie_tag mt" +
				" where mt.movie_id=m.id and mt.tag_id="+id+
				" order by num_links desc ";

		not_linked_query = " select distinct m.id, m.name, m.num_links, m.year_made " +
				" from curr_cinema_movies m ";
		
		linked_attrs = new String[]{"id", "movie name", "rating", "release year"};
		label_min_parameter="min. release year:";
		linked_category_name = "MOVIES";
	}
	public void set_artist_data(){
		
		attrs = new String[]{"artist name:", "rating:", "birth year:"};
		attr_vals = new String[]{selected[0].getText(1), selected[0].getText(2), selected[0].getText(3)};
		
		linked_query =	" select distinct c.id, c.name, c.num_links, c.year_made " +
											" from curr_music_creations c, " +
											" curr_music_artist_creation ac" +
											" where ac.creation_id=c.id and ac.artist_id="+id+
											" order by num_links desc ";
		
		
		not_linked_query = " select distinct c.id, c.name, c.num_links, c.year_made " +
				" from curr_music_creations c ";
		
		linked_attrs = new String[]{"id", "creation name", "rating", "release year"};
		label_min_parameter="min. release year:";
		linked_category_name = "CREATIONS";
	}
	public void set_creation_data(){

		attrs = new String[]{"creation name:", "rating:", "release year:"};
		attr_vals = new String[]{selected[0].getText(1), selected[0].getText(2), selected[0].getText(3)};
		
		linked_query =	" select distinct a.id, a.name, a.num_links, a.birth_year " +
											" from curr_music_artists a, " +
											" curr_music_artist_creation ac " +
											" where ac.artist_id=a.id and ac.creation_id="+id+
											" order by num_links desc ";
				
		not_linked_query = " select distinct a.id, a.name, a.num_links, a.birth_year " +
				" from curr_music_artists a ";
		
		linked_attrs = new String[]{"id", "artist name", "rating", "birth year"};
		label_min_parameter="min. birth year:";
		linked_category_name = "ARTISTS";
	}
	public void set_country_data(){

		attrs = new String[]{"country name:", "area (1000 km^2):", "GDP per capita (1000 $):",
				"population (million):", "capital:", "GDP (billion $):"};
		attr_vals = new String[]{selected[0].getText(1), selected[0].getText(2), selected[0].getText(3),
									selected[0].getText(4), selected[0].getText(5), selected[0].getText(6)};
		
		linked_query =	" select distinct l.id, l.name, l.num_links, l.population " +
											" from curr_places_locations l, " +
											" curr_places_location_country lc " +
											" where lc.location_id=l.id and lc.country_id="+id+
											" order by num_links desc ";
				
		not_linked_query = " select distinct l.id, l.name, l.num_links, l.population " +
				" from curr_places_locations l ";
		
		linked_attrs = new String[]{"id", "location name", "rating", "population"};
		label_min_parameter="min. population:";
		linked_category_name = "LOCATIONS";
	}
	
	public void set_location_data(){

		attrs = new String[]{"location name:", "rating:", "population:"};
		attr_vals = new String[]{selected[0].getText(1), selected[0].getText(2), selected[0].getText(3)};
		
		linked_query =	" select distinct c.`id`, c.`name`, c.`area (1000 km^2)`, c.`GDP per capita (1000 $)`, " +
								"c.`population (million)`, c.`capital`, c.`GDP (billion $)` " +
											" from curr_places_countries c, " +
											" curr_places_location_country lc " +
											" where lc.country_id=c.id and lc.location_id="+id+
											" order by c.`GDP (billion $)` desc ";
				
		not_linked_query = " select distinct c.`id`, c.`name`, c.`area (1000 km^2)`, c.`GDP per capita (1000 $)`, " +
				" c.`population (million)`, c.`capital`, c.`GDP (billion $)` " +
				" from curr_places_countries c ";
		
		linked_attrs = new String[]{"id:", "country name:", "area (1000 km^2):", "GDP per capita (1000 $):",
				"population (million):", "capital:", "GDP (billion $):"};
		label_min_parameter="min. population:";
		linked_category_name = "COUNTRIES";
	}
	public void set_player_data(String league){
		
		attrs = new String[]{"player name:", "rating:", "birth year:"};
		attr_vals = new String[]{selected[0].getText(1), selected[0].getText(2), selected[0].getText(3)};
		
		linked_query =	" select distinct t.id, t.name, t.links_to_team, t.creation_year " +
											" from curr_"+league+"_teams t, " +
											" curr_"+league+"_player_team pt" +
											" where pt.team_id=t.id and pt.player_id="+id+
											" order by links_to_team desc ";
		
		
		not_linked_query = " select distinct t.id, t.name, t.links_to_team, t.creation_year " +
				" from curr_"+league+"_teams t ";
		
		linked_attrs = new String[]{"id", "team name", "rating", "creation year"};
		label_min_parameter="min. creation year:";
		linked_category_name = "TEAMS";
	}
	public void set_team_data(String league){

		attrs = new String[]{"team name:", "rating:", "release year:"};
		attr_vals = new String[]{selected[0].getText(1), selected[0].getText(2), selected[0].getText(3)};
		
		linked_query =	" select distinct p.id, p.name, p.links_to_player, p.birth_year " +
											" from curr_"+league+"_players p, " +
											" curr_"+league+"_player_team pt " +
											" where pt.player_id=p.id and pt.team_id="+id+
											" order by links_to_player desc ";
				
		not_linked_query = " select distinct p.id, p.name, p.links_to_player, p.birth_year " +
				" from curr_"+league+"_players p ";
		
		linked_attrs = new String[]{"id", "player name", "rating", "birth year"};
		label_min_parameter="min. birth year:";
		linked_category_name = "PLAYERS";
	}
}

