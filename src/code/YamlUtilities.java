package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

public class YamlUtilities {
	static final Yaml yaml = new Yaml();

	public static HashMap<String, String> getMatchingYamlData(File f) {
		FileReader reader;
		Object data = null;
		try {
			reader = new FileReader(f);
			data = yaml.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return (HashMap<String, String>) data;
	}
}
