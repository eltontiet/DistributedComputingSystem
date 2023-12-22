package Implementations;

import Thread.DistributedRunnable;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SleepMultiply extends DistributedRunnable<Long, Long> {



    @Override
    public Long func(List<Long> data) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {

        }

        if (data.size() < 10) {
            long res = 1;
            for (Long i : data) {
                res *= i;
            }
            return res;
        } else {
            createThread(new ArrayList<>(data.subList(data.size()/2, data.size())));
            return func(data.subList(0, data.size()/2));
        }
    }

    @Override
    public File getFile() {
        return new File("src/Implementations/SleepMultiply.java");
    }
}


