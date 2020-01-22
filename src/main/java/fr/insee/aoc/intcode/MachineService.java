package fr.insee.aoc.intcode;

public class MachineService {
	
	
	public static int searchOutput(String path, Integer res) {
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				Machine machine = new Machine(path,i,j);
				if (machine.exec()==res) {
					return 100*i+j;
				}
			}
		}
		throw new UnsupportedOperationException("resultat impossible");
	}
}
