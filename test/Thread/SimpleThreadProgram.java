package Thread;

import java.io.File;
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

    @Override
    public File getFile() {
        return null;
    }

    public static int getNumThreads(List<Integer> data) {
        int num = 0;

        for (int i = data.size()/2; i > 2048; i/=2) {
            num++;
        }

        return num;
    }
}
