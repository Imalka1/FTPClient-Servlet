package controller.url_controller;

import auth.FtpClientConnection;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/ftp_logout")//---URL extension which mapped to this servlet object
public class LogoutController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sessionLogin = req.getSession(false);//---Load the current session
        int connectionCount = 0;

        while (connectionCount < 20) {
            try {
                connectionCount++;
                FTPClient ftpClient = FtpClientConnection.getFtpClientConnection(sessionLogin,connectionCount);
                if (ftpClient.isConnected()) {
                    if (sessionLogin != null) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                        sessionLogin.invalidate();//---Remove the current session
                        connectionCount = 20;
                    }
                    resp.sendRedirect("index.jsp");//---Navigate (redirect) to login page
                }
            } catch (Exception e) {

            }
        }
    }
}
