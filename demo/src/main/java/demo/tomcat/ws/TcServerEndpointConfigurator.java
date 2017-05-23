package demo.tomcat.ws;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
 
public class TcServerEndpointConfigurator extends ServerEndpointConfig.Configurator{
    
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response){
        System.out.println("modifyHandshake......");
        System.out.println(request.getRequestURI().getPath());
        System.out.println(request.getParameterMap().get("uname").get(0));
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        config.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
    
}
