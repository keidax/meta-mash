package code;

import javax.swing.SwingUtilities;

public class Driver {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow();
			}
		});
	}
}
