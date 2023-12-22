package Thread;

import Implementations.Multiply;
import Implementations.SleepMultiply;
import Server.Runner.BenchmarkRunner;
import Server.Runner.CodeRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadTest {

    @BeforeEach
    public void setup() {

    }

    private static List<Integer> createIncrementingList(int max) {
        return IntStream.rangeClosed(1, max)
                .boxed().collect(Collectors.toList());
    }

    private static int sequentialAll(List<Integer> list) {
        return list.stream().reduce(0, Integer::sum);
    }

    private static long sequentialSleepMultiply(List<Long> list) {
        long ret = 1;
        for (int i = 0; i < list.size(); i += 10) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            for (int j = 0; j < 10 && i + j < list.size(); j++) {
                ret *= list.get(i + j);
            }
        }
        return ret;
    }


    @Test
    public void generalTest() {
        CodeRunner<Integer, Integer> codeRunner = new CodeRunner<Integer, Integer>(SimpleThreadProgram.class, SimpleCombine.class);
        assertEquals(codeRunner.runProgram(Arrays.asList(1, 2, 3, 4), 0), 10);
        assertEquals(codeRunner.runProgram(createIncrementingList(100), 0), sequentialAll(createIncrementingList(100)));
        assertEquals(codeRunner.runProgram(createIncrementingList(1273), 0), sequentialAll(createIncrementingList(1273)));
    }

    @Test
    public void testBenchmark() {
        BenchmarkRunner<Integer, Integer> codeRunner = new BenchmarkRunner<Integer, Integer>(SimpleThreadProgram.class, SimpleCombine.class);
        assertEquals(codeRunner.runProgram(Arrays.asList(1, 2, 3, 4), 0), SimpleThreadProgram.getNumThreads(Arrays.asList(1, 2, 3, 4)));
        assertEquals(codeRunner.runProgram(createIncrementingList(100), 0), SimpleThreadProgram.getNumThreads(createIncrementingList(100)));
        assertEquals(codeRunner.runProgram(createIncrementingList(1273), 0), SimpleThreadProgram.getNumThreads(createIncrementingList(1273)));
    }

    public static void main(String[] args) {
//        CodeRunner<Integer, Integer> codeRunner = new CodeRunner<Integer, Integer>(SimpleThreadProgram.class, SimpleCombine.class);
        CodeRunner codeRunner = new CodeRunner(SleepMultiply.class, Multiply.class);
        List<?> list = createIncrementingList(11).stream().map((integer -> (long) integer)).collect(Collectors.toList());
        Object defaultValue = (Object) 1L;
        long startTime = System.nanoTime();
        long valPar = Long.class.cast(codeRunner.runProgram(list, defaultValue));
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Parallel: " + totalTime);

        startTime = System.nanoTime();
        long valSeq = sequentialSleepMultiply(list.stream().map((integer -> (long) integer)).collect(Collectors.toList()));
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Sequential: " + totalTime);

        System.out.println("Sequential: " + valSeq + " Parallel: " + valPar);
    }
}
