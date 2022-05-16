package CSLabs.Lab3.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Set;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class TCPClient {
    // Constants members:

    private static final int WAITTIME = 50;

    // Data members:

    private final IWorker worker;
    private final Socket clientSocket;
    private InetSocketAddress socketAddress;

    // Constructors:

    private TCPClient(IWorker clientWorker) throws IOException {
        worker = clientWorker;
        clientSocket = new Socket();

        clientSocket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
    }

    public TCPClient(IWorker clientWorker, InetAddress address, int port) throws IOException {
        this(clientWorker);
        socketAddress = new InetSocketAddress(address, port);
    }

    public TCPClient(IWorker clientWorker, String hostname, int port) throws IOException {
        this(clientWorker);
        socketAddress = new InetSocketAddress(hostname, port);
    }

    // Main methods:

    public void start() throws IOException {
        try {
            clientSocket.connect(socketAddress);

            InputStream socketIS = clientSocket.getInputStream();
            OutputStream socketOS = clientSocket.getOutputStream();

            worker.run(socketIS, socketOS);
        }
        catch (InterruptedException ignored) {}
        finally {
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
            clientSocket.close();
        }
    }

    // Getters:

    public IWorker getWorker() { return worker; }
    public boolean isConnect() { return clientSocket.isConnected() && !clientSocket.isClosed();}

    public InetAddress getInetAddress() { return clientSocket.getInetAddress(); }
    public String getHostName() { return clientSocket.getInetAddress().getHostName(); }
    public int getPort() { return clientSocket.getLocalPort(); }

    public int getReceiveBufferSize() throws SocketException { return clientSocket.getReceiveBufferSize(); }
    public boolean isAddressReuse() throws SocketException { return clientSocket.getReuseAddress(); }
    public <T> T getOption(SocketOption<T> name) throws IOException { return clientSocket.getOption(name); }
    public Set<SocketOption<?>> supportedOptions() { return clientSocket.supportedOptions(); }

    // Setters:

    public void setReceiveBufferSize(int bufferSize) throws SocketException {
        clientSocket.setReceiveBufferSize(bufferSize);
    }
    public void setReuseAddress(boolean isReuseAddress) throws SocketException {
        clientSocket.setReuseAddress(isReuseAddress);
    }
    public void setPerformance(int connectionTime, int latency, int bandwidth) {
        clientSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
    }
    public <T> void setOption(SocketOption<T> name, T value) throws IOException {
        clientSocket.setOption(name, value);
    }
}
