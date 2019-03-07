import com.sxt.grpc.Message;
import com.sxt.grpc.Response;
import com.sxt.grpc.TestServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageServiceImplBaseImpl extends TestServiceGrpc.TestServiceImplBase {

    private Logger logger = Logger.getLogger(MessageServiceImplBaseImpl.class.getName());

    public MessageServiceImplBaseImpl() {
    }

    @Override
    public void getMessage(Message request, StreamObserver<Response> responseObserver) {
        logger.log(Level.INFO, String.format("接收到客户端的请求%s", request.getContent()));
        Response response = Response.newBuilder().setMessage(Message.newBuilder().setContent(String.format("返回给客户端的信息%s", request.getContent()))).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
