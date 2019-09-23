package auth;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class FtpClientConnection {
    private static FtpClientConnection ftpClientConnection;
    private FTPClient ftpClient;
    private String[] usernameAndPassword = new String[2];

    private FtpClientConnection() {
        try {
            Connection connection = DBConnection.getDBConnection().getConnection();//---Get database connection
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users");//---Prepare sql as a java object
            ResultSet rst = preparedStatement.executeQuery();//---Execute sql and store result
            if (rst.next()) {//---Navigate pointer to results
                ftpClient = new FTPClient();
                ftpClient.connect("127.0.0.1", 21);

                int reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    throw new IOException("Exception in connecting to FTP Server");
                }
                ftpClient.login(rst.getString(1), rst.getString(2));
                usernameAndPassword[0] = rst.getString(1);
                usernameAndPassword[1] = rst.getString(2);
            }

            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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
        try {
            ftpClient.sendNoOp();
        } catch (IOException e) {
            ftpClientConnection = new FtpClientConnection();
        }
        return ftpClient;
    }
}
