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

//--Load files--

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
            url: window.location.origin + $('#path_name').val() + "/ftp_client",
            data: {
                fileType: $('#fileType').val(),
                filePath: filePath
            },
            success: function (response) {
                console.log(response)
                var tableData = '';
                var filesObj = JSON.parse(response).Files;
                // console.log(filesObj)
                for (var i = 0; i < filesObj.length; i++) {
                    tableData += '<tr>';
                    if (filesObj[i].FileType === 'Directory') {
                        tableData += '' +
                            '<td class="directoryName" style="cursor: pointer"><i class="fa fa-folder" style="color: #E0A800;padding-right: 8px;padding-left: 5px;font-size: 25px"></i><span>' + filesObj[i].FileName + '</span></td>' +
                            '<td style="text-align: center;cursor: pointer"><span style="color: #94948c;text-align: center;font-size: 15px;padding: 5px">Not Available</span></td>';
                    } else if (filesObj[i].FileType === 'File') {
                        tableData += '' +
                            '<td style="cursor: pointer"><i class="fa fa-file" style="color: #9e7500;padding-right: 8px;padding-left: 5px;font-size: 25px"></i><span>' + filesObj[i].FileName + '</span></td>' +
                            '<td style="text-align: center;cursor: pointer"><a href="ftp://' + $('#username').val() + ':' + $('#password').val() + '@' + window.location.hostname + folderPath + '/' + filesObj[i].FileName + '" style="text-decoration: inherit;color:#94948c "><span style="margin-right: 3px">Download</span><i class="fa fa-arrow-circle-down" style="color: #94948c;text-align: center;font-size: 20px;padding: 5px"></i></a></td>';
                    }
                    tableData +=
                        '<td class="btnRename" style="text-align: center;cursor: pointer"><i class="fa fa-pencil" style="color: #62625c;text-align: center;font-size: 25px;padding: 5px"></i></td>' +
                        '<td class="btnDelete" style="text-align: center;cursor: pointer"><i class="fa fa-times" style="color: #dd1f08;text-align: center;font-size: 25px;padding: 5px"></i></td>' +
                        '</tr>';
                }
                $('#fileBody').html(tableData);
            },
            error: function (err) {

            }
        }
    );
}

//--Rename--

var oldName = '';
$(document).on('click', '.btnRename', function () {
    if ($(this).parent().children('td').eq(2).children().attr('class') === 'fa fa-pencil') {

        $(this).parent().children('td').eq(0).removeClass();
        $(this).html('<i class="fa fa-check" style="color: #62625c;text-align: center;font-size: 25px;padding: 5px"></i>');
        oldName = $(this).parent().children('td').eq(0).children('span').text();
        $(this).parent().children('td').eq(0).children('span').html('<input type="text" value="' + oldName + '">');

    } else if ($(this).parent().children('td').eq(2).children().attr('class') === 'fa fa-check') {

        var newName = $(this).parent().children('td').eq(0).children('span').children('input').val();
        console.log(folderPath + '/' + oldName)
        console.log(folderPath + '/' + newName)
        renameFile(folderPath + '/' + oldName, folderPath + '/' + newName, newName, this);


    }
})

function renameFile(oldName, newName, newNameView, that) {
    $.ajax(
        {
            type: "post",
            url: window.location.origin + $('#path_name').val() + "/ftp_rename",
            data: {
                oldName: oldName,
                newName: newName
            },
            success: function (response) {
                if (JSON.parse(response) === true) {
                    $(that).parent().children('td').eq(0).addClass('directoryName');
                    $(that).html('<i class="fa fa-pencil" style="color: #62625c;text-align: center;font-size: 25px;padding: 5px"></i>');
                    $(that).parent().children('td').eq(0).children('span').html(newNameView);
                }
            },
            error: function () {

            }
        }
    );
}

//--Create new folder--

$('#btnNewFolder').click(function () {
    $.ajax(
        {
            type: "post",
            url: window.location.origin + $('#path_name').val() + "/ftp_new",
            data: {
                folderPath: folderPath + '/New folder'
            },
            success: function (response) {
                if (JSON.parse(response) === true) {
                    loadFiles(folderPath);
                }
            },
            error: function () {

            }
        }
    );
})

//--Delete folder--

$(document).on('click', '.btnDelete', function () {
    var r = confirm("Do you need to delete this folder / file?");
    if (r === true) {
        $.ajax(
            {
                type: "post",
                url: window.location.origin + $('#path_name').val() + "/ftp_delete",
                data: {
                    folderPath: folderPath + '/' + $(this).parent().children('td').eq(0).children('span').text(),
                    fileOrFolder: $(this).parent().children('td').eq(0).children('i').attr('class')
                },
                success: function (response) {
                    if (JSON.parse(response) === true) {
                        loadFiles(folderPath);
                    }
                },
                error: function () {

                }
            }
        );
    }
})

//--Upload file--

var fileToUpload;
$('input[type=file]').change(function () {
    fileToUpload = this.files;
    $('#successCount').text('');
});

$('#btnUploadFile').click(function () {
    var successCount = 0;
    for (var i = 0; i < fileToUpload.length; i++) {
        var myFormData = new FormData();
        myFormData.append('fileToUpload', fileToUpload[i]);
        myFormData.append('folderPath', folderPath + '/');

        $.ajax(
            {
                async: false,
                type: "post",
                url: window.location.origin + $('#path_name').val() + "/ftp_upload",
                processData: false, // important
                contentType: false, // important
                data: myFormData,
                success: function (response) {
                    if (JSON.parse(response) === true) {
                        successCount++;
                        $('#successCount').text('( ' + (successCount) + ' / ' + fileToUpload.length + ' )')
                    }
                },
                error: function () {

                }
            }
        );
    }
    if (successCount === fileToUpload.length) {
        $('input[type=file]').val('')
        loadFiles(folderPath);
    } else {
        loadFiles(folderPath);
    }
});
