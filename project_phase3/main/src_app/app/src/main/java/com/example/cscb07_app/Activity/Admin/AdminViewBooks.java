package com.example.cscb07_app.Activity.Admin;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.store.SalesLog;
import com.b07.users.Admin;
import com.example.cscb07_app.R;
import java.sql.SQLException;

public class AdminViewBooks extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_view_books);

    Admin admin = (Admin) getIntent().getSerializableExtra("adminObject");

    TextView viewBooksData = findViewById(R.id.viewBooksText);

    SalesLog salesLog = null;

    try {
      salesLog = DatabaseHelperAdapter.getSales();
    } catch (SQLException e) {
    }

    viewBooksData.setText(salesLog.viewBooks());
  }
}
