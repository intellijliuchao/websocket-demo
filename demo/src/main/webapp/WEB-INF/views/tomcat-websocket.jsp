<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String ctxPath = request.getContextPath();
%>
<script src="<%=ctxPath %>/assets/js/sockjs-0.3.min.js"></script>
<script type="text/javascript">

    var websocket;
    
    function setConnected(connected) {
        $("#connect").prop("disabled", !!connected);
        $("#disconnect").prop("disabled", !connected);
    }

    function connect() {
        var username = $("#username").val();
        if (username && $.trim(username)) {
            username = $.trim(username);
        } else {
            alert("用户名不能为空！");
            return;
        }
        if ("WebSocket" in window) {
            $("#response").append("Websocket supported<br/>");
            websocket = new WebSocket("ws://localhost:8080/demo/tomcat/websocket/"+username+"?uname="+username);
            $("#response").append("Connection attempted<br/>");
        } else if ("MozWebSocket" in window) {
            $("#response").append("MozWebSocket supported<br/>");
            websocket = new MozWebSocket("ws://localhost:8080/demo/tomcat/websocket/"+username+"?uname="+username);
            $("#response").append("MozWebSocket attempted<br/>");
        } else {
            $("#response").append("Websocket not supported<br/>");
            return;
        }
        
        websocket.onopen = function() {
            $("#response").append("Connection open! -->"+username+"<br/>");
            setConnected(true);
        }

        websocket.onclose = function() {
            $("#response").append("Disconnecting connection! -->"+username+"<br/>");
            setConnected(false);
        }

        websocket.onmessage = function(evt) {
            var received_msg = evt.data;
            $("#response").append("receive:"+received_msg+"<br/>");
        }
        
        websocket.onerror = function (evnt) {
            $("#response").append("Connection error! -->"+username+"<br/>");
            setConnected(false);
        };
    }

    function disconnect() {
        if (websocket) {
            websocket.close();
            websocket = null;
        }
        setConnected(false);
    }

    function sendMSGInfo() {
        if (!websocket || websocket.readyState!=1) {
            alert("无连接，无法发送消息");
            return;
        }
        var message = $("#sendMSG").val();
        if (message) {
            message = $.trim(message);
        }
        if (!message) {
            alert("消息不能为空！");
            return;
        }
        $("#response").append("sendMSG:"+message+"<br/>");
        websocket.send(JSON.stringify({
            "message" : message
        }));
    }
    
    function sendServerMSG() {
        var message = $("#serverSendAllMSG").val();
        if (message) {
            message = $.trim(message);
        }
        if (!message) {
            alert("消息不能为空！");
            return;
        }
        $("#response").append("serverSendAllMSG:"+message+"<br/>");
        $.ajax({
            method: "GET",
            dataType: "json",
            contentType: "application/json",
            url: "<%=ctxPath %>/admin/tcws/mywebsocket/sendMSGToAll?message="+message,
            success: function(data, textStatus, jqXHR) {
                if (data) {
                    $("#response").append("serverSendAllMSG success!<br/>");
                } else {
                    $("#response").append("serverSendAllMSG fail!<br/>");
                }
            },
            error:function() {
                alert("服务器异常，刷新后重试！");
            }
        })
    }
    
    function sendServerMSGToOne() {
        var message = $("#serverSendMSGToOne").val();
        var userId = $("#serverSendUserId").val();
        if (message) {
            message = $.trim(message);
        }
        if (userId) {
            userId = $.trim(userId);
        }
        if (!message) {
            alert("消息不能为空！");
            return;
        }
        if (!userId) {
            alert("请输入用户ID！");
            return;
        }
        $("#response").append("serverSendMSGTo "+userId+":"+message+"<br/>");
        $.ajax({
            method: "GET",
            dataType: "json",
            contentType: "application/json",
            url: "<%=ctxPath %>/admin/tcws/mywebsocket/sendMSGToOne?message=" + message + "&sid=" + userId,
            success: function(data, textStatus, jqXHR) {
                if (data) {
                    $("#response").append("serverSendMSGTo "+userId+" success!<br/>");
                } else {
                    $("#response").append("serverSendMSGTo "+userId+" fail!<br/>");
                }
            },
            error:function() {
                alert("服务器异常，刷新后重试！");
            }
        })
    }
    
    function clearConsole() {
        $("#response").empty();
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。  
    window.onbeforeunload = function(){  
        disconnect();  
    }  
</script>
<div class="panel-heading">
    <h3 class="panel-title">Java后端WebSocket的Tomcat实现</h3>
</div>
<div class="panel-body">
    <div class="dataTables_wrapper form-inline dt-bootstrap">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="username" class="control-label">填写链接用户名</label>
                    <input type="text" class="form-control" id="username"/>
                </div>
            </div>
            <div class="col-md-6">
                <button type="button" class="btn btn-info pull-right" id="disconnect" disabled="disabled" onclick="disconnect()">断开</button>
                <button type="button" class="btn btn-info pull-right" style="margin-right:10px;" id="connect" onclick="connect()">连接</button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <div class="form-group">
                        <label for="sendMSG" class="control-label">发送自己的消息</label>
                        <input type="text" class="form-control" id="sendMSG"/>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <button type="button" class="btn btn-info pull-right" onclick="sendMSGInfo()">测试发送自己</button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <div class="form-group">
                        <label for="serverSendAllMSG" class="control-label">服务器群发消息</label>
                        <input type="text" class="form-control" id="serverSendAllMSG"/>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <button type="button" class="btn btn-info pull-right" onclick="sendServerMSG()">发送所有用户</button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-5">
                <div class="form-group">
                    <div class="form-group">
                        <label for="serverSendMSGToOne" class="control-label">发指定用户消息</label>
                        <input type="text" class="form-control" id="serverSendMSGToOne"/>
                    </div>
                </div>
            </div>
            <div class="col-md-5">
                <div class="form-group">
                    <div class="form-group">
                        <label for="serverSendUserId" class="control-label">用户ID</label>
                        <input type="text" class="form-control" id="serverSendUserId"/>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <button type="button" class="btn btn-info pull-right" onclick="sendServerMSGToOne()">发送指定用户</button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <button type="button" class="btn btn-info pull-right" onclick="clearConsole()">清空控制台</button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div id="response"></div>
            </div>
        </div>
    </div>
</div>
