package Server.Runner;

import Thread.DistributedRunnable;
import Thread.CombineFunction;

import java.util.List;

public class Functions {
    private Class<? extends DistributedRunnable> runnable;
    private Class<? extends CombineFunction> combine;
    private List<?> data;
    private Object defaultValue;

    public boolean canRun() {
        return runnable != null && combine != null && data != null && defaultValue != null;
    }

    public Class<? extends DistributedRunnable> getRunnable() {
        return runnable;
    }
    public void setRunnable(Class<? extends DistributedRunnable> runnable) {
        this.runnable = runnable;
    }
    public Class<? extends CombineFunction> getCombine() {
        return combine;
    }
    public void setCombine(Class<? extends CombineFunction> combine) {
        this.combine = combine;
    }
    public List<?> getData() {
        return data;
    }
    public void setData(List<?> data) {
        this.data = data;
    }
    public Object getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
