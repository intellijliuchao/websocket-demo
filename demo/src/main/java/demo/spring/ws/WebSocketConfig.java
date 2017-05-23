package demo.spring.ws;
import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author liuchao
 * 开启websocket服务，绑定WebSocketHandler和HandshakeInterceptor
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    
    @Resource
    private SystemWebSocketHandler webSocketHandler;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 支持websocket的访问链接
        registry.addHandler(webSocketHandler, "/webSocketServer")
                .addInterceptors(new WebSocketHandshakeInterceptor());
        // 不支持websocket的访问链接
        registry.addHandler(webSocketHandler, "/sockjs/webSocketServer")
                .addInterceptors(new WebSocketHandshakeInterceptor())
                .setAllowedOrigins("*").withSockJS();
    }

}