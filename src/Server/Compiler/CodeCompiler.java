package Server.Compiler;

import Thread.DistributedRunnable;
import Thread.CombineFunction;
import org.mdkt.compiler.InMemoryJavaCompiler;

public class CodeCompiler {
    public static Class<?> compile(String className, String code) {
        try {
            return InMemoryJavaCompiler.newInstance().compile(className, code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
