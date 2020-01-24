package fr.insee.aoc.intcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import fr.insee.aoc.utils.Utils;

public class Machine {

	private List<Integer> program;
	private BlockingQueue<Integer> input = new LinkedBlockingQueue<Integer>();
	private BlockingQueue<Integer> output = new LinkedBlockingQueue<Integer>();
	private static final int NB_MAX_PARAMS = 3;

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

	public void exec() {
		int pointer = 0;
		execution: while (true) {
			switch (program.get(pointer)) {
			case 1:
				program.set(program.get(pointer + 3),
						program.get(program.get(pointer + 1)) + program.get(program.get(pointer + 2)));
				pointer += 4;
				break;
			case 2:
				program.set(program.get(pointer + 3),
						program.get(program.get(pointer + 1)) * program.get(program.get(pointer + 2)));
				pointer += 4;
				break;
			case 3:
				int myInput = readInput();
				program.set(program.get(pointer + 1), myInput);
				pointer += 2;
				break;
			case 4:
				writeOutput(program.get(program.get(pointer + 1)));
				pointer += 2;
				break;
			case 99:
				break execution;
			default:
				throw new UnsupportedOperationException(
						"Ca a buggé ici : " + pointer + " valeur:" + program.get(pointer));
			}
		}
	}

	// Seule la machine peut lire ses inputs
	private int readInput() {
		return this.input.poll();
	}

	// Seule la machine peut écrire ses outputs
	private boolean writeOutput(int output) {
		return this.output.offer(output);
	}

	// L'écriture des inputs est externe à la machine
	public boolean writeInput(int input) {
		return this.input.offer(input);
	}

	// L'output doit être lisible hors de la classe
	public int readOutput() {
		return this.output.poll();
	}

	public List<Integer> getProgram() {
		return program;
	}

	/**
	 * Détermination des modes de lecture des paramètres
	 * 
	 * 1002 -> Multiplication du premier paramètre relatif et du second littéral
	 * 
	 * 1 -> Addition des deux paramètres en relatif (0001)
	 * 
	 * @param opcode complet (mode + code opération)
	 * @return une liste de mode, dans l'ordre des paramètres
	 */
	private List<Mode> manageLiteral(int opcode) {
		List<Mode> list = new ArrayList<>();
		int resultante = opcode / 100;
		while (resultante != 0) {
			if (resultante % 10 == 1) {
				list.add(Mode.LITTERAL);
			} else {
				list.add(Mode.RELATIF);
			}
			resultante /= 10;
		}
		while (list.size() < NB_MAX_PARAMS) {
			list.add(Mode.RELATIF);
		}
		return list;
	}

	// Lecture du paramètre en mode littéral (valeur brute) ou relative (on prend la
	// valeur de la case mémoire associée)
	private enum Mode {
		LITTERAL, RELATIF
	}

}
