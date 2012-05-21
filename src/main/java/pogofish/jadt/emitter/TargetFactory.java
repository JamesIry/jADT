package pogofish.jadt.emitter;

import java.io.IOException;

public interface TargetFactory {
    public Target createTarget(String className) throws IOException;
}
