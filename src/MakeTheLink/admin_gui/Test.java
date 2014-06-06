package MakeTheLink.admin_gui;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.TableCursor;

import MakeTheLink.db.Connection_pooling;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class Test extends Shell {
	private final Table table;
	
	static String[][] actors;

	/**
	 * Launch the application.
	 * @param args
	 * @throws PropertyVetoException 
	 * @throws SQLException 
	 */
	public static void main(String args[]) throws PropertyVetoException, SQLException {
		
		Connection_pooling.create_pool("root", "1");
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		int sz;
		ResultSet rst = stmt.executeQuery(" select count(*) from curr_cinema_actors ");
		rst.next();
		sz=rst.getInt(1);
		actors = new String[sz][4];
		rst = stmt.executeQuery(" select * from curr_cinema_actors order by num_links desc");
		
		for(int i=0; rst.next(); i++){
			for(int j=1;j<=4; j++){
				actors[i][j-1]=rst.getString(j);
			}
		}
		
		rst.close();
		stmt.close();
		
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
	 */
	public Test(Display display) {
		super(display, SWT.SHELL_TRIM | SWT.BORDER);
		setLayout(new GridLayout(1, false));
		
		Button btnNewButton = new Button(this, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.heightHint = 101;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("New Button");
		
		TabFolder tabFolder_1 = new TabFolder(this, SWT.NONE);
		GridData gd_tabFolder_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tabFolder_1.heightHint = 263;
		tabFolder_1.setLayoutData(gd_tabFolder_1);
		
		TabItem tbtmCinema = new TabItem(tabFolder_1, SWT.NONE);
		tbtmCinema.setText("Cinema");
		
		TabFolder tabFolder = new TabFolder(tabFolder_1, SWT.NONE);
		tbtmCinema.setControl(tabFolder);
		
		TabItem tbtmActors = new TabItem(tabFolder, SWT.NONE);
		tbtmActors.setText("Actors");
		
		table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		tbtmActors.setControl(table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setSelection(new int[] {2});
		table.setSelection(3);
		
		final TableColumn tblclmnId = new TableColumn(table, SWT.CENTER);
		tblclmnId.setWidth(100);
		tblclmnId.setText("id");
		
		final TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.CENTER);
		tblclmnNewColumn_1.setText("name");
		tblclmnNewColumn_1.setWidth(123);
		
		final TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("rating");
		
		final TableColumn tblclmnNewc = new TableColumn(table, SWT.NONE);
		tblclmnNewc.setWidth(115);
		tblclmnNewc.setText("birth year");
		
		
		
		
		
		
		
		table.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				int index = table.indexOf(item);
				String[] datum = actors[index];
				item.setText(new String[] {datum[0],
						datum[1], datum[2], datum[3],  });
			}

		});

		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Movies");
		
		

		
		TabItem tbtmMusic = new TabItem(tabFolder_1, SWT.NONE);
		tbtmMusic.setText("Music");
		// Add sort indicator and sort data when column selected
		Listener sortListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = table.getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = table.getSortDirection();
				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					table.setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				final int index = currentColumn == tblclmnId ? 0 : currentColumn == tblclmnNewColumn ? 2 : 
												currentColumn == tblclmnNewc ? 3 : 1;
				final int direction = dir;
				Arrays.sort(actors, new Comparator<String[]>() {
					@Override
					public int compare(String[] a, String[] b) {
						try{
							if (a[index].compareTo(b[index]) == 0) return 0;
							if (direction == SWT.UP) {
								return Integer.parseInt(a[index]) < Integer.parseInt(b[index]) ? -1 : 1;
							}
							return Integer.parseInt(a[index]) < Integer.parseInt(b[index]) ? 1 : -1;
						}
						catch (Exception e){
							if (a[index].compareTo(b[index]) == 0) return 0;
							if (direction == SWT.UP) {
								return a[index].compareTo(b[index])<0 ? -1 : 1;
							}
							return a[index].compareTo(b[index])<0 ? 1 : -1;
						}
					}
				});
				// update data displayed in table
				table.setSortDirection(dir);
				table.clearAll();
			}
		};
		
		
		tblclmnId.addListener(SWT.Selection, sortListener);
		tblclmnNewColumn.addListener(SWT.Selection, sortListener);
		tblclmnNewc.addListener(SWT.Selection, sortListener);
		tblclmnNewColumn_1.addListener(SWT.Selection, sortListener);
		table.setSortColumn(tblclmnNewColumn);
		table.setSortDirection(SWT.DOWN);
		
		
		for(int i=0; i<actors.length; i++){
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] {actors[i][0], actors[i][1], actors[i][2], actors[i][3]});
		}
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
