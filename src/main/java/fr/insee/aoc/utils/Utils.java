package fr.insee.aoc.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Utils {

	private Utils() {
		//
	}

	public static Stream<String> streamOfLines(String input) {
		try {
			return Files.lines(Paths.get(input));
		} catch (IOException e) {
			//
		}
		throw new UnsupportedOperationException("Erreur dans la lecture du fichier");
	}

	public static String readLine(String input) {
		return streamOfLines(input).findFirst().orElse("");
	}

}
