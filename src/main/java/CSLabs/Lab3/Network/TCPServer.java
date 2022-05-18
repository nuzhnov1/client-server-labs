package CSLabs.Lab3.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.*;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class TCPServer {
    // Nested structures

    enum ServerStatus {
        NOT_RUNNING, ACTIVE, SUSPEND, DEAD
    }

    // Constants members:

    private static final int WAITTIME = 50;

    // Static members:
    private static int instancesCount = 0;
    
    // Data members:

    private final IWorker worker;
    private final ServerSocket serverSocket;
    private final ThreadGroup threadGroup = new ThreadGroup(String.format("Server%d thread group", instancesCount));
    private InetSocketAddress socketAddress;
    private int maxConnections = -1;

    private volatile String serverName = String.format("Server%d", instancesCount++);
    private volatile PrintStream logStream = System.out;
    private volatile ServerStatus status = ServerStatus.NOT_RUNNING;
    private volatile boolean isLogging = true;

    // Constructors:

    private TCPServer(IWorker clientWorker) throws IOException {
        worker = clientWorker;
        serverSocket = new ServerSocket();
    }

    public TCPServer(IWorker clientWorker, int port) throws IOException {
        this(clientWorker);
        socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), port);
    }

    public TCPServer(IWorker clientWorker, InetAddress address, int port) throws IOException {
        this(clientWorker);
        socketAddress = new InetSocketAddress(address, port);
    }

    public TCPServer(IWorker clientWorker, String hostname, int port) throws IOException {
        this(clientWorker);
        socketAddress = new InetSocketAddress(hostname, port);
    }

    // Main methods:

    public void run() throws IOException, SecurityException, IllegalStateException {
        if (status == ServerStatus.DEAD)
            throw new IllegalStateException(String.format("server \"%s\" is dead", serverName));

        try {
            status = ServerStatus.ACTIVE;
            serverSocket.bind(socketAddress, maxConnections);
            serverSocket.setSoTimeout(WAITTIME);
            log(String.format("%s: начинаю свою работу.", serverName));
            log(getServerInfo());

            while (true) {
                while (status == ServerStatus.SUSPEND)
                    Thread.sleep(WAITTIME);

                try {
                    Socket clientSocket = serverSocket.accept();
                    Thread clientThread = new Thread(threadGroup, () -> clientHandler(clientSocket));

                    clientThread.start();
                }
                catch (SocketTimeoutException ignored) {}
            }
        }
        catch (SocketException error) {
            String message = String.format(
                    "%s: критическая ошибка. Не удалось установить значение таймаута. " +
                    "Ошибка в базовом протоколе TCP. Сообщение ошибки: %s.",
                    serverName, error.getMessage()
            );

            log(message);
            throw new SocketException(message);
        }
        catch (SecurityException error) {
            String message = String.format(
                    "%s: критическая ошибка при ожидании соединения. Операция не доступна. Сообщение ошибки: %s.",
                    serverName, error.getMessage()
            );

            log(message);
            throw new SecurityException(message);
        }
        catch (IOException error) {
            String message = String.format(
                    "%s: критическая ошибка при ожидании соединения. Сообщение ошибки: %s.",
                    serverName, error.getMessage()
            );

            log(message);
            throw new IOException(message);
        }
        catch (InterruptedException ignored) {}
        finally {
            log(String.format("%s: завершаю свою работу.", serverName));
            threadGroup.interrupt();

            try {
                serverSocket.close();
            }
            catch (IOException error) {
                log(String.format(
                        "%s: не удалось закрыть серверный сокет. Сообщение ошибки: %s.",
                        serverName, error.getMessage()
                ));
            }
        }
    }

    private void clientHandler(Socket clientSocket) {
        log(String.format(
                "%s: клиент \"%s\" подсоединился к серверу. Начинаю обработку его запросов.",
                serverName, clientSocket.getInetAddress()
        ));

        try {
            InputStream socketIS = clientSocket.getInputStream();
            OutputStream socketOS = clientSocket.getOutputStream();

            worker.run(socketIS, socketOS);
        }
        catch (IOException error) {
            log(String.format(
                    "%s: ошибка при обработке запросов клиента \"%s\". Сообщение ошибки: %s.",
                    serverName, clientSocket.getInetAddress(), error.getMessage()
            ));
        }
        catch (InterruptedException ignored) {}
        finally {
            log(String.format(
                    "%s: разрываю соединение с клиентом \"%s\".",
                    serverName, clientSocket.getInetAddress()
            ));

            try {
                clientSocket.shutdownInput();
                clientSocket.shutdownOutput();
                clientSocket.close();
            }
            catch (IOException error) {
                log(String.format(
                        "%s: ошибка при разрыве соединения. Сообщение ошибки: %s.",
                        serverName, error.getMessage()
                ));
            }
        }
    }

    private void log(String str) {
        if (isLogging)
            logStream.println(str);
    }

    // Getters:

    public IWorker getWorker() { return worker; }
    public int getMaxConnections() { return maxConnections; }
    public String getServerName() { return serverName; }
    public PrintStream getLogStream() { return logStream; }
    public boolean isLogging() { return isLogging; }

    public boolean isRun() { return status != ServerStatus.NOT_RUNNING; }
    public boolean isActive() { return status == ServerStatus.ACTIVE; }
    public boolean isPause() { return status == ServerStatus.SUSPEND; }
    public boolean isDead() { return status == ServerStatus.DEAD; }

    public InetAddress getInetAddress() { return serverSocket.getInetAddress(); }
    public String getHostName() { return serverSocket.getInetAddress().getHostName(); }
    public int getPort() { return serverSocket.getLocalPort(); }
    public boolean isAddressReuse() throws SocketException { return serverSocket.getReuseAddress(); }

    public String getThreadsGroupName() { return threadGroup.getName(); }
    public int getActiveConnections() { return threadGroup.activeCount(); }

    public String getServerInfo() {
        String status = (isActive()) ? "активен" : "приостановлен";
        String maxConnectionsString = (maxConnections <= 0) ? "по-умолчанию" : String.valueOf(maxConnections);

        return String.format("""
                Информация о сервере "%s":
                    IP-адрес сервера: %s
                    Имя хоста сервера: %s
                    Порт сервера: %d
                    Статус сервера: %s
                    Текущее число соединений: %d
                    Максимальное число соединений: %s
                    Наименование группы потоков: %s
                """,
                serverName, getInetAddress(), getHostName(), getPort(),
                status, getActiveConnections(), maxConnectionsString,
                getThreadsGroupName()
        );
    }

    // Setters:

    public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
    public void setServerName(String name) { serverName = name; }
    public void setLogStream(PrintStream logStream) { this.logStream = logStream; }
    public void setLogging(boolean isLogging) { this.isLogging = isLogging; }

    public void activate() {
        if ((status != ServerStatus.NOT_RUNNING) && (status != ServerStatus.DEAD))
            status = ServerStatus.ACTIVE;
    }
    public void pause() {
        if ((status != ServerStatus.NOT_RUNNING) && (status != ServerStatus.DEAD))
            status = ServerStatus.SUSPEND;
    }

    public void setReuseAddress(boolean isReuseAddress) throws SocketException {
        serverSocket.setReuseAddress(isReuseAddress);
    }
    public void setPerformance(int connectionTime, int latency, int bandwidth) {
        serverSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
    }
}
