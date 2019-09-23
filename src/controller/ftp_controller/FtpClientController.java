package controller.ftp_controller;

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
import java.io.IOException;

@WebServlet(urlPatterns = "/ftp_client")
public class FtpClientController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject obj = new JSONObject();//---Creates a JSON object {}
        JSONArray filesJson = new JSONArray();//---Creates a JSON array to store JSON objects []
        FTPClient client = new FTPClient();

        try {
            client.connect("localhost", 21);
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                throw new IOException("Exception in connecting to FTP Server");
            }
            client.login("imalka2", "imalka2");

            client.enterLocalPassiveMode();
            if (client.isConnected()) {
                // Obtain a list of filenames in the current working
                // directory. When no file found an empty array will
                // be returned.
//                String[] names = client.listNames();
//                for (String name : names) {
//                    System.out.println("Name = " + name);
//                }

//                FTPFile[] ftpDirectories = client.listDirectories();
//                for (FTPFile ftpDirectory : ftpDirectories) {
//                    System.out.println(ftpDirectory.getName());
//                }
//
//                System.out.println("--------------------------------------");

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
                        if(ftpFile.getName().endsWith(req.getParameter("fileType").trim())){
                            fileJson.put("FileType", "File");
                            fileJson.put("FileName", ftpFile.getName());
                            filesJson.add(fileJson);
                        }
                    }
                }
                obj.put("Files", filesJson);
                resp.getWriter().println(obj.toJSONString());//---Print and reply JSON as a text
            }
            client.logout();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void ftpClient() {
//
//    }
}
