import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class MessageServer {

    private Logger logger = Logger.getLogger(MessageServer.class.getName());
    private static final int DEFAULT_PORT = 8088;
    private int port;//服务端口号
    private Server server;

    public MessageServer(int port) {
        this(port, ServerBuilder.forPort(port));
    }

    public MessageServer(int port, ServerBuilder<?> serverBuilder) {
        this.port = port;
        server = serverBuilder.addService(new MessageServiceImplBaseImpl()).build();
    }

    private void start() throws IOException {
        server.start();
        logger.info(String.format("服务启动, 端口: %s", port));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                MessageServer.this.stop();
            }
        });
    }

    private void stop() {
        if (server != null)
            server.shutdown();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        MessageServer messageServer;
        if (args.length > 0) {
            messageServer = new MessageServer(Integer.parseInt(args[0]));
        } else {
            messageServer = new MessageServer(DEFAULT_PORT);
        }
        messageServer.start();
        messageServer.blockUntilShutdown();
    }
}