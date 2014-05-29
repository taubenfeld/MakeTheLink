package gui;

import org.eclipse.swt.widgets.Shell;

public abstract class AbstractScreenUI implements ScreenGui {

	protected Shell shell;
	
	public AbstractScreenUI(Shell shell, String[] to_init, String shellText) {
		this.shell = shell;
		this.shell.setText(shellText);
		ShellUtil.cleanShell(this.shell);
		initialize(to_init);
		setLayout();
		makeWidgets();
		shell.pack();
	}

	@Override
	public void initialize(String[] st) {
		// TODO Auto-generated method stub
		
	}
}
