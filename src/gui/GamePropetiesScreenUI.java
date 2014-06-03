package gui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class GamePropetiesScreenUI extends AbstractScreenUI {
	
	private Composite categorySelectionScreen;
	private Composite playerSelectionScreen;
	private Scale difficultyScale;
	private Spinner input_rounds;
	private Group categories_list;
	
	
	
	/*
	 * Images that use for switch button at categories selection. need to be final because it is
	 * Accessed from an anonymous function
	 */
	final static Image switchButtonOn = new Image(Display.getDefault(), "button&stuff\\onButton.png");
	final static Image switchButtonOff = new Image(Display.getDefault(), "button&stuff\\offButton.png");
	
	
	private static String[] mainCategories = {"ACTORS", "SPORTS",  "MOVIES", "MUSICANS", "COUNTRIES"};
	
	private static java.util.List<Label> categoriesLabels = new ArrayList<>(); //will Contain the switch buttons
	private static java.util.List<Scale> categoriesScales = new ArrayList<>(); //will Contain the scales buttons
	

	
	
	
	public GamePropetiesScreenUI(Shell shell) {
		super(shell,null, "Game Properties");
	}
	
	public void setLayout() {
		shell.setLayout(new StackLayout());
	}
	
	public void makeWidgets() {
		
		
		buildCategoriesSelectionScreen();
		
		buildPlayerSelectionScreen();

		((StackLayout)shell.getLayout()).topControl = categorySelectionScreen;
	}

	private <playerS> void buildPlayerSelectionScreen() {
		playerSelectionScreen = new Composite(shell, SWT.NONE);
		
		//set color to composite
		Display display = Display.getCurrent(); 
		playerSelectionScreen.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		
		playerSelectionScreen.setLayout(new GridLayout(3, false));
		
		Label add_players_label = new Label(playerSelectionScreen, SWT.NONE);
		Image image = new Image(Display.getDefault(), "button&stuff\\addPlayer.png");
		add_players_label.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		add_players_label.setImage(image);
		
		final Text input_players = new Text(playerSelectionScreen, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		input_players.setLayoutData(data);
		
		Label addButton = new Label(playerSelectionScreen, SWT.NONE);
		image = new Image(Display.getDefault(), "button&stuff\\add.png");
		addButton.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		addButton.setImage(image);
		
		Label players_label = new Label(playerSelectionScreen, SWT.NONE);
		image = new Image(Display.getDefault(), "button&stuff\\players.png");
		players_label.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		players_label.setImage(image);
		
		final List players = new List(playerSelectionScreen, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		data = new GridData(GridData.FILL_BOTH);
//		data.horizontalSpan = 2;
		players.setLayoutData(data);

		addButton.addMouseListener(new MouseAdapter () {
			public void mouseDown(MouseEvent e) {
				if (!input_players.getText().equals("")) {
					players.add(input_players.getText());
					input_players.setText("");
				}
			}
			
		});
		

		Label prev_comp = new Label(playerSelectionScreen, SWT.NONE);
		prev_comp.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		image = new Image(Display.getDefault(), "button&stuff\\backButton.png");
		prev_comp.setImage(image);
		
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		data.horizontalSpan = 2;
		prev_comp.setLayoutData(data);
		
		prev_comp.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				((StackLayout)shell.getLayout()).topControl = categorySelectionScreen;
				shell.layout();
			}
		});
		
		
		Label start = new Label(playerSelectionScreen, SWT.NONE);
		start.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		image = new Image(Display.getDefault(), "button&stuff\\nextButton.png");
		start.setImage(image);
		
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.horizontalSpan = 1;
		start.setLayoutData(data);
		
		start.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				new GameScreenUI(shell, players.getItems(), difficultyScale.getSelection(),
						Integer.parseInt(input_rounds.getText()), getCategorySelection());
			}

		});
	}

	private void buildCategoriesSelectionScreen() {
		

		categorySelectionScreen = new Composite(shell, SWT.NONE);
		
		//set color to composite
		Display display = Display.getCurrent(); 
		categorySelectionScreen.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		
		categorySelectionScreen.setLayout(new GridLayout(3, false));
		

		
		categories_list = new Group(categorySelectionScreen, SWT.SHADOW_OUT);
		categories_list.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		categories_list.setLayout(new GridLayout(5, true));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 5;
		categories_list.setLayoutData(data);
		
		Label categoriesLabel = new Label(categories_list, SWT.NONE);
		Image image = new Image(Display.getDefault(), "button&stuff\\categories.png");
		categoriesLabel.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		categoriesLabel.setImage(image);
		categoriesLabel.setLayoutData(data);
		
		
		createCategoriesButtons();
		
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		Label difficulty = new Label(categorySelectionScreen, SWT.NONE);
		image = new Image(Display.getDefault(), "button&stuff\\difficulty.png");
		difficulty.setImage(image);
		
		difficultyScale = new Scale(categorySelectionScreen, SWT.NONE);
		difficultyScale.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		difficultyScale.setMaximum (10);
		difficultyScale.setPageIncrement(1);
		difficultyScale.setMinimum(1);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		difficultyScale.setLayoutData(data);
		
		Label rounds = new Label(categorySelectionScreen, SWT.NONE);
		image = new Image(Display.getDefault(), "button&stuff\\numberOfRounds.png");
		rounds.setImage(image);
		
		input_rounds = new Spinner(categorySelectionScreen, SWT.READ_ONLY);
		input_rounds.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		
		Font initialFont = input_rounds.getFont();
	    FontData[] fontData = initialFont.getFontData();
	    for (int i = 0; i < fontData.length; i++) {
	      fontData[i].setHeight(16);
	    }
	    Font newFont = new Font(Display.getCurrent(), fontData);
	    input_rounds.setFont(newFont);
		
		input_rounds.setMinimum(1);
		input_rounds.setMaximum(10);
		data = new GridData(GridData.FILL);
		//data.horizontalSpan = 2;
		input_rounds.setLayoutData(data);
		
		
		Label back_main = new Label(categorySelectionScreen, SWT.NONE);
		difficultyScale.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		image = new Image(Display.getDefault(), "button&stuff\\backButton.png");
		back_main.setImage(image);
		
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		data.horizontalSpan = 2;
		back_main.setLayoutData(data);
		

		
		back_main.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				new MainMenuScreenUI(shell);
			}
		});
		
		Label next_comp = new Label(categorySelectionScreen, SWT.NONE);
		difficultyScale.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		image = new Image(Display.getDefault(), "button&stuff\\nextButton.png");
		next_comp.setImage(image);
		
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.horizontalSpan = 1;
		next_comp.setLayoutData(data);
		
		//change the top controller (the screen that is displayed) to be playerSelection screen
		next_comp.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				((StackLayout)shell.getLayout()).topControl = playerSelectionScreen;
				shell.layout();
			}
		});
	}

	private void createCategoriesButtons() {
		
		GridData data = new GridData ( GridData.FILL ); 


		
		
		for(String CategoryName: mainCategories){
			
			Label categoryName = new Label(categories_list, SWT.NONE);
			final Label switchButton = new Label(categories_list, SWT.NONE);
			Image nameImage = new Image(Display.getDefault(), 
					String.format("button&stuff\\%s.png", CategoryName) );
			categoryName.setImage(nameImage);
			switchButton.setImage(switchButtonOn);
			switchButton.setLayoutData(data);
			switchButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
			categoriesLabels.add (switchButton);
			
			if(CategoryName.equals("COUNTRIES")){ //listeners to the other categories need to disable scale
				switchButton.addMouseListener(new MouseAdapter(){

					public void mouseDown(MouseEvent e) {
						if (switchButton.getImage() == switchButtonOn)
							switchButton.setImage(switchButtonOff);
						else
							switchButton.setImage(switchButtonOn);
					}
				});
			}
			else{ //countries don't have start year 
				final Scale yearScale = new Scale(categories_list, SWT.NONE);
				final Text value = new Text(categories_list, SWT.SINGLE|SWT.READ_ONLY);
				
			    Font initialFont = value.getFont();
			    FontData[] fontData = initialFont.getFontData();
			    for (int i = 0; i < fontData.length; i++) {
			      fontData[i].setHeight(12);
			    }
			    
			    Font newFont = new Font(Display.getCurrent(), fontData);
			    value.setFont(newFont);
				value.setText("From Year: 1900");
				value.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
				value.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				//value.setLayoutData(data);
				
				yearScale.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
				yearScale.setPageIncrement(5);
				yearScale.setMinimum(0);
				yearScale.setMaximum (100);
				//from some reason when I use the variable date instead of date 2 it cause bags
				GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
				data2.horizontalSpan = 2;
				yearScale.setLayoutData(data2);
				yearScale.addListener(SWT.Selection, new Listener() {	
					@Override
					public void handleEvent(Event event) {
				        int perspectiveValue = 1900 + yearScale.getSelection();
				        value.setText("From Year: " + perspectiveValue);
					}
				    });
				
				switchButton.addMouseListener(new MouseAdapter(){ // listener that  also disables scale
					public void mouseDown(MouseEvent e) {
						if (switchButton.getImage() == switchButtonOn){
							switchButton.setImage(switchButtonOff);
							yearScale.setEnabled(false);
						}
							
						else{
							switchButton.setImage(switchButtonOn);
							yearScale.setEnabled(true);
						}
					}
				});
				
				categoriesScales.add(yearScale);
			}
		}

		
	}

	
	private Map<String, Integer> getCategorySelection() {
		
		Map<String, Integer> selectedCategories = new HashMap<>();
		for (int i=0; i<5; i++)
		{
			if ( (categoriesLabels.get(i)).getImage() == switchButtonOn ){
				if(i==4) // countries don't have scale
					selectedCategories.put(mainCategories[i], 0);
				else
					selectedCategories.put(mainCategories[i], categoriesScales.get(i).getSelection());
			}
		}
		
		return selectedCategories;
			
	}
	
}
