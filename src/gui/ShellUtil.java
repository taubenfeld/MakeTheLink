package gui;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ShellUtil {
	
	static Display display = new Display();

	// clean the widgets in the shell (dispose them)
	public static void cleanShell(Shell shell) {
		for (Control widget : shell.getChildren())
			widget.dispose();
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
