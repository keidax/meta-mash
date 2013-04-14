package code;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;

public class FileConverter {
	public FileConverter(List<File> files, File baseOutputDirectory, final Updater updater, boolean isCBR, int qualityOrBitRate){
		updater.setTotalSongs(files.size());
		int cores = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(cores);
		

		String avCommand = "avconv";

		for (File file : files) {

			String fileBase = file.getPath();
			fileBase = fileBase.substring(0, fileBase.lastIndexOf("."));

			String tempNewFilePath = baseOutputDirectory.getAbsolutePath() + "/";

			final File artworkFile;
			final HashMap<String, String> yamlData;
			final boolean containsArtwork;
			final boolean hasYaml;

			String yamlFilePath = fileBase + ".yml";
			File yamlFile = new File(yamlFilePath);
			if (yamlFile.exists()) {
				hasYaml = true;
				yamlData  = YamlUtilities.getMatchingYamlData(yamlFile);
				tempNewFilePath += yamlData.get("artist") + "/" + yamlData.get("album") + "/";
				if (yamlData.containsKey("artwork"))
				{

					String artworkPath = yamlData.get("artwork");
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
				hasYaml = false;
				yamlData = null;
			}
			File albumOutputDirectory = new File(tempNewFilePath);
			albumOutputDirectory.mkdirs();

			tempNewFilePath += file.getName();
			// make sure output file is mp3
			final String finalNewFilePath = tempNewFilePath.substring(0, tempNewFilePath.lastIndexOf(".")) + ".mp3";
			final String[] command = { avCommand, "-y","-i", file.getAbsolutePath(), 
					isCBR ? "-b" : "-q", qualityOrBitRate+"", finalNewFilePath };
			pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Process process = Runtime.getRuntime().exec(command);
//						IOUtils.copy(process.getInputStream(), System.out);
//						IOUtils.copy(process.getErrorStream(), System.out);
						process.waitFor();
						
						File finishedFile = new File(finalNewFilePath);
						if (finishedFile.exists()) {
							if (hasYaml) {
								AudioTaggerUtilities.setFileArtworkAndTags(finishedFile, artworkFile, containsArtwork, yamlData);
							}
						} else {
							System.err.println("File "+finalNewFilePath+" not created! Oh no!");
						}
						updater.threadDone();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
