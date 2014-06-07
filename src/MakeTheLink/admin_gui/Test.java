package MakeTheLink.admin_gui;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import MakeTheLink.db.Connection_pooling;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Test extends Shell {
	//Table actors_table;

	TabFolder main_folder = null;
	TabFolder cinema_folder = null;
	TabItem movies_item = null;
	TabItem categories_item = null;
	
	boolean movies_loaded=false;
	boolean categories_loaded=false;

	/**
	 * Launch the application.
	 * @param args
	 * @throws PropertyVetoException 
	 * @throws SQLException 
	 */
	public static void main(String args[]) throws PropertyVetoException, SQLException {
		
		Connection_pooling.create_pool("root", "1");
		
		try {
			Display display = Display.getDefault();
			Test shell = new Test(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 * @throws SQLException 
	 */
	public Test(Display display) throws SQLException {
		super(display, SWT.SHELL_TRIM | SWT.BORDER);
		setLayout(new GridLayout(1, false));
		
		Button btnNewButton = new Button(this, SWT.NONE);

		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.heightHint = 33;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("New Button");
		
		main_folder = new TabFolder(this, SWT.NONE);
		GridData gd_tabFolder_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tabFolder_1.heightHint = 263;
		main_folder.setLayoutData(gd_tabFolder_1);
				
		Cinema.create_cinema_menu(this);
	    
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try{
					Table tb=
					(Table)(((TabFolder)(main_folder.getSelection()[0].getControl())).getSelection()[0].getControl());
					TableItem[] selected = tb.getSelection();
					int lngth = selected.length;
					for(int i=0; i<lngth; i++){
						
						System.out.println(Integer.parseInt(selected[i].getText(0)));
					}
				}
				catch (Exception ex){
					
				}
				
			}
		});
		
		
		//Music.create_music_menu(this);
		
		TabItem tbtmMusic = new TabItem(main_folder, SWT.NONE);
		tbtmMusic.setText("Music");
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(446, 491);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	
	
}
