package pogofish.jadt.emitter;

import java.io.File;
import java.io.IOException;

public class FileTargetFactory implements TargetFactory {
    private final String destDirName;

    public FileTargetFactory(String destDirName) {
        this.destDirName = destDirName;
    }

    @Override
    public Target createTarget(String className) throws IOException {
        return new FileTarget(convertToPath(className));
    }

    public String convertToPath(String className) {
        final String fixedDir = destDirName.endsWith(File.separator) ? destDirName: destDirName + File.separator;
        final String fixedClassName = className.replace('.', '/');
        return fixedDir + fixedClassName + ".java";
    }      
}
