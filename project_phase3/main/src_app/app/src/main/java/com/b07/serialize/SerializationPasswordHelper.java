package com.b07.serialize;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.b07.database.helper.DatabaseMethodHelper;
import com.b07.exceptions.DatabaseInsertException;
import java.sql.SQLException;

/**
 * A quick hack to allow for insertion of unhashed passwords. THIS IS NOT CROSSPLATFORM, SHOULD BE
 * DIFFERENT ON ANDROID
 *
 * @author aidan
 */
public class SerializationPasswordHelper {

  public static int insertUserNoHash(
      String name, int age, String address, String password, Context context)
      throws DatabaseInsertException, SQLException {

    DatabaseMethodHelper databaseMethodHelper = new DatabaseMethodHelper(context);
    long userId = -1;
    SQLiteDatabase sqLiteDatabase = databaseMethodHelper.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    contentValues.put("AGE", age);
    contentValues.put("ADDRESS", address);

    userId = sqLiteDatabase.insert("USERS", null, contentValues);

    contentValues = new ContentValues();

    contentValues.put("USERID", userId);
    contentValues.put("PASSWORD", password);
    sqLiteDatabase.insert("USERPW", null, contentValues);
    sqLiteDatabase.close();
    return Math.toIntExact(userId);
  }
}
