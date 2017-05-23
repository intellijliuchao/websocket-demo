package demo.controllers;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.websocket.DeploymentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import demo.spring.ws.SystemWebSocketHandler;
import demo.ws.client.WsConnectTest;

@Controller
@RequestMapping(value = "/index")
public class IndexController {

    @Autowired
    private WsConnectTest wsConnectTest;
    @Resource
    private SystemWebSocketHandler webSocketHandler;
    
    @RequestMapping("")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
    
    @RequestMapping(value= "/buildConnect", method = RequestMethod.GET)
    @ResponseBody
    public String buildConnect() throws DeploymentException, IOException{
        long start = System.currentTimeMillis();
        //添加一千个用户
        wsConnectTest.testWsClient1000();
        System.out.println("目前连接数："+SystemWebSocketHandler.userSocketSessionMap.size());
        System.out.println("建立连接总耗时："+(System.currentTimeMillis()-start));
        return "success";
    }
    
    @RequestMapping(value= "/test2all", method = RequestMethod.GET)
    @ResponseBody
    public String test2all() throws DeploymentException, IOException{
        long start = System.currentTimeMillis();
        //服务器每隔3秒向所有用户发送大数据包
        wsConnectTest.startTest(webSocketHandler);
        for (Map.Entry<String, String> enter : WsConnectTest.temp.entrySet()) {
            System.out.println(enter.getKey()+":"+enter.getValue());
        }
        System.out.println("群发消息总耗时："+(System.currentTimeMillis()-start));
        return "success";
    }
    
}