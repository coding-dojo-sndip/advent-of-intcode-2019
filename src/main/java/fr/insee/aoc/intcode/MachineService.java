package fr.insee.aoc.intcode;

public class MachineService {

	/**
	 * 
	 * Selon le programme passé en path, cherche les valeur à remplacer dans le
	 * programme en position 1 (noun) et 2 (verb) afin d'obtenir en fin d'éxécution
	 * la valeur res en position 0
	 * 
	 * @param path: chemin vers le fichier du programme
	 * @param res:  valeur à trouver en position 0
	 * @return 100 * noun + verb lorsque la valeur demandée est trouvée
	 */
	public static int searchOutput(String path, Integer res) {
		for (int noun = 0; noun < 100; noun++) {
			for (int verb = 0; verb < 100; verb++) {
				Machine machine = new Machine(path, noun, verb);
				machine.exec();
				if (machine.getProgram().get(0).equals(res)) {
					return 100 * noun + verb;
				}
			}
		}
		throw new UnsupportedOperationException("resultat impossible");
	}
}
