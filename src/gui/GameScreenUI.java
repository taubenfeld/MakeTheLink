package gui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import App.Game;
import App.Question;

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
	private Group score_group;
	private Group answers_group;
	private Map<Integer, String> players_keys = new HashMap<>();
	private volatile boolean round_finished = false;
	
	public GameScreenUI(Shell shell, final String[] players, final int difficultLevel,
			final int numOfRounds, final Map <String, Integer> CategoryMap) {
		super(shell, players, "Game");
		(new Runnable() {
			
			@Override
			public void run() {
				game = new Game(difficultLevel, numOfRounds, CategoryMap, players);
				start_game.setEnabled(!start_game.getEnabled());
			}
			
		}).run();
	}
	
	@Override
	public void initialize(String[] players) {
		this.players = players;
		this.players_score = new int[this.players.length];
		this.players_label = new Label[this.players.length];
		this.round = 0;
	}
	
	@Override
	public void setLayout() {
		shell.setLayout(new GridLayout(4, false));
	}

	@Override
	public void makeWidgets() {
		score_group = new Group(shell, SWT.NONE);
		
		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.verticalSpan = 2;
		score_group.setLayoutData(data);
		score_group.setLayout(new GridLayout(2, false));
		
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
		
		answers_group = new Group(shell, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		answers_group.setLayoutData(data);
		answers_group.setLayout(new GridLayout(2, true));
		
		
		
		Shell wait = new Shell(Display.getCurrent(), SWT.ON_TOP);
		createWaitingShell(wait);
		wait.pack();
		wait.open();
	}
	
	private void createWaitingShell(final Shell wait) {
		wait.setLayout(new GridLayout(2, false));
		
		for(int i = 0; i< players.length; i++) {
			Label name = new Label(wait, SWT.NONE);
			name.setText(players[i]+", your key is: ");
			
			Label key=new Label(wait, SWT.NONE);
			key.setText(""+(i+1));
			
			players_keys.put(i+1, players[i]);
		}
		
		Button back = new Button(wait, SWT.PUSH);
		back.setText("back to main menu");
		
		back.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new MainMenuScreenUI(shell);
			}
		});
		
		start_game = new Button(wait, SWT.PUSH);
		start_game.setText("start game");
		start_game.setEnabled(!start_game.getEnabled());
		
		start_game.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				wait.close();
				(new Runnable() {

					@Override
					public void run() {
						runGame();
						
					}}).run();
			}
		});
	}

	public void startTimerUI() {
		this.timerWidget.startTimerUI();
	}
	
	public void runGame() {
		startTimerUI();
		
		for (int i = 1; i<= game.getNumOfRounds(); i++) {
			round++;
			round_label.setText("Round: " + round);
			
			runIteration();
			
			while(!round_finished);
			
			round_finished = false;
		}
	}
	
	private void runIteration() {		
		final Question thisQuestion = game.getThisQuestion();
			
		final Label answer1 = new Label(answers_group, SWT.NONE);
		answer1.setText(thisQuestion.getAnswerOptions()[0]);
			
		final Label answer2 = new Label(answers_group, SWT.NONE);
		answer2.setText(thisQuestion.getAnswerOptions()[1]);
			
		final Label answer3 = new Label(answers_group, SWT.NONE);
		answer3.setText(thisQuestion.getAnswerOptions()[2]);
			
		final Label answer4 = new Label(answers_group, SWT.NONE);
		answer4.setText(thisQuestion.getAnswerOptions()[3]);
			
		String clue = thisQuestion.getHintsList()[0];
		clues_list.add(clue);
		
		final CluesRunnable clues_run = new CluesRunnable(thisQuestion, clues_list);
		clues_run.run();
		
		clues_list.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}
			
			@Override
			public void keyPressed(final KeyEvent arg0) {
				createAnswerListener(answer1, arg0.keyCode);
				createAnswerListener(answer2, arg0.keyCode);
				createAnswerListener(answer3, arg0.keyCode);
				createAnswerListener(answer4, arg0.keyCode);
			}

			private void createAnswerListener(final Label answer, final int key) {
				
				answer.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseUp(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseDown(MouseEvent arg0) {
						
						if(players_keys.containsKey(key)) {
							if(game.checkAnswerAndUpdate(players_keys.get(key), answer.getText(),
									timerWidget.getTime())) {
								clues_run.terminate();
								round_finished = true;
								return;
							}
						}
					}
					
					@Override
					public void mouseDoubleClick(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
}
