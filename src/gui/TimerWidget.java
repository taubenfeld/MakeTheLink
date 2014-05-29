package gui;

import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class TimerWidget extends Composite {

	private Label one_second_label;
	private Label ten_second_label;
	private Timer timer;

	public TimerWidget(Composite parent, int style) {
		super(parent, style);
		createControl();
	}

	private void createControl() {
		RowLayout rowLayout = new RowLayout();
		rowLayout.spacing = 0;
		rowLayout.wrap = false;
		setLayout(rowLayout);
		(new Label(this, SWT.NONE)).setText("Timer: ");
		ten_second_label = new Label(this, SWT.NONE);
		one_second_label = new Label(this, SWT.NONE);
		clearTimerUI();
	}

	public void clearTimerUI() {
		if (one_second_label == null || one_second_label.isDisposed()) {
			return;
		}
		one_second_label.setText("0");
		ten_second_label.setText("6");
	}

	public void startTimerUI() {
		if (one_second_label == null) {
			return;
		}
		
		clearTimerUI();
		final long startTime = System.currentTimeMillis();
		if (timer != null) {
			timer.cancel();
		}
		
		timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				long elapsedSecond = (System.currentTimeMillis() - startTime) / 1000;
				final int second = (int) (60 - elapsedSecond);
				Display display = Display.getDefault();
				if (second == 0 || one_second_label.isDisposed()
						|| display.isDisposed()) {
					
					display.asyncExec(new Runnable() {
						public void run() {
								one_second_label.setText("0");
								ten_second_label.setText("0");
						}
					});
					timer.cancel();
					return;
				}
				
				display.asyncExec(new Runnable() {
					public void run() {
						if (one_second_label.isDisposed()) {
							return;
						}
						one_second_label.setText("" + (second % 10));
						ten_second_label.setText("" + (second / 10));
					}
				});
			}
		};
		timer.scheduleAtFixedRate(timerTask, 0, 500);
	}

	public void stopTimerUI() {
		if (timer != null) {
			timer.cancel();
		}
		timer = null;
	}

}
