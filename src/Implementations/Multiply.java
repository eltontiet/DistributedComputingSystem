package Implementations;

import Thread.CombineFunction;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
