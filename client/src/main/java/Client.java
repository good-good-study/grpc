import com.sxt.grpc.Message;
import com.sxt.grpc.Response;
import com.sxt.grpc.TestServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class Client {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8088;
    private ManagedChannel managedChannel;

    private TestServiceGrpc.TestServiceBlockingStub nameServiceBlockingStub;

    public Client(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build());
    }

    public Client(ManagedChannel managedChannel) {
        this.managedChannel = managedChannel;
        this.nameServiceBlockingStub = TestServiceGrpc.newBlockingStub(managedChannel);
    }

    public void shutdown() throws InterruptedException {
        managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public Message getMessage(String n) {
        Message message = Message.newBuilder().setContent(n).build();
        Response response = nameServiceBlockingStub.getMessage(message);
        return response.getMessage();
    }

    public static void main(String[] args) {
        Client client = new Client(DEFAULT_HOST, DEFAULT_PORT);
        args = new String[]{"Java", "Kotlin", "Flutter"};
        for (String arg : args) {
            Message message = client.getMessage(arg);
            System.out.println(String.format("从服务获取到的信息%s", message.getContent()));
        }
    }
}
