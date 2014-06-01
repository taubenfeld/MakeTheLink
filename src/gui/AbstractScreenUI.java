package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractScreenUI implements ScreenGui {

	protected Shell shell;
	
	/*
	 * at properties screen setLayout creates the stack layout and makeWidget 
	 * creates each grid layout 
	 */
	public AbstractScreenUI(Shell shell, String[] to_init, String shellText) {
		this.shell = shell;
		this.shell.setText(shellText);
		ShellUtil.cleanShell(this.shell); //clean the previous widgets
		initialize(to_init); // non empty override in gameScreenUI
		setLayout();
		makeWidgets(); //Creates the widght for the new window
			
		shell.pack();
		shell.setSize(800, 800);
		
		Display display = Display.getCurrent();
		shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
	}

	@Override
	public void initialize(String[] st) {
		// TODO Auto-generated method stub
		
	}
}
