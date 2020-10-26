package com.b07.serialize;

import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.DatabaseInsertException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A quick hack to allow for insertion of unhashed passwords. THIS IS NOT CROSSPLATFORM, SHOULD BE
 * DIFFERENT ON ANDROID
 *
 * @author aidan
 */
public class SerializationPasswordHelper {

  public static int insertUserNoHash(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException {
    if (age < 0 || name == null || address.length() > 100) {
      throw new DatabaseInsertException();
    }

    Connection connection = DatabaseHelperAdapter.connectOrCreateDataBase();
    int userId = -1;
    String sql = "INSERT INTO USERS(NAME, AGE, ADDRESS) VALUES(?,?,?);";
    try {
      PreparedStatement preparedStatement =
          connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, name);
      preparedStatement.setInt(2, age);
      preparedStatement.setString(3, address);
      int id = preparedStatement.executeUpdate();
      if (id > 0) {
        ResultSet uniqueKey = preparedStatement.getGeneratedKeys();
        if (uniqueKey.next()) {
          int returnValue = uniqueKey.getInt(1);
          uniqueKey.close();
          preparedStatement.close();
          userId = returnValue;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    sql = "INSERT INTO USERPW(USERID, PASSWORD) VALUES(?,?);";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, userId);
      preparedStatement.setString(2, password);
      preparedStatement.executeUpdate();
      preparedStatement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    connection.close();
    return userId;
  }
}
