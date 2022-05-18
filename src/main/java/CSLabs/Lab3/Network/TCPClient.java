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

    private final Socket clientSocket;

    // Constructors:

    TCPClient(Socket socket) {
        clientSocket = socket;
    }

    public TCPClient() throws IOException {
        clientSocket = new Socket();
        clientSocket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
    }

    // Main methods:

    public void connect(InetAddress address, int port) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        clientSocket.connect(socketAddress);
    }

    public void work(IWorker worker) throws IOException {
        try {
            InputStream socketIS = clientSocket.getInputStream();
            OutputStream socketOS = clientSocket.getOutputStream();

            worker.run(socketIS, socketOS);
        }
        catch (InterruptedException ignored) {}
    }

    public void close() {
        try {
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
            clientSocket.close();
        }
        catch (IOException ignored) {}
    }

    // Getters:

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
}
