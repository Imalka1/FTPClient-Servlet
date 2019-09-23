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
    <style>
        .col-center {
            float: none;
            margin: 0 auto
        }
    </style>
</head>
<body class="container-fluid">
<div class="row" style="margin-top: 50px">
    <div class="col-10" style="text-align: center">
        FTP Client
    </div>
    <div class="col-2">
        <button class="btn btn-warning" style="position: absolute;left: 50%;transform: translateX(-50%)">Logout</button>
    </div>
</div>
<div class="row" style="margin-top: 50px;margin-bottom: 100px">
    <div class="col-12">
        <div class="row" style="border: 1px #674c00 solid;margin: 30px;padding: 10px">
            <div class="col-12" style="text-align: center;margin-bottom: 20px">File Upload</div>
            <div class="col-3"></div>
            <div class="col-4">
                <input type="file">
            </div>
            <div class="col-2">
                <button class="btn btn-warning" style="position: relative;float: right">
                    Upload
                </button>
            </div>
            <div class="col-3"></div>
        </div>
    </div>
    <div class="col-12" style="margin-top: 80px">
        <div class="row">
            <div class="col-3"></div>
            <div class="col-2">File Type</div>
            <div class="col-4">
                <select name="" id="fileType" class="form-control">
                    <option value=".jpg">JPG(.jpg)</option>
                    <option value=".mp3">mp3(.mp3)</option>
                    <option value=".rar">ZIP(.zip)</option>
                    <option value=".docx">Word(.docx)</option>
                    <option value=".pdf">PDF(.pdf)</option>
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
    <div class="col-1" style="margin-top: 5px">Path -></div>
    <div class="col-9" style="margin-top: 5px" id="folderPath"></div>
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
<script>
    $('#fileType').change(function () {
        loadFiles(folderPath);
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
    var folderPath = '';
    var folderPathView = '';

    function manageFolders(pushOrPop, folderName) {
        folderPath = '';
        folderPathView = '';
        if (pushOrPop === 'push') {
            folders.push(folderName);
        } else if (pushOrPop === 'pop') {
            folders.pop();
            if (folders.length === 0) {
                folderPathView = '/';
            }
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
                        tableData += '<tr>';
                        if (filesObj[i].FileType === 'Directory') {
                            tableData += '' +
                                '<td class="directoryName" style="cursor: pointer"><i class="fa fa-folder" style="color: #E0A800;padding-right: 8px;padding-left: 5px;font-size: 25px"></i><span>' + filesObj[i].FileName + '</span></td>';
                        } else if (filesObj[i].FileType === 'File') {
                            tableData += '' +
                                '<td style="cursor: pointer"><i class="fa fa-file" style="color: #9e7500;padding-right: 8px;padding-left: 5px;font-size: 25px"></i>' + filesObj[i].FileName + '</td>';
                        }
                        tableData +=
                            '<td style="text-align: center;cursor: pointer"><i class="fa fa-arrow-circle-down" style="color: #94948c;text-align: center;font-size: 25px;padding: 5px"></i></td>' +
                            '<td class="btnRename" style="text-align: center;cursor: pointer"><i class="fa fa-pencil" style="color: #62625c;text-align: center;font-size: 25px;padding: 5px"></i></td>' +
                            '<td style="text-align: center;cursor: pointer"><i class="fa fa-times" style="color: #dd1f08;text-align: center;font-size: 25px;padding: 5px"></i></td>' +
                            '</tr>';
                    }
                    $('#fileBody').html(tableData);
                },
                error: function () {

                }
            }
        );
    }

    $(document).on('click', '.btnRename', function () {
        if ($(this).parent().children('td').eq(2).children().attr('class') === 'fa fa-pencil') {

            $(this).parent().children('td').eq(0).removeClass();
            $(this).html('<i class="fa fa-check" style="color: #62625c;text-align: center;font-size: 25px;padding: 5px"></i>');
            var folderName = $(this).parent().children('td').eq(0).children('span').text();
            $(this).parent().children('td').eq(0).children('span').html('<input type="text" value="' + folderName + '">');

        } else if ($(this).parent().children('td').eq(2).children().attr('class') === 'fa fa-check') {

            $(this).parent().children('td').eq(0).addClass('directoryName');
            $(this).html('<i class="fa fa-pencil" style="color: #62625c;text-align: center;font-size: 25px;padding: 5px"></i>');
            var folderName = $(this).parent().children('td').eq(0).children('span').children('input').val();
            console.log(folderName)
            $(this).parent().children('td').eq(0).children('span').html(folderName);

        }
    })
</script>
</body>
</html>
