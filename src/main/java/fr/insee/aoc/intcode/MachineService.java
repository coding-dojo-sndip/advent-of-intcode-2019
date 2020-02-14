package fr.insee.aoc.intcode;

import java.util.ArrayList;
import java.util.List;

public class MachineService {

	/**
	 * (day2) Selon le programme passé en path, cherche les valeur à remplacer dans
	 * le programme en position 1 (noun) et 2 (verb) afin d'obtenir en fin
	 * d'éxécution la valeur res en position 0
	 * 
	 * @param path chemin vers le fichier du programme
	 * @param res  valeur à trouver en position 0
	 * @return 100 * noun + verb lorsque la valeur demandée est trouvée
	 */
	public static int searchOutput(String path, Integer res) {
		for (int noun = 0; noun < 100; noun++) {
			for (int verb = 0; verb < 100; verb++) {
				Machine machine = new Machine(path, noun, verb);
				machine.run();
				if (machine.getProgram().get(0).equals(res)) {
					return 100 * noun + verb;
				}
			}
		}
		throw new UnsupportedOperationException("resultat impossible");
	}

	/**
	 * (day7) Selon le programme passé en path, génère 5 machines avec les codes de
	 * phase indiqués dans "inputs"
	 * 
	 * @param path   chemin vers le fichier du programme
	 * @param inputs permutation des phases à tester
	 * @return l'output du 5e amplificateur
	 */
	public static int amplificationCircuit(List<Integer> inputs, String path) {

		// Creation des 5 machines avec leur premier input correspondant à la phase
		List<Machine> machines = new ArrayList<>();
		for (Integer input : inputs) {
			Machine machine = new Machine(path);
			machine.writeInput(input);
			machines.add(machine);
		}
		List<Thread> listThreads = new ArrayList<>();
		// Demarrage des 5 machines en parallèles (donc 5 threads)
		for (Machine machine : machines) {
			Thread thread = new Thread(machine);
			thread.start();
			listThreads.add(thread);
		}

		// On donne l'input 0 à la première machine
		// On attend ensuite que chaque machine fournisse son output que l'on donne à la
		// machine suivante
		int n = 0;
		machines.get(0).writeInput(0);
		int result = machines.get(n).readOutput();

		// Lorsqu'on arrive au début d'une nouvelle série d'amplificateur (n+1=0) on
		// vérifie si le premier amplificateur est encore vivant
		while ((n + 1) % listThreads.size() != 0 || listThreads.get(0).isAlive()) {
			machines.get((n + 1) % listThreads.size()).writeInput(result);
			n = (n + 1) % listThreads.size();
			result = machines.get(n).readOutput();
		}

		return result;
	}

	// Retourne la liste des permutations d'une liste
	// thx to stackoverflow :)
	public static <T> List<List<T>> generatePerm(List<T> original) {
		if (original.size() == 0) {
			List<List<T>> result = new ArrayList<List<T>>();
			result.add(new ArrayList<T>());
			return result;
		}
		T firstElement = original.remove(0);
		List<List<T>> returnValue = new ArrayList<List<T>>();
		List<List<T>> permutations = generatePerm(original);
		for (List<T> smallerPermutated : permutations) {
			for (int index = 0; index <= smallerPermutated.size(); index++) {
				List<T> temp = new ArrayList<T>(smallerPermutated);
				temp.add(index, firstElement);
				returnValue.add(temp);
			}
		}
		return returnValue;
	}

}
