<%--
  Created by IntelliJ IDEA.
  User: Imalka Gunawardana
  Date: 2019-09-22
  Time: 10:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="/assets/font-awesome/latest/css/font-awesome.min.css">
    <script src="/assets/js/jquery-3.2.1.min.js"></script>
</head>
<body class="container-fluid">
<div class="row" style="margin-top: 50px">
    <div class="col-2" style="margin-top: 6px;text-align: right">
        File Type
    </div>
    <div class="col-4">
        <select name="" id="fileType" class="form-control">
            <option value=".jpg">JPG(.jpg)</option>
            <option value=".mp3">mp3(.mp3)</option>
            <option value=".rar">ZIP(.zip)</option>
            <option value=".docx">Word(.docx)</option>
            <option value=".pdf">PDF(.pdf)</option>
        </select>
    </div>
    <div class="col-6">
        <button class="btn btn-warning" style="position: absolute;left: 50%;transform: translateX(-50%)">Logout</button>
    </div>
</div>
<div class="row" style="margin-top: 80px">
    <div class="col-2">
        <button class="btn btn-warning" id="btnBack">Go Back</button>
    </div>
    <div class="col-1" style="margin-top: 5px">Path -></div>
    <div class="col-9" style="margin-top: 5px" id="folderPath"></div>
</div>
<div class="row" style="margin-top: 10px">
    <div class="col-12">
        <table border="1px" style="width: 100%">
            <th width="80%" style="text-align: center">File</th>
            <th width="10%" style="text-align: center">Download</th>
            <th width="10%" style="text-align: center">Edit</th>
            <tbody id="fileBody">

            </tbody>
        </table>
    </div>
</div>
<div class="row" style="margin-top: 100px;margin-bottom: 100px">
    <div class="col-8">
        <input type="file">
    </div>
    <div class="col-4">
        <button class="btn btn-warning" style="position: absolute;left: 50%;transform: translateX(-50%)">Upload</button>
    </div>
</div>
<script>
    $('#fileType').change(function () {
        loadFiles('');
    })

    $(window).on("load", function () {
        loadFiles('');
        $('#folderPath').text('/');
    });

    $(document).on('click', '.directoryName', function () {
        manageFolders('push', $(this).children('span').text())
    })

    $('#btnBack').click(function () {
        manageFolders('pop')
    })

    var folders = Array();

    function manageFolders(pushOrPop, folderName) {
        var folderPath = '';
        var folderPathView = '';
        if (pushOrPop === 'push') {
            folders.push(folderName);
        } else if (pushOrPop === 'pop') {
            folders.pop();
        }
        for (var i = 0; i < folders.length; i++) {
            folderPath += '/' + folders[i];
            folderPathView += ' / ' + folders[i];
        }
        $('#folderPath').text(folderPathView);
        loadFiles(folderPath);
    }

    function loadFiles(filePath) {
        $.ajax(
            {
                type: "post",
                url: window.location.origin + "/ftp_client",
                data: {
                    fileType: $('#fileType').val(),
                    filePath: filePath
                },
                success: function (response) {
                    var tableData = '';
                    var filesObj = JSON.parse(response).Files;
                    // console.log(filesObj)
                    for (var i = 0; i < filesObj.length; i++) {
                        if (filesObj[i].FileType === 'Directory') {
                            tableData += '' +
                                '<tr>' +
                                '<td class="directoryName" style="cursor: pointer"><i class="fa fa-folder" style="color: #E0A800;padding-right: 8px;padding-left: 5px;font-size: 25px"></i><span>' + filesObj[i].FileName + '</span></td>' +
                                '<td style="text-align: center;cursor: pointer"><i class="fa fa-arrow-circle-down" style="color: #94948c;text-align: center;font-size: 25px;padding: 5px"></i></td>' +
                                '<td style="text-align: center;cursor: pointer"></td>' +
                                '</tr>';
                        } else if (filesObj[i].FileType === 'File') {
                            tableData += '' +
                                '<tr>' +
                                '<td style="cursor: pointer"><i class="fa fa-file" style="color: #9e7500;padding-right: 8px;padding-left: 5px;font-size: 25px"></i>' + filesObj[i].FileName + '</td>' +
                                '<td style="text-align: center;cursor: pointer"><i class="fa fa-arrow-circle-down" style="color: #94948c;text-align: center;font-size: 25px;padding: 5px"></i></td>' +
                                '<td style="text-align: center;cursor: pointer"></td>' +
                                '</tr>';
                        }
                    }
                    $('#fileBody').html(tableData);
                },
                error: function () {

                }
            }
        );
    }
</script>
</body>
</html>
