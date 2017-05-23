package demo.spring.ws;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author liuchao
 * webSocket链接状态及消息发送处理器
 */
@Component
public class SystemWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(SystemWebSocketHandler.class);
    public static final Map<String, WebSocketSession> userSocketSessionMap;

    static {
        // 存储链接WebsocketSession的集合
        userSocketSessionMap = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("connect to the websocket success......");
        System.out.println("connect to the websocket success......");
        Object username = session.getAttributes().get(WebSocketHandshakeInterceptor.USERNAME);
        StringBuilder usernameStr = new StringBuilder();
        if (username != null) {
            usernameStr.append((String)username);
        }
        usernameStr.append(session.getId());
        userSocketSessionMap.put(usernameStr.toString(), session);
//        session.sendMessage(new TextMessage("connect to the websocket success, [username : "+usernameStr.toString()+"]"));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.debug("handleMessage......");
        System.out.println("handleMessage-->message="+message.getPayload());//{"message":"xxx"}
        if (message != null && message.getPayload() != null) {
            JsonNode jn = new ObjectMapper().readTree(message.getPayload().toString());
            session.sendMessage(new TextMessage(jn.get("message").asText()));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.debug("websocket connectionError closed......");
        System.out.println("websocket connectionError closed......!");
        if(session.isOpen()){
            session.close();
        }
        removeSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.debug("websocket connection closed......");
        System.out.println("websocket connection closed......!");
        removeSession(session);
    }
    
    private void removeSession(WebSocketSession session) {
        Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Entry<String, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(session.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                break;
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给所有在线用户发送消息异步
     * @param message
     * @throws IOException
     */
    public void sendMessageToUsersAsync(TextMessage message) throws IOException {
        Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
        // 多线程群发
        while (it.hasNext()) {
            Entry<String, WebSocketSession> entry = it.next();
            if (entry.getValue() != null && entry.getValue().isOpen()) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            entry.getValue().sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
    
    /**
     * 给所有在线用户发送消息同步
     * @param message
     * @throws IOException
     */
    public void sendMessageToUsersSync(TextMessage message) throws IOException {
        long start = System.currentTimeMillis();
        Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
        // 多线程群发
        while (it.hasNext()) {
            Entry<String, WebSocketSession> entry = it.next();
            if (entry.getValue() != null && entry.getValue().isOpen()) {
                entry.getValue().sendMessage(message);
            }
        }
        System.out.println("群发消息："+(System.currentTimeMillis()-start));
    }

    /**
     * 给某个用户发送消息
     * @param sid
     * @param message
     * @throws IOException
     */
    public void sendMessageToUser(String sid, TextMessage message) throws IOException {
        WebSocketSession session = userSocketSessionMap.get(sid);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }
}