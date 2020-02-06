package fr.insee.aoc.intcode;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class Day7Test {

	@Test
	public void amplificationCircuitTest() {

		List<Integer> inputs = IntStream.range(0, 5).mapToObj(a -> (Integer) a).collect(Collectors.toList());

		MachineService.amplificationCircuit(inputs, "./src/main/resources/day7");
	}

}
