package com.example.cscb07_app.Activity.Admin;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cscb07_app.Controller.AdminController;
import com.example.cscb07_app.R;

/** Activity to serialize the database to a file */
public class AdminSaveAppData extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_save_app_data);
    Button createCoupon = findViewById(R.id.saveDataBtn);
    createCoupon.setOnClickListener(new AdminController(this));
  }
}
