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

		// Demarrage des 5 machines en parallèles (donc 5 threads)
		for (Machine machine : machines) {
			Thread thread = new Thread(machine);
			thread.start();
		}

		// On donne l'input 0 à la première machine
		// On attend ensuite que chaque machine fournisse son output que l'on donne à la
		// machine suivante
		machines.get(0).writeInput(0);
		machines.get(1).writeInput(machines.get(0).readOutput());
		machines.get(2).writeInput(machines.get(1).readOutput());
		machines.get(3).writeInput(machines.get(2).readOutput());
		machines.get(4).writeInput(machines.get(3).readOutput());
		return machines.get(4).readOutput();
	}
}
