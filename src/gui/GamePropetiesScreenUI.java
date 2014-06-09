package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
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
	


	private  java.util.List<Label> categoriesLabels;
	private  java.util.List<Scale> categoriesScales;

	public GamePropetiesScreenUI(Shell shell) {
		super(shell, null, "Game Properties");
	}

	public void setLayout() {
		this.shell.setLayout(new StackLayout());
	}

	public void makeWidgets() {
		buildCategoriesSelectionScreen();

		buildPlayerSelectionScreen();

		((StackLayout) this.shell.getLayout()).topControl = this.categorySelectionScreen;
	}

	private <playerS> void buildPlayerSelectionScreen() {
		this.playerSelectionScreen = new Composite(this.shell, 0);

		Display display = Display.getCurrent();
		this.playerSelectionScreen.setBackground(display.getSystemColor(10));

		this.playerSelectionScreen.setLayout(new GridLayout(3, false));

		Label add_players_label = new Label(this.playerSelectionScreen, 0);
		Image image = new Image(Display.getDefault(),
				"button&stuff/addPlayer.png");
		add_players_label.setBackground(display.getSystemColor(10));
		add_players_label.setImage(image);

		final Text input_players = new Text(this.playerSelectionScreen, SWT.FILL);
		GridData data = new GridData(768);
		input_players.setLayoutData(data);

		Label addButton = new Label(this.playerSelectionScreen, 0);
		image = new Image(Display.getDefault(), "button&stuff/add.png");
		addButton.setBackground(display.getSystemColor(10));
		addButton.setImage(image);

		Label players_label = new Label(this.playerSelectionScreen, 0);
		image = new Image(Display.getDefault(), "button&stuff/players.png");
		players_label.setBackground(display.getSystemColor(10));
		players_label.setImage(image);

		final List players = new List(this.playerSelectionScreen, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH);

		players.setLayoutData(data);

		addButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				String inputText = input_players.getText();
				java.util.List<String> curPlayers = Arrays.asList(players.getItems());
				if (curPlayers.contains(inputText)){
					MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR);
					errorBox.setMessage("Player name already exist");
					errorBox.open();
					return;
				}
				if (!inputText.equals("")) {
					players.add(input_players.getText());
					input_players.setText("");
				}
			}
		});
		
		input_players.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent event) {
				if (event.detail == SWT.TRAVERSE_RETURN) {
					if (!input_players.getText().equals("")) {
						players.add(input_players.getText());
						input_players.setText("");
					}
				}	
			}
		});
		
		Label prev_comp = new Label(this.playerSelectionScreen, 0);
		prev_comp.setBackground(display.getSystemColor(10));
		image = new Image(Display.getDefault(), "button&stuff/backButton.png");
		prev_comp.setImage(image);

		data = new GridData(32);
		data.horizontalSpan = 2;
		prev_comp.setLayoutData(data);

		prev_comp.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				((StackLayout) GamePropetiesScreenUI.this.shell.getLayout()).topControl = GamePropetiesScreenUI.this.categorySelectionScreen;
				GamePropetiesScreenUI.this.shell.layout();
			}
		});
		Label start = new Label(this.playerSelectionScreen, 0);
		start.setBackground(display.getSystemColor(10));
		image = new Image(Display.getDefault(), "button&stuff/nextButton.png");
		start.setImage(image);

		data = new GridData(128);
		data.horizontalSpan = 1;
		start.setLayoutData(data);

		start.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if(players.getItems().length == 0){
					MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR);
					errorBox.setMessage("Cant start game with no players");
					errorBox.open();
					return;
				}
				Map<String, Integer> nameAndKey = new HashMap<>();
				int i = 0;
				for (String playerName : players.getItems()) {
					nameAndKey.put(playerName, Integer.valueOf(i));
					i++;
				}
				new GameScreenUI(
						GamePropetiesScreenUI.this.shell,
						nameAndKey,
						GamePropetiesScreenUI.this.difficultyScale.getSelection(),
						Integer.parseInt(GamePropetiesScreenUI.this.input_rounds.getText()),
						GamePropetiesScreenUI.this.getCategorySelection());
			}
		});
	}

	private void buildCategoriesSelectionScreen() {
		this.categorySelectionScreen = new Composite(this.shell, 0);

		Display display = Display.getCurrent();
		this.categorySelectionScreen.setBackground(display.getSystemColor(10));

		this.categorySelectionScreen.setLayout(new GridLayout(3, false));

		this.categories_list = new Group(this.categorySelectionScreen, 8);
		this.categories_list.setBackground(display.getSystemColor(10));
		this.categories_list.setLayout(new GridLayout(5, true));
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 5;
		this.categories_list.setLayoutData(data);

		Label categoriesLabel = new Label(this.categories_list, 0);
		Image image = new Image(Display.getDefault(),
				"button&stuff/categories.png");
		categoriesLabel.setBackground(display.getSystemColor(10));
		categoriesLabel.setImage(image);
		categoriesLabel.setLayoutData(data);

		createCategoriesButtons();

		data = new GridData(768);
		data.horizontalSpan = 3;
		Label difficulty = new Label(this.categorySelectionScreen, 0);
		image = new Image(Display.getDefault(), "button&stuff/difficulty.png");
		difficulty.setImage(image);

		difficultyScale = new Scale(this.categorySelectionScreen, SWT.CENTER);
		difficultyScale.setBackground(display.getSystemColor(10));
		difficultyScale.setMaximum(10);
		difficultyScale.setPageIncrement(1);
		difficultyScale.setMinimum(1);
		data = new GridData(768);
		data.horizontalSpan = 2;
		difficultyScale.setLayoutData(data);

		
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 1;
		Label rounds = new Label(this.categorySelectionScreen, 0);
		image = new Image(Display.getDefault(),
				"button&stuff/numberOfRounds.png");
		rounds.setBackground(display.getSystemColor(10));
		rounds.setImage(image);
		rounds.setLayoutData(data);

		input_rounds = new Spinner(this.categorySelectionScreen, 8);
		input_rounds.setBackground(display.getSystemColor(1));

		Font initialFont = this.input_rounds.getFont();
		FontData[] fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setHeight(16);
		}
		Font newFont = new Font(Display.getCurrent(), fontData);
		input_rounds.setFont(newFont);

		input_rounds.setMinimum(1);
		input_rounds.setMaximum(10);
		data = new GridData(4);

		input_rounds.setLayoutData(data);

		Label back_main = new Label(this.categorySelectionScreen, 0);
		back_main.setBackground(display.getSystemColor(10));
		image = new Image(Display.getDefault(), "button&stuff/backButton.png");
		back_main.setImage(image);

		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		back_main.setLayoutData(data);

		back_main.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				new MainMenuScreenUI(GamePropetiesScreenUI.this.shell);
			}
		});
		Label next_comp = new Label(this.categorySelectionScreen, 0);
		next_comp.setBackground(display.getSystemColor(10));
		image = new Image(Display.getDefault(), "button&stuff/nextButton.png");
		next_comp.setImage(image);

		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.horizontalSpan = 1;
		next_comp.setLayoutData(data);

		next_comp.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				((StackLayout) GamePropetiesScreenUI.this.shell.getLayout()).topControl = GamePropetiesScreenUI.this.playerSelectionScreen;
				GamePropetiesScreenUI.this.shell.layout();
			}
		});
	}

	private void createCategoriesButtons() {
		categoriesLabels = new ArrayList<>();
		categoriesScales = new ArrayList<>();
		GridData data = new GridData(4);
		for (String CategoryName : ShellUtil.mainCategories) {
			Label categoryName = new Label(this.categories_list, 0);
			final Label switchButton = new Label(this.categories_list, 0);
			Image nameImage = new Image(Display.getDefault(), String.format(
					"button&stuff/%s.png", new Object[] { CategoryName }));
			categoryName.setImage(nameImage);
			switchButton.setImage(ShellUtil.switchButtonOn);
			switchButton.setLayoutData(data);
			switchButton.setBackground(Display.getCurrent().getSystemColor(10));
			categoriesLabels.add(switchButton);
			if (CategoryName.equals("COUNTRIES")) { //countries don't need year scale
				switchButton.addMouseListener(new MouseAdapter() {
					public void mouseDown(MouseEvent e) {
						if (switchButton.getImage() == ShellUtil.switchButtonOn) {
							switchButton
									.setImage(ShellUtil.switchButtonOff);
						} else {
							switchButton
									.setImage(ShellUtil.switchButtonOn);
						}
					}
				});
			} else {
				final Scale yearScale = new Scale(this.categories_list, 0);
				final Text value = new Text(this.categories_list, 12);

				Font initialFont = value.getFont();
				FontData[] fontData = initialFont.getFontData();
				for (int i = 0; i < fontData.length; i++) {
					fontData[i].setHeight(12);
				}
				Font newFont = new Font(Display.getCurrent(), fontData);
				value.setFont(newFont);
				value.setText("From Year: 1900");
				value.setBackground(Display.getCurrent().getSystemColor(10));
				value.setForeground(Display.getCurrent().getSystemColor(1));

				yearScale
						.setBackground(Display.getCurrent().getSystemColor(10));
				yearScale.setPageIncrement(5);
				yearScale.setMinimum(0);
				yearScale.setMaximum(100);

				GridData data2 = new GridData(768);
				data2.horizontalSpan = 2;
				yearScale.setLayoutData(data2);
				yearScale.addListener(13, new Listener() {
					public void handleEvent(Event event) {
						int perspectiveValue = 1900 + yearScale.getSelection();
						value.setText("From Year: " + perspectiveValue);
					}
				});
				switchButton.addMouseListener(new MouseAdapter() {
					public void mouseDown(MouseEvent e) {
						if (switchButton.getImage() == ShellUtil.switchButtonOn) {
							switchButton
									.setImage(ShellUtil.switchButtonOff);
							yearScale.setEnabled(false);
						} else {
							switchButton
									.setImage(ShellUtil.switchButtonOn);
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
		for (int i = 0; i < 5; i++) {
			if (((Label) categoriesLabels.get(i)).getImage() == ShellUtil.switchButtonOn) {
				if (i == 4) {
					selectedCategories.put(ShellUtil.mainCategories[i],
							Integer.valueOf(0));
				} else {
					selectedCategories.put(ShellUtil.mainCategories[i], Integer
							.valueOf(((Scale) categoriesScales.get(i))
									.getSelection()));
				}
			}
		}
		return selectedCategories;
	}

}
