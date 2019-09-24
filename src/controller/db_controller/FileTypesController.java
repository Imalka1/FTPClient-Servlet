package controller.db_controller;

import auth.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileTypesController {
    public List<String[]> getFileTypes() {
        List<String[]> fileTypes = new ArrayList<>();//---Creates an array object (ArrayList) to store multiple objects
        try {
            Connection connection = DBConnection.getDBConnection().getConnection();//---Get database connection
            PreparedStatement preparedStatement = connection.prepareStatement("select * from file_types");//---Prepare sql as a java object
            ResultSet rst = preparedStatement.executeQuery();//---Execute sql and store result
            while (rst.next()) {//---Navigate pointer to result rows until it ends
                String[] fileType = new String[2];
                fileType[0] = rst.getString(1);
                fileType[1] = rst.getString(2);
                fileTypes.add(fileType);//---Add semester object to array object
            }
        } catch (
                SQLException e) {//--Catch if any sql exception occurred
            e.printStackTrace();
        }
        return fileTypes;//---Return semesters array object with a length > 0 if semesters exists, if not array object returns with a length = 0
    }
}
