package Thread;

import java.util.List;

public class SimpleThreadProgram extends DistributedRunnable<Integer, Integer> {

    private static int sequentialAll(List<Integer> list) {
        return list.stream().reduce(0, Integer::sum);
    }

    @Override
    public Integer func(List<Integer> data) {
        int lastI = data.size();
        for (int i = data.size()/2; i > 2048; i/=2) {
            List<Integer> splitList = data.subList(i, lastI);
            createThread(splitList);
            lastI = i;
        }
        return sequentialAll(data.subList(0, lastI));
    }
}
