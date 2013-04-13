package code;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

public class Driver {
	public static void main(String args[]) {
		// String[] command =
		// {"avconv","-i","/home/gabriel/code/music-converter/test3/zoe.m4a","-q","0","/home/gabriel/code/music-converter/test3/out.mp3"};
		String[] command = { "ls" };

		/*try {
			Process process = Runtime.getRuntime().exec(command);
			Scanner sc = new Scanner(process.getInputStream());
			while (sc.hasNext()) {
				System.out.println(sc.nextLine());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new MainWindow();
				
			}
		});

		
	}
}
