package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;

import App.Question;

public class CluesRunnable implements Runnable {

	private volatile boolean running = true;
	private Question thisQuestion;
	private List clues_list;

	public CluesRunnable(Question thisQuestion, List clues_list) {
		this.thisQuestion = thisQuestion;
		this.clues_list = clues_list;
	}

	public void terminate() {
		running = false;
	}

	@Override
	public void run() {
		Display display = Display.getDefault();
		for (int j = 1; j < thisQuestion.getHintsList().length; j++) {

			try {
				Thread.sleep(1000 * 60 / (thisQuestion.getHintsList().length));

			} catch (InterruptedException e) {
				running = false;
			}
			if (!running)
				return;

			final String hint = thisQuestion.getHintsList()[j];

			display.asyncExec(new Runnable() {
				public void run() {
					clues_list.add(hint);
				}
			});
		}
	}

}
