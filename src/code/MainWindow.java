package code;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	final Updater updater;

	AtomicInteger runningSongs = new AtomicInteger();
	AtomicInteger totalSongs = new AtomicInteger();
	AtomicInteger songsCompleted = new AtomicInteger();

	JProgressBar progress;
	
	String[] fileFormats = { "mp3", "ogg", "m4a" };

	public MainWindow() {
		super();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		final JTextField field1 = new JTextField();
		final JTextField field2 = new JTextField();
		JButton butt1 = new JButton("Browse");
		JButton butt2 = new JButton("Browse");
		JButton butt3 = new JButton("Convert your Files Now!");
		JPanel topPanel = new JPanel();
		JPanel middlePanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JPanel optionst = new JPanel();
		JPanel optionsb = new JPanel();
		JPanel optionstsub = new JPanel();
		JPanel optionsbsub = new JPanel();
		
		JRadioButton vbrrad = new JRadioButton();
		JRadioButton cbrrad = new JRadioButton();
		vbrrad.setBorder(null);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(vbrrad);
		bg.add(cbrrad);
		
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.getContentPane().add(topPanel);
		this.getContentPane().add(middlePanel);
		this.getContentPane().add(bottomPanel);
		this.getContentPane().add(optionst);
		this.getContentPane().add(optionstsub);
		this.getContentPane().add(optionsb);
		this.getContentPane().add(optionsbsub);
		JLabel vbrlabel = new JLabel("VBR - Higher quality, slower time");
		JLabel sbrlabel = new JLabel("SBR - Lower quality, more specific control");
		JSlider vbrslider = new JSlider(0,31);
		JSlider sbrslider = new JSlider(0,100);
		sbrslider.setPaintTicks(true);
		sbrslider.setMajorTickSpacing(25);
		sbrslider.setSnapToTicks(true);
		sbrslider.getSnapToTicks();
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		field1.setColumns(25);
		topPanel.add(field1);
		topPanel.add(butt1);
		field2.setColumns(25);
		middlePanel.add(field2);
		middlePanel.add(butt2);
		bottomPanel.add(butt3);
		
		optionst.setLayout(new FlowLayout(FlowLayout.CENTER));
		optionsb.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		optionst.add(vbrrad);
		optionst.add(vbrlabel);
		optionstsub.add(vbrslider);
		
		optionsbsub.add(sbrslider);
		optionsb.add(cbrrad);
		optionsb.add(sbrlabel);
		
		vbrslider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		sbrslider.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		progress = new JProgressBar(0, 100);
		this.add(progress);
		
		updater = new Updater(progress);
		
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
		FileConverter conv = new FileConverter(files, baseOutputDirectory, updater);

	}

}
