package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MainMenuScreenUI extends AbstractScreenUI {
	
	public MainMenuScreenUI(Shell shell) {
		super(shell,null, "Main Menu");
	}

	public void setLayout() {
		GridLayout layout = new GridLayout(1, false);
		//layout.horizontalSpacing = 30;
//		layout.verticalSpacing = 50;
//		layout.marginHeight = 30;
//		layout.marginWidth = 200;
		shell.setLayout(layout);
	}
	
	public void makeWidgets() {
		
		
		
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessVerticalSpace = true;
		data.grabExcessHorizontalSpace = true;
		
		Label label = new Label(shell, SWT.NONE );
		Image image = new Image(Display.getDefault(), "button&stuff\\makeTheLink.png");
		label.setImage(image);
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE)); 
		label.setLayoutData(data);
		
		
		Label newGameButton = new Label(shell, SWT.RIGHT);
		newGameButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE)); 
		image = new Image(Display.getDefault(), "button&stuff\\startGameButton3.png");
		newGameButton.setImage(image);
		newGameButton.setLayoutData(data);
		
	

		Label highScoresButton = new Label(shell, SWT.RIGHT);
		image = new Image(Display.getDefault(), "button&stuff\\highScores3.png");
		highScoresButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE)); 
		highScoresButton.setImage(image);
		highScoresButton.setLayoutData(data);
		
		Label instructionsButton = new Label(shell, SWT.RIGHT);
		image = new Image(Display.getDefault(), "button&stuff\\instructions3.png");
		instructionsButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE)); 
		instructionsButton.setImage(image);
		instructionsButton.setLayoutData(data);
		

		newGameButton.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				new GamePropetiesScreenUI(shell);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				new GamePropetiesScreenUI(shell);
				
			}
			
		});
		


	}
}
