package Implementations;

import Thread.CombineFunction;

public class Multiply extends CombineFunction<Long> {
    @Override
    public Long combine(Long left, Long right) {
        return left * right;
    }
}
