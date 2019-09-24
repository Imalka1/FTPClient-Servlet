package controller.url_controller;

import auth.FtpClientConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/ftp_login")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sessionLogin = req.getSession(true);

        //---------------------------------Retrieve data which submitted to the server----------------------------------
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        try {
            if (!username.equals("") && !password.equals("")) {
                FtpClientConnection.getFtpClientConnection(username, password, sessionLogin);
                resp.sendRedirect("/file_view.jsp");
            } else {
                resp.sendRedirect("/index.jsp?error=error");
            }
        } catch (IOException e) {
            sessionLogin.invalidate();
            resp.sendRedirect("/index.jsp?error=error");
            e.printStackTrace();
        }
    }
}
