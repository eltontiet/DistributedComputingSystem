package Implementations;

import Thread.CombineFunction;

import java.io.File;

public class Multiply extends CombineFunction<Long> {
    @Override
    public Long combine(Long left, Long right) {
        return left * right;
    }

    @Override
    public File getFile() {
        return new File("src/Implementations/Multiply.java");
    }
}
