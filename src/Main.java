import Implementations.Multiply;
import Implementations.SleepMultiply;
import Server.Server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        Server.getInstance();
        System.out.println(Server.getInstance().getAddress().getHostAddress());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Connect to a server? (y/n)");
            if (scanner.nextLine().equals("y")) {
                System.out.println("What is the address of the server?");
                Server.getInstance().connectToServer(InetAddress.getByName(scanner.nextLine()));
            }

            System.out.println("Click any button to run the sleep: ");
            scanner.nextLine();
            System.out.println("Running...");
            List<Integer> list = createIncrementingList(11);
            System.out.println(Server.getInstance().run(SleepMultiply.class, Multiply.class, list.stream().map(i -> (long) i).collect(Collectors.toList()), 1L));
        }

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
}