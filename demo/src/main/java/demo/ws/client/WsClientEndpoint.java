package demo.ws.client;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * ClientEndpoint
 * @author liuchao
 */
@ClientEndpoint
public class WsClientEndpoint {

    private Session session;
    private MessageHandler messageHandler;

    public WsClientEndpoint() {}
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("opening websocket");
        if (this.session == null) this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("closing websocket");
        try {
            if (session != null) session.close();
            this.session = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        if (this.messageHandler != null) this.messageHandler.handleMessage(message);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println("onError:"+t.getMessage());
        try {
            if (session != null) session.close();
            this.session = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
    
    public Session getSession() {
        return session;
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}
