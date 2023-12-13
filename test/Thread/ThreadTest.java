package Thread;

import Client.Client;
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


    @Test
    public void generalTest() {
        Client<Integer, Integer> client = new Client<Integer, Integer>(SimpleThreadProgram.class, SimpleCombine.class);
        assertEquals(client.runProgram(Arrays.asList(1, 2, 3, 4), 0), 10);
        assertEquals(client.runProgram(createIncrementingList(100), 0), sequentialAll(createIncrementingList(100)));
        assertEquals(client.runProgram(createIncrementingList(1273), 0), sequentialAll(createIncrementingList(1273)));
    }

    public static void main(String[] args) {
        Client<Integer, Integer> client = new Client<Integer, Integer>(SimpleThreadProgram.class, SimpleCombine.class);
        List<Integer> list = createIncrementingList(12734666);
        long startTime = System.nanoTime();
        client.runProgram(list, 0);
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Parallel: " + totalTime);

        startTime = System.nanoTime();
        sequentialAll(list);
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Sequential: " + totalTime);
    }
}
