package com.example.cscb07_app.Activity.Admin;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.users.Admin;
import com.example.cscb07_app.Controller.AdminController;
import com.example.cscb07_app.R;

/** activity to promote an employee */
public class AdminPromoteEmployee extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_promote_employee);

    Admin admin = (Admin) getIntent().getSerializableExtra("adminObject");

    Button promoteEmployeeBtn = findViewById(R.id.promoteEmployeeButton);
    promoteEmployeeBtn.setOnClickListener(new AdminController(this, admin));
  }
}
