package demo.spring.ws;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author liuchao
 * webSocket创建链接握手拦截器
 */
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    public static final String USERNAME = "username";
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            if (session != null) {
                String username = servletRequest.getServletRequest().getParameter(USERNAME);
                if (!StringUtils.isBlank(username)) {
                    attributes.put(USERNAME, username);
                }
                //建立连接前的操作
                System.out.println("ServletServerHttpRequest beforeHandshake......");
            }
        }
        System.out.println("beforeHandshake......");
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {
        System.out.println("afterHandshake......");
    }
}