package fr.insee.aoc.intcode;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Day5Test {

	@Test
	public void part1result() {
		Machine machine = new Machine("./src/main/resources/day5");
		machine.writeInput(1);
		machine.run();
		Integer output;
		int resultat = 0;
		while ((output = machine.readOutputImmediat()) != null) {
			resultat = output;
		}
		assertEquals(resultat, 9219874);
	}

	@Test
	public void part2result() {
		Machine machine = new Machine("./src/main/resources/day5");
		machine.writeInput(5);
		machine.run();
		Integer output;
		int resultat = 0;
		while ((output = machine.readOutputImmediat()) != null) {
			resultat = output;
		}
		assertEquals(resultat, 5893654);
	}

}
