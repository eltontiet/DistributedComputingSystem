package Thread;

public class SimpleCombine extends CombineFunction<Integer> {

    @Override
    public Integer combine(Integer left, Integer right) {
        return left + right;
    }
}
