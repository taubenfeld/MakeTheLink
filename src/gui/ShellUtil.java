package gui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ShellUtil {
	static Display display = new Display();
	
	/*use to make sure that when moving to the next round at game screen
	* key listener wont try to accesses the previous widgets
	*/
	public static int isKeyListenerDisposed = 0;

	static final Image switchButtonOn = new Image(Display.getDefault(),
			"button&stuff/onButton.png");
	static final Image switchButtonOff = new Image(Display.getDefault(),
			"button&stuff/offButton.png");
	static String[] mainCategories = { "ACTORS", "SPORTS", "MOVIES",
		"MUSICANS", "COUNTRIES" };
	
	// clean the widgets in the shell (dispose them)
	public static void cleanShell(Shell shell) {
		for (Control widget : shell.getChildren()) {
			widget.dispose();
		}
	}

	public static Shell getShell() {
		Shell shell = new Shell(display);
		return shell;
	}

	public static void openShell(Shell shell) {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
