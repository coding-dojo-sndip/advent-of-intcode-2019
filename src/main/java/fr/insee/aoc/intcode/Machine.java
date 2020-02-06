package fr.insee.aoc.intcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import fr.insee.aoc.utils.Utils;

public class Machine implements Runnable {

	private List<Integer> program;
	private BlockingQueue<Integer> input = new LinkedBlockingQueue<Integer>();
	private BlockingQueue<Integer> output = new LinkedBlockingQueue<Integer>();
	private static final int NB_MAX_PARAMS = 3;
	private static final int NB_MAX_DEPASSEMENT_LISTE = 3;

	/**
	 * Crée une machine à intcode selon le programme lu dans path
	 */
	public Machine(String path) {
		program = Arrays.asList(Utils.readLine(path).split(",")).stream().map(s -> Integer.valueOf(s))
				.collect(Collectors.toList());
		for (int i = 0; i < NB_MAX_DEPASSEMENT_LISTE; i++) {
			program.add(0);
		}
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

	@Override
	public void run() {
		int pointer = 0;
		execution: while (true) {
			List<Integer> parametres = obtenirParametres(pointer);
			switch (program.get(pointer) % 100) {
			case 1:
				program.set(program.get(pointer + 3), parametres.get(0) + parametres.get(1));
				pointer += 4;
				break;
			case 2:
				program.set(program.get(pointer + 3), parametres.get(0) * parametres.get(1));
				pointer += 4;
				break;
			case 3:
				int myInput = readInput();
				program.set(program.get(pointer + 1), myInput);
				pointer += 2;
				break;
			case 4:
				System.out.println("on a output " + parametres.get(0));
				writeOutput(parametres.get(0));
				pointer += 2;
				break;
			case 5:
				pointer = (parametres.get(0) != 0) ? parametres.get(1) : pointer + 3;
				break;
			case 6:
				pointer = (parametres.get(0) == 0) ? parametres.get(1) : pointer + 3;
				break;
			case 7:
				if (parametres.get(0) < parametres.get(1)) {
					program.set(program.get(pointer + 3), 1);
				} else {
					program.set(program.get(pointer + 3), 0);
				}
				pointer += 4;
				break;
			case 8:
				if (parametres.get(0).equals(parametres.get(1))) {
					program.set(program.get(pointer + 3), 1);
				} else {
					program.set(program.get(pointer + 3), 0);
				}
				pointer += 4;
				break;
			case 99:
				break execution;
			default:
				throw new UnsupportedOperationException(
						"Ca a buggé ici : " + pointer + " valeur:" + program.get(pointer));
			}
		}
	}

	// Fournit systématiquement les potentiels 3 paramètres suivant selon le mode de
	// lecture
	private List<Integer> obtenirParametres(int pointer) {
		int tailleMax = program.size();
		List<Mode> listModes = manageLiteral(program.get(pointer));
		List<Integer> parametres = new ArrayList<>();
		for (int i = 0; i < NB_MAX_PARAMS; i++) {
			int parametre;
			if (listModes.get(i) == Mode.IMMEDIATE) {
				parametre = program.get(pointer + i + 1);
			} else {
				if (program.get(pointer + i + 1) >= tailleMax) {
					parametre = 0;
				} else {
					parametre = program.get(program.get(pointer + i + 1));
				}
			}
			parametres.add(parametre);
		}
		return parametres;
	}

	// Seule la machine peut lire ses inputs
	private int readInput() {
		try {
			return this.input.poll(3600, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UnsupportedOperationException();
		}
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
	public Integer readOutput() {
		try {
			return this.output.poll(3600, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UnsupportedOperationException();
		}
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
				list.add(Mode.IMMEDIATE);
			} else {
				list.add(Mode.POSITION);
			}
			resultante /= 10;
		}
		while (list.size() < NB_MAX_PARAMS) {
			list.add(Mode.POSITION);
		}
		return list;
	}

	// Lecture du paramètre en mode littéral (valeur brute) ou relative (on prend la
	// valeur de la case mémoire associée)
	private enum Mode {
		IMMEDIATE, POSITION
	}

}
