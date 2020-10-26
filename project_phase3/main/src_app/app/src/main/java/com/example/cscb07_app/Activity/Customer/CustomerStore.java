package com.example.cscb07_app.Activity.Customer;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.database.helper.DatabaseAndroidHelper;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.database.helper.DatabaseMethodHelper;
import com.b07.inventory.Item;
import com.b07.users.Customer;
import com.example.cscb07_app.Controller.CustomerController;
import com.example.cscb07_app.R;
import java.sql.SQLException;

public class CustomerStore extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_store);

    DatabaseMethodHelper methodHelper = new DatabaseMethodHelper(getApplicationContext());
    DatabaseAndroidHelper androidHelper = new DatabaseAndroidHelper();
    androidHelper.setDriver(methodHelper);
    DatabaseHelperAdapter.setPlatformHelper(androidHelper);

    addItemNames();

    Customer customer = (Customer) getIntent().getSerializableExtra("customer");

    ImageButton logoutBtn = findViewById(R.id.customerStoreLogoutBtn);
    logoutBtn.setOnClickListener(new CustomerController(this, customer));

    ImageButton viewCartBtn = findViewById(R.id.customerStoreViewCartBtn);
    viewCartBtn.setOnClickListener(new CustomerController(this, customer));
  }

  /** Add item names based on their ids in the database. */
  public void addItemNames() {
    String[] itemNames = new String[5];

    try {
      for (int itemId = 1; itemId < 6; itemId++) {
        Item item = DatabaseHelperAdapter.getItem(itemId);
        itemNames[itemId - 1] = item.getName();
      }
    } catch (SQLException e) {
    }

    TextView item1 = findViewById(R.id.item1);
    item1.setText(itemNames[0]);
    TextView item2 = findViewById(R.id.item2);
    item2.setText(itemNames[1]);
    TextView item3 = findViewById(R.id.item3);
    item3.setText(itemNames[2]);
    TextView item4 = findViewById(R.id.item4);
    item4.setText(itemNames[3]);
    TextView item5 = findViewById(R.id.item5);
    item5.setText(itemNames[4]);
  }
}
