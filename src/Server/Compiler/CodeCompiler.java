package Server.Compiler;

import Thread.DistributedRunnable;
import Thread.CombineFunction;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.io.*;
import java.util.stream.Collectors;

public class CodeCompiler {
    public static Class<?> compile(String classPath, String code) {
        try {
            return InMemoryJavaCompiler.newInstance().compile( getClassName(code), code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(File file) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsoluteFile())));
            String separator = System.getProperty("line.separator");
            return br.lines().collect(Collectors.joining(separator));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getClassName(String file) {

        return file.split("package ")[1].split(";")[0] + "." + file.split("public class ")[1].split(" ")[0];
    }
}
