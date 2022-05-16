package CSLabs.Lab3.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("unused")
@FunctionalInterface
public interface IWorker {
    void run(InputStream is, OutputStream os) throws IOException, InterruptedException;
}
