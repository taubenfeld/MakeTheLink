package MakeTheLink.admin_gui;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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

public class Edit_data_gui extends Shell {

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
	private Text text;
	
	/**
	 * Launch the application.
	 * @param args
	 * @throws PropertyVetoException 
	 * @throws SQLException 
	 */
	public static void main(String args[]) throws PropertyVetoException, SQLException {
		
		Connection_pooling.create_pool("root", "1");
		
		try {
			Display display = Display.getDefault();
			Edit_data_gui shell = new Edit_data_gui(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 * @throws SQLException 
	 */
	public Edit_data_gui(Display display) throws SQLException {
		super(display, SWT.SHELL_TRIM | SWT.BORDER);
		setLayout(new GridLayout(16, false));
		
		Button btnEditSelectedRow = new Button(this, SWT.NONE);
		btnEditSelectedRow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnEditSelectedRow.setText("add new row");
		
		Button edit_row_button = new Button(this, SWT.NONE);
		edit_row_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		edit_row_button.setText("edit selected row");
		
		edit_row_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try{
					Table tb=
					(Table)(((TabFolder)(main_folder.getSelection()[0].getControl())).getSelection()[0].getControl());
					TableItem[] selected = tb.getSelection();
					
					
					
					int id = Integer.parseInt(selected[0].getText(0));
					
					Edit_window.doo(Edit_data_gui.this);
					
					
					String st = ((TabFolder)(main_folder.getSelection()[0].getControl())).getSelection()[0].getText();
					
					System.out.println(id + " " + st );
					

				}
				catch (Exception ex){
					
				}
				
			}
		});
		
		Button delete_button = new Button(this, SWT.NONE);
		
				GridData gd_delete_button = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				gd_delete_button.widthHint = 122;
				gd_delete_button.heightHint = 33;
				delete_button.setLayoutData(gd_delete_button);
				delete_button.setText("delete selected rows");
				
				delete_button.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						try{
							Table tb=
							(Table)(((TabFolder)(main_folder.getSelection()[0].getControl())).getSelection()[0].getControl());
							TableItem[] selected = tb.getSelection();
							int lngth = selected.length;
							for(int i=0; i<lngth; i++){
								
								System.out.println(Integer.parseInt(selected[i].getText(0)));
							}
						}
						catch (Exception ex){
							
						}
						
					}
				});
		
		Button crop_button = new Button(this, SWT.NONE);
		crop_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		crop_button.setText("crop selected rows");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		

		
		main_folder = new TabFolder(this, SWT.NONE);
		GridData gd_main_folder = new GridData(SWT.FILL, SWT.FILL, true, true, 16, 6);
		gd_main_folder.minimumWidth = 20;
		gd_main_folder.heightHint = 263;
		main_folder.setLayoutData(gd_main_folder);
		
		
		//create the menus
		Cinema.create_cinema_menu(this);
		Music.create_music_menu(this);
		Places.create_places_menu(this);
		Sports.create_sports_menu(this);
		
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
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Button search_button = new Button(this, SWT.NONE);
		GridData gd_search_button = new GridData(SWT.LEFT, SWT.FILL, false, false, 7, 2);
		gd_search_button.heightHint = 27;
		gd_search_button.widthHint = 181;
		search_button.setLayoutData(gd_search_button);
		search_button.setText("FILTER / SEARCH");
		
		text = new Text(this, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 73;
		text.setLayoutData(gd_text);
		
		Spinner sp_min_rating = new Spinner(this, SWT.BORDER);
		sp_min_rating.setMaximum(2000000000);
		GridData gd_spinner = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_spinner.minimumWidth = 10;
		sp_min_rating.setLayoutData(gd_spinner);
		

		
		Spinner sp_min_birth = new Spinner(this, SWT.BORDER);
		sp_min_birth.setMaximum(2000000000);
		sp_min_birth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		
		Spinner sp_min_population = new Spinner(this, SWT.BORDER);
		sp_min_population.setMaximum(2000000000);
		sp_min_population.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
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
	
	
	
}
