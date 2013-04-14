package code;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class MainWindow extends JFrame {
	
	int threadsCompleted = 0;

	String[] fileFormats = { "mp3", "ogg", "m4a" };

	public MainWindow() {
		super();
		this.setSize(200, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFileChooser fileChooser = new JFileChooser(new File(
				"/home/gabriel/code/music-converter"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		System.out.println("choose music directory");
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File musicDirectory = fileChooser.getSelectedFile();

			List<File> inputFiles = getMusicFilesFromDirectory(musicDirectory);
			System.out.println(inputFiles.size() + " music files found.");
			System.out.println("choose output directory");
			File outputDirectory = getOutputDirectory();
			// System.out.println(outputDirectory.getPath());
			convertFiles(inputFiles, outputDirectory);
		}
	}

	public File getOutputDirectory() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFileChooser fileChooser = new JFileChooser(new File(
				"/home/gabriel/code/music-converter"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();

		} else {
			return null;
		}

	}

	public boolean isMusicFile(File file) {
		if (file.isDirectory()) {
			return false;
		}
		String filename = file.getName();
		String extension = filename.substring(filename.lastIndexOf(".") + 1,
				filename.length());
		for (String format : fileFormats) {
			if (extension.equals(format)) {
				return true;
			}
		}
		return false;
	}

	public List<File> getMusicFilesFromDirectory(File directory) {
		List<File> musicFiles = new ArrayList<File>();
		if (directory.isHidden() || !directory.isDirectory()
				|| !directory.canRead()) {
			return musicFiles;
		}
		// System.out.println("looking in directory "+directory.getPath());
		for (File child : directory.listFiles()) {
			if (child.exists()) {
				if (child.isDirectory()) {
					musicFiles.addAll(getMusicFilesFromDirectory(child));
				} else if (isMusicFile(child)) {
					musicFiles.add(child);
				}
			}

		}
		return musicFiles;
	}

	public void convertFiles(List<File> files, File outputDirectory) {
		ExecutorService pool = Executors.newCachedThreadPool();
		for (File file : files) {
			String newFilePath = outputDirectory.getAbsolutePath() + "/"
					+ file.getName();
			newFilePath = newFilePath
					.substring(0, newFilePath.lastIndexOf(".")) + ".mp3";
			System.out.println(newFilePath);
			final String[] command = { "avconv", "-i", file.getAbsolutePath(),
					"-q", "0", newFilePath };
			pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Process process = Runtime.getRuntime().exec(command);
						process.waitFor();
						threadDone();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		}
		System.out.println("Files converted!");
	}
	
	private void threadDone(){
		threadsCompleted++;
		System.out.println("converted "+threadsCompleted+" songs");
	}

}
