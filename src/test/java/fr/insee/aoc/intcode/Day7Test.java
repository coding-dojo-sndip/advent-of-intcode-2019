package fr.insee.aoc.intcode;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class Day7Test {

	@Test
	public void amplificationCircuitTest() {

		// boxed() = mapToObj(a -> (Integer) a)
		List<Integer> inputs = IntStream.range(0, 5).boxed().collect(Collectors.toList());
		List<List<Integer>> permutations = MachineService.generatePerm(inputs);

		assertEquals((Integer) 20413,
				permutations.stream()
						.map(perm -> MachineService.amplificationCircuit(perm, "./src/main/resources/day7"))
						.max(Comparator.naturalOrder()).orElseThrow(UnsupportedOperationException::new));
	}
	
	@Test
	public void amplificationCircuitTest2() {

		// boxed() = mapToObj(a -> (Integer) a)
		List<Integer> inputs = IntStream.range(5, 10).boxed().collect(Collectors.toList());
		List<List<Integer>> permutations = MachineService.generatePerm(inputs);

		assertEquals((Integer) 3321777,
				permutations.stream()
						.map(perm -> MachineService.amplificationCircuit(perm, "./src/main/resources/day7"))
						.max(Comparator.naturalOrder()).orElseThrow(UnsupportedOperationException::new));
	}

}
