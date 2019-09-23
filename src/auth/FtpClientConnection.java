package auth;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

public class FtpClientConnection {
    private static FtpClientConnection ftpClientConnection;
    private FTPClient ftpClient;

    private FtpClientConnection() {
        try {
            ftpClient = new FTPClient();
            ftpClient.connect("localhost", 21);

            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new IOException("Exception in connecting to FTP Server");
            }
            ftpClient.login("imalka2", "imalka2");

            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FtpClientConnection getFtpClientConnection() {
        if (ftpClientConnection == null) {
            ftpClientConnection = new FtpClientConnection();
        }
        return ftpClientConnection;
    }

    public FTPClient getFtpClient() {
        if (ftpClient.getReplyCode() == 421) {
            ftpClientConnection = new FtpClientConnection();
        }
        return ftpClient;
    }
}
