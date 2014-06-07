package App;

import gui.MainMenuScreenUI;
import gui.ShellUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.eclipse.swt.widgets.Shell;

public class MakeTheLinkMain {
	public static ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(5);
	public static int isDisposed = 1;

	public static void main(String[] args) {
		Shell shell = ShellUtil.getShell();
		new MainMenuScreenUI(shell);
		ShellUtil.openShell(shell);
		threadPool.shutdown();
	}
}
