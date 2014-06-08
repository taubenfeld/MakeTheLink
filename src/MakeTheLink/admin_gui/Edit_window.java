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


public class Edit_window {
	
	int[] not_linked_loaded = new int[]{0};

	protected Shell shell;
	protected Object result;
	private Text txtPrewritten;
	private TabItem tbtmNewItem;
	private TabItem tbtmNotLinked;
	private Button btnNewButton;
	private Label lblSearchText;
	private Text text;
	private Label lblMinimumRating;
	private Spinner spinner;
	private Label lblCreationYear;
	private Spinner spinner_1;
	private Button btnFiltersearch;

	Edit_data_gui edg;
	int id;
	TableItem[] selected;
	String[] attrs = null;
	String[] attr_vals = null;
	String[] linked_attrs;
	String linked_query = null;
	String not_linked_query = null;
	String label_min_parameter;
	String linked_category;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void doo(Edit_data_gui edg) {
		try {
			Edit_window window = new Edit_window();
			
			window.open(edg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void open(Edit_data_gui edg) throws SQLException {
		this.edg=edg;
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


	protected void createContents() throws SQLException {
		shell = new Shell();

		shell.setText("SWT Application");

		shell.setSize(900, 600);

		shell.setLayout(new GridLayout(11, false));
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnNewButton_1.setText("save attributes");
		
		String table = ((TabFolder)(edg.main_folder.getSelection()[0].getControl())).getSelection()[0].getText();
		Table tb=
		(Table)(((TabFolder)(edg.main_folder.getSelection()[0].getControl())).getSelection()[0].getControl());
		selected = tb.getSelection();
		id = Integer.parseInt(selected[0].getText(0));
		
		if(table.compareTo("Actors")==0)
			set_actor_data();
		if(table.compareTo("Movies")==0)
			set_movie_data();
		if(table.compareTo("Movies")==0)
			set_movie_data();
		if(table.compareTo("Categories")==0)
			set_category_data();
		if(table.compareTo("Artists")==0)
			set_artist_data();
		if(table.compareTo("Creations")==0)
			set_creation_data();
		if(table.compareTo("Countries")==0)
			set_country_data();
		if(table.compareTo("Locations")==0)
			set_location_data();
		if(table.compareTo("NBA players")==0)
			set_player_data("nba");
		if(table.compareTo("NBA teams")==0)
			set_team_data("nba");
		if(table.compareTo("Israeli soccer players")==0)
			set_player_data("israeli_soccer");
		if(table.compareTo("Israeli soccer teams")==0)
			set_team_data("israeli_soccer");
		if(table.compareTo("World soccer players")==0)
			set_player_data("world_soccer");
		if(table.compareTo("World soccer teams")==0)
			set_team_data("world_soccer");
		
		
		int j=0;
		for(; j<attrs.length; j++){
			Label lblNewLabel = new Label(shell, SWT.CENTER);
			lblNewLabel.setText(attrs[j]);
		}
		for(;j<10;j++){
			new Label(shell, SWT.NONE);
		}
		
		btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("add/remove links");
		
		for(int i=0; i<attr_vals.length; i++){
			txtPrewritten = new Text(shell, SWT.BORDER);
			txtPrewritten.setText(attr_vals[i]);
			txtPrewritten.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
								
		TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true, 11, 8);
		gd_tabFolder.heightHint = 170;
		tabFolder.setLayoutData(gd_tabFolder);
		
		tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("LINKED " + linked_category +" (TO BE REMOVED)");
		
		Table linked_table = Helper_functions.create_table(tabFolder, 
				linked_attrs,
				linked_query);
		tbtmNewItem.setControl(linked_table);
		
		tbtmNotLinked = new TabItem(tabFolder, SWT.NONE);
		tbtmNotLinked.setText(linked_category +" LIST (TO BE ADDED)");
		
		Helper_functions.add_tab_listener
		(		tabFolder, 
				not_linked_loaded, 
				linked_category +" LIST (TO BE ADDED)", 
				tbtmNotLinked, 
				linked_attrs, 
				not_linked_query,
				null);
		
		lblSearchText = new Label(shell, SWT.NONE);
		lblSearchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblSearchText.setText("Search text:");
		
		lblMinimumRating = new Label(shell, SWT.NONE);
		lblMinimumRating.setText("min. rating:");
		
		lblCreationYear = new Label(shell, SWT.NONE);
		lblCreationYear.setText(label_min_parameter);
		new Label(shell, SWT.NONE);
		
		btnFiltersearch = new Button(shell, SWT.NONE);
		btnFiltersearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 7, 2));
		btnFiltersearch.setText("FILTER/SEARCH");
		
		text = new Text(shell, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		spinner = new Spinner(shell, SWT.BORDER);
		spinner.setMaximum(2000000000);
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		spinner_1 = new Spinner(shell, SWT.BORDER);
		spinner_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		spinner_1.setMaximum(2000000000);
		new Label(shell, SWT.NONE);
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
		linked_category = "MOVIES";
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
		linked_category = "ACTORS";
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
		linked_category = "MOVIES";
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
		linked_category = "CREATIONS";
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
		linked_category = "ARTISTS";
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
		linked_category = "LOCATIONS";
	}
	
	public void set_location_data(){

		attrs = new String[]{"location name:", "rating:", "population:"};
		attr_vals = new String[]{selected[0].getText(1), selected[0].getText(2), selected[0].getText(3)};
		
		linked_query =	" select distinct c.`name`, c.`area (1000 km^2)`, c.`GDP per capita (1000 $)`, " +
								"c.`population (million)`, c.`capital`, c.`GDP (billion $)` " +
											" from curr_places_countries c, " +
											" curr_places_location_country lc " +
											" where lc.country_id=c.id and lc.location_id="+id+
											" order by c.`GDP (billion $)` desc ";
				
		not_linked_query = " select distinct c.`name`, c.`area (1000 km^2)`, c.`GDP per capita (1000 $)`, " +
				" c.`population (million)`, c.`capital`, c.`GDP (billion $)` " +
				" from curr_places_countries c ";
		
		linked_attrs = new String[]{"country name:", "area (1000 km^2):", "GDP per capita (1000 $):",
				"population (million):", "capital:", "GDP (billion $):"};
		label_min_parameter="min. population:";
		linked_category = "COUNTRIES";
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
		linked_category = "TEAMS";
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
		linked_category = "PLAYERS";
	}
}

