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
//		System.out.println(s);
		FileReader reader;
		try {
			reader = new FileReader(file);
			Yaml yammy = new Yaml();
		//	yammy.load(reader);
			System.out.println(yammy.toString());
			System.out.println(yammy.dump(yammy.load(reader)));
		} catch (FileNotFoundException e) {}
	}
}
