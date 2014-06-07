package gui;

import java.util.Map;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractScreenUI implements ScreenGui {
	protected Shell shell;
	protected Listener answerListener;

	public AbstractScreenUI(Shell shell, Map<String, Integer> playersAndKeys,
			String shellText) {
		if (this.answerListener != null) {
			Display.getDefault().removeFilter(1, this.answerListener);
		}
		this.shell = shell;
		this.shell.setText(shellText);
		ShellUtil.cleanShell(this.shell);
		initialize(playersAndKeys);
		setLayout();
		makeWidgets();

		shell.pack();
		shell.setSize(800, 800);

		Display display = Display.getCurrent();
		shell.setBackground(display.getSystemColor(10));
	}

	public void initialize(Map<String, Integer> playersAndKeys) {
		// NON EMPTY OVERIDE ONLY IN GAMESCREEN
	}
}
