package fr.insee.aoc.intcode;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Day2Test {

	@Test
	public void part1test0() {
		Machine machine = new Machine("./src/test/resources/day2-part1");
		machine.run();
		assertEquals((int)machine.getProgram().get(0), 3500);
	}

	@Test
	public void part1result() {
		Machine machine = new Machine("./src/main/resources/day2", 12, 2);
		machine.run();
		assertEquals((int)machine.getProgram().get(0), 3895705);

	}

	@Test
	/*
	 * Pas d'exemple dans le descriptif, mais on sait via part1 que 12 et 2 donnent
	 * un output 3895705, donc searchOutput de 3895705 devrait donner 1202
	 */
	public void part2test0() {
		assertEquals(MachineService.searchOutput("./src/main/resources/day2", 3895705), 1202);
	}

	@Test
	public void part2result() {
		assertEquals(MachineService.searchOutput("./src/main/resources/day2", 19690720), 6417);
	}

}
