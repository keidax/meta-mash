package code;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class MainWindow extends JFrame {

	AtomicInteger runningSongs = new AtomicInteger();
	AtomicInteger totalSongs = new AtomicInteger();
	AtomicInteger songsCompleted = new AtomicInteger();

	String[] fileFormats = { "mp3", "ogg", "m4a" };

	public MainWindow() {
		super();
		final JTextField field1 = new JTextField();
		final JTextField field2 = new JTextField();
		JButton butt1 = new JButton("Browse");
		JButton butt2 = new JButton("Browse");
		JButton butt3 = new JButton("Convert your Files Now!");

		this.getContentPane().setLayout(new GridLayout(2, 2));
		this.getContentPane().add(butt1);
		this.getContentPane().add(butt2);
		this.getContentPane().add(butt3);
		field1.setColumns(17);
		field2.setColumns(17);
		this.getContentPane().add(field1);
		this.getContentPane().add(field2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		System.out.println(System.getProperty("os.name"));
		

		butt1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Insert JFileChooser code
				JFileChooser fileChooser = new JFileChooser(new File("/home/gabriel/code/music-converter"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				System.out.println("choose music directory");
				int returnVal = fileChooser.showOpenDialog(MainWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File musicDirectory = fileChooser.getSelectedFile();
					field1.setText(musicDirectory.getAbsolutePath().toString());
					List<File> inputFiles = getMusicFilesFromDirectory(musicDirectory);
					System.out.println(inputFiles.size() + " music files found.");
				}
				//Insert JFileChooser code
			}
			
		});
		
		butt2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Insert JFileChoose code
				File outputDirectory = getOutputDirectory();
				field2.setText(outputDirectory.toString());
			}
			
		});
		
		butt3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Convert files
				if (! field1.getText().equals("") && ! field2.getText().equals("")) {	
					convertFiles(getMusicFilesFromDirectory(toFile(field1)), toFile(field2));
				}
				
			}
			
		});
		
		this.pack();
	}

	public static File toFile(JTextField field){
		File file = new File(field.getText());
		return file;
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
		totalSongs.set( files.size());

		// workaround for mac
		String avCommand = System.getProperty("os.name").equalsIgnoreCase(
				"linux") ? "avconv" : "avconvert";

		for (File file : files) {

			String fileBase = file.getPath();
			fileBase = fileBase.substring(0, fileBase.lastIndexOf("."));

			String tempNewFilePath = baseOutputDirectory.getAbsolutePath()
					+ "/";

			final File artworkFile;
			final boolean containsArtwork;

			String yamlFilePath = fileBase + ".yml";
			File yamlFile = new File(yamlFilePath);
			if (yamlFile.exists()) {
				Object data = YamlUtilities.getMatchingYamlData(yamlFile);
				HashMap<String, String> realData = (LinkedHashMap<String, String>) data;
				/*for (Entry<String, String> entry : realData.entrySet()) {
					System.out.println(entry.getKey() + " : "
							+ entry.getValue());
				}*/
				tempNewFilePath += realData.get("artist") + "/"
						+ realData.get("album") + "/";
				if (realData.containsKey("artwork"))
				{

					String artworkPath = realData.get("artwork");
					if (artworkPath != null && !artworkPath.equals("")) {
						containsArtwork = true;
						artworkFile = new File(file.getParentFile() +"/"+ artworkPath);
					} else {
						containsArtwork = false;
						artworkFile = null;
					}

				} else {
					containsArtwork = false;
					artworkFile = null;
				}

			} else { // no yaml file
				// TODO try to guess artist/album from filename
				tempNewFilePath += "Unknown Artist/Unknow Album/";
				containsArtwork = false;
				artworkFile = null;
			}
			File albumOutputDirectory = new File(tempNewFilePath);
			albumOutputDirectory.mkdirs();

			tempNewFilePath += file.getName();
			// make sure output file is mp3
			final String finalNewFilePath = tempNewFilePath.substring(0,
					tempNewFilePath.lastIndexOf(".")) + ".mp3";
//			System.out.println(finalNewFilePath);

			// System.out.println(newFilePath);
			final String[] command = { avCommand, "-i", file.getAbsolutePath(),
					"-b", "192K", finalNewFilePath };
			// "-q","1", newFilePath };
			pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// runningSongs.incrementAndGet();
						Process process = Runtime.getRuntime().exec(command);
						process.waitFor();
						File finishedFile = new File(finalNewFilePath);
						if (finishedFile.exists()) {
							if (containsArtwork) {
								AudioTaggerUtilities.setFileArtwork(finishedFile, artworkFile);
							}
						} else {
							System.err.println("File not created! Oh no!");
						}
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
		// runningSongs.decrementAndGet();
		songsCompleted.incrementAndGet();
		System.out.println("converted " + songsCompleted.get() + "/" + totalSongs.get()
				+ " songs");
		if (songsCompleted.get() == totalSongs.get()) {
			System.out.println("All songs converted!");
		}
	}

}
