package gui;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.*;

public class passwordScreenUI extends AbstractScreenUI{
	//
	protected String username;
	protected String password;
	//protected boolean added = false;

	public passwordScreenUI(Shell shell) {
		super(shell, null, "User Name and Password");
//		if(added == true){
//			
//		}
		
	}


	public void setLayout() {
		GridLayout layout = new GridLayout(1, false);
		shell.setLayout(layout);
		
	}


	public void makeWidgets() {
		
		Label title = new Label(shell, SWT.NONE);
		title.setText("Please input your mySql username and your mySql password into the appropriate fields.");
		GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		title.setLayoutData(data);
		
		
		Label labelUser = new Label(shell, SWT.NONE);
	    labelUser.setText("MySql username:");
	    final Text usernameText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    usernameText.setLayoutData(data);
		
	    Label labelPassword = new Label(shell, SWT.NONE);
	    labelPassword.setText("MySql password:");
	    final Text passwordText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    passwordText.setLayoutData(data);
	    
	    Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {		                   
				username = usernameText.getText();   // not secure!! open to SQL injection, directory traversal?
				password = passwordText.getText();
				DatabaseConnection.databaseConnection.setUsername(username);
				DatabaseConnection.databaseConnection.setPassword(password);
				new MainMenuScreenUI(shell);
			}
		});
		
	}

}
