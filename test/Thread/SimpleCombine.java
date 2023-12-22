package Thread;

import java.io.File;

public class SimpleCombine extends CombineFunction<Integer> {

    @Override
    public Integer combine(Integer left, Integer right) {
        return left + right;
    }

    @Override
    public File getFile() {
        return null;
    }
}
