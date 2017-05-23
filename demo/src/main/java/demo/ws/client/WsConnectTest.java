package demo.ws.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.java_websocket.drafts.Draft_75;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import demo.spring.ws.SystemWebSocketHandler;

@Component
public class WsConnectTest {
    public static final Map<String, String> temp = Collections.synchronizedMap(new HashMap<>());
    /**
     * 测试WebSocketClient链接
     */
    public void conByWsClient() {
        WsClient wsc = null;
        try {
            //ws://localhost:8080/demo/admin/webSocketServer?username=client
            wsc = new WsClient(new URI("ws://localhost:8080/demo/tomcat/websocket/liuchao?uname=liuchao"), new Draft_75());
            wsc.connect();
            System.out.println(wsc.getURI());
            JSONObject obj = new JSONObject();
            obj.put("test", "addtest");
            obj.put("testtoo", "oktesttoo");
            wsc.send(obj.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if (wsc != null) wsc.close();
        }
    }

    /**
     * 测试ClientEndpoint链接
     */
    public void conByWsClientEndpoint() {
        // ws://localhost:8080/demo/tomcat/websocket/liuchao?uname=liuchao
        Session session = null;
        try {
            WsClientEndpoint clientEndPoint = new WsClientEndpoint();
            clientEndPoint.addMessageHandler(new WsClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(
                    clientEndPoint, URI.create("ws://localhost:8080/demo/admin/webSocketServer?username=client"));
            Thread.sleep(10000);
            clientEndPoint.sendMessage("This example demonstrates");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (session != null) session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 测试创建多个链接
     * @throws DeploymentException
     * @throws IOException
     */
    public void testWsClient1000() throws DeploymentException, IOException {
        String uri = "ws://192.168.1.16:8080/demo/admin/webSocketServer?username=client";
        for (int i=0; i<1500; i++) {
            WsClientEndpoint clientEndPoint = new WsClientEndpoint();
            clientEndPoint.addMessageHandler(new WsClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(clientEndPoint, URI.create(uri));
        }
    }
    
    /**
     * 测试服务器给所有用户发送信息
     * @param webSocketHandler
     */
    public void startTest(SystemWebSocketHandler webSocketHandler) {
        JSONObject obj = new JSONObject();
        for (int i=0; i<10; i++) {
            obj.put("test"+i, "This example demonstrates how to create a websocket connection to a server. Only the most important callbacks are overloaded");
            obj.put("too"+i, "if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient");
        }
        for (int i=0; i<6; i++) {
            final int lun = i+1;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        long start = System.currentTimeMillis();
                        webSocketHandler.sendMessageToUsersSync(new TextMessage(obj.toString()));
                        temp.put("send"+lun, "第"+lun+"次发送耗时："+(System.currentTimeMillis()-start));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
