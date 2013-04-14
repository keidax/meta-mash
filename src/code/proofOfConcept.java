package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

public class proofOfConcept {

	public static void main(String[] args){
		File file = new File("test_in/song_01.yml");
		String s = file.getAbsolutePath();
		FileReader reader;
		try {
			reader = new FileReader(s);
			Yaml yammy = new Yaml();
			Object data = yammy.load(reader);
			method(data);
			System.out.println(data);
			System.out.println(data.getClass().toString());
			System.out.println(yammy.dump(data));
		} catch (FileNotFoundException e) {}
	}
	
	public static String method(Object data){
		
		Set<String> sSet = ((LinkedHashMap) data).keySet();
		System.out.println(sSet);
		String sArtist = "artist: "+ ((LinkedHashMap) data).get("artist")+ "\n";
		String sAlbum = "album: "+ ((LinkedHashMap) data).get("album")+ "\n";
		String sTitle = "title: "+ ((LinkedHashMap) data).get("title");
		System.out.println(sArtist + sAlbum + sTitle);
		return sArtist + sAlbum + sTitle;
	}
}
