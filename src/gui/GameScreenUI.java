package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import App.Game;

public class GameScreenUI extends AbstractScreenUI {

	private String[] players;
	private int[] players_score;
	private Label[] players_label;
	private Label round_label;
	private List clues_list;
	private int round;
	private TimerWidget timerWidget;
	private Game game;
	private Button start_game;
	
	public GameScreenUI(Shell shell, final String[] players, final int difficultLevel,
			final int numOfRounds, final String[] categoryList) {
		super(shell, players, "Game");
		(new Runnable() {
			
			@Override
			public void run() {
				game = new Game(difficultLevel, numOfRounds, categoryList, players);
				start_game.setEnabled(!start_game.getEnabled());
			}
			
		}).run();
	}
	
	@Override
	public void initialize(String[] players) {
		this.players = players;
		this.players_score = new int[this.players.length];
		this.players_label = new Label[this.players.length];
		this.round = 1;
	}
	
	@Override
	public void setLayout() {
		shell.setLayout(new GridLayout(4, false));
	}

	@Override
	public void makeWidgets() {
		Group score_group = new Group(shell, SWT.NONE);
		
		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.verticalSpan = 2;
		score_group.setLayoutData(data);
		score_group.setLayout(new GridLayout(1, false));
		
		for(int i = 0; i< players_label.length; i++) {
			players_label[i] = new Label(score_group, SWT.NONE);
			players_label[i].setText(players[i] + ": " + players_score[i]);
		}
		
		//clock
		this.timerWidget = new TimerWidget(score_group, SWT.NONE);
		
		round_label = new Label(score_group, SWT.NONE);
		round_label.setText("Round: " + round);
		
		this.clues_list = new List(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		clues_list.setLayoutData(data);
		
		Group answers_group = new Group(shell, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		answers_group.setLayoutData(data);
		answers_group.setLayout(new GridLayout(2, true));
		
		
		
		Shell wait = new Shell(Display.getCurrent(), SWT.ON_TOP);
		createWaitingShell(wait);
		wait.pack();
		wait.open();
	}
	
	private void createWaitingShell(Shell wait) {
		wait.setLayout(new GridLayout(2, false));
		
		for(int i = 0; i< players.length; i++) {
			Label name = new Label(wait, SWT.NONE);
			name.setText(players[i]+", your key is: ");
			
			Label key=new Label(wait, SWT.NONE);
			key.setText(""+(i+1));
		}
		
		Button back = new Button(wait, SWT.PUSH);
		back.setText("back to main menu");
		
		start_game = new Button(wait, SWT.PUSH);
		start_game.setText("start game");
		start_game.setEnabled(!start_game.getEnabled());
	}

	public void startTimerUI() {
		this.timerWidget.startTimerUI();
	}
}
