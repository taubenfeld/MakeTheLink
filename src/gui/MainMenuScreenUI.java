package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

public class MainMenuScreenUI extends AbstractScreenUI {
	
	public MainMenuScreenUI(Shell shell) {
		super(shell,null, "Main Menu");
	}

	public void setLayout() {
		shell.setLayout(new FillLayout(SWT.VERTICAL));
	}
	
	public void makeWidgets() {
		Button new_game = new Button(shell, SWT.PUSH);
		new_game.setText("new game");
		
		Button high_scores = new Button(shell, SWT.PUSH);
		high_scores.setText("high scores");
		
		Button instructions = new Button(shell, SWT.PUSH);
		instructions.setText("instructions");
		
		new_game.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new GamePropetiesScreenUI(shell);
			}
		});
	}
}
