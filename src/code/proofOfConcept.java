package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
			System.out.println(data);
			System.out.println(data.getClass().toString());
			System.out.println(yammy.dump(data));
		} catch (FileNotFoundException e) {}
	}
}
