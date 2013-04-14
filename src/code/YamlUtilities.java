package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

public class YamlUtilities {

	public static void main(String[] args) {
		File file = new File("test_in/song_01.yml");
		String s = file.getAbsolutePath();
		FileReader reader;
		try {
			reader = new FileReader(s);
			Yaml yammy = new Yaml();
			Object data = yammy.load(reader);
			getData(data);
			System.out.println(data);
			System.out.println(data.getClass().toString());
			System.out.println(yammy.dump(data));
		} catch (FileNotFoundException e) {
		}
	}

	public static Object getMatchingYamlData(File f) {
		FileReader reader;
		Object data = null;
		try {
			reader = new FileReader(f);
			Yaml yaml = new Yaml();
			data = yaml.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static String getData(Object data) {
		String sArtist = getArtist(data);
		String sAlbum = getAlbum(data);
		String sTitle = getTitle(data);
		System.out.println(sArtist + sAlbum + sTitle);
		return sArtist + sAlbum + sTitle;
	}

	public static String getArtist(Object data) {
		return "artist: " + ((LinkedHashMap) data).get("artist");
	}

	public static String getAlbum(Object data) {
		return "album: " + ((LinkedHashMap) data).get("album");
	}

	public static String getTitle(Object data) {
		return "title: " + ((LinkedHashMap) data).get("title");
	}
}
