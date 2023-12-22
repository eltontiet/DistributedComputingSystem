package Thread;

import java.io.File;
import java.nio.file.Path;

public abstract class CombineFunction<Output> {

    protected Path filePath;

    public abstract Output combine(Output left, Output right);

    public abstract File getFile();
}
