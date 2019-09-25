<%@ page import="controller.ftp_controller.FtpClientController" %><%--
  Created by IntelliJ IDEA.
  User: Imalka Gunawardana
  Date: 2019-09-21
  Time: 1:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //--------------------------------------------Load the current session----------------------------------------------
    HttpSession sessionLogin = request.getSession(false);

    //------------------------------------Check whether the session variable is alive-------------------------------
    if (sessionLogin.getAttribute("ftpClientobj") != null) {
%>

<%------------------------------------------Navigate to admin landing page--------------------------------------------%>
<jsp:forward page="file_view.jsp"/>
<%
    }
%>
<html>
<head>
    <title>$Title$</title>
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
</head>
<body class="container-fluid">
<div class="row">
    <div class="col-12" style="text-align: center;margin-top: 100px;font-size: 35px">
        Login
    </div>
</div>
<form action="ftp_login" method="post">
    <div class="row" style="margin-left: 190px">
        <div class="col-3" style="margin-top: 35px;text-align: right">
            Username
        </div>
        <div class="col-4" style="margin-top: 30px">
            <input class="form-control" type="text" name="username">
        </div>
    </div>
    <div class="row" style="margin-left: 190px">
        <div class="col-3" style="margin-top: 25px;text-align: right">
            Password
        </div>
        <div class="col-4" style="margin-top: 20px">
            <input class="form-control" type="password" name="password">
        </div>
    </div>
    <div class="row">
        <div class="col-12" style="margin-top: 50px">
            <button type="submit" class="btn btn-warning"
                    style="position: absolute;left: 50%;transform: translateX(-50%)">Sign In
            </button>
        </div>
    </div>
</form>
</body>
</html>
