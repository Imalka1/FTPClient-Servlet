package controller.ftp_controller;

import auth.FtpClientConnection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/ftp_client")
public class FtpClientController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sessionLogin = req.getSession(false);
        JSONObject obj = new JSONObject();//---Creates a JSON object {}
        JSONArray filesJson = new JSONArray();//---Creates a JSON array to store JSON objects []
        int connectionCount = 0;

        while (connectionCount < 10) {
            try {
                connectionCount++;
                FTPClient client = FtpClientConnection.getFtpClientConnection(sessionLogin,connectionCount);
                if (client.isConnected()) {
                    // Obtain a list of filenames in the current working
                    // directory. When no file found an empty array will
                    // be returned.

                    FTPFile[] ftpFiles = client.listFiles(req.getParameter("filePath").trim());
                    for (FTPFile ftpFile : ftpFiles) {
                        JSONObject fileJson = new JSONObject();//---Creates a JSON object {}
                        if (ftpFile.getType() == FTPFile.DIRECTORY_TYPE) {
                            fileJson.put("FileType", "Directory");
                            fileJson.put("FileName", ftpFile.getName());
                            filesJson.add(fileJson);
                        }
                    }

                    for (FTPFile ftpFile : ftpFiles) {
                        JSONObject fileJson = new JSONObject();//---Creates a JSON object {}
                        // Check if FTPFile is a regular file
                        if (ftpFile.getType() == FTPFile.FILE_TYPE) {
//                        System.out.printf("FTPFile: %s; %s%n",
//                                ftpFile.getName(),
//                                FileUtils.byteCountToDisplaySize(ftpFile.getSize()));
                            if (ftpFile.getName().endsWith(req.getParameter("fileType").trim())) {
                                fileJson.put("FileType", "File");
                                fileJson.put("FileName", ftpFile.getName());
                                filesJson.add(fileJson);
                            }
                        }
                    }
                    obj.put("Files", filesJson);
                    resp.getWriter().println(obj.toJSONString());//---Print and reply JSON as a text
                    connectionCount = 10;
                }
//            client.logout();
            } catch (Exception e) {

            }
        }
//        } finally {
//            try {
////                client.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
