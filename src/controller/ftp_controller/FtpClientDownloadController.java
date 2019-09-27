package controller.ftp_controller;

import auth.FtpClientConnection;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.Properties;

@WebServlet(urlPatterns = "/ftp_download")
public class FtpClientDownloadController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sessionLogin = req.getSession(false);
        FileInputStream inStream = null;
        OutputStream outStream = null;

        int connectionCount = 0;

        while (connectionCount < 20) {
            try {
                connectionCount++;
                FTPClient client = FtpClientConnection.getFtpClientConnection(sessionLogin, connectionCount);
                if (client.isConnected()) {

                    InputStream input = new FileInputStream(this.getClass().getClassLoader().getResource("ftp_client_properties/server.properties").getPath());
                    Properties prop = new Properties();

                    prop.load(input);

                    String filePath = prop.getProperty("download_folder_path") + req.getParameter("folder_path") + "/" + req.getParameter("file_name");

                    File downloadFile = new File(filePath);
                    inStream = new FileInputStream(downloadFile);

                    // obtains ServletContext
                    ServletContext context = getServletContext();

                    // gets MIME type of the file
                    String mimeType = context.getMimeType(filePath);
                    if (mimeType == null) {
                        // set to binary type if MIME mapping not found
                        mimeType = "application/octet-stream";
                    }

                    resp.setContentType(mimeType);
                    resp.setContentLength((int) downloadFile.length());

                    resp.setHeader("Content-disposition", "attachment; filename=" + req.getParameter("file_name"));

                    // obtains response's output stream
                    outStream = resp.getOutputStream();

                    byte[] buffer = new byte[1048];
                    int bytesRead = -1;

                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }

                    connectionCount = 20;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inStream.close();
                outStream.close();
            }
        }
    }
}
