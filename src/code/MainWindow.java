package code;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class MainWindow extends JFrame {

	AtomicInteger runningSongs = new AtomicInteger();
	int totalSongs = 0;
	int songsCompleted = 0;

	String[] fileFormats = { "mp3", "ogg", "m4a" };

	public MainWindow() {
		
		super();
		System.out.println(System.getProperty("os.name"));
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

	public void convertFiles(List<File> files, File baseOutputDirectory) {
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println("# of cores: " + cores);
		ExecutorService pool = Executors.newFixedThreadPool(cores);
		totalSongs = files.size();
		
		//workaround for mac
		String avCommand = System.getProperty("os.name").equalsIgnoreCase("linux")? "avconv":"avconvert";
		
		for (File file : files) {
			
			String fileBase = file.getPath();
			fileBase = fileBase.substring(0, fileBase.lastIndexOf("."));
			
			String newFilePath = baseOutputDirectory.getAbsolutePath() + "/";
			

			String yamlFilePath = fileBase + ".yml";
			File yamlFile = new File(yamlFilePath);
			if(yamlFile.exists()){
				Object data = YamlUtilities.getMatchingYamlData(yamlFile);
				HashMap<String, String> realData = ((LinkedHashMap<String, String>) data);
//				System.out.println("artist: "+realData.get("artist"));
				newFilePath+=realData.get("artist")+"/"+realData.get("album")+"/";
			} else { //no yaml file
				//TODO try to guess artist/album from filename
				newFilePath+="Unknown Artist/Unknow Album/";
			}
			File albumOutputDirectory = new File(newFilePath);
			albumOutputDirectory.mkdirs();
			
			
			newFilePath+=file.getName();
			//make sure output file is mp3
			newFilePath = newFilePath.substring(0, newFilePath.lastIndexOf(".")) + ".mp3";
			System.out.println(newFilePath);

			// System.out.println(newFilePath);
			final String[] command = { avCommand, "-i", file.getAbsolutePath(),
					"-metadata", "artist=sadf",
					"-b", "192K", newFilePath };
//					"-q","1", newFilePath };
			pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
//						runningSongs.incrementAndGet();
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
	}

	private void threadDone() {
//		runningSongs.decrementAndGet();
		songsCompleted++;
		System.out.println("converted " + songsCompleted + "/" + totalSongs
				+ " songs");
		if (songsCompleted == totalSongs) {
			System.out.println("All songs converted!");		
		}
	}

}
