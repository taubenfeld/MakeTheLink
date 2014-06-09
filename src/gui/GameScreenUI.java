package gui;

import App.Game;
import App.MakeTheLinkMain;
import App.Question;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class GameScreenUI extends AbstractScreenUI {
	private Map<String, Integer> playersAndKeys;
	private String[] players;
	private Label[] players_label;
	private Label round_label;
	private List clues_list;
	private int round;
	private Game game;
	private Button start_game;
	private Group gameInformation;
	private Group score_group;
	private Group answers_group;
	private Font font;
	boolean isAnswerButtonPressed = false;
	private String currentAnsweringUser;
	private boolean isListenerActivate = false;
	
	 static CluesRunnable clueGenrator;
	 static TimerWidget timerWidget;

	public GameScreenUI(Shell shell, Map<String, Integer> playersAndKeys,
			final int difficultLevel, final int numOfRounds,
			final Map<String, Integer> CategoryMap) {
		super(shell, playersAndKeys, "Game");

		Runnable gameInitializer = new Runnable() {
			public void run() {
				try {
					GameScreenUI.this.game = new Game(difficultLevel,
							numOfRounds, CategoryMap,
							GameScreenUI.this.players);
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							GameScreenUI.this.setPlayersScore();
						}
					});
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							GameScreenUI.this.start_game
									.setEnabled(!GameScreenUI.this.start_game
											.getEnabled());
						}
					});
				} catch (Exception e) {
					System.out
							.println("Problem while initilazing Game object");
					System.out.println(e);
				}
			}
		};
		MakeTheLinkMain.threadPool.execute(gameInitializer);
		
	}

	public void initialize(Map<String, Integer> playersAndKeys) {
		this.playersAndKeys = playersAndKeys;
		this.players = ((String[]) playersAndKeys.keySet().toArray(
				new String[playersAndKeys.keySet().size()]));

		this.players_label = new Label[this.players.length];
	}

	private void setPlayersScore() {
		for (Control control : this.score_group.getChildren()) {
			control.dispose();
		}
		Label scores = new Label(this.score_group, 0);
		scores.setText("Players&Scores");
		for (int i = 0; i < this.players_label.length; i++) {
			this.players_label[i] = new Label(this.score_group, 0);
			this.players_label[i].setText(this.players[i] + ": "
					+ this.game.getPlayerNameAndScore().get(this.players[i]));
		}
		this.gameInformation.layout();
		this.score_group.layout();
	}

	public void setLayout() {
		this.shell.setLayout(new GridLayout(4, false));
	}

	public void makeWidgets() {
		this.font = new Font(Display.getDefault(), "Ariel", 16, 1);
		this.gameInformation = new Group(this.shell, 0);

		GridData data = new GridData(1040);

		this.gameInformation.setLayoutData(data);

		this.gameInformation.setLayout(new GridLayout(1, false));

		this.timerWidget = new TimerWidget(this.gameInformation, 0, this);

		this.round_label = new Label(this.gameInformation, 0);
		this.round_label.setText("Round: " + this.round);

		this.score_group = new Group(this.gameInformation, 0);
		this.score_group.setLayout(new GridLayout(1, false));

		this.clues_list = new List(this.shell, 2562);
		this.clues_list.setFont(this.font);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		this.clues_list.setLayoutData(data);

		this.answers_group = new Group(this.shell, 0);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		this.answers_group.setLayoutData(data);

		this.answers_group.setLayout(new GridLayout(2, true));
		this.answers_group.setEnabled(false);

		Shell wait = new Shell(Display.getCurrent(), SWT.ON_TOP);
		createWaitingShell(wait);
		wait.pack();
		wait.open();
	}

	private void createWaitingShell(final Shell waitScreen) {
		waitScreen.setLayout(new GridLayout(2, false));

		GridData data = new GridData(2);
		data.horizontalSpan = 2;

		Label waiting = new Label(waitScreen, 0);
		waiting.setText("Initializing Game");
		waiting.setLayoutData(data);

		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 1;
		for (String playerName : this.playersAndKeys.keySet()) {
			Label name = new Label(waitScreen, 0);
			name.setText(playerName + ", your key is: ");

			Label key = new Label(waitScreen, 0);
			key.setText("" + this.playersAndKeys.get(playerName));
		}
		Button backButton = new Button(waitScreen, 8);
		backButton.setText("Exit Game");

		backButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				waitScreen.close();
				new MainMenuScreenUI(GameScreenUI.this.shell);
			}
		});
		this.start_game = new Button(waitScreen, 8);
		this.start_game.setText("start game");
		this.start_game.setEnabled(!this.start_game.getEnabled());

		this.start_game.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				waitScreen.close();
				GameScreenUI.this.runGame();
			}
		});
	}

	public void startTimerUI() {
		this.timerWidget.startTimerUI();
	}

	public void runGame() {
		
		this.round += 1;

		startTimerUI();
		
		Display display = Display.getDefault();
		display.asyncExec(new Runnable() {
			public void run() {
				GameScreenUI.this.round_label.setText("Round: "
						+ GameScreenUI.this.round);
				GameScreenUI.this.shell.layout();
			}
		});
		runIteration();
	}

	private void finishGame() {
		
		final Shell endScreen = new Shell(Display.getCurrent(), SWT.ON_TOP);
		endScreen.setLayout(new GridLayout(1, false));
		GridData data = new GridData(2);
		
		Map <String, Integer> getPlayerNameAndScore = game.getPlayerNameAndScore();
		String winnerName = (String) ( getPlayerNameAndScore.keySet().toArray() )[0];
		int winnerScorer = getPlayerNameAndScore.get(winnerName);
		for (String playerName : getPlayerNameAndScore.keySet() ){
			if (getPlayerNameAndScore.get(playerName) > winnerScorer){
				winnerScorer = getPlayerNameAndScore.get(playerName);
				winnerName = playerName;
			}
		}
		
//		data.horizontalSpan = 2;

		Label winnerLabel = new Label(endScreen, SWT.FILL);
		winnerLabel.setText("Game end -- the winer is:"  + winnerName + " with score of " + winnerScorer);
		winnerLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_BOTH);

		Button backButton = new Button(endScreen, SWT.PUSH);
		backButton.setText("Main Menu");

		backButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				endScreen.close();
				clueGenrator.terminate();
				new MainMenuScreenUI(GameScreenUI.this.shell);
			}
		});
		endScreen.pack();
		endScreen.open();

	}
	

	private void runIteration() {
		GridData data = new GridData(GridData.FILL_BOTH);

		Question thisQuestion = this.game.getThisQuestion();
		for (int i = 0; i < 4; i++) {
			final Button answer = new Button(this.answers_group, SWT.PUSH);

			answer.setText(thisQuestion.getAnswerOptions()[i]);
			answer.setFont(this.font);
			answer.setLayoutData(data);

			//check if selected answer is the correct answer.
			answer.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					System.out.println("the button was pressed");
					GameScreenUI.this.answers_group.setEnabled(false);

					isAnswerButtonPressed = true;
					
					boolean isCorrectAnswer = GameScreenUI.this.game
							.checkAnswerAndUpdate( GameScreenUI.this.currentAnsweringUser,
									answer.getText(), GameScreenUI.this.timerWidget.getTime());
					
					if (isCorrectAnswer) {
						GameScreenUI.this.userWasRight();
					} else {
						GameScreenUI.this.userWasWrong();
					}
					System.out.println("exit button listener");
				}
			});
		}
		this.shell.layout();

		String clue = thisQuestion.getHintsList()[0];
		this.clues_list.add(clue);

		this.clueGenrator = new CluesRunnable(thisQuestion, this.clues_list);
		MakeTheLinkMain.threadPool.execute(this.clueGenrator);

		ShellUtil.isKeyListenerDisposed = 0;
		createAnswerListener();
	}

	private void userWasRight() {
		while (MakeTheLinkMain.threadPool.getActiveCount() == 0) {
			System.out.println("Number of active threads "
					+ MakeTheLinkMain.threadPool.getActiveCount());
		}
		clueGenrator.terminate();
		ShellUtil.isKeyListenerDisposed = 1;
		this.timerWidget.clearTimerUI();

		updateRound();
	}

	public void updateRound() {
		this.clues_list.setItems(new String[0]);
		for (Control widget : this.answers_group.getChildren()) {
			widget.dispose();
		}
		setPlayersScore();
		this.shell.layout();
		if (round >= game.getNumOfRounds())
			finishGame();
		else
			runGame();
	}

	private void userWasWrong() {
		//update this.game score is done in key listener (if user didn't choose an answer for a few sec)
		//or in this.game.checkAnswerAndUpdate. if user choose the wrong answer
		isListenerActivate = false;
		setPlayersScore();
	}
	
/*
 * creates a display listener that will freeze the screen when user key is pressed.
 * after the key is pressed the user will get 4 seconds to choose a answer. 
 * if he don't choose an answer, then it is like he choose the wrong answer.
 */
	private void createAnswerListener() {
		answerListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (isListenerActivate){ 
					//if some one already preesed a key wont let him press again for 4 sec
					return;
				}
				isListenerActivate = true;
				
				//checking to which player the key that was pressed belongs to
				for (String playerName : GameScreenUI.this.playersAndKeys.keySet()) {
					
					if (ShellUtil.isKeyListenerDisposed == 1) {
						isListenerActivate = false;
						return;
					}
					String playerKey = ""+GameScreenUI.this.playersAndKeys.get(playerName);
					String eventKey = "" + event.character;
					if (playerKey.equals(eventKey)) { 
						if (ShellUtil.isKeyListenerDisposed == 1) {
							isListenerActivate = false;
							return;
						}
						//allow player to choose an answer
						GameScreenUI.this.answers_group.setEnabled(true);

						currentAnsweringUser = playerName;
						Runnable keyChecker = new Runnable() {
							public void run() {
								try {
									System.out.println("going to sleep");
									Thread.sleep(4000L);
									if (ShellUtil.isKeyListenerDisposed == 1) {
										isListenerActivate = false;
										return;
									}
									System.out.println("waking up");
									if (!isAnswerButtonPressed) {
										//user didn't choose an answer for 4 sec his score decreased
										Display.getDefault().syncExec(
												new Runnable() {
													public void run() {
														GameScreenUI.this.game
																.updateScore(
																		GameScreenUI.this.currentAnsweringUser,
																		GameScreenUI.this.timerWidget
																				.getTime(),
																		false);
														if (ShellUtil.isKeyListenerDisposed == 1) {
															isListenerActivate = false;
															return;
														}
														GameScreenUI.this.userWasWrong();
													}
												});
									}
									if (ShellUtil.isKeyListenerDisposed == 1) {
										isListenerActivate = false;
										return;
									}
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													if (ShellUtil.isKeyListenerDisposed == 1) {
														isListenerActivate = false;
														return;
													}
													answers_group
															.setEnabled(false);
													isAnswerButtonPressed = false;
												}
											});
								} catch (Exception e) {
									System.out.println("Sleep Interruption"); 
								}
							}
						};
						if (ShellUtil.isKeyListenerDisposed == 1) {
							isListenerActivate = false;
							return;
						}
						MakeTheLinkMain.threadPool.execute(keyChecker);
						break;
					}
				}
				isListenerActivate = false;
			}
			
		};
		
		Display.getDefault().addFilter(SWT.KeyDown, answerListener);
	}
	


	public Game getGame() {
		return this.game;
	}
}
