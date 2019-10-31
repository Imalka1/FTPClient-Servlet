<%@ page import="controller.db_controller.FileTypesController" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Imalka Gunawardana
  Date: 2019-09-22
  Time: 10:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //--------------------------------------------Load the current session----------------------------------------------
    HttpSession sessionLogin = request.getSession(false);

    //------------------------------------Check whether the session variable is alive-------------------------------
    if (sessionLogin.getAttribute("ftpClientobj") == null) {
%>

<%------------------------------------------Navigate to admin landing page--------------------------------------------%>
<jsp:forward page="index.jsp"/>
<%
    }
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/font-awesome/latest/css/font-awesome.min.css">
    <script src="assets/js/jquery-3.2.1.min.js"></script>
    <style>
        .col-center {
            float: none;
            margin: 0 auto
        }
    </style>
</head>
<body class="container-fluid">
<input type="hidden" id="contextPath" value="<%= request.getContextPath()%>">
<input type="hidden" value="<%= sessionLogin.getAttribute("username")%>" id="username">
<input type="hidden" value="<%= sessionLogin.getAttribute("password")%>" id="password">
<input type="hidden" value="<%= sessionLogin.getAttribute("server")%>" id="server">
<div class="row" style="margin-top: 50px">
    <div class="col-10" style="text-align: center;font-size: 23px;font-weight: bold">
        FTP Client
    </div>
    <div class="col-2">
        <form action="${pageContext.request.contextPath}/ftp_logout" method="post">
            <button type="submit" class="btn btn-warning"
                    style="position: absolute;left: 50%;transform: translateX(-50%)">Logout
            </button>
        </form>
    </div>
</div>
<div class="row" style="margin-top: 50px;margin-bottom: 100px">
    <div class="col-12">
        <div class="row" style="border: 1px #674c00 solid;margin: 30px;padding: 10px">
            <div class="col-12" style="text-align: center;margin-bottom: 20px;font-weight: bold">File Upload</div>
            <div class="col-3"></div>
            <div class="col-3">
                <input type="file" id="fileUpload" multiple="multiple">
            </div>
            <div class="col-2">
                <button type="submit" class="btn btn-warning" style="position: relative;float: right"
                        id="btnUploadFile">
                    Upload
                </button>
            </div>
            <div class="col-1" style="margin-top: 4px" id="successCount">

            </div>
            <div class="col-3"></div>
        </div>
    </div>
    <div class="col-12" style="margin-top: 80px">
        <div class="row">
            <div class="col-3"></div>
            <div class="col-2" style="font-weight: bold;margin-top: 5px">File Type</div>
            <div class="col-4">
                <select name="" id="fileType" class="form-control">
                    <%
                        {
                            List<String[]> fileTypes = new FileTypesController().getFileTypes();
                            for (String[] fileType : fileTypes) {
                    %>
                    <option value="<%= fileType[0]%>"><%= fileType[1]%>
                    </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="col-3"></div>
        </div>
    </div>
</div>
<div class="row" style="margin-top: 80px">
    <div class="col-2">
        <button class="btn btn-warning" id="btnBack">Go Back</button>
    </div>
    <div class="col-1" style="margin-top: 5px;font-weight: bold">Path -></div>
    <div class="col-7" style="margin-top: 5px" id="folderPath"></div>
    <div class="col-2">
        <button class="btn btn-warning" id="btnNewFolder" style="position: relative;float: right">Create Folder</button>
    </div>
</div>
<div class="row" style="margin-top: 10px;margin-bottom: 100px">
    <div class="col-12">
        <table border="1px" style="width: 100%">
            <th width="70%" style="text-align: center">File</th>
            <th width="10%" style="text-align: center">Download</th>
            <th width="10%" style="text-align: center">Rename</th>
            <th width="10%" style="text-align: center">Delete</th>
            <tbody id="fileBody">

            </tbody>
        </table>
    </div>
</div>
<script src="controller/file_view_controller.js"></script>
</body>
</html>
