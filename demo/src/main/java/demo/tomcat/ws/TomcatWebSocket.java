package demo.tomcat.ws;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @ServerEndpoint 
 * 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 * configurator 指定客户端和服务气端握手时的处理实现类
 */
@ServerEndpoint(value = "/tomcat/websocket/{username}", configurator = TcServerEndpointConfigurator.class)
@Component
public class TomcatWebSocket {
    
    private static final Logger logger = LoggerFactory.getLogger(TomcatWebSocket.class);
    private String username;
    private Session session;
    private static final Map<String, Session> socketSessionMap;

    static {
        // 存储链接socketSessionMap的集合
        socketSessionMap = Collections.synchronizedMap(new HashMap<>());
    }
    
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam(value = "username") String username) throws IOException {
        logger.debug("connect to the Tomcatwebsocket success......");
        System.out.println("connect to the Tomcatwebsocket success......");
        //System.out.println("PathParameters:"+session.getPathParameters().get("username"));
        //System.out.println("RequestParameter:"+session.getRequestParameterMap().get("uname").get(0));
        //HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.session = session;
        this.username = addSession(username, session);
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText("connect to the Tomcatwebsocket success, [username : "+this.username+"]");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam(value = "username") String username) {
        logger.debug("Tomcatwebsocket connection closed......");
        System.out.println("Tomcatwebsocket connection closed......!");
        if (session != null) {
            removeSession(session);
        } else {
            removeSession(this.username);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam(value = "username") String username) throws IOException {
        logger.debug("Tomcatwebsocket handleMessage......");
        System.out.println("Tomcatwebsocket handleMessage-->message="+message);//{"message":"xxx"}
        if (StringUtils.isBlank(message)) return;
        if (session != null && session.isOpen()) {
            JsonNode jn = new ObjectMapper().readTree(message);
            session.getBasicRemote().sendText(jn.get("message").asText());
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        logger.debug("Tomcatwebsocket connectionError closed......");
        System.out.println("Tomcatwebsocket connectionError closed......!");
        if (session != null) {
            if(session.isOpen()){
                session.close();
            }
            removeSession(session);
        } else {
            removeSession(this.username);
        }
    }

    private String addSession(String username, Session wsSession) {
        if (wsSession == null) return "session is null";
        StringBuilder usernameStr = new StringBuilder();
        if (!StringUtils.isEmpty(username)) {
            usernameStr.append(username);
        }
        usernameStr.append(wsSession.getId());
        socketSessionMap.put(usernameStr.toString(), wsSession);
        return usernameStr.toString();
    }
    
    private void removeSession(Session wsSession) {
        if (wsSession == null) return;
        Iterator<Entry<String, Session>> it = socketSessionMap.entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Entry<String, Session> entry = it.next();
            if (entry.getValue() != null && entry.getValue().getId().equals(wsSession.getId())) {
                socketSessionMap.remove(entry.getKey());
                break;
            }
        }
    }
    
    private void removeSession(String uname) {
        if (StringUtils.isEmpty(uname)) return;
        socketSessionMap.remove(uname);
    }

    public void sendMessageToUsers(String message) throws IOException {
        if (StringUtils.isBlank(message)) return;
        for (Session session : socketSessionMap.values()) {
            if (session != null && session.isOpen()) {
                //session.getBasicRemote().sendText(message);
                session.getAsyncRemote().sendText(message);
            }
        }
    }
    
    public void sendMessageToUser(String sid, String message) throws IOException {
        if (StringUtils.isBlank(sid) || StringUtils.isBlank(message)) return;
        Session session = socketSessionMap.get(sid);
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(message);
            //session.getAsyncRemote().sendText(message);
        }
    }
}