package gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class GamePropetiesScreenUI extends AbstractScreenUI {
	
	private Composite game_other;
	private Composite players_comp;
	private Scale input_difficulty;
	private Spinner input_rounds;
	private Group categories_list;
	
	private static String[] mainCategories = {"ACTORS", "ARTIST", "COUNTRIES", "MOVIES", "MUSICANS"};
	
	
//	private String[] moviesCategories = {"Action","Adventure","Animated","Comedy",
//			"Crime", "Documentary","Drama","Fantasy","Horror", "Musical", "Mystery","Romance","Science fiction",
//			"Sports","Teen","Television","Thriller","War", "Western"};
	
	private String[] sportCategories = { "World soccer" , "Israeli soccer", "NBA" };
	

	public GamePropetiesScreenUI(Shell shell) {
		super(shell,null, "Game Properties");
	}
	
	public void setLayout() {
		shell.setLayout(new StackLayout());
	}
	
	public void makeWidgets() {
		
		
		buildGameOther();
		
		buildPlayersComp();

		((StackLayout)shell.getLayout()).topControl = game_other;
	}

	private void buildPlayersComp() {
		players_comp = new Composite(shell, SWT.NONE);
		
		players_comp.setLayout(new GridLayout(3, false));
		
		Label add_players_label = new Label(players_comp, SWT.NONE);
		add_players_label.setText("Add player:");
		
		final Text input_players = new Text(players_comp, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		input_players.setLayoutData(data);
		
		Button add_player = new Button(players_comp, SWT.PUSH);
		add_player.setText("add");
		
		Label players_label = new Label(players_comp, SWT.NONE);
		players_label.setText("players:");
		
		final List players = new List(players_comp, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		players.setLayoutData(data);
		
		add_player.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!input_players.getText().equals("")) {
					players.add(input_players.getText());
					input_players.setText("");
				}
			}
		});
		
		Button prev_comp = new Button(players_comp, SWT.PUSH);
		prev_comp.setText("previous");
		
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		data.horizontalSpan = 2;
		prev_comp.setLayoutData(data);
		
		prev_comp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((StackLayout)shell.getLayout()).topControl = game_other;
				shell.layout();
			}
		});
		
		Button start = new Button(players_comp, SWT.PUSH);
		start.setText("start");
		
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.horizontalSpan = 1;
		start.setLayoutData(data);
		
		start.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new GameScreenUI(shell, players.getItems(), input_difficulty.getSelection(),
						Integer.parseInt(input_rounds.getText()), getCategorySelection());
			}

		});
	}

	private void buildGameOther() {
		game_other = new Composite(shell, SWT.NONE);
		
		game_other.setLayout(new GridLayout(3, false));
		
		Label categories = new Label(game_other, SWT.NONE);
		categories.setText("categories:");
		
		categories_list = new Group(game_other, SWT.SHADOW_OUT);
		createCategoriesButtons();
		categories_list.setLayout(new FillLayout());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		categories_list.setLayoutData(data);
		
		Label difficulty = new Label(game_other, SWT.NONE);
		difficulty.setText("difficulty:");
		
		input_difficulty = new Scale(game_other, SWT.NONE);
		input_difficulty.setMaximum (10);
		input_difficulty.setPageIncrement(1);
		input_difficulty.setMinimum(1);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		input_difficulty.setLayoutData(data);
		
		Label rounds = new Label(game_other, SWT.NONE);
		rounds.setText("rounds:");
		
		input_rounds = new Spinner(game_other, SWT.BORDER);
		input_rounds.setMinimum(1);
		input_rounds.setMaximum(10);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		input_rounds.setLayoutData(data);
		
		Button back_main = new Button(game_other, SWT.PUSH);
		back_main.setText("Main Menu");
		
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		data.horizontalSpan = 2;
		back_main.setLayoutData(data);
		
		back_main.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new MainMenuScreenUI(shell);
			}
		});
		
		Button next_comp = new Button(game_other, SWT.PUSH);
		next_comp.setText("next");
		
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.horizontalSpan = 1;
		next_comp.setLayoutData(data);
		
		next_comp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((StackLayout)shell.getLayout()).topControl = players_comp;
				shell.layout();
			}
		});
	}

	private void createCategoriesButtons() {
		Button curButton;
		for (String curName : mainCategories ){
			curButton = new Button(categories_list, SWT.CHECK);
			curButton.setText(curName);
			curButton.setSelection(true);
		}
	}
	
	private String[] getCategorySelection() {
		ArrayList<String> curSelectedCategories = new ArrayList<>();
		for (Control widget : categories_list.getChildren())
		{
			if(((Button)widget).getSelection())
				curSelectedCategories.add(((Button)widget).getText());
		}
		String[] outputSelectedCategories =new String[curSelectedCategories.size()];
		for(int i = 0; i<curSelectedCategories.size();i++)
			outputSelectedCategories[i] = curSelectedCategories.get(i);
		return outputSelectedCategories;
			
	}
	
}
