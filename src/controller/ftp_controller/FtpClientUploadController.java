package controller.ftp_controller;

import auth.FtpClientConnection;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@WebServlet(urlPatterns = "/ftp_upload")
@MultipartConfig
public class FtpClientUploadController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sessionLogin = req.getSession(false);

        int connectionCount = 0;

        while (connectionCount < 20) {
            try {
                connectionCount++;
                FTPClient client = FtpClientConnection.getFtpClientConnection(sessionLogin, connectionCount);
                if (client.isConnected()) {

                    Part filePart = req.getPart("fileToUpload"); // Retrieves <input type="file" name="file">
                    InputStream fileContent = filePart.getInputStream();
                    client.setFileType(FTP.BINARY_FILE_TYPE);
                    String firstRemoteFile = req.getParameter("folderPath") + Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                    boolean done = client.storeFile(firstRemoteFile, fileContent);
                    connectionCount = 20;
                    fileContent.close();
                    if (done) {
                        resp.getWriter().println(true);
                    } else {
                        resp.getWriter().println(false);
                    }
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }
}
