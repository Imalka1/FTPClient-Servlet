package controller.ftp_controller;

import auth.FtpClientConnection;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/ftp_delete")
public class FtpClientFileDeleteController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sessionLogin = req.getSession(false);
        int connectionCount = 0;

        while (connectionCount < 10) {
            try {
                connectionCount++;
                FTPClient client = FtpClientConnection.getFtpClientConnection(sessionLogin,connectionCount);
                if (client.isConnected()) {
                    if (req.getParameter("fileOrFolder").equals("fa fa-file")) {
                        if (client.deleteFile(req.getParameter("folderPath"))) {
                            resp.getWriter().println(true);
                            connectionCount = 10;
                        }
                    } else if (req.getParameter("fileOrFolder").equals("fa fa-folder")) {
                        if (client.removeDirectory(req.getParameter("folderPath"))) {
                            resp.getWriter().println(true);
                            connectionCount = 10;
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
    }
}
