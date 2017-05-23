package demo.ws.client;

import java.net.URI;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class WsClient extends WebSocketClient {

    public WsClient(URI serverURI) {
        super(serverURI);
    }

    public WsClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public WsClient(URI serverUri, Draft draft, Map<String, String> headers, int connecttimeout) {
        super(serverUri, draft, headers, connecttimeout);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("wsClient onClose......");
        System.out.println("code:"+code+",reason:"+reason+",remote:"+remote);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("wsClient onError......");
        System.out.println("msg:"+ex.getMessage());
    }

    @Override
    public void onMessage(String message) {
        System.out.println("wsClient onMessage......");
        System.out.println("message:"+message);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("wsClient onOpen......");
        System.out.println("HttpStatus:"+handshakedata.getHttpStatus());
    }

}
