package fr.insee.aoc.intcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.insee.aoc.utils.Utils;

public class Machine {

	List<Integer> program;

	/**
	 * Crée une machine à intcode selon le programme lu dans path
	 */
	public Machine(String path) {
		program = Arrays.asList(Utils.readLine(path).split(",")).stream().map(s -> Integer.valueOf(s))
				.collect(Collectors.toList());
	}

	/**
	 * Crée une machine à intcode selon le programme lu dans path. Modifie les
	 * position 1 et 2 du programme par respectivement noun et verb
	 * 
	 * @param noun: int devant remplacer la position 1 du programme
	 * @param verb: int devant remplacer la position 2 du programme
	 */
	public Machine(String path, Integer noun, Integer verb) {
		program = Arrays.asList(Utils.readLine(path).split(",")).stream().map(s -> Integer.valueOf(s))
				.collect(Collectors.toList());
		program.set(1, noun);
		program.set(2, verb);
	}

	public int exec() {
		for (int i = 0; i < program.size(); i = i + 4) {
			switch (program.get(i)) {
			case 1:
				program.set(program.get(i + 3), program.get(program.get(i + 1)) + program.get(program.get(i + 2)));
				break;
			case 2:
				program.set(program.get(i + 3), program.get(program.get(i + 1)) * program.get(program.get(i + 2)));
				break;
			case 99:
				return program.get(0);
			default:
				throw new UnsupportedOperationException("Ca a buggé ici : " + i + " valeur:" + program.get(i));
			}
		}
		throw new UnsupportedOperationException("Le programme ne s'est pas arreté correctement");
	}

}
