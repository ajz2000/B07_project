package com.example.cscb07_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.database.helper.DatabaseAndroidHelper;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.database.helper.DatabaseMethodHelper;
import com.example.cscb07_app.Activity.Initialization.InitializationCreateFirstAdmin;
import com.example.cscb07_app.Activity.Login.LoginMenu;
import java.sql.SQLException;

/** Main activity of the application */
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    DatabaseMethodHelper methodHelper = new DatabaseMethodHelper(getApplicationContext());
    DatabaseAndroidHelper androidHelper = new DatabaseAndroidHelper();
    androidHelper.setDriver(methodHelper);
    DatabaseHelperAdapter.setPlatformHelper(androidHelper);

    boolean firstBoot = false;
    try {
      firstBoot = DatabaseHelperAdapter.getUserDetails(1) == null;
    } catch (SQLException e) {
    }

    if (firstBoot) {
      startActivity(new Intent(MainActivity.this, InitializationCreateFirstAdmin.class));
    } else {
      startActivity(new Intent(MainActivity.this, LoginMenu.class));
    }
  }
}
