package gui;

import org.eclipse.swt.widgets.Shell;

public class Test {

	public static void main(String[] args) {
		Shell shell = ShellUtil.getShell();
		new MainMenuScreenUI(shell);
		ShellUtil.openShell(shell);
		
		
	}

}
