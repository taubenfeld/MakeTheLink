package gui;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

public class chooseUpdateUI extends AbstractScreenUI{

	public chooseUpdateUI(Shell shell) {
		super(shell, null, "choose update type");
		
	}

	@Override
	public void setLayout() {
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		
	}

	@Override
	public void makeWidgets() {
		
		Button fullUpdate = new Button(shell, SWT.PUSH);
		fullUpdate.setText("Full Update from YAGO files");
		
		Button manualUpdate = new Button(shell, SWT.PUSH);
		manualUpdate.setText("Manual Update");
		
		Button back = new Button(shell, SWT.PUSH);
		back.setText("Back");
		
		fullUpdate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {		                   
				new MainMenuScreenUI(shell);   //fix! talk to michael, problematic constractor on admin_gui
			}
		});
		
		manualUpdate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {		                   
				new MainMenuScreenUI(shell);   //fix! talk to michael, problematic constractor on admin_gui
			}
		});
		
		back.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {		                   
				new MainMenuScreenUI(shell);
			}
		});
		
		
		
	}

}
