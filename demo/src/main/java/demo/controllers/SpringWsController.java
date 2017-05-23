package demo.controllers;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import demo.spring.ws.SystemWebSocketHandler;

@Controller
@RequestMapping(value = "/ws")
public class SpringWsController {

    @Resource
    private SystemWebSocketHandler webSocketHandler;
    
    @RequestMapping("/mywebsocket")
    public ModelAndView index(){
        return new ModelAndView("spring-websocket");
    }
    
    /**
     * webSocket服务器群发消息
     * @param message
     * @throws IOException 
     */
    @RequestMapping(value = "/mywebsocket/sendMSGToAll", method = RequestMethod.GET)
    @ResponseBody
    public String sendMSGToAll(@RequestParam(required=true) String message) throws IOException{
        webSocketHandler.sendMessageToUsersAsync(new TextMessage(message));
        return "success";
    }
    
    /**
     * webSocket服务器给特定用户发消息
     * @param message
     * @param sid
     * @throws IOException
     */
    @RequestMapping(value = "/mywebsocket/sendMSGToOne", method = RequestMethod.GET)
    @ResponseBody
    public String sendMSGToOne(@RequestParam(required=true) String message, @RequestParam(required=true) String sid) throws IOException{
        webSocketHandler.sendMessageToUser(sid, new TextMessage(message));
        return "success";
    }
    
}