package auth;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class FtpClientConnection {
    private static FtpClientConnection ftpClientConnection;
    private FTPClient ftpClient;
    private String server;
    private String pathName;

    private FtpClientConnection(String username, String password) throws IOException {
//        try {
        InputStream input = new FileInputStream(this.getClass().getClassLoader().getResource("ftp_client_properties/server.properties").getPath());
        Properties prop = new Properties();

        prop.load(input);

        ftpClient = new FTPClient();
        ftpClient.connect(prop.getProperty("server"), 21);
        server = prop.getProperty("server");
        pathName = prop.getProperty("path_name");

//        int reply = ftpClient.getReplyCode();

//        !FTPReply.isPositiveCompletion(reply)
        if (!ftpClient.login(username, password)) {
            ftpClient.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftpClient.enterLocalPassiveMode();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static FTPClient getFtpClientConnection(String username, String password, HttpSession sessionLogin, int connectionCount) throws IOException {
        return createFtpClient(username, password, sessionLogin, connectionCount);
    }

    public static FTPClient getFtpClientConnection(HttpSession sessionLogin, int connectionCount) throws IOException {
        return createFtpClient(sessionLogin.getAttribute("username").toString(), sessionLogin.getAttribute("password").toString(), sessionLogin, connectionCount);
    }

    private static FTPClient createFtpClient(String username, String password, HttpSession sessionLogin, int connectionCount) throws IOException {
        if (sessionLogin.getAttribute("ftpClientobj") == null) {
            ftpClientConnection = new FtpClientConnection(username, password);
            sessionLogin.setAttribute("ftpClientobj", ftpClientConnection.getFtpClient());
            sessionLogin.setAttribute("username", username);
            sessionLogin.setAttribute("password", password);
            sessionLogin.setAttribute("server", ftpClientConnection.getServer());
            sessionLogin.setAttribute("path_name", ftpClientConnection.getPathName());
        } else {
            try {
                ftpClientConnection.getFtpClient().sendNoOp();
                if (connectionCount == 10) {
                    sessionLogin.setAttribute("ftpClientobj", null);
                }
            } catch (IOException e) {
                ftpClientConnection = new FtpClientConnection(username, password);
                sessionLogin.setAttribute("ftpClientobj", ftpClientConnection.getFtpClient());
            }
        }
        return (FTPClient) sessionLogin.getAttribute("ftpClientobj");
    }

    private FTPClient getFtpClient() {
        return ftpClient;
    }

    private String getServer() {
        return server;
    }

    private String getPathName() {
        return pathName;
    }
}
