package com.example.cscb07_app.Activity.Admin;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.users.Admin;
import com.example.cscb07_app.Controller.AdminController;
import com.example.cscb07_app.R;

public class AdminViewHistoricAccounts extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_view_historic_accounts);

    Admin admin = (Admin) getIntent().getSerializableExtra("adminObject");

    Button viewHistoricAccounts = findViewById(R.id.viewHistoricAccountsBtn);
    viewHistoricAccounts.setOnClickListener(new AdminController(this, admin));
  }
}
