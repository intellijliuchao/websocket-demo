<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<% 
    String ctxPath = request.getContextPath();
 %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="description" content="Xenon Boostrap Admin Panel"/>
    <meta name="author" content=""/>
    <title>websocket-test</title>
    <jsp:include page="include/commonHeadImport.jsp" />
    <jsp:include page="include/commonDataTableHeadImport.jsp" />
    <style type="text/css">
        .page-container .main-content {
             position: static; 
        }
    </style>
</head>
<body class="page-body">
    <div class="page-container">
        <div class="sidebar-menu toggle-others fixed">
            <div class="sidebar-menu-inner">    
                <header class="logo-env">
                    <div class="logo">
                        <a href="#" class="logo-expanded">
                            <img src="<%=ctxPath %>/assets/images/logo@2x.png" width="80" alt="" />
                        </a>
                        <a href="#" class="logo-collapsed">
                            <img src="<%=ctxPath %>/assets/images/logo-collapsed@2x.png" width="40" alt="" />
                        </a>
                    </div>
                    <div class="mobile-menu-toggle visible-xs">
                        <a href="#" data-toggle="user-info-menu">
                            <i class="fa-bell-o"></i>
                            <span class="badge badge-success">7</span>
                        </a>
                        <a href="#" data-toggle="mobile-menu">
                            <i class="fa-bars"></i>
                        </a>
                    </div>
                    <div class="settings-icon"></div>
                </header>
                <ul id="main-menu" class="main-menu">
                    <li>
                        <a href="#">
                            <i class="linecons-cloud"></i>
                            <span class="title">websocket</span>
                        </a>
                        <ul>
                            <li>
                                 <a href="#" onclick="loadHtml('/admin/ws/mywebsocket')">
                                    <span class="title">spring-websocket</span>
                                </a>
                            </li>
                        </ul>
                        <ul>
                            <li>
                                 <a href="#" onclick="loadHtml('/admin/tcws/mywebsocket')">
                                    <span class="title">tomcat-websocket</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
        <div class="main-content">
            <nav class="navbar user-info-navbar" role="navigation">
                <ul class="user-info-menu left-links list-inline list-unstyled">
                    <li class="hidden-sm hidden-xs">
                        <a href="#" data-toggle="sidebar">
                            <i class="fa-bars"></i>
                        </a>
                    </li>
                </ul>
                <ul class="user-info-menu right-links list-inline list-unstyled">
                    <li class="dropdown user-profile">
                        <a href="#" data-toggle="dropdown">
                            <img src="<%=ctxPath %>/assets/images/user-4.png" alt="user-image" class="img-circle img-inline userpic-32" width="28" />
                            <span>欢迎您
                                <i class="fa-angle-down"></i>
                            </span>
                        </a>
                        <ul class="dropdown-menu user-profile-menu list-unstyled">
                            <li class="last"></li>
                        </ul>
                    </li>
                </ul>
            </nav>
            <script type="text/javascript">
                function loadHtml(url) {
                    var menu = "<%=ctxPath %>" + url;
                    $("#jdbPanel").load(menu, function(response, status, xhr) {
                        var xhrStatus = xhr.status;
                        if (xhrStatus == 404) {
                            $("#jdbPanel").text("您请求的页面不存在！");
                        }
                    });
                    return false;
                }
            </script>
            <div id="jdbPanel" class="panel panel-default"></div>
        </div>
    </div>
    <jsp:include page="include/commonFootImport.jsp" />
    <jsp:include page="include/commonDataTableFootImport.jsp" />
</body>
</html>