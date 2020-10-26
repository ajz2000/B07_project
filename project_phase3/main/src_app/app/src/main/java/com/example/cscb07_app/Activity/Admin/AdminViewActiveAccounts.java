package com.example.cscb07_app.Activity.Admin;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.users.Admin;
import com.example.cscb07_app.Controller.AdminController;
import com.example.cscb07_app.R;

public class AdminViewActiveAccounts extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_view_active_accounts);

    Admin admin = (Admin) getIntent().getSerializableExtra("adminObject");

    Button viewActiveAccounts = findViewById(R.id.viewActiveAccountsBtn);
    viewActiveAccounts.setOnClickListener(new AdminController(this, admin));
  }
}
