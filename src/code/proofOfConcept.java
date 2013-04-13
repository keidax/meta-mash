package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

public class proofOfConcept {

	public static void main(String[] args){
		File file = new File("test.txt");
		String s = file.getAbsolutePath();
		FileReader reader;
		try {
			reader = new FileReader(file);
			Yaml yammy = new Yaml();
			Object data = yammy.load(reader);
			System.out.println(data);
			System.out.println(data.getClass().toString());
			System.out.println(yammy.dump(data));
		} catch (FileNotFoundException e) {}
	}
}
