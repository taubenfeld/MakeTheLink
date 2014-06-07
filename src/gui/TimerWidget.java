package gui;

import App.Game;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class TimerWidget extends Composite {
	private Label one_second_label;
	private Label ten_second_label;
	private Timer timer;
	private GameScreenUI gameScreen;

	public TimerWidget(Composite parent, int style, GameScreenUI gameScreen) {
		super(parent, style);
		createControl();
		this.gameScreen = gameScreen;
	}

	private void createControl() {
		RowLayout rowLayout = new RowLayout();
		rowLayout.spacing = 0;
		rowLayout.wrap = false;
		setLayout(rowLayout);
		new Label(this, 0).setText("Timer: ");
		this.ten_second_label = new Label(this, 0);
		this.one_second_label = new Label(this, 0);
		clearTimerUI();
	}

	public void clearTimerUI() {
		if ((this.one_second_label == null)
				|| (this.one_second_label.isDisposed())) {
			return;
		}
		this.one_second_label.setText("0");
		this.ten_second_label.setText("6");
	}

	public void startTimerUI() {
		if (this.one_second_label == null) {
			return;
		}
		clearTimerUI();
		final long startTime = System.currentTimeMillis();
		if (this.timer != null) {
			this.timer.cancel();
		}
		this.timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			public void run() {
				long elapsedSecond = (System.currentTimeMillis() - startTime) / 1000L;
				final int second = (int) (60L - elapsedSecond);
				Display display = Display.getDefault();
				if ( TimerWidget.this.one_second_label.isDisposed()
						 || display.isDisposed() ){
					TimerWidget.this.timer.cancel();
					
				}
				if ((second == 0)) {
					display.asyncExec(new Runnable() {
						public void run() {
							TimerWidget.this.timer.cancel();
							TimerWidget.this.one_second_label.setText("0");
							TimerWidget.this.ten_second_label.setText("0");
							TimerWidget.this.gameScreen.getGame()
									.moveToNextRound();
							TimerWidget.this.gameScreen.updateRound();
						}
					});
					return;
				}
				display.asyncExec(new Runnable() {
					public void run() {
						if (TimerWidget.this.one_second_label.isDisposed()) {
							return;
						}
						TimerWidget.this.one_second_label.setText("" + (second % 10));
						TimerWidget.this.ten_second_label.setText("" + (second / 10));
					}
				});
			}
		};
		this.timer.scheduleAtFixedRate(timerTask, 0L, 500L);
	}

	public void stopTimerUI() {
		if (this.timer != null) {
			this.timer.cancel();
		}
		this.timer = null;
	}

	public int getTime() {
		return Integer.parseInt(this.ten_second_label.getText()) * 10
				+ Integer.parseInt(this.one_second_label.getText());
	}
}
